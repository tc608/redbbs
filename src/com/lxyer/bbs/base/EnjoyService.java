package com.lxyer.bbs.base;

import com.jfinal.template.Engine;
import org.redkale.net.http.RestService;
import org.redkale.service.Service;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by Lxy at 2017/10/1 8:41.
 */
@RestService(automapping = false)
public class EnjoyService extends Engine implements Service {
    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    @Override
    public void init(AnyValue config) {
        setBaseTemplatePath(webroot.getPath());

        addSharedObject("EJ", new EJ());
        addSharedFunction("/_t/layout.html");
    }
}
