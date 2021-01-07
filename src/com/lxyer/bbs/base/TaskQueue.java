package com.lxyer.bbs.base;

import com.lxyer.bbs.base.entity.VisLog;
import com.lxyer.bbs.base.user.UserService;
import com.lxyer.bbs.content.Content;
import com.lxyer.bbs.content.ContentInfo;
import com.lxyer.bbs.content.ContentService;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestService;
import org.redkale.source.ColumnValue;
import org.redkale.source.Flipper;
import org.redkale.util.AnyValue;
import org.redkale.util.Comment;
import org.redkale.util.Sheet;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liangxianyou at 2018/6/20 22:54.
 */
@RestService(name = "xxx", automapping = true, comment = "日志记录")
public class TaskQueue<T extends Object> extends BaseService implements Runnable {

    @Resource
    private ContentService contentService;
    @Resource
    private UserService userService;

    protected static LinkedBlockingQueue queue = new LinkedBlockingQueue();

    @Override
    public void init(AnyValue config) {

        // 独立线程，用户访问行为记录到数据库
        new Thread(() -> {
            while (true) {
                try {
                    T task = take();
                    //记录访问日志，如果是访问的文章详情：对文章访问数量更新
                    if (task instanceof VisLog) {

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public TaskQueue() {
        new Thread(this).start();
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
    @RestMapping(ignore = true, comment = "")
    public void run() {

    }

    /*@Comment("帖子阅读数处理")
    private void updateViewNum(VisLog visLog) {

        String aql = String.format("for d in vis_log_dev\n" +
                "    filter d.uri == '%s' and d.ip == '%s' and (d.userid == %s or d.userid==0)\n" +
                "    collect WITH COUNT INTO total\n" +
                "    return total", visLog.getUri(), visLog.getIp(), visLog.getUserid());

        long total = ArangoService.findInt(aql);

        if (total <= 1) {
            String uri = visLog.getUri();
            int contentid = Integer.parseInt(uri.replace("/jie/detail/", ""));
            source.updateColumn(Content.class, contentid, ColumnValue.inc("viewnum", 1));
        }
    }*/

    @RestMapping(ignore = true, comment = "访问热帖数据")
    public Sheet<ContentInfo> hotView(String sessionid) {
        /*int limit = 8;
        String cacheKey = "hotView";
        Object ids = cacheSource.get(cacheKey);
        if (isEmpty.test(ids)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, -7);

            Map para = new HashMap();
            para.put("time", cal.getTimeInMillis());
            //查询一周某热帖记录
            List<Count> hotArticle = ArangoService.find(
                    "for d in " + (isDev ? "vis_log_dev" : "vis_log") + "\n" +
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

        int[] contentids = new int[((List<Integer>) ids).size()];
        for (int i = 0; i < ((List<Integer>) ids).size(); i++) {
            contentids[i] = ((List<Integer>) ids).get(i);
        }

        Flipper flipper = new Flipper().limit(limit);
        FilterNode node = FilterNode.create("contentid", FilterExpress.IN, contentids).and("status", FilterExpress.NOTEQUAL, -10);

        //权限过滤
        UserInfo userInfo = userService.current(sessionid);
        if (userInfo == null) {  //访客
            node.and("status", FilterExpress.NOTEQUAL, 30);
        } else if (!userService.isAdmin(userInfo.getUserid())) { //非管理员
            node.and(FilterNode.create("status", FilterExpress.NOTEQUAL, 30).or(FilterNode.create("status", 30).and("userid", userInfo.getUserid())));
        }
        return contentService.contentQuery(flipper, node);*/

        return Sheet.empty();
    }

    /**
     * TODO:帖子访客记录 --待完成
     */
    @RestMapping(ignore = true, comment = "帖子访客记录")
    public Sheet<Map> readRecordAsync(Flipper flipper, int contentid) {
        /*Bson filter = eq("uri", "/jie/detail/"+ contentid);

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

        return sheet;*/
        return null;
    }
}
