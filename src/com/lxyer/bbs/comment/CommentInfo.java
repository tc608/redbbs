package com.lxyer.bbs.comment;

import com.lxyer.bbs.base.iface.CI;
import com.lxyer.bbs.base.iface.UI;
import com.lxyer.bbs.base.user.UserRecord;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author lxyer
 */
public class CommentInfo implements UI<CommentInfo>, Serializable, CI<CommentInfo> {

    @Column(comment = "[评论id]")
    private int commentid;

    @Column(comment = "[评论用户id]")
    private int userid;

    @Column(comment = "[评论父id]")
    private int pid;

    @Column(comment = "[评论的类型]")
    private int cate;

    @Column(comment = "[被评论内容的id]")
    private int contentid;

    @Column(comment = "[评论内容]")
    private String content = "";

    @Column(comment = "[支持数]")
    private int supportnum;

    @Column(comment = "[状态]1正常，-1删除")
    private int status = 1;


    private String createtime;

    private CommentInfo pCommentInfo;

    private String title;
    private int hadsupport = -1;

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public int getCommentid() {
        return this.commentid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return this.userid;
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

    public void setContentid(int contentid) {
        this.contentid = contentid;
    }

    public int getContentid() {
        return this.contentid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSupportnum() {
        return supportnum;
    }

    public void setSupportnum(int supportnum) {
        this.supportnum = supportnum;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public CommentInfo getpCommentInfo() {
        return pCommentInfo;
    }

    public void setpCommentInfo(CommentInfo pCommentInfo) {
        this.pCommentInfo = pCommentInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHadsupport() {
        return hadsupport;
    }

    public void setHadsupport(int hadsupport) {
        this.hadsupport = hadsupport;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }

    //----
    private UserRecord user;

    @Override
    public UserRecord getUser() {
        return user;
    }

    @Override
    public CommentInfo setUser(UserRecord user) {
        this.user = user;
        return this;
    }
}
