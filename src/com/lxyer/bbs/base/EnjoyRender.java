package com.lxyer.bbs.base;

import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.lxyer.bbs.base.kit.EJ;
import com.lxyer.bbs.base.user.UserInfo;
import org.redkale.convert.Convert;
import org.redkale.net.http.*;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

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
        UserInfo mine = request.currentUser();//当前登录人

        Template template = engine.getTemplate(scope.getReferid());
        Map attr = scope.getAttributes();
        if (attr == null) attr = Kv.create();
        attr.put("mine", mine);

        String str = template.renderToString(attr);
        response.setContentType("text/html; charset=UTF-8");
        response.finish(str);
    }

    @Override
    public Class getType() {
        return HttpScope.class;
    }
}
