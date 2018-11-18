package com.lxyer.bbs.base.entity;

/**
 * 用来计数用
 *
 * @author: liangxianyou at 2018/11/18 20:42.
 */
public class Count {
    private String name;
    private long total;

    //-------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Count{" +
                "name='" + name + '\'' +
                ", total=" + total +
                '}';
    }
}
