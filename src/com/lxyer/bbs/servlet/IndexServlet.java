package com.lxyer.bbs.servlet;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.BaseServlet;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.content.ContentInfo;
import org.redkale.net.http.HttpMapping;
import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.WebServlet;
import org.redkale.source.FilterNode;
import org.redkale.source.Flipper;
import org.redkale.util.Sheet;

import static org.redkale.source.FilterExpress.GREATERTHAN;
import static org.redkale.source.FilterExpress.NOTEQUAL;


/**
 * Created by Lxy at 2017/11/25 12:31.
 */
@WebServlet({"/"
        /* ,"/article","/article/*" */
})
public class IndexServlet extends BaseServlet {

    @HttpMapping(url = "/", auth = false, comment = "社区首页")
    public void abc(HttpRequest request, HttpResponse response){
        Flipper flipper = new Flipper().limit(30).sort("top DESC,createTime DESC");
        //置顶贴
        FilterNode topNode = FilterNode.create("status", NOTEQUAL, -1).and("top", GREATERTHAN, 0);
        Sheet<ContentInfo> top = contentService.contentQuery(flipper, setPrivate(topNode));

        //非置顶贴
        FilterNode untopNode = FilterNode.create("status", NOTEQUAL, -1).and("top", 0);
        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, setPrivate(untopNode));

        //热帖
        /*Flipper flipper2 = new Flipper().limit(8).sort("viewNum DESC");
        Sheet<ContentInfo> hotView = contentService.contentQuery(flipper2, "");*/

        //热议
        Flipper flipper3 = new Flipper().limit(8).sort("replyNum DESC");
        Sheet<ContentInfo> hotReply = contentService.contentQuery(flipper3, "", currentId);

        //最新加入
        Sheet<UserInfo> lastReg = userService.lastReg();

        //用户统计
        Number userCount = userService.userCount();

        Kv kv = Kv.by("top", top).set("contents", contents).set("hotReply", hotReply).set("lastReg", lastReg).set("userCount", userCount);
        finish("index.html", kv);
    }

    @HttpMapping(url = "/site", auth = false, comment = "网站首页")
    public void site(HttpRequest request, HttpResponse response){

        finish("/site.html");
    }

    //====================================文章相关====================================
    /*@HttpMapping(url = "/article", auth = false, comment = "文章首页")
    public void article(HttpRequest request, HttpResponse response){

        finish("/article/index.html");
    }*/
}
