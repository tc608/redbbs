package com.lxyer.bbs.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lxyer
 */
@Setter
@Getter
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
