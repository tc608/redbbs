package com.lxyer.bbs.comment;

import com.lxyer.bbs.base.kit.LxyKit;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.*;

/**
 *
 * @author lxyer
 */
@Cacheable(interval = 5*60)
@Table(catalog = "redbbs", name = "comment", comment = "[评论表]")
public class Comment implements java.io.Serializable {

    @Id
    @GeneratedValue
    @Column(comment = "[评论id]")
    private int commentId;

    @Column(comment = "[评论用户id]")
    private int userId;

    @Column(comment = "[评论父id]")
    private int pid;

    @Column(comment = "[评论的类型]")
    private int cate;

    @Column(comment = "[被评论内容的id]")
    private int contentId;

    @Column(comment = "[评论内容]")
    private String content = "";

    @Column(comment = "[创建时间]")
    private long createTime;

    @Column(comment = "[支持数]")
    private int supportNum;

    @Column(comment = "[状态]1正常，-1删除")
    private int status = 1;

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommentId() {
        return this.commentId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCate() {
        return cate;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getContentId() {
        return this.contentId;
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

    public int getSupportNum() {
        return supportNum;
    }

    public void setSupportNum(int supportNum) {
        this.supportNum = supportNum;
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

    public CommentInfo createCommentInfo(){
        CommentInfo info = new CommentInfo();
        info.setCommentId(commentId);
        info.setUserId(userId);
        info.setPid(pid);
        info.setCate(cate);
        info.setContentId(contentId);
        info.setContent(content);
        info.setSupportNum(supportNum);
        info.setStatus(status);

        info.setCreateTime(LxyKit.dateFmt(createTime));
        return info;
    }
}
