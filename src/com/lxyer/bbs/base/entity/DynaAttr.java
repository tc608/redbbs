package com.lxyer.bbs.base.entity;

import org.redkale.convert.json.JsonConvert;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author lxyer
 */
@Cacheable(interval = 5*60)
@Table(catalog = "redbbs", name = "dyna_attr", comment = "[动态属性表]")
public class DynaAttr implements java.io.Serializable {

    @Id
    @Column(comment = "[目标数据id]")
    private int tid;

    @Column(comment = "[类型]1文章, 2xx, 3...,")
    private int cate;

    @Column(length = 32, comment = "")
    private String attr = "";

    @Column(comment = "[属性值]")
    private String value = "";

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getTid() {
        return this.tid;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public int getCate() {
        return this.cate;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getAttr() {
        return this.attr;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
