package com.lxyer.bbs.base.user;

import org.redkale.convert.ConvertColumn;
import org.redkale.convert.ConvertType;
import org.redkale.convert.json.JsonConvert;
import org.redkale.util.Utility;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author lxyer
 */
@Cacheable(interval = 5*60)
@Table(catalog = "redbbs", name = "user")
public class User implements java.io.Serializable {

    @Id
    //@GeneratedValue
    @Column(comment = "[用户id]", updatable = false)
    private int userId;

    @Column(length = 32, comment = "[登录名]")
    private String username = "";

    @Column(comment = "[性别]默认1 1男，2女")
    private int sex = 1;

    @Column(length = 64, comment = "[密码]")
    private String password = "";

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

    @Column(length = 2, comment = "[用户角色]")
    private int roleId = 0;

    @Column(length = 128, comment = "[个人博客地址]")
    private String site = "";

    @Column(length = 128, comment = "[码云/GitHub]")
    private String git = "";

    @Column(comment = "[创建时间]", updatable = false)
    private long createTime;

    @Column(length = 256, comment = "[签名]")
    private String sign = "";

    @Column(length = 64, comment = "[所在城市]")
    private String city = "";

    @Column(comment = "[状态]")
    private int status = 1;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @ConvertColumn(ignore = true, type = ConvertType.JSON)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return (email == null || email.isEmpty()) ? " " : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }

    public UserInfo createUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUsername(username);
        userInfo.setSex(sex);
        userInfo.setPassword(password);
        userInfo.setPhone(phone);
        userInfo.setNickname(nickname);
        userInfo.setAvatar(avatar);
        userInfo.setRelaname(realname);
        userInfo.setEmail(email);
        userInfo.setRoleId(roleId);
        userInfo.setSite(site);
        userInfo.setGit(git);
        userInfo.setCreateTime(createTime);
        userInfo.setSign(sign);
        userInfo.setCity(city);
        userInfo.setStatus(getStatus());
        return userInfo;
    }
}
