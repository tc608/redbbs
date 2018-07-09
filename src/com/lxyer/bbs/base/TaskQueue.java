package com.lxyer.bbs.base;

import com.lxyer.bbs.base.kit.LxyKit;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.base.user.UserRecord;
import com.lxyer.bbs.base.user.UserService;
import com.lxyer.bbs.content.Content;
import com.lxyer.bbs.content.ContentInfo;
import com.lxyer.bbs.content.ContentService;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.redkale.source.ColumnValue;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterNode;
import org.redkale.source.Flipper;
import org.redkale.util.AnyValue;
import org.redkale.util.Sheet;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static com.mongodb.client.model.Filters.*;
import static java.util.Arrays.asList;

/**
 * Created by liangxianyou at 2018/6/20 22:54.
 */
public class TaskQueue<T extends Object> extends BaseService implements Runnable {

    @Resource
    private ContentService contentService;
    @Resource
    private UserService userService;

    @Resource(name = "property.mongo.host")
    private String mongoHost;
    @Resource(name = "property.mongo.database")
    private String mongoDatabase;

    protected static LinkedBlockingQueue queue = new LinkedBlockingQueue();

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> visLog;

    public TaskQueue() {
        new Thread(this).start();
    }

    @Override
    public void init(AnyValue config) {
        mongoClient = new MongoClient(mongoHost, 27017);
        database = mongoClient.getDatabase(winos ? mongoDatabase + "_dev": mongoDatabase);
        visLog = database.getCollection("vis_log");
    }

    public T take() throws InterruptedException {
        return (T) queue.take();
    }

    public void put(T t) throws InterruptedException {
        queue.put(t);
    }

    @Override
    public void run() {
        try {
            while (true){
                Map logData = (Map) take();

                logData.put("ftime", String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", logData.get("time")));
                visLog.insertOne(new Document(logData));

                //在这里处理日志数据
                String uri = logData.get("uri")+"";

                //[访问量]
                if (uri.startsWith("/jie/detail/")){
                    updateViewNumAsync(logData);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 帖子阅读数处理
     * @param logData
     */
    private void updateViewNumAsync(Map logData) {
        CompletableFuture.runAsync(()->{
            Bson filter = and(
                    eq("uri", logData.get("uri"))//帖子
                    ,eq("ip", logData.get("ip"))//IP
                    ,or(
                        eq("userid", logData.get("userid"))//登录人
                        ,eq("userid", 0)//未登录userid=0
                    )
            );
            long count = visLog.count(filter);
            if (count <= 1){
                String uri = logData.get("uri") + "";
                int contentid = Integer.parseInt(uri.replace("/jie/detail/", ""));
                source.updateColumn(Content.class, contentid, ColumnValue.inc("viewnum", 1));
            }
        });
    }

    public Sheet<ContentInfo> hotView(String sessionid){
        int limit = 8;
        String cacheKey = "hotView";
        Object ids = cacheSource.get(cacheKey);
        if (ids == null){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, -7);

            //查询一周某热帖记录
            Bson filter = and(ne("userid", 100001)
                    ,regex("uri", "/jie/detail/*")
                    ,ne("ip", "")
                    ,gt("time", cal.getTimeInMillis())
            );
            List<Bson> list = asList(
                    Aggregates.match(filter)
                    ,Aggregates.group("$uri", Accumulators.sum("count", 1))
                    ,Aggregates.sort(new Document("count", -1))
                    ,Aggregates.limit(8)
            );
            AggregateIterable<Document> documents = visLog.aggregate(list, Document.class);

            List<Integer> _ids = new ArrayList<>(limit);
            documents.forEach((Block<? super Document>) x->{
                String uri = x.getString("_id");
                _ids.add(Integer.parseInt(uri.replace("/jie/detail/", "")));
            });

            cacheSource.set(30 * 60, cacheKey, ids = _ids);
        }

        int[] contentids = new int[limit];
        for (int i = 0; i < ((List<Integer>) ids).size(); i++) {
            contentids[i] = ((List<Integer>) ids).get(i);
        }

        Flipper flipper = new Flipper().limit(limit);
        FilterNode node = FilterNode.create("contentid", FilterExpress.IN, contentids);

        //权限过滤
        UserInfo userInfo = userService.current(sessionid);
        if (userInfo == null){
            node.and("status", FilterExpress.NOTEQUAL, 30);
        }else if (!userService.isAdmin(userInfo.getUserid())){
            node.and(FilterNode.create("status", FilterExpress.NOTEQUAL, 30).or(FilterNode.create("status", 30).and("userid", userInfo.getUserid())));
        }
        return contentService.contentQuery(flipper, node);
    }

    /**
     * TODO:帖子访客记录 --待完成
     * @return
     */
    public Sheet<Map> readRecordAsync(Flipper flipper ,int contentid){
        Bson filter = eq("uri", "/jie/detail/"+ contentid);

        FindIterable<Document> documents = visLog.find(filter).limit(flipper.getLimit()).skip(flipper.getOffset());
        long total = visLog.countDocuments(filter);

        List<Map> rows = new ArrayList<>();
        List<Integer> uids = new ArrayList<>();
        documents.forEach((Block<? super Document>) x->{
            Integer userid = x.getInteger("userid");
            if (userid > 0) uids.add(userid);

            Map row = new HashMap<String, Object>();
            row.put("userid", userid);
            row.put("ip", x.getString("ip"));
        });

        int[] userids = LxyKit.listToArray(uids, new int[uids.size()]);
        List<UserRecord> records = source.queryList(UserRecord.class, FilterNode.create("userid", FilterExpress.IN, userids));

        rows.forEach(x->{
            UserRecord record = records.stream().filter(y -> (Integer) x.get("userid") == y.getUserid()).findFirst().orElse(new UserRecord());
            x.put("nickname", record.getRealname());
            x.put("avatar", record.getAvatar());
        });

        Sheet<Map> sheet = new Sheet<>();
        sheet.setTotal(total);
        sheet.setRows(rows);

        return sheet;
    }
}
