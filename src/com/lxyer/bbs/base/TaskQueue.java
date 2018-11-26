package com.lxyer.bbs.base;

import com.lxyer.bbs.base.entity.Count;
import com.lxyer.bbs.base.entity.VisLog;
import com.lxyer.bbs.base.kit.LxyKit;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.base.user.UserRecord;
import com.lxyer.bbs.base.user.UserService;
import com.lxyer.bbs.content.Content;
import com.lxyer.bbs.content.ContentInfo;
import com.lxyer.bbs.content.ContentService;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestService;
import org.redkale.source.ColumnValue;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterNode;
import org.redkale.source.Flipper;
import org.redkale.util.AnyValue;
import org.redkale.util.Comment;
import org.redkale.util.Sheet;
import org.redkale.util.Utility;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by liangxianyou at 2018/6/20 22:54.
 */
@RestService(name = "xxx",automapping = true, comment = "日志记录")
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

    @RestMapping(ignore = true)
    public T take() throws InterruptedException {
        return (T) queue.take();
    }

    @RestMapping(ignore = true)
    public void put(T t) throws InterruptedException {
        queue.put(t);
    }

    @Override
    @RestMapping(ignore = true, comment = "独立线程，用户访问行为记录到数据库")
    public void run() {
        do {
            try {
                T task = take();

                //记录访问日志，如果是访问的文章详情：对文章访问数量更新
                if (task instanceof VisLog) {
                    System.out.println(task);
                    /* todo: 需要记录 访问日志，此处添加记录日志逻辑
                    ArangoKit.save(task).thenAcceptAsync((_task) -> {
                        VisLog visLog = (VisLog) _task;
                        //[访问量]
                        String uri = visLog.getUri();
                        if (uri != null && uri.startsWith("/jie/detail/")){
                            updateViewNum(visLog);
                        }
                    });
                    */
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    @Comment("帖子阅读数处理")
    private void updateViewNum(VisLog visLog) {

        String aql = String.format("for d in vis_log_dev\n" +
                "    filter d.uri == '%s' and d.ip == '%s' and (d.userid == %s or d.userid==0)\n" +
                "    collect WITH COUNT INTO total\n" +
                "    return total", visLog.getUri(), visLog.getIp(), visLog.getUserid());

        long total = ArangoKit.findInt(aql);


        if (total <= 1) {
            String uri = visLog.getUri();
            int contentid = Integer.parseInt(uri.replace("/jie/detail/", ""));
            source.updateColumn(Content.class, contentid, ColumnValue.inc("viewnum", 1));
        }
    }

    @RestMapping(ignore = true, comment = "访问热帖数据")
    public Sheet<ContentInfo> hotView(String sessionid){
        int limit = 8;
        String cacheKey = "hotView";
        Object ids = null;//cacheSource.get(cacheKey);
        if (ids == null){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, -7);

            Map para = new HashMap();
            para.put("time", cal.getTimeInMillis());
            //查询一周某热帖记录
            List<Count> hotArticle = ArangoKit.find(
                    "for d in vis_log_dev\n" +
                            "    filter d.uri =~ '^/jie/detail/[0-9]+$' and d.userid != 100001 and d.time > @time\n" +
                            "    COLLECT uri=d.uri WITH COUNT INTO total\n" +
                            "    sort total desc\n" +
                            "    limit 10\n" +
                            "    return {name: uri,total:total}",
                    Utility.ofMap("time", cal.getTimeInMillis()),
                    Count.class);

            Function<List<Count>, List<Integer>> deal = (counts) -> {
                List<Integer> _ids = new ArrayList<>();
                counts.forEach(x -> {
                    _ids.add(Integer.parseInt(x.getName().replace("/jie/detail/", "")));
                });
                return _ids;
            };

            ids = deal.apply(hotArticle);
            cacheSource.set(30 * 60, cacheKey, ids);
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
     */
    @RestMapping(ignore = true, comment = "帖子访客记录")
    public Sheet<Map> readRecordAsync(Flipper flipper ,int contentid){
        Bson filter = eq("uri", "/jie/detail/"+ contentid);

        FindIterable<Document> documents = visLog.find(filter).limit(flipper.getLimit()).skip(flipper.getOffset());
        long total = visLog.countDocuments(filter);

        List<Map> rows = new ArrayList<>();
        List<Integer> uids = new ArrayList<>();
        documents.forEach((Consumer<? super Document>) x->{
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
