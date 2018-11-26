package com.lxyer.bbs.servlet;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.BaseServlet;
import com.lxyer.bbs.comment.CommentInfo;
import com.lxyer.bbs.content.ContentInfo;
import org.redkale.net.http.*;
import org.redkale.source.FilterNode;
import org.redkale.source.Flipper;
import org.redkale.util.Sheet;

import static org.redkale.source.FilterExpress.NOTEQUAL;

/**
 * 帖子相关
 * Created by liangxianyou at 2018/6/4 13:15.
 */
@WebServlet(value = {"/jie" ,"/jie/*","/column","/column/*"}, comment = "文章帖子入口")
public class ContentServlet extends BaseServlet {
    @HttpMapping(url = "/jie", auth = false, comment = "问答列表")
    public void jie(HttpRequest request, HttpResponse response){
        String actived = getPara(request, 0, "all");
        int curr = request.getIntParameter("curr", 1);

        //分页帖子列表
        Flipper flipper = new Flipper().offset((curr-1)*15).limit(15).sort("top DESC,createtime DESC");
        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, actived, request.getSessionid(false));

        Kv kv = Kv.by("contents", contents).set("url", request.getRequestURI())
                .set("actived", actived).set("curr", curr);

        response.finish(HttpScope.refer("/jie/index.html").attr(kv));
    }

    @HttpMapping(url = "/jie/add", comment = "发表/编辑问答")
    @HttpParam(name = "#", type = int.class, comment = "内容ID")
    public void add(HttpRequest request, HttpResponse response){
        int contentid = getParaToInt(request, 0);

        ContentInfo contentInfo = null;
        if (contentid > 0){
            contentInfo = contentService.contentInfo(request.getSessionid(false), contentid);
        }

        Kv kv = Kv.by("bean", contentInfo);
        response.finish(HttpScope.refer("/jie/add.html").attr(kv));
    }

    @HttpMapping(url = "/jie/detail", auth = false, comment = "问答详情")
    public void detail(HttpRequest request, HttpResponse response){
        int contentid = getParaToInt(request,0);
        String sessionid = request.getSessionid(false);

        ContentInfo content = contentService.contentInfo(sessionid, contentid);
        Sheet<CommentInfo> comments = commentService.commentQuery(sessionid,contentid, new Flipper().limit(30));

        //热帖
        //Flipper flipper2 = new Flipper().limit(8).sort("viewNum DESC");
        //Sheet<ContentInfo> hotView = contentService.contentQuery(flipper2, "");

        //热议
        /*Flipper flipper3 = new Flipper().limit(8).sort("replynum DESC");
        Sheet<ContentInfo> hotReply = contentService.contentQuery(flipper3, "", sessionid);*/

        Sheet<ContentInfo> hotView = Sheet.empty();//logQueue.hotView(sessionid); TODO: 依赖日志记录，需记录日志后可使用

        Kv kv = Kv.by("bean", content).set("comments", comments).set("hotView", hotView)/*.set("hotReply", hotReply)*/;
        response.finish(HttpScope.refer("/jie/detail.html").attr(kv));
    }

    @HttpMapping(url = "/column", auth = false, comment = "帖子栏目")
    public void column(HttpRequest request, HttpResponse response){
        String sessionid = request.getSessionid(false);

        String para = getPara(request);//空，qz，fx，jy，gg，dt，
        int solved = request.getIntParameter("solved", -1);
        int wonderful = request.getIntParameter("wonderful", -1);
        int curr = request.getIntParameter("curr", 1);

        Kv column = Kv.by("qz", 10).set("fx", 20).set("jy", 30).set("gg", 40).set("dt", 50);//栏目

        Flipper flipper = new Flipper().offset((curr-1) * 20).limit(20).sort("top DESC,createtime DESC");
        //帖子列表
        FilterNode filterNode = FilterNode.create("status", NOTEQUAL, -10).and("type", column.getAs(para));
        if (solved > 0) filterNode.and("solved", 20);
        if (wonderful > 0) filterNode.and("wonderful", 20);
        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, setPrivate(request, setPrivate(request, filterNode)));

        //热帖
        Sheet<ContentInfo> hotView = Sheet.empty();//logQueue.hotView(sessionid); TODO: 依赖日志记录，需记录日志后可使用

        Kv kv = Kv.by("contents", contents).set("hotView", hotView)
                .set("solved", solved).set("wonderful", wonderful)
                .set("column", para).set("curr", curr);
        response.finish(HttpScope.refer("/jie/index.html").attr(kv));
    }
}
