package com.lxyer.bbs.content;

import javax.persistence.*;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.UF;
import com.lxyer.bbs.base.kit.LxyKit;
import org.redkale.convert.json.*;

import java.io.Serializable;

/**
 *
 * @author lxyer
 */
@Cacheable(interval = 5*60)
@Table(catalog = "redbbs", name = "sys_content", comment = "[内容表]")
public class Content implements Serializable, UF<ContentInfo> {

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

    @Column(updatable = false, comment = "[创建时间]")
    private long createtime;

    @Column(comment = "[类别]")
    private short cate;

    @Column(comment = "[内容栏目]10求助，20分享，30建议，40公告，50动态")
    private short type;

    @Column(comment = "[评论数]")
    private int replynum;

    @Column(comment = "[阅读量]")
    private int viewnum;

    @Column(comment = "[精] 10否，20是")
    private short wonderful = 10;

    @Column(comment = "[置顶]10否，20是")
    private short top = 10;

    @Column(comment = "[结帖]10否，20是")
    private short solved = 10;

    @Column(comment = "[状态] -10删除 10未结帖 20结帖 30私密")
    private short status = 10;

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

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getCreatetime() {
        return this.createtime;
    }

    public void setCate(short cate) {
        this.cate = cate;
    }

    public short getCate() {
        return this.cate;
    }

    public void setType(short type) {
        this.type = type;
    }

    public short getType() {
        return this.type;
    }

    public void setReplynum(int replynum) {
        this.replynum = replynum;
    }

    public int getReplynum() {
        return this.replynum;
    }

    public void setViewnum(int viewnum) {
        this.viewnum = viewnum;
    }

    public int getViewnum() {
        return this.viewnum;
    }

    public void setWonderful(short wonderful) {
        this.wonderful = wonderful;
    }

    public short getWonderful() {
        return this.wonderful;
    }

    public void setTop(short top) {
        this.top = top;
    }

    public short getTop() {
        return this.top;
    }

    public void setSolved(short solved) {
        this.solved = solved;
    }

    public short getSolved() {
        return this.solved;
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

    private static final Kv types = Kv.by(10, "求助").set(20, "分享").set(30, "讨论").set(40, "公告").set(50, "动态");

    @Override
    public ContentInfo createInfo() {
        ContentInfo info = new ContentInfo();
        info.setContentid(contentid);
        info.setUserid(userid);
        info.setTitle(title);
        info.setContent(content);
        info.setCate(cate);
        info.setType(type);
        info.setViewnum(viewnum);
        info.setReplynum(replynum);
        info.setWonderful(wonderful);
        info.setTop(top);
        info.setSolved(solved);
        info.setStatus(status);

        info.setTypename(types.getOrDefault((int)type, "其他").toString());
        info.setCreatetime(LxyKit.dateFmt(createtime));
        return info;
    }
}
