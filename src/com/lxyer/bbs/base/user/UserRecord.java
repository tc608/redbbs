package com.lxyer.bbs.base.user;

import javax.persistence.*;
import org.redkale.convert.json.*;
import org.redkale.util.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author lxyer
 */
@Cacheable(interval = 5*60)
@Table(catalog = "redbbs", name = "sys_userrecord")
public class UserRecord implements java.io.Serializable {

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

    @Column(comment = "")
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

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setSex(short sex) {
        this.sex = sex;
    }

    public short getSex() {
        return this.sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRealname() {
        return this.realname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getRoleid() {
        return this.roleid;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSite() {
        return this.site;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public String getGit() {
        return this.git;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getCreatetime() {
        return this.createtime;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return this.sign;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public short getStatus() {
        return this.status;
    }

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

    public String passwordForMd5(){
        return md5IfNeed(password);
    }

    public static String md5IfNeed(String password){
        if (password == null || password.isEmpty()) return "";
        if (password.length() == 32) return password;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] bytes = password.trim().getBytes();
        bytes = md5.digest(bytes);
        return new String(Utility.binToHex(bytes));
    }
}