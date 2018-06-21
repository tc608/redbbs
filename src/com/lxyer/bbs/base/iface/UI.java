package com.lxyer.bbs.base.iface;

import com.lxyer.bbs.base.user.UserRecord;

/**
 * Created by liangxianyou at 2018/6/9 13:45.
 */
public interface UI<I extends UI> {

    //抽象方法
    int getUserid();
    UserRecord getUser();
    I setUser(UserRecord user);

    //默认实现方法
    default String getRealname(){
        return getUser() == null ? null : getUser().getRealname();
    }
    default String getNickname(){
        return getUser() == null ? null : getUser().getNickname();
    }
    default String getSite(){
        return getUser() == null ? "" : getUser().getSite();
    }
    default String getGit(){
        return getUser() == null ? "" : getUser().getGit();
    }
    default String getAvatar(){
        return getUser() == null ? null : getUser().getAvatar();
    }
}
