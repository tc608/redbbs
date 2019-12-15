package com.lxyer.bbs.base;

import com.jfinal.kit.Kv;
import org.redkale.net.http.*;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterNode;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static com.lxyer.bbs.base.kit.RetCodes.RET_USER_UNLOGIN;

/**
 * Created by Lxy at 2017/10/3 13:39.
 */
@HttpUserType(com.lxyer.bbs.base.user.UserInfo.class)
public class BaseServlet extends HttpServlet {

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    @Resource
    protected com.lxyer.bbs.base.user.UserService userService;

    @Resource
    protected com.lxyer.bbs.content.ContentService contentService;

    @Resource
    protected com.lxyer.bbs.comment.CommentService commentService;

    @Resource
    protected com.lxyer.bbs.base.TaskQueue<com.lxyer.bbs.base.entity.VisLog> logQueue;

    @Override
    public void init(HttpContext context, AnyValue config) {

    }

    @Override
    protected void preExecute(HttpRequest request, HttpResponse response) throws IOException {
        /*if (true){
            response.finish(HttpScope.refer("404.html"));
            return;
        }*/

        String sessionid = request.getSessionid(true);
        int currentid = 0;
        if (sessionid != null) {
            request.setCurrentUser(userService.current(sessionid));
            currentid = userService.currentUserid(sessionid);
        }

        String uri = request.getRequestURI();
        if (uri.startsWith("/res")) {
            File file = new File(webroot + uri);
            response.finish(file);
            return;
        }
        if (uri.endsWith(".html")) {
            response.finish(HttpScope.refer(uri));
            return;
        }

        //异步记录访问日志
        final int userid = currentid;
        CompletableFuture.runAsync(() -> {

            Kv para = Kv.create();
            for (String key : request.getParameterNames()) {
                para.set(key, request.getParameter(key));
            }
            Kv headers = Kv.create();
            request.getHeaders().forEach((k, v) -> {
                headers.set(k, request.getHeader(k));
            });

            com.lxyer.bbs.base.entity.VisLog visLog = new com.lxyer.bbs.base.entity.VisLog();
            visLog.setIp(request.getRemoteAddr());
            visLog.setUri(request.getRequestURI());
            visLog.setHeaders(headers);
            visLog.setUserid(userid);
            visLog.setPara(para);
            visLog.setTime(System.currentTimeMillis());
            visLog.setFtime(String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", visLog.getTime()));

            try {
                logQueue.put(visLog);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        response.nextEvent();
    }

    @Override
    protected void authenticate(HttpRequest request, HttpResponse response) throws IOException {
        if (request.currentUser() == null) {
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.finish(com.lxyer.bbs.base.kit.RetCodes.retResult(RET_USER_UNLOGIN, "未登录，登录后重试").toString());
            } else {
                response.finish(HttpScope.refer("/user/login.html"));
            }
            return;
        }
        response.nextEvent();
    }

    public int getLimit(HttpRequest request) {
        return request.getIntParameter("limit", 1);
    }

    public int getOffset(HttpRequest request) {
        return request.getIntParameter("offset", 10);
    }

    public String getPara(HttpRequest request) {
        String requestURI = request.getRequestURI();
        String subStr = requestURI.substring(requestURI.lastIndexOf("/") + 1);
        return subStr.contains("-") ? subStr.substring(0, subStr.indexOf("-")) : subStr;
    }

    public String getPara(HttpRequest request, int index) {
        String requestURI = request.getRequestURI();
        String subStr = requestURI.substring(requestURI.lastIndexOf("/") + 1);

        String[] paraArr = subStr.split("-");
        if (index < 0) {
            return paraArr.length < -index ? null : paraArr[paraArr.length + index];
        } else {
            return paraArr.length < index + 1 ? null : paraArr[index];
        }
    }

    public <T> T getPara(HttpRequest request, int index, T defaultValue) {
        T para = (T) getPara(request, index);
        return para == null || "".equals(para) ? defaultValue : para;
    }

    public int getParaToInt(HttpRequest request, int index, int defaultValue) {
        String para = getPara(request, index);
        return para == null || "".equals(para) ? defaultValue : Integer.parseInt(para);
    }

    public int getParaToInt(HttpRequest request, int index) {
        int n = 0;
        String para = getPara(request, index);
        if (para == null || "".equals(para)) n = 0;
        try {
            n = Integer.parseInt(para);
        } catch (Exception e) {

        }
        return n;
    }

    //设置私密帖子过滤
    protected FilterNode setPrivate(HttpRequest request, FilterNode node) {
        com.lxyer.bbs.base.user.UserInfo userInfo = request.currentUser();
        if (userInfo == null) {
            node.and("status", FilterExpress.NOTEQUAL, 30);
        } else if (!userService.isAdmin(userInfo.getUserid())) {
            //select * from content c where c.status != -1 and (c.status!=30 or (c.status=30 and c.userid=100001))
            node.and(FilterNode.create("status", FilterExpress.NOTEQUAL, 30).or(FilterNode.create("status", 30).and("userid", userInfo.getUserid())));
        }

        return node;
    }
}
