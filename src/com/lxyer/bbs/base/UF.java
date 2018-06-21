package com.lxyer.bbs.base;

import com.lxyer.bbs.base.iface.UI;

/**
 * user foreign key (userId)
 * Created by liangxianyou at 2018/6/9 14:50.
 */
public interface UF<I extends UI> {
    int getUserid();
    I createInfo();
}
