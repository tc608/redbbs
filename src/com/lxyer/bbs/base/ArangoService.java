package com.lxyer.bbs.base;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestService;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author: liangxianyou at 2018/11/18 9:02.
 */
@RestService(automapping = true, comment = "Arango服务")
public class ArangoService extends BaseService {

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    protected Function<String, String> chDev = (s) -> s + (isDev ? "_dev" : "");

    @Resource(name = "property.arango.host")
    private String arangoHost = "127.0.0.1";
    @Resource(name = "property.arango.port")
    private int port = 8529;
    @Resource(name = "property.arango.database")
    private String database = "redbbs";
    @Resource(name = "property.arango.user")
    private String user = "root";
    @Resource(name = "property.arango.password")
    private String password = "root";

    //日志存放doc名称
    private static final String VIS_LOG = "vis_log";

    //Arango
    protected static ArangoDB arangoDb;
    protected static ArangoDatabase db;
    protected static ArangoCollection colVisLog;

    @Override
    public void init(AnyValue config) {
        CompletableFuture.runAsync(() -> {
            System.out.println("isDev :" + isDev);

            arangoDb = new ArangoDB.Builder().host(arangoHost, port).user(user).password(password).build();
            db = arangoDb.db(chDev.apply(database));
            colVisLog = db.collection(chDev.apply(VIS_LOG));

            if (!db.exists()) {
                db.create();
            }
            if (!colVisLog.exists()) {
                colVisLog.create();
            }
        });
    }

    @RestMapping(auth = false)
    public List<Map> hi() {
        System.out.println("colVisLog :" + colVisLog.exists());
        String aql = String.format("for d in %s limit 10 return d", chDev.apply(VIS_LOG));
        List<Map> visLogs = db.query(aql, Map.class).asListRemaining();
        return visLogs;
    }

    public static <T> CompletableFuture<T> save(T t) {
        return CompletableFuture.supplyAsync(() -> {
            if (t instanceof com.lxyer.bbs.base.entity.VisLog) {
                colVisLog.insertDocument(t);
            }
            return t;
        });
    }

    public static long findInt(String aql) {
        return db.query(aql, long.class).first();
    }

    public static long findInt(String aql, Map para) {
        return db.query(aql, long.class).first();
    }

    public static <T> List<T> find(String aql, Class<T> clazz) {
        return db.query(aql, clazz).asListRemaining();
    }

    public static <T> List<T> find(String aql, Map para, Class<T> clazz) {

        return db.query(aql, para, clazz).asListRemaining();
    }

}
