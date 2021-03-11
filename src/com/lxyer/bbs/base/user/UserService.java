package com.lxyer.bbs.base.user;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.BaseService;
import com.lxyer.bbs.base.Utils;
import com.lxyer.bbs.base.kit.RetCodes;
import org.redkale.net.http.*;
import org.redkale.service.RetResult;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterFunc;
import org.redkale.source.FilterNode;
import org.redkale.source.Flipper;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.lxyer.bbs.base.kit.RetCodes.RET_USER_ACCOUNT_PWD_ILLEGAL;

/**
 * Created by Lxy at 2017/10/3 14:02.
 */
@RestService(automapping = true, comment = "用户服务")
public class UserService extends BaseService {

    @RestMapping(auth = false, comment = "登录校验")
    public RetResult<UserInfo> login(LoginBean bean) {
        if (bean == null || bean.emptyUsername()) {
            return retError("参数错误");
        }

        final RetResult retResult = new RetResult();

        UserRecord user = dataSource.find(UserRecord.class, "username", bean.getUsername());
        if (user == null || !Objects.equals(user.getPassword(), bean.getPassword())) {
            //log(null, 0, "用户或密码错误");
            return RetCodes.retResult(RET_USER_ACCOUNT_PWD_ILLEGAL, "用户名或密码错误");
        }
        sessions.setAsync(sessionExpireSeconds, bean.getSessionid(), (long) user.getUserid());
        retResult.setRetcode(0);
        retResult.setResult(Kv.by("token", bean.getSessionid()));
        retResult.setRetinfo("登录成功.");
        return retResult;
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
        UserRecord user = dataSource.find(UserRecord.class, userid);
        UserInfo bean = user.createUserInfo();
        return bean;
    }

    @RestMapping(name = "logout", auth = false, comment = "退出登录")
    public RetResult logout(@RestSessionid String sessionid) {
        sessions.remove(sessionid);

        return RetResult.success();
        //return new HttpResult().header("Location", "/").status(302);
    }

    @RestMapping(name = "query", auth = false, comment = "用户数据查询")
    public Sheet<UserRecord> query(Flipper flipper, @RestParam(name = "bean", comment = "过滤条件") final UserBean userBean) {
        Sheet<UserRecord> users = dataSource.querySheet(UserRecord.class, flipper, userBean);

        return users;
    }

    @RestMapping(name = "changepwd", comment = "修改密码")
    public RetResult updatePwd(@RestUserid int userid, String pass, String nowpass) {
        UserInfo user = find(userid);
        if (!Objects.equals(user.getPassword(), UserRecord.md5IfNeed(nowpass))) {
            return retError("密码错误");
        }
        if (pass == null || pass.length() < 6 || Objects.equals(pass, nowpass)) {
            return retError("密码设置无效");
        }
        dataSource.updateColumn(UserRecord.class, user.getUserid(), "password", UserRecord.md5IfNeed(pass));
        return RetResult.success();
    }

    @RestMapping(name = "register", auth = false, comment = "用户注册")
    public RetResult register(UserRecord bean) {
        /*用户名、密码、邮箱*/
        if (bean.getEmail() == null) {
            return retError("邮件地址无效");
        }
        if (bean.getPassword() == null || bean.getPassword().length() < 6) {
            return retError("密码设置无效");
        }
        if (dataSource.exists(UserRecord.class, FilterNode.create("email", bean.getEmail()))) {
            return retError("用户名已存在");
        }

        bean.setCreatetime(System.currentTimeMillis());
        bean.setPassword(bean.passwordForMd5());
        bean.setStatus((short) 10);
        bean.setUsername(bean.getEmail());
        bean.setAvatar("/res/images/avatar/" + new Random().nextInt(21) + ".jpg");//默认头像

        synchronized (this) {
            int maxid = dataSource.getNumberResult(UserRecord.class, FilterFunc.MAX, 10_0000, "userid").intValue();
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
    public RetResult userUpdate(@RestUserid int userid, UserRecord bean, String[] columns) {
        String nickname = bean.getNickname();
        if (nickname == null && nickname.isEmpty()) {
            return retError("昵称无效");
        }

        nickname = nickname.replace(" ", "");
        FilterNode node = FilterNode.create("nickname", nickname).and("userid", FilterExpress.NOTEQUAL, userid);
        if (dataSource.exists(UserRecord.class, node)) {
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
        Sheet<UserRecord> users = dataSource.querySheet(UserRecord.class
                , SelectColumn.includes("userid", "nickname", "avatar", "createtime")
                , new Flipper().sort("createtime DESC").limit(8)
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
        return dataSource.getNumberResult(UserRecord.class, FilterFunc.COUNT, "userid", FilterNode.create("status", FilterExpress.NOTEQUAL, -10));
    }

    @RestMapping(ignore = true, comment = "判断用户是否是管理员")
    public boolean isAdmin(int userid) {
        if (userid <= 0) {
            return false;
        }

        List<Integer> userIds = dataSource.queryColumnList("userid", UserRecord.class, FilterNode.create("roleid", 1));
        for (Integer x : userIds) {
            if (userid == x) {
                return true;
            }
        }

        return false;
    }
}
