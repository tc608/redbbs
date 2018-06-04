package com.lxyer.bbs.base.kit;

import org.redkale.service.RetLabel;
import org.redkale.service.RetResult;

/**
 * Created by Lxy at 2017/10/1 11:21.
 */
public abstract class RetCodes {

    @RetLabel("参数无效")
    public static final int RET_PARAMS_ILLEGAL = 3001_0001;

    //------------------------------------- 用户模块 -----------------------------------------
    @RetLabel("未登陆")
    public static final int RET_USER_UNLOGIN = 3002_0001;

    @RetLabel("用户或密码错误")
    public static final int RET_USER_ACCOUNT_PWD_ILLEGAL = 3002_0002;

    @RetLabel("用户权限不够")
    public static final int RET_USER_AUTH_ILLEGAL = 3002_0003;

    @RetLabel("密码设置无效")
    public static final int RET_USER_PASSWORD_ILLEGAL = 3002_0004;

    @RetLabel("用户不存在")
    public static final int RET_USER_NOTEXISTS = 3002_0005;

    @RetLabel("用户名已存在")
    public static final int RET_USER_USERNAME_EXISTS = 3002_0006;

    @RetLabel("用户名无效")
    public static final int RET_USER_USERNAME_ILLEGAL = 3002_0007;

    @RetLabel("邮箱已存在")
    public static final int RET_USER_EMAIL_EXISTS = 3002_0008;

    @RetLabel("邮箱无效")
    public static final int RET_USER_EMAIL_ILLEGAL = 3002_0009;

    @RetLabel("昵称无效")
    public static final int RET_USER_NICKNAME_ILLEGAL = 3002_0010;

    @RetLabel("昵称已存在")
    public static final int RET_USER_NICKNAME_EXISTS = 3002_0011;

    //------------------------------------- 内容模块 -----------------------------------------

    //------------------------------------- 评论模块 -----------------------------------------
    @RetLabel("评论内容无效")
    public static final int RET_COMMENT_CONTENT_ILLEGAL = 3004_0001;

    @RetLabel("评论参数无效")
    public static final int RET_COMMENT_PARA_ILLEGAL = 3004_0002;



    public static RetResult retResult(int retcode) {
        return new RetResult(retcode);
    }

    public static RetResult retResult(int retcode, String retinfo) {
        return new RetResult(retcode, retinfo);
    }
}
