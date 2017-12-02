package com.lxyer.bbs.base;

import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.lxyer.bbs.service.UserService;
import org.redkale.net.http.HttpContext;
import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.HttpServlet;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lxyer.bbs.base.RetCodes.RET_USER_UNLOGIN;

/**
 * Created by Lxy at 2017/10/3 13:39.
 */
public class BaseServlet extends HttpServlet {

    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    /*@Resource
    protected EnjoyService enjoyService;*/
    @Resource
    protected UserService userService;

    private HttpRequest request;
    private HttpResponse response;
    private static final Kv _kv = Kv.create();
    private static Engine engine;
    protected String sessionid;

    @Override
    public void init(HttpContext context, AnyValue config) {
        if (engine == null){
            engine = new Engine();
            engine.setBaseTemplatePath(webroot.getPath());
            engine.addSharedObject("EJ", new EJ());
            engine.addSharedFunction("/_t/layout.html");
        }
    }

    @Override
    protected void preExecute(HttpRequest request, HttpResponse response) throws IOException {
        sessionid = request.getSessionid(false);
        if (sessionid != null) {
            request.setCurrentUser(userService.current(sessionid));
            _kv.set("mine", request.currentUser());
        }

        this.request = request;
        this.response = response;
        String uri = request.getRequestURI();
        if (uri.startsWith("/res")){
            File file = new File(webroot + uri);
            response.finish(file);
            return;
        }
        if (uri.endsWith(".html")){
            Kv kv = Kv.by("resSys", "resSys");
            finish(uri, kv);
            return;
        }


        /*if ("/".equals(uri)){
            finish("/front/index.html", Kv.by("",""));
            return;
        }*/


        response.nextEvent();
    }

    @Override
    protected void authenticate(HttpRequest request, HttpResponse response) throws IOException {
        if (request.currentUser() == null) {
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                response.finish(RetCodes.retResult(RET_USER_UNLOGIN, "未登录，登录后重试").toString());
            }else {
                finish("/user/login.html");
            }
            return;
        }
        response.nextEvent();
    }

    public void finish(String view, Kv kv) {
        if (request.currentUser() != null) kv.set("mine", request.currentUser());

        Template template = engine.getTemplate(view);
        String str = template.renderToString(kv);
        response.setContentType("text/html; charset=UTF-8");
        response.finish(str);
    }

    public void finish(String view){
        finish(view, _kv);
    }

    public int getLimit(){
        return request.getIntParameter("limit", 1);
    }
    public int getOffset(){
        return request.getIntParameter("offset", 10);
    }

    /*
    测试用例
    System.out.println("sb="+getPara());
    System.out.println("sb(0)="+getPara(0));
    System.out.println("sb(1)="+getPara(1));
    System.out.println("sb(-1)="+getPara(-1));
    System.out.println("sb(-2)="+getPara(-2));
    * */
    public String getPara(){
        String requestURI = request.getRequestURI();
        String subStr = requestURI.substring(requestURI.lastIndexOf("/") + 1);
        return subStr.contains("-") ? subStr.substring(0, subStr.indexOf("-")) : subStr;
    }
    public String getPara(int index){
        String requestURI = request.getRequestURI();
        String subStr = requestURI.substring(requestURI.lastIndexOf("/") + 1);

        String[] paraArr = subStr.split("-");
        if (index < 0){
            return paraArr.length < -index ? null : paraArr[paraArr.length+index];
        }else {
            return paraArr.length < index+1 ? null : paraArr[index];
        }
    }
    public <T> T getPara(int index, T defaultValue){
        T para = (T)getPara(index);
        return para == null || "".equals(para) ? defaultValue : para;
    }
    public int getParaToInt(int index, int defaultValue){
        String para = getPara(index);
        return para == null || "".equals(para) ? defaultValue : Integer.parseInt(para);
    }
    public int getParaToInt(int index){
        int n = 0;
        String para = getPara(index);
        if (para == null || "".equals(para)) n = 0;
        try {
            n = Integer.parseInt(para);
        }catch (Exception e){

        }
        return n;
    }
}
