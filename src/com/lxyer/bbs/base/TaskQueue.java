package com.lxyer.bbs.base;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liangxianyou at 2018/6/20 22:54.
 */
public class TaskQueue<T extends Object> extends BaseService implements Runnable {

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
                Map take = (Map) take();

                take.put("ftime", String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", take.get("time")));
                visLog.insertOne(new Document(take));

                //在这里处理日志数据[访问量]
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
