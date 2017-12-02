package com.lxyer.bbs.base;

import org.redkale.service.Service;
import org.redkale.source.DataSource;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by Lxy at 2017/10/3 13:50.
 */
public class BaseService implements Service {

    protected final int sessionExpireSeconds = 30 * 60;
    protected final int contentinfoExpireSeconds = 30 * 60;

    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    @Resource(name = "art123")
    protected DataSource source;

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

}
