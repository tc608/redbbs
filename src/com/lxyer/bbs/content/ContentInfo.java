package com.lxyer.bbs.content;

import com.lxyer.bbs.base.UI;
import com.lxyer.bbs.base.user.User;
import org.redkale.convert.ConvertColumn;

import java.io.Serializable;

/**
 * Created by Lxy at 2017/11/26 20:52.
 */
public class ContentInfo implements UI<ContentInfo>,Serializable {

    private int contentId;
    private int userId;
    private String title = "";
    private String digest = "";
    private String content = "";
    private String createTime;
    private int cate;
    private int type;
    private int replyNum;
    private int viewNum;
    private int wonderful;
    private int top;
    private int solved;
    private int status = 1;

    private String typeName;
    private int hadCollect = -1;

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCate() {
        return cate;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getHadCollect() {
        return hadCollect;
    }

    public void setHadCollect(int hadCollect) {
        this.hadCollect = hadCollect;
    }


    //-----------
    private User user;
    @ConvertColumn(ignore = true)
    @Override
    public User getUser() {
        return user;
    }

    @Override
    public ContentInfo setUser(User user) {
        this.user = user;
        return this;
    }
}
