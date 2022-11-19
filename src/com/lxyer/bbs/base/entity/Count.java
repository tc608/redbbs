package com.lxyer.bbs.base.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 用来计数用
 *
 * @author: liangxianyou at 2018/11/18 20:42.
 */
@Setter
@Getter
public class Count {
    private String name;
    private long total;

    //-------------------

    @Override
    public String toString() {
        return "Count{" +
                "name='" + name + '\'' +
                ", total=" + total +
                '}';
    }
}
