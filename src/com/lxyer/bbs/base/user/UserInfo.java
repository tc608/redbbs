package com.lxyer.bbs.base.user;

import org.redkale.convert.ConvertColumn;
import org.redkale.convert.ConvertType;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 *
 * @author lxyer
 */
public class UserInfo implements java.io.Serializable {

    @Id
    @Column(comment = "[用户id]")
    private int userId;

    private String username = "";
    private int sex = 1;
    private String password = "";
    private String phone = "";
    private String nickname = "";
    private String avatar = "";
    private String relaname = "";
    private String email = "";
    private int roleId = 0;
    private long createTime;
    private String sign = "";
    private String city = "";
    private int status = 1;
    private String time = "";

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @ConvertColumn(type = ConvertType.JSON)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setRelaname(String relaname) {
        this.relaname = relaname;
    }

    public String getRelaname() {
        return this.relaname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email == null || this.email.isEmpty() ? "" : this.email;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCreateTime() {
        return this.createTime;
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

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }

    /**
     * 检查用户权限
     * @param moduleid
     * @param actionid
     * @return
     */
    public boolean checkAuth(int moduleid, int actionid) {

        return !(moduleid == 2 && actionid == 1);
    }
}
