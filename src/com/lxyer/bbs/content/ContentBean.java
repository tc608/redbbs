package com.lxyer.bbs.content;

import org.redkale.convert.json.JsonConvert;
import org.redkale.source.FilterBean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author lxyer
 */
@Table(catalog = "db_redbbs", name = "content", comment = "[内容表]")
public class ContentBean implements FilterBean,java.io.Serializable {

    @Id
    @GeneratedValue
    @Column(comment = "[内容id]")
    private int contentid;

    @Column(comment = "[用户id]")
    private int userid;

    @Column(length = 64, comment = "[标题]")
    private String title = "";

    @Column(length = 256, comment = "[摘要]")
    private String digest = "";

    @Column(comment = "[内容]")
    private String content = "";

    @Column(comment = "[创建时间]")
    private long createtime;

    @Column(comment = "[类别]")
    private int cate;

    @Column(comment = "[内容类型]1新闻，2作品")
    private int type;

    @Column(comment = "[评论数]")
    private int replynum;

    @Column(comment = "[阅读量]")
    private int viewnum;

/*    @Column(comment = "[精] 0否，1是")
    private int wonderful;

    @Column(comment = "[置顶] 0否，1是")
    private int top;

    @Column(comment = "[结帖]大于0结帖")
    private int solved;

    @Column(comment = "[状态]")
    private int status = 1;*/

    public void setContentid(int contentid) {
        this.contentid = contentid;
    }

    public int getContentid() {
        return this.contentid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return this.digest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getCreatetime() {
        return this.createtime;
    }

    public int getCate() {
        return cate;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public int getReplynum() {
        return replynum;
    }

    public void setReplynum(int replynum) {
        this.replynum = replynum;
    }

    public int getViewnum() {
        return viewnum;
    }

    public void setViewnum(int viewnum) {
        this.viewnum = viewnum;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
