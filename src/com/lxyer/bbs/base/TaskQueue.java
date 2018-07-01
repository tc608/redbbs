package com.lxyer.bbs.base;

import com.lxyer.bbs.content.Content;
import com.lxyer.bbs.content.ContentService;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.redkale.source.ColumnValue;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by liangxianyou at 2018/6/20 22:54.
 */
public class TaskQueue<T extends Object> extends BaseService implements Runnable {

    @Resource
    private ContentService contentService;

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
}
