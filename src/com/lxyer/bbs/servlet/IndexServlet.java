package com.lxyer.bbs.servlet;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.BaseServlet;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.comment.CommentBean;
import com.lxyer.bbs.content.ContentInfo;
import org.redkale.net.http.*;
import org.redkale.source.FilterNode;
import org.redkale.source.Flipper;
import org.redkale.util.Sheet;

import static org.redkale.source.FilterExpress.GREATERTHANOREQUALTO;
import static org.redkale.source.FilterExpress.NOTEQUAL;


/**
 * Created by Lxy at 2017/11/25 12:31.
 */
@WebServlet(value = {"/", "/project"}, comment = "首页一级菜单入口")
public class IndexServlet extends BaseServlet {

    @HttpMapping(url = "/", auth = false, comment = "社区首页")
    public void index(HttpRequest request, HttpResponse response) {

        String sessionid = request.getSessionid(false);

        Flipper flipper = new Flipper().limit(15).sort("top DESC,createtime DESC");
        //置顶贴
        FilterNode topNode = FilterNode.create("status", NOTEQUAL, -10).and("top", GREATERTHANOREQUALTO, 20);
        Sheet<ContentInfo> top = contentService.contentQuery(flipper, setPrivate(request, topNode));

        //非置顶贴
        FilterNode untopNode = FilterNode.create("status", NOTEQUAL, -10).and("top", 10);
        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, setPrivate(request, untopNode));

        //热帖
        /*Flipper flipper2 = new Flipper().limit(8).sort("viewNum DESC");
        Sheet<ContentInfo> hotView = contentService.contentQuery(flipper2, "");*/

        //热议
        /*Flipper flipper3 = new Flipper().limit(8).sort("replynum DESC");
        Sheet<ContentInfo> hotReply = contentService.contentQuery(flipper3, "", sessionid);*/

        Sheet<ContentInfo> hotView = logQueue.hotView(sessionid);

        //最新加入
        Sheet<UserInfo> lastReg = userService.lastReg();

        //用户统计
        Number userCount = userService.userCount();

        Kv kv = Kv.by("top", top).set("contents", contents).set("hotView", hotView).set("lastReg", lastReg).set("userCount", userCount);
        response.finish(HttpScope.refer("index.html").attr(kv));
    }

    @HttpMapping(url = "/site", auth = false, comment = "网站首页")
    public void site(HttpRequest request, HttpResponse response) {

        response.finish(HttpScope.refer("/site.html"));
    }

    //====================================文章相关====================================
    /*@HttpMapping(url = "/article", auth = false, comment = "文章首页")
    public void article(HttpRequest request, HttpResponse response){

        finish("/article/index.html");
    }*/

    //====================================项目相关====================================
    @HttpMapping(url = "/project", auth = false, comment = "项目首页")
    public void project(HttpRequest request, HttpResponse response) {
        Integer userid = request.currentUserid(int.class);
        int contentid = 22;

        ContentInfo content = contentService.info(userid, contentid);
        Sheet<CommentBean> comments = commentService.query(userid, contentid, new Flipper().limit(30));

        Kv kv = Kv.by("bean", content).set("comments", comments);
        response.finish(HttpScope.refer("/project/index.html").attr(kv));
    }

}
