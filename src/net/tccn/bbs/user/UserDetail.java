package net.tccn.bbs.user;

import lombok.Getter;
import lombok.Setter;
import org.redkale.convert.json.JsonConvert;
import org.redkale.util.Utility;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lxyer
 */
@Setter
@Getter
@Cacheable(interval = 5 * 60)
@Table(comment = "用户表")
public class UserDetail implements java.io.Serializable {

    @Id
    @Column(comment = "[用户id]")
    private int userid;

    @Column(length = 32, comment = "[登录名]")
    private String username = "";

    @Column(length = 64, comment = "[密码]")
    private String password = "";

    @Column(comment = "[性别]默认 10男，20女")
    private short sex;

    @Column(length = 32, comment = "[电话号码]")
    private String phone = "";

    @Column(length = 64, comment = "[昵称]")
    private String nickname = "";

    @Column(length = 128, comment = "[头像地址]")
    private String avatar = "";

    @Column(length = 32, comment = "[真实姓名]")
    private String realname = "";

    @Column(length = 32, comment = "[邮箱]")
    private String email = "";

    @Column()
    private int roleid;

    @Column(length = 128, comment = "[个人博客地址]")
    private String site = "";

    @Column(length = 128, comment = "[码云/GitHub]")
    private String git = "";

    @Column(updatable = false, comment = "[创建时间]")
    private long createtime;

    @Column(length = 256, comment = "[签名]")
    private String sign = "";

    @Column(length = 64, comment = "[所在城市]")
    private String city = "";

    @Column(comment = "[状态]-10删除 10正常")
    private short status = 10;


    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }

    //------

    public UserInfo createUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid(userid);
        userInfo.setUsername(username);
        userInfo.setSex(sex);
        userInfo.setPassword(password);
        userInfo.setPhone(phone);
        userInfo.setNickname(nickname);
        userInfo.setAvatar(avatar);
        userInfo.setRelaname(realname);
        userInfo.setEmail(email);
        userInfo.setRoleid(roleid);
        userInfo.setSite(site);
        userInfo.setGit(git);
        userInfo.setCreatetime(createtime);
        userInfo.setSign(sign);
        userInfo.setCity(city);
        userInfo.setStatus(getStatus());
        return userInfo;
    }

    public String passwordForMd5() {
        return md5IfNeed(password);
    }

    public static String md5IfNeed(String password) {
        return Utility.md5Hex(password);
    }
}
