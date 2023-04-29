package net.tccn.bbs.user;

import com.jfinal.kit.Kv;
import net.tccn.bbs.base.BaseService;
import net.tccn.bbs.base.Utils;
import net.tccn.bbs.base.util.RetCodes;
import org.redkale.net.http.*;
import org.redkale.service.RetResult;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterFunc;
import org.redkale.source.FilterNode;
import org.redkale.source.Flipper;
import org.redkale.util.Comment;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import java.io.Serializable;
import java.util.*;

import static net.tccn.bbs.base.util.RetCodes.RET_USER_ACCOUNT_PWD_ILLEGAL;

/**
 * Created by Lxy at 2017/10/3 14:02.
 */
@RestService(comment = "用户服务")
public class UserService extends BaseService {

    @RestMapping(name = "login", auth = false, comment = "登录校验")
    public RetResult<UserInfo> login(LoginBean bean) {
        if (bean == null || bean.emptyUsername()) {
            return retError("参数错误");
        }

        RetResult retResult = new RetResult();

        UserDetail user = dataSource.find(UserDetail.class, "username", bean.getUsername());
        if (user == null || !Objects.equals(user.getPassword(), Utils.genMd5(bean.getPassword()))) {
            //log(null, 0, "用户或密码错误");
            return RetCodes.retResult(RET_USER_ACCOUNT_PWD_ILLEGAL, "用户名或密码错误");
        }
        sessions.setAsync(sessionExpireSeconds, bean.getSessionid(), (long) user.getUserid());
        retResult.setRetcode(0);
        retResult.setResult(Kv.by("token", bean.getSessionid()));
        retResult.setRetinfo("登录成功.");
        return retResult;
    }

    @RestMapping(ignore = true)
    public int currentUserid(String sessionid) {
        if (sessionid == null) return 0;
        long userid = 0;
        try {
            userid = sessions.getLong(sessionid, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) userid;
    }

    public UserInfo current(String sessionid) {
        if (sessionid == null) {
            return null;
        }

        long userid = 0;
        try {
            userid = sessions.getLong(sessionid, 0);
            sessions.getAndRefresh(sessionid, sessionExpireSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userid == 0) {
            return null;
        }
        UserInfo info = find((int) userid);
        return info;
    }

    @RestMapping(name = "info", comment = "用户信息")
    public UserInfo find(int userid) {
        if (userid == 0) {
            return null;
        }
        UserDetail user = dataSource.find(UserDetail.class, userid);
        UserInfo bean = user.createUserInfo();
        return bean;
    }

    @Comment("根据用户ID, 批量查询一批用户信息")
    public Map<Integer, UserInfo> queryUserMap(Collection<Integer> userids) {
        if (Utils.isEmpty(userids)) {
            return Map.of();
        }

        FilterNode node = FilterNode.create("userid", (Serializable) userids);
        List<UserDetail> records = dataSource.queryList(UserDetail.class, node);
        Map<Integer, UserInfo> map = Utils.toMap(records, x -> x.getUserid(), x -> x.createUserInfo());
        return map;
    }

    @RestMapping(name = "logout", auth = false, comment = "退出登录")
    public RetResult logout(@RestSessionid String sessionid) {
        sessions.remove(sessionid);

        return RetResult.success();
        //return new HttpResult().header("Location", "/").status(302);
    }

    @RestMapping(name = "query", auth = false, comment = "用户数据查询")
    public Sheet<UserDetail> query(Flipper flipper, @RestParam(name = "bean", comment = "过滤条件") final UserBean userBean) {
        Sheet<UserDetail> users = dataSource.querySheet(UserDetail.class, flipper, userBean);

        return users;
    }

    @RestMapping(name = "changepwd", comment = "修改密码")
    public RetResult updatePwd(@RestUserid int userid, String pass, String nowpass) {
        UserInfo user = find(userid);
        if (!Objects.equals(user.getPassword(), UserDetail.md5IfNeed(nowpass))) {
            return retError("密码错误");
        }
        if (pass == null || pass.length() < 6 || Objects.equals(pass, nowpass)) {
            return retError("密码设置无效");
        }
        dataSource.updateColumn(UserDetail.class, user.getUserid(), "password", UserDetail.md5IfNeed(pass));
        return RetResult.success();
    }

    @RestMapping(name = "register", auth = false, comment = "用户注册")
    public RetResult register(UserDetail bean) {
        /*用户名、密码、邮箱*/
        if (bean.getEmail() == null) {
            return retError("邮件地址无效");
        }
        if (bean.getPassword() == null || bean.getPassword().length() < 6) {
            return retError("密码设置无效");
        }
        if (dataSource.exists(UserDetail.class, FilterNode.create("email", bean.getEmail()))) {
            return retError("用户名已存在");
        }

        bean.setCreatetime(System.currentTimeMillis());
        bean.setPassword(bean.passwordForMd5());
        bean.setStatus((short) 10);
        bean.setUsername(bean.getEmail());
        bean.setAvatar("/res/images/avatar/" + new Random().nextInt(21) + ".jpg");//默认头像

        synchronized (this) {
            int maxid = dataSource.getNumberResult(UserDetail.class, FilterFunc.MAX, 10_0000, "userid").intValue();
            if (maxid < 10_0000) {
                maxid = 10_0000;
            }
            bean.setUserid(maxid + 1);
        }
        dataSource.insert(bean);

        //记录日志
        return RetResult.success();
    }

    @RestMapping(name = "update", comment = "用户信息修改")
    public RetResult userUpdate(@RestUserid int userid, UserDetail bean, String[] columns) {
        String nickname = bean.getNickname();
        if (nickname == null && nickname.isEmpty()) {
            return retError("昵称无效");
        }

        nickname = nickname.replace(" ", "");
        FilterNode node = FilterNode.create("nickname", nickname).and("userid", FilterExpress.NOTEQUAL, userid);
        if (dataSource.exists(UserDetail.class, node)) {
            return retError("昵称已存在");
        }

        bean.setNickname(nickname);//去除昵称中的空格
        dataSource.updateColumn(bean
                , FilterNode.create("userid", userid)
                , SelectColumn.includes(columns)
        );
        return RetResult.success();
    }

    //最新加入
    public Sheet<UserInfo> lastReg() {
        Sheet<UserDetail> users = dataSource.querySheet(UserDetail.class
                , SelectColumn.includes("userid", "nickname", "avatar", "createtime")
                , new Flipper(8, "createtime DESC")
                , FilterNode.create("status", 10));

        Sheet<UserInfo> infos = new Sheet<>();
        ArrayList<UserInfo> list = new ArrayList<>();

        users.forEach(x -> {
            UserInfo info = x.createUserInfo();
            info.setTime(Utils.dateFmt(x.getCreatetime()));
            list.add(info);
        });

        infos.setRows(list);
        infos.setTotal(users.getTotal());

        return infos;
    }

    @RestMapping(name = "usercount", auth = false, comment = "用户数据统计")
    public Number userCount() {
        return dataSource.getNumberResult(UserDetail.class, FilterFunc.COUNT, "userid", FilterNode.create("status", FilterExpress.NOTEQUAL, -10));
    }

    @RestMapping(ignore = true, comment = "判断用户是否是管理员")
    public boolean isAdmin(int userid) {
        if (userid <= 0) {
            return false;
        }

        List<Integer> userIds = dataSource.queryColumnList("userid", UserDetail.class, FilterNode.create("roleid", 1));
        for (Integer x : userIds) {
            if (userid == x) {
                return true;
            }
        }

        return false;
    }
}
