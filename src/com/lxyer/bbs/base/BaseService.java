package com.lxyer.bbs.base;

import org.redkale.net.http.RestMapping;
import org.redkale.service.Service;
import org.redkale.source.CacheSource;
import org.redkale.source.DataSource;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by Lxy at 2017/10/3 13:50.
 */
public class BaseService implements Service {

    protected final int sessionExpireSeconds = 7 * 24 * 60 * 60;

    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    @Resource(name = "redbbs")
    protected DataSource source;

    /* 使用redis 代码中配置此处即可
    @Resource(name = "redis")*/
    @Resource(name = "cacheSource")
    protected CacheSource<Long> sessions;

    @Resource(name = "cacheSource")
    protected CacheSource cacheSource;

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    public static Predicate isEmpty = (x) -> {
        if (x == null)
            return true;
        if (x instanceof List)
            return ((List) x).isEmpty();
        if (x instanceof String)
            return ((String) x).isEmpty();
        if (x instanceof Map)
            return ((Map) x).isEmpty();
        if (x instanceof Collection)
            return ((Collection) x).isEmpty();
        return false;
    };

    @RestMapping(ignore = true)
    public DataSource getSource() {
        return source;
    }

    @RestMapping(ignore = true)
    public int currentUserid(String sessionid) {
        if (sessionid == null) return 0;
        long userid = 0;
        try {
            userid = sessions.getLong(sessionid, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) userid;
    }

}
