package com.lxyer.bbs.base;

import org.redkale.net.http.RestMapping;
import org.redkale.service.Service;
import org.redkale.source.CacheSource;
import org.redkale.source.DataSource;
import org.redkalex.cache.RedisCacheSource;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Lxy at 2017/10/3 13:50.
 */
public class BaseService implements Service {

    protected final int sessionExpireSeconds = 7 * 24 * 60 * 60;

    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    @Resource(name = "art123")
    protected DataSource source;

    @Resource(name = "redis")
    protected CacheSource<Integer> sessions;

    @Resource(name = "cacheSource")
    protected CacheSource cacheSource;

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    @RestMapping(ignore = true)
    public DataSource getSource() {
        return source;
    }

    @RestMapping(ignore = true)
    public int currentUserid(String sessionid){
        if (sessionid == null) return 0;
        Object userid = null;
        try {
            userid = sessions.getAndRefresh(sessionid, sessionExpireSeconds);
        }catch (Exception e){
            e.printStackTrace();
        }
        return userid == null ? 0 : (Integer)userid;
    }

}
