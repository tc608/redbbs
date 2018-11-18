package com.lxyer.bbs.base;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.lxyer.bbs.base.entity.VisLog;

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

    //check exists
    static {
        if (!dbDev.exists()) {
            dbDev.create();
        }

        if (!colVisLog.exists()) {
            colVisLog.create();
        }
    }

    public static <T> void save(T t) {
        if (t instanceof VisLog) {
            colVisLog.insertDocument(t);
        }
    }

}
