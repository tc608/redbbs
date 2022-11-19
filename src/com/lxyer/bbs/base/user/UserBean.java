package com.lxyer.bbs.base.user;

import lombok.Getter;
import lombok.Setter;
import org.redkale.source.FilterBean;

import javax.persistence.Column;

/**
 * Created by Lxy at 2017/10/10 8:30.
 */
@Setter
@Getter
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
}
