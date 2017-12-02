package com.lxyer.bbs.base.bean;

import org.redkale.convert.json.JsonConvert;
import org.redkale.source.FilterBean;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author lxyer
 */
@Table(catalog = "db_redbbs", name = "act_log")
public class ActLogBean implements Serializable, FilterBean {

    @Column(comment = "[日志id]")
    private int logid;

    @Column(comment = "[日志类型]")
    private int cate;

    @Column(comment = "[目标数据id]")
    private int tid;

    @Column(comment = "[用户id]")
    private int userId;

    @Column(comment = "[创建时间]")
    private long createTime;

    @Column(length = 128, comment = "[说明]")
    private String remark = "";

    @Column(comment = "[状态]-1删除 1正常")
    private int status = 1;

    public void setLogid(int logid) {
        this.logid = logid;
    }

    public int getLogid() {
        return this.logid;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public int getCate() {
        return this.cate;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getTid() {
        return this.tid;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
