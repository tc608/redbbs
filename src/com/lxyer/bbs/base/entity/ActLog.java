package com.lxyer.bbs.base.entity;

import org.redkale.convert.json.JsonConvert;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lxyer
 */
@Cacheable(interval = 5 * 60)
@Table(catalog = "redbbs", name = "sys_actlog")
public class ActLog implements java.io.Serializable {

    @Id
    @Column(comment = "[日志id]")
    private int logid;

    @Column(comment = "[日志类型]10赞，20收藏，30阅读")
    private short cate;

    @Column(comment = "[目标数据id]")
    private int tid;

    @Column(comment = "[用户id]")
    private int userid;

    @Column(updatable = false, comment = "[创建时间]")
    private long createtime;

    @Column(length = 128, comment = "[说明]")
    private String remark = "";

    @Column(comment = "[状态]-1删除 1正常")
    private short status = 10;

    public void setLogid(int logid) {
        this.logid = logid;
    }

    public int getLogid() {
        return this.logid;
    }

    public void setCate(short cate) {
        this.cate = cate;
    }

    public short getCate() {
        return this.cate;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getTid() {
        return this.tid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getCreatetime() {
        return this.createtime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public short getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }

    //----
    public ActLog() {

    }

    public ActLog(int cate, int tid, int userid) {
        this.cate = (short) cate;
        this.tid = tid;
        this.userid = userid;
    }
}
