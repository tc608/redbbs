package com.lxyer.bbs.base.user;

import org.redkale.source.FilterBean;

import javax.persistence.Column;

/**
 * Created by Lxy at 2017/10/10 8:30.
 */
public class UserBean implements FilterBean {


    @Column(length = 32, comment = "[登录名]")
    private String username;

    @Column(length = 32, comment = "[电话号码]")
    private String phone;

    @Column(length = 64, comment = "[昵称]")
    private String nickname;

    @Column(length = 32, comment = "[真实姓名]")
    private String realname;

    @Column(length = 32, comment = "[邮箱]")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
