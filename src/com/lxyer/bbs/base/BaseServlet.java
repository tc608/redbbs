package com.lxyer.bbs.base;

import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.lxyer.bbs.base.kit.EJ;
import com.lxyer.bbs.base.kit.RetCodes;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.base.user.UserService;
import com.lxyer.bbs.comment.CommentService;
import com.lxyer.bbs.content.ContentService;
import org.redkale.net.http.HttpContext;
import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.HttpServlet;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterNode;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.lxyer.bbs.base.kit.RetCodes.RET_USER_UNLOGIN;

/**
 * Created by Lxy at 2017/10/3 13:39.
 */
public class BaseServlet extends HttpServlet {
    private HttpRequest request;
    private HttpResponse response;
    private static final Kv _kv = Kv.create();
    private static Engine engine;
    protected String sessionid;
    protected int currentid;//登录人id

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    /*@Resource
    protected EnjoyService enjoyService;*/
    @Resource
    protected UserService userService;

    /*@Resource(name = "redis")
    protected RedisCacheSource<String> cache;*/

    @Resource
    protected ContentService contentService;

    @Resource
    protected CommentService commentService;

    @Resource
    protected TaskQueue<Map> logQueue;


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
            currentid = userService.currentUserId(sessionid);
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

        Kv visLog = new Kv();//ip、time、userid、uri、para
        visLog.set("ip", request.getRemoteAddr());
        request.getHost();
        visLog.set("time", System.currentTimeMillis());
        visLog.set("userid", currentid);
        visLog.set("uri", request.getRequestURI());

        Kv para = Kv.create();
        for (String key : request.getParameterNames()){
            para.set(key, request.getParameter(key));
        }
        visLog.set("para", para);
        try {
            logQueue.put(visLog);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        if (request.currentUser() != null){
            kv.set("mine", request.currentUser());
        }

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

    //设置私密帖子过滤
    protected FilterNode setPrivate(FilterNode node){
        UserInfo userInfo = request.currentUser();
        if (userInfo == null){
            node.and("status", FilterExpress.NOTEQUAL, 30);
        }else if (!userService.isAdmin(currentid)){
            //select * from content c where c.status != -1 and (c.status!=30 or (c.status=30 and c.userid=100001))
            node.and(FilterNode.create("status", FilterExpress.NOTEQUAL, 30).or(FilterNode.create("status", 30).and("userid", userInfo.getUserid())));
        }

        return node;
    }
}
