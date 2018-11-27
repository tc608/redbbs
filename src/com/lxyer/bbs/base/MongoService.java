package com.lxyer.bbs.base;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.redkale.net.http.RestService;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;

@RestService(comment = "Mongo服务")
public class MongoService extends BaseService {

    @Resource(name = "property.mongo.host")
    private String mongoHost;
    @Resource(name = "property.mongo.database")
    private String databaseName;
    @Resource(name = "property.mongo.port")
    private int port;

    //日志存放doc名称
    private static final String VIS_LOG = "vis_log";

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> visLog;

    /* todo：启用本service 打开注释
    @Override
    public void init(AnyValue config) {
        mongoClient = new MongoClient(mongoHost, port);
        database = mongoClient.getDatabase(isDev ? databaseName + "_dev": databaseName);
        visLog = database.getCollection(VIS_LOG);
    }*/

    //todo: 编写mongo操作逻辑 待完成

}
