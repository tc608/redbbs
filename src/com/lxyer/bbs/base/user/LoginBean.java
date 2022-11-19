package com.lxyer.bbs.base.user;

import lombok.Getter;
import lombok.Setter;
import org.redkale.net.http.RestSessionid;
import org.redkale.source.FilterBean;
import org.redkale.util.Comment;

import javax.persistence.Transient;

/**
 * Created by Lxy at 2017/10/1 10:40.
 */
@Setter
@Getter
public class LoginBean implements FilterBean {
    private String username;
    private String password;

    @Transient
    @Comment("会话SESSIONID")
    @RestSessionid(create = true)
    private String sessionid = "";

    public boolean emptyUsername() {
        return username == null || username.isEmpty();
    }
}
