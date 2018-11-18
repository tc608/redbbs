package com.lxyer.bbs.base.user;

import com.lxyer.bbs.base.BaseService;
import com.lxyer.bbs.base.kit.LxyKit;
import com.lxyer.bbs.base.kit.RetCodes;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestParam;
import org.redkale.net.http.RestService;
import org.redkale.net.http.RestSessionid;
import org.redkale.service.RetResult;
import org.redkale.source.*;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.lxyer.bbs.base.kit.RetCodes.*;

/**
 * Created by Lxy at 2017/10/3 14:02.
 */
@RestService(automapping = true, comment = "用户服务")
public class UserService extends BaseService {

    @RestMapping(auth = false, comment = "登录校验")
    public RetResult<UserInfo> login(@RestParam(name = "bean") LoginBean loginBean){
        if (loginBean == null || loginBean.emptyUsername()) return RetCodes.retResult(RetCodes.RET_PARAMS_ILLEGAL, "参数错误");

        final RetResult retResult = new RetResult();

        UserRecord user = source.find(UserRecord.class, "username", loginBean.getUsername());
        if (user == null || !Objects.equals(user.getPassword(), loginBean.getPassword())){
            //log(null, 0, "用户或密码错误");
            return RetCodes.retResult(RetCodes.RET_USER_ACCOUNT_PWD_ILLEGAL, "用户或密码错误");
        }

        //log(user, 0, "用户登录成功.");
        UserInfo userInfo = user.createUserInfo();

        sessions.setAsync(sessionExpireSeconds, loginBean.getSessionid(), userInfo.getUserid());
        retResult.setRetcode(0);
        retResult.setResult(userInfo);
        retResult.setRetinfo("登录成功.");
        return retResult;
    }

    public UserInfo current(String sessionid){
        if (sessionid == null) return null;

        long userid = 0;
        try {
            userid = sessions.getLong(sessionid, 0);
            sessions.getAndRefresh(sessionid, sessionExpireSeconds);
        }catch (Exception e){
            e.printStackTrace();
        }
        return userid == 0 ? null : findUserInfo((int)userid);
    }

    @RestMapping(name = "info", comment = "用户信息")
    public UserInfo findUserInfo(int userid) {
        UserRecord user = source.find(UserRecord.class, userid);
        return user == null ? null : user.createUserInfo();
    }

    @RestMapping(name = "logout", auth = false, comment = "退出登录")
    public RetResult logout(@RestSessionid String sessionid){
        sessions.remove(sessionid);

        return RetResult.success();
        //return new HttpResult().header("Location", "/").status(302);
    }

    @RestMapping(name = "query", auth = false, comment = "用户数据查询")
    public Sheet<UserRecord> queryUser(Flipper flipper, @RestParam(name = "bean", comment = "过滤条件") final UserBean userBean){
        Sheet<UserRecord> users = source.querySheet(UserRecord.class, flipper, userBean);

        return users;
    }

    @RestMapping(name = "changepwd", comment = "修改密码")
    public RetResult updatePwd(@RestSessionid String sessionid, String pass, String nowpass){
        UserInfo userInfo = current(sessionid);//不会为空

        if (!Objects.equals(userInfo.getPassword(), UserRecord.md5IfNeed(nowpass)))
            return RetCodes.retResult(RET_USER_ACCOUNT_PWD_ILLEGAL, "密码错误");
        if (pass == null || pass.length() < 6 || Objects.equals(pass, nowpass))
            return RetCodes.retResult(RET_USER_PASSWORD_ILLEGAL, "密码设置无效");
        source.updateColumn(UserRecord.class, userInfo.getUserid(), "password", UserRecord.md5IfNeed(pass));
        return RetResult.success();
    }

    @RestMapping(name = "register", auth = false, comment = "用户注册")
    public RetResult register(@RestParam(name = "bean") UserRecord user){
        /*用户名、密码、邮箱*/
        if (user.getEmail() == null) return RetCodes.retResult(RET_USER_EMAIL_ILLEGAL, "邮件地址无效");
        if (user.getPassword() == null || user.getPassword().length() < 6) return RetCodes.retResult(RET_USER_PASSWORD_ILLEGAL, "密码设置无效");

        UserRecord _user = source.find(UserRecord.class, FilterNode.create("email", user.getEmail()));
        if (_user != null) return RetCodes.retResult(RET_USER_USERNAME_EXISTS, "用户名已存在");

        user.setCreatetime(System.currentTimeMillis());
        user.setPassword(user.passwordForMd5());
        user.setStatus((short) 10);
        user.setUsername(user.getEmail());
        user.setAvatar("/res/images/avatar/"+ new Random().nextInt(21) +".jpg");//默认头像

        int maxId = source.getNumberResult(UserRecord.class, FilterFunc.MAX,  10_0000, "userid").intValue();
        if (maxId < 10_0000) maxId = 10_0000;
        user.setUserid(maxId+1);
        source.insert(user);

        //记录日志
        return RetResult.success();
    }

    @RestMapping(name = "update", comment = "用户信息修改")
    public RetResult userUpdate(@RestSessionid String sessionid, @RestParam(name = "bean") UserRecord user, String[] columns){
        String nickname = user.getNickname();
        if (nickname == null && nickname.isEmpty())
            return RetCodes.retResult(RET_USER_NICKNAME_ILLEGAL, "昵称无效");

        nickname = nickname.replace(" ", "");
        UserRecord _user = source.find(UserRecord.class, FilterNode.create("nickname", nickname));
        if (_user != null && _user.getUserid() != currentUserid(sessionid))
            return RetCodes.retResult(RET_USER_NICKNAME_EXISTS, "昵称已存在");

        user.setNickname(nickname);//去除昵称中的空格
        source.updateColumn(user
                ,FilterNode.create("userid", currentUserid(sessionid))
                ,SelectColumn.includes(columns)
        );
        return RetResult.success();
    }

    //最新加入
    public Sheet<UserInfo> lastReg(){
        Sheet<UserRecord> users = source.querySheet(UserRecord.class
                , SelectColumn.includes("userid", "nickname", "avatar", "createtime")
                , new Flipper().sort("createtime DESC").limit(8)
                , FilterNode.create("status", 10));

        Sheet<UserInfo> infos = new Sheet<>();
        ArrayList<UserInfo> list = new ArrayList<>();

        users.forEach(x->{
            UserInfo info = x.createUserInfo();
            info.setTime(LxyKit.dateFmt(x.getCreatetime()));
            list.add(info);
        });

        infos.setRows(list);
        infos.setTotal(users.getTotal());

        return infos;
    }

    @RestMapping(name = "usercount", auth = false, comment = "用户数据统计")
    public Number userCount() {
        return source.getNumberResult(UserRecord.class, FilterFunc.COUNT, "userid", FilterNode.create("status", FilterExpress.NOTEQUAL, -10));
    }

    @RestMapping(ignore = true, comment = "判断用户是否是管理员")
    public boolean isAdmin(int userid){
        if (userid <= 0) return false;

        List<Integer> userIds = source.queryColumnList("userid", UserRecord.class, FilterNode.create("roleid", 1));
        for (Integer x : userIds) {
            if (userid == x) {
                return true;
            }
        }

        return false;
    }
}
