package com.lxyer.bbs.base.user;

import org.redkale.net.http.RestSessionid;
import org.redkale.source.FilterBean;
import org.redkale.util.Comment;

import javax.persistence.Transient;

/**
 * Created by Lxy at 2017/10/1 10:40.
 */
public class LoginBean implements FilterBean {
    private String username;
    private String password;

    @Transient
    @Comment("会话SESSIONID")
    @RestSessionid(create = true)
    private String sessionid = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return UserRecord.md5IfNeed(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public boolean emptyUsername() {
        return username == null || username.isEmpty();
    }
}
