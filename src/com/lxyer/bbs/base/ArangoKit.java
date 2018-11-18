package com.lxyer.bbs.base;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.lxyer.bbs.base.entity.VisLog;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author: liangxianyou at 2018/11/18 9:02.
 */
public class ArangoKit {

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    protected static Function<String, String> chDev = (s) -> s  + (winos ? "_dev" : "");

    //Arango
    protected static ArangoDB arangoDb = new ArangoDB.Builder().host("120.24.230.60", 8529).user("root").password("root").build();
    protected static ArangoDatabase dbDev = arangoDb.db(chDev.apply("redbbs"));
    protected static ArangoCollection colVisLog = dbDev.collection(chDev.apply("vis_log"));

    static {
        if (!dbDev.exists()) {
            dbDev.create();
        }

        if (!colVisLog.exists()) {
            colVisLog.create();
        }

        //java.net.SocketTimeoutException: Read timed out  加入下面两行，观察是否正常
        System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(1000));
        System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(1000));
    }

    public static <T> CompletableFuture<T> save(T t) {
        return CompletableFuture.supplyAsync(() -> {
            if (t instanceof VisLog) {
                colVisLog.insertDocument(t);
            }
            return t;
        });
    }

    public static long findInt(String aql) {
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
    }

}
