package com.lxyer.bbs.base.bean;

import org.redkale.convert.json.JsonConvert;

import javax.persistence.Column;

/**
 *
 * @author lxyer
 */
public class CommentInfo implements java.io.Serializable {

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

    @Column(comment = "[支持数]")
    private int supportNum;

    @Column(comment = "[状态]1正常，-1删除")
    private int status = 1;


    private String createTime;

    private CommentInfo pCommentInfo;
    private String nickname = "";
    private String avatar = "";

    private String title;
    private int hadSupport = -1;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public CommentInfo getpCommentInfo() {
        return pCommentInfo;
    }

    public void setpCommentInfo(CommentInfo pCommentInfo) {
        this.pCommentInfo = pCommentInfo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHadSupport() {
        return hadSupport;
    }

    public void setHadSupport(int hadSupport) {
        this.hadSupport = hadSupport;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
