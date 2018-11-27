package com.lxyer.bbs.base;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.lxyer.bbs.base.entity.VisLog;
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
@RestService(automapping = false, comment = "Arango服务")
public class ArangoService extends BaseService {

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    protected Function<String, String> chDev = (s) -> s  + (isDev ? "_dev" : "");

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
    protected static ArangoDatabase dbDev;
    protected static ArangoCollection colVisLog;

    /* todo：启用本service 打开注释
    @Override
    public void init(AnyValue config) {
        System.out.println("isDev :" + isDev);

        arangoDb = new ArangoDB.Builder().host(arangoHost, port).user(user).password(password).build();
        dbDev = arangoDb.db(chDev.apply(database));
        colVisLog = dbDev.collection(chDev.apply(VIS_LOG));

        if (!dbDev.exists()) {
            dbDev.create();
        }
        if (!colVisLog.exists()) {
            colVisLog.create();
        }
    }*/

    @RestMapping(auth = false)
    public List<Map> hi() {
        System.out.println("colVisLog :" + colVisLog.exists());
        String aql = String.format("for d in %s limit 10 return d", chDev.apply(VIS_LOG));
        List<Map> visLogs = dbDev.query(aql, Map.class).asListRemaining();
        return visLogs;
    }

    public static <T> CompletableFuture<T> save(T t) {
        return CompletableFuture.supplyAsync(() -> {
            if (t instanceof VisLog) {
                colVisLog.insertDocument(t);
            }
            return t;
        });
    }

    /*public static long findInt(String aql) {
        return dbDev.query(aql, long.class).first();
    }
    public static long findInt(String aql, Map para) {
        return dbDev.query(aql, long.class).first();
    }

    public static <T> List<T> find(String aql, Class<T> clazz) {
        return dbDev.query(aql, clazz).asListRemaining();
    }

    public static <T> List<T> find(String aql, Map para, Class<T> clazz) {

        return dbDev.query(aql, para, clazz).asListRemaining();
    }*/

}
