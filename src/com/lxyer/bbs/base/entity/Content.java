package com.lxyer.bbs.base.entity;

import com.lxyer.bbs.base.LxyKit;
import com.lxyer.bbs.base.bean.ContentInfo;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author lxyer
 */
@Cacheable(interval = 60)
@Table(catalog = "db_redbbs", name = "content", comment = "[内容表]")
public class Content implements java.io.Serializable {

    @Id
    @GeneratedValue
    @Column(comment = "[内容id]")
    private int contentId;

    @Column(comment = "[用户id]")
    private int userId;

    @Column(length = 64, comment = "[标题]")
    private String title = "";

    @Column(length = 256, comment = "[摘要]")
    private String digest = "";

    @Column(comment = "[内容]")
    private String content = "";

    @Column(comment = "[创建时间]")
    private long createTime;

    @Column(comment = "[类别]")
    private int cate;

    @Column(comment = "[内容类型]1新闻，2作品")
    private int type;

    @Column(comment = "[评论数]")
    private int replyNum;

    @Column(comment = "[阅读量]")
    private int viewNum;

    @Column(comment = "[精] 0否，1是")
    private int wonderful;

    @Column(comment = "[置顶] 0否，1是")
    private int top;

    @Column(comment = "[结帖]大于0结帖")
    private int solved;

    @Column(comment = "[状态]")
    private int status = 1;

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getContentId() {
        return this.contentId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return this.userId;
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

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCreateTime() {
        return this.createTime;
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


    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public int getWonderful() {
        return wonderful;
    }

    public void setWonderful(int wonderful) {
        this.wonderful = wonderful;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getSolved() {
        return solved;
    }

    public void setSolved(int solved) {
        this.solved = solved;
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

    public ContentInfo createContentInfo(){
        ContentInfo info = new ContentInfo();
        info.setContentId(contentId);
        info.setUserId(userId);
        info.setTitle(title);
        info.setContent(content);
        info.setCate(cate);
        info.setViewNum(viewNum);
        info.setReplyNum(replyNum);
        info.setWonderful(wonderful);
        info.setTop(top);
        info.setSolved(solved);

        info.setCateName(cateName());
        info.setCreateTime(LxyKit.dateFmt(createTime));
        return info;
    }

    public String cateName(){
        switch (cate){
            case 1: return "Redkale框架综合";
            case 2: return "JFinal框架综合";
            case 3: return "Layui框架综合";
            case 4: return "JSON解析";
            default: return "其他";
        }
    }
}
