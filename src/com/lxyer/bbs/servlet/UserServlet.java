package com.lxyer.bbs.servlet;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.BaseServlet;
import com.lxyer.bbs.base.user.User;
import com.lxyer.bbs.base.user.UserBean;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.comment.CommentInfo;
import com.lxyer.bbs.content.ContentBean;
import com.lxyer.bbs.content.ContentInfo;
import org.redkale.net.http.HttpMapping;
import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.WebServlet;
import org.redkale.source.Flipper;
import org.redkale.util.Sheet;

/**
 * 用户相关的servlet
 * Created by liangxianyou at 2018/6/4 13:12.
 */
@WebServlet({"/user", "/user/*"})
public class UserServlet extends BaseServlet {


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
}
