package com.lxyer.bbs.servlet;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.BaseServlet;
import com.lxyer.bbs.base.user.User;
import com.lxyer.bbs.base.user.UserBean;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.comment.CommentInfo;
import com.lxyer.bbs.comment.CommentService;
import com.lxyer.bbs.content.ContentBean;
import com.lxyer.bbs.content.ContentInfo;
import com.lxyer.bbs.content.ContentService;
import org.redkale.net.http.*;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterNode;
import org.redkale.source.Flipper;
import org.redkale.util.Sheet;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Created by Lxy at 2017/11/25 12:31.
 */
@WebServlet({"/","/column","/column/*"
        ,"/user", "/user/*"
        ,"/jie" ,"/jie/*"
        ,"/upload","/upload/*"
})
public class IndexServlet extends BaseServlet {

    @Resource
    private ContentService contentService;

    @Resource
    private CommentService commentService;

    @HttpMapping(url = "/", auth = false, comment = "社区首页")
    public void abc(HttpRequest request, HttpResponse response){
        Flipper flipper = new Flipper().limit(30).sort("top DESC,createTime DESC");
        //置顶贴
        FilterNode topNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -1).and("top", FilterExpress.GREATERTHAN, 0);
        Sheet<ContentInfo> top = contentService.contentQuery(flipper, topNode);

