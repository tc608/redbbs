package com.lxyer.bbs.base;

import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import org.redkale.convert.Convert;
import org.redkale.net.http.*;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by JUECHENG at 2018/1/30 0:18.
 */
public class EnjoyRender implements HttpRender<HttpScope> {

    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    private static final Engine engine = new Engine();

    @Override
    public void init(HttpContext context, AnyValue config) {
        engine.setBaseTemplatePath(webroot.getPath());
        engine.addSharedObject("EJ", new EJ());
        engine.addSharedFunction("/_t/layout.html");
    }

    @Override
    public void renderTo(HttpRequest request, HttpResponse response, Convert convert, HttpScope scope) {
        Template template = engine.getTemplate(scope.getReferid());
        String str = template.renderToString(scope.getAttributes());
        response.setContentType("text/html; charset=UTF-8");
        response.finish(str);
    }

    @Override
    public Class getType() {
        return HttpScope.class;
    }
}
