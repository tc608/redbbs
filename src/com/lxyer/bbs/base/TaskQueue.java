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

    protected static LinkedBlockingQueue queue = new LinkedBlockingQueue();

    private static MongoClient mongoClient;
    private static MongoDatabase redbbs;
    private static MongoCollection<Document> visLog;

    public TaskQueue() {
        new Thread(this).start();
    }

    @Override
    public void init(AnyValue config) {
        mongoClient = new MongoClient(mongoHost, 27017);
        redbbs = mongoClient.getDatabase(winos ? "redbbs_dev": "redbbs");
        visLog = redbbs.getCollection("vis_log");
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

                take.put("ftime", String.format("%1$tY%1$tm%1$td%1$tH%1$tM", take.get("time")));
                visLog.insertOne(new Document(take));

                //在这里处理日志数据[访问量]
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        //测试mongodb 连通性
        FindIterable<Document> documents = visLog.find().limit(10);
        documents.forEach((Block<? super Document>) x->{
            System.out.println(x);
        });
    }*/
}