        //非置顶贴
        FilterNode untopNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -1).and("top", 0);
        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, untopNode);

        //热帖
        /*Flipper flipper2 = new Flipper().limit(8).sort("viewNum DESC");
        Sheet<ContentInfo> hotView = contentService.contentQuery(flipper2, "");*/

        //热议
        Flipper flipper3 = new Flipper().limit(8).sort("replyNum DESC");
        Sheet<ContentInfo> hotReply = contentService.contentQuery(flipper3, "");

        //最新加入
        Sheet<UserInfo> lastReg = userService.lastReg();

        //用户统计
        Number userCount = userService.userCount();

        Kv kv = Kv.by("top", top).set("contents", contents).set("hotReply", hotReply).set("lastReg", lastReg).set("userCount", userCount);
        finish("index.html", kv);
    }

    @HttpMapping(url = "/column", auth = false, comment = "社区首页")
    public void column(HttpRequest request, HttpResponse response){
        String para = getPara();//空，qz，fx，jy，gg，dt，
        int solved = request.getIntParameter("solved", -1);
        int wonderful = request.getIntParameter("wonderful", -1);
        int curr = request.getIntParameter("curr", 1);

        Kv column = Kv.by("qz", 10).set("fx", 20).set("jy", 30).set("gg", 40).set("dt", 50);//栏目

        Flipper flipper = new Flipper().offset((curr-1) * 20).limit(20).sort("top DESC,createTime DESC");
        //帖子列表
        FilterNode filterNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -1).and("type", column.getAs(para));
        if (solved > -1) filterNode.and("solved", solved);
        if (wonderful > -1) filterNode.and("wonderful", wonderful);

        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, filterNode);

        //热议
        Flipper flipper3 = new Flipper().limit(8).sort("replyNum DESC");
        Sheet<ContentInfo> hotReply = contentService.contentQuery(flipper3, "");


        Kv kv = Kv.by("contents", contents).set("hotReply", hotReply)
                .set("solved", solved).set("wonderful", wonderful).set("column", para).set("curr", curr);
        finish("/jie/index.html", kv);
    }

    @HttpMapping(url = "/site", auth = false, comment = "网站首页")
    public void site(HttpRequest request, HttpResponse response){

        finish("/site.html");
    }

    //====================================用户相关====================================

    @HttpMapping(url = "/user/login", auth = false, comment = "前往登录页")
    public void login(HttpRequest request, HttpResponse response){

        finish("/user/login.html");
    }
    @HttpMapping(url = "/user/reg", auth = false, comment = "前往登录页")
    public void reg(HttpRequest request, HttpResponse response){
        /*List<Kv> list = new ArrayList<>();
        list.add(Kv.by("k", 1).set("a", "1+1=?").set("q", 2));
        list.add(Kv.by("k", 2).set("a", "1*1=?").set("q", 1));
        list.add(Kv.by("k", 3).set("a", "3+2-5=?").set("q", 0));
        list.add(Kv.by("k", 4).set("a", "Math.abs(-3)=?").set("q", 3));*/

        finish("/user/login.html");
    }

    @HttpMapping(url = "/user/set", auth = true, comment = "用户设置")
    public void set(HttpRequest request, HttpResponse response){
        finish("/user/set.html");
    }


    @HttpMapping(url = "/user", auth = false, comment = "用户首页")
    public void user(HttpRequest request, HttpResponse response){
        String para = getPara();

        //-------个人中心---------
        if ("user".equals(para) || "".equals(para)){
            UserInfo user = request.currentUser();
            if (user == null){
                finish("/user/login.html");
                return;
            }

            //创建的帖子
            Flipper flipper = new Flipper().limit(8).sort("createTime DESC");
            ContentBean bean = new ContentBean();
            bean.setUserId(user.getUserId());
            Sheet<ContentInfo> contents = contentService.queryByBean(flipper, bean);

            //收藏的帖子
            Sheet<ContentInfo> collects = contentService.collectQuery(sessionid);

            Kv kv = Kv.by("contents", contents).set("collects", collects);
            finish("/user/index.html", kv);
            return;
        }

        //-------用户主页------
        int userId = 0;
        if ("nick".equals(para)){//通过@ 点击跳转
            String nickname = request.getParameter("nickname");
            UserBean userBean = new UserBean();
            userBean.setNickname(nickname);
            Sheet<User> users = userService.queryUser(new Flipper().limit(1), userBean);
            if (users.getTotal() > 0){
                userId = users.stream().findFirst().orElse(null).getUserId();
            }
        }else {//直接访问
            userId = getParaToInt(0);
        }

        //用户信息
        UserInfo user = userService.findUserInfo(userId);

        //帖子
        Flipper flipper = new Flipper().limit(8).sort("createTime DESC");
        ContentBean bean = new ContentBean();
        bean.setUserId(userId);
        Sheet<ContentInfo> contents = contentService.queryByBean(flipper, bean);

        //回复
        Sheet<CommentInfo> comments = commentService.queryByUserid(userId);

        Kv kv = Kv.by("contents", contents).set("user", user).set("comments", comments);
        finish("/user/home.html", kv);
    }

    //====================================帖子相关====================================


    @HttpMapping(url = "/jie", auth = false, comment = "问答列表")
    public void jie(HttpRequest request, HttpResponse response){
        String actived = getPara(0, "all");
        int curr = request.getIntParameter("curr", 1);

        //分页帖子列表
        Flipper flipper = new Flipper().offset((curr-1)*15).limit(15).sort("top DESC,createTime DESC");
        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, actived);

        Kv kv = Kv.by("contents", contents).set("url", request.getRequestURI())
                .set("actived", actived).set("curr", curr);
        finish("/jie/index.html", kv);
    }

    @HttpMapping(url = "/jie/add", comment = "发表/编辑问答")
    @HttpParam(name = "#", type = int.class, comment = "内容ID")
    public void add(HttpRequest request, HttpResponse response){
        int contentid = getParaToInt(0);

        ContentInfo contentInfo = null;
        if (contentid > 0){
            contentInfo = contentService.contentInfo(sessionid, contentid);
        }

        finish("/jie/add.html", Kv.by("bean", contentInfo));
    }

    @HttpMapping(url = "/jie/detail", auth = false, comment = "问答详情")
    public void detail(HttpRequest request, HttpResponse response){
        int contentid = getParaToInt(0);

        ContentInfo content = contentService.contentInfo(sessionid, contentid);
        Sheet<CommentInfo> comments = commentService.commentQuery(request.getSessionid(false) ,contentid, new Flipper().limit(30));

        //热帖
        //Flipper flipper2 = new Flipper().limit(8).sort("viewNum DESC");
        //Sheet<ContentInfo> hotView = contentService.contentQuery(flipper2, "");

        //热议
        Flipper flipper3 = new Flipper().limit(8).sort("replyNum DESC");
        Sheet<ContentInfo> hotReply = contentService.contentQuery(flipper3, "");

        //更新
        CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                User user = request.currentUser();
                if (user == null || user.getUserId() > 10_0003)
                    contentService.incrViewNum(contentid);
                return "";
            }
        });

        Kv kv = Kv.by("bean", content).set("comments", comments)/*.set("hotView", hotView)*/.set("hotReply", hotReply);
        finish("/jie/detail.html", kv);
    }


    //====================================帖子相关====================================

    //====================================上传相关====================================、

    private static final String dir = "/var/www/upload/redbbs/";
    private static final String view = "http://img.1216.top/redbbs/";
    private static final String format = "%1$tY%1$tm%1$td%1$tH%1$tM%1$tS";
    protected static final boolean winos = System.getProperty("os.name").contains("Window");
    @HttpMapping(url = "/upload/img", auth = false, comment = "图片上传")
    public void uploadImg(HttpRequest request, HttpResponse response){
        try {
            Map ret = new HashMap();
            ret.put("errno", 0);
            List data = new ArrayList();

            for (MultiPart part : request.multiParts()) {
                String name = part.getName();
                String suffix = name.substring(name.lastIndexOf("."));
                String path = String.format(format, System.currentTimeMillis()) + suffix;
                File destFile = new File((winos ? "D:/wk/_own/redbbs/root/tem/" : dir) + path);
                destFile.getParentFile().mkdir();

                part.save(destFile);

                data.add((winos ? "/tem/": view) + path);
            }
            ret.put("data", data);

            response.setContentType("application/json; charset=utf-8");
            response.finish(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
