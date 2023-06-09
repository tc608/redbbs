package net.tccn.bbs.servlet;

import com.jfinal.kit.Kv;
import net.tccn.bbs.base.BaseServlet;
import net.tccn.bbs.comment.CommentInfo;
import net.tccn.bbs.content.ContentInfo;
import net.tccn.bbs.user.UserBean;
import net.tccn.bbs.user.UserInfo;
import net.tccn.bbs.user.UserDetail;
import org.redkale.net.http.*;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterNode;
import org.redkale.source.Flipper;
import org.redkale.util.Sheet;

/**
 * 用户相关的servlet
 * Created by liangxianyou at 2018/6/4 13:12.
 */
@WebServlet(value = {"/user", "/user/*"}, comment = "用户请求入口")
public class UserServlet extends BaseServlet {


    @HttpMapping(url = "/user/login", auth = false, comment = "前往登录页")
    public void login(HttpRequest request, HttpResponse response) {

        response.finish(HttpScope.refer("/user/login.html"));
    }

    @HttpMapping(url = "/user/reg", auth = false, comment = "前往登录页")
    public void reg(HttpRequest request, HttpResponse response) {


        response.finish(HttpScope.refer("/user/login.html"));
    }

    @HttpMapping(url = "/user/set", comment = "用户设置")
    public void set(HttpRequest request, HttpResponse response) {
        response.finish(HttpScope.refer("/user/set.html"));
    }


    @HttpMapping(url = "/user", auth = false, comment = "用户首页")
    public void user(HttpRequest request, HttpResponse response) {
        Integer userid = request.currentUserid(int.class);
        String para = getPara(request);

        //-------个人中心---------
        if ("user".equals(para) || "".equals(para)) {
            UserInfo user = request.currentUser();
            if (user == null) {
                response.finish(HttpScope.refer("/user/login.html"));
                return;
            }

            //创建的帖子
            Flipper flipper = new Flipper(8,"createtime DESC");

            FilterNode node = FilterNode.create("userid", user.getUserid()).and("status", FilterExpress.NOTEQUAL, -10);
            Sheet<ContentInfo> contents = contentService.contentQuery(flipper, setPrivate(request, node));//queryByBean(flipper, bean);

            //收藏的帖子
            Sheet<ContentInfo> collects = contentService.collectQuery(userid);

            Kv kv = Kv.by("contents", contents).set("collects", collects);
            response.finish(HttpScope.refer("/user/index.html").attr(kv));
            return;
        }

        //-------用户主页------
        if ("nick".equals(para)) {//通过@ 点击跳转
            String nickname = request.getParameter("nickname");
            UserBean userBean = new UserBean();
            userBean.setNickname(nickname);
            Sheet<UserDetail> users = userService.query(new Flipper(1), userBean);
            if (users.getTotal() > 0) {
                userid = users.stream().findFirst().orElse(null).getUserid();
            }
        } else {//直接访问
            userid = getParaToInt(request, 0);
        }

        //用户信息
        UserInfo user = userService.find(userid);

        //帖子
        Flipper flipper = new Flipper(8, "createtime DESC");
        FilterNode node = FilterNode.create("userid", userid).and("status", FilterExpress.NOTEQUAL, -10);
        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, setPrivate(request, node));

        //回复
        Sheet<CommentInfo> comments = commentService.queryByUserid(userid);

        Kv kv = Kv.by("contents", contents).set("user", user).set("comments", comments);
        response.finish(HttpScope.refer("/user/home.html").attr(kv));
    }
}
