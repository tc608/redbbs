package net.tccn.bbs.base;

import org.redkale.net.http.RestMapping;
import org.redkale.service.RetResult;
import org.redkale.service.Service;
import org.redkale.source.CacheSource;
import org.redkale.source.DataSource;

import javax.annotation.Resource;

/**
 * Created by Lxy at 2017/10/3 13:50.
 */
public class BaseService implements Service {

    protected final int sessionExpireSeconds = 7 * 24 * 60 * 60;

    protected final static RetResult RET_SUCCESS = RetResult.success();

    @Resource(name = "redbbs")
    protected DataSource dataSource;

    @Resource(name = "redis_cache")
    protected CacheSource cacheSource;

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    @RestMapping(ignore = true)
    public DataSource getSource() {
        return dataSource;
    }

    protected RetResult retError(String info) {
        return new RetResult<>(-1, info);
    }

}
