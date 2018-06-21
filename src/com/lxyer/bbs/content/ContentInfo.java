package com.lxyer.bbs.content;

import com.lxyer.bbs.base.iface.CI;
import com.lxyer.bbs.base.iface.UI;
import com.lxyer.bbs.base.user.UserRecord;
import org.redkale.convert.ConvertColumn;

import java.io.Serializable;

/**
 * Created by Lxy at 2017/11/26 20:52.
 */
public class ContentInfo implements UI<ContentInfo>,Serializable, CI {

    private int contentid;
    private int userid;
    private String title = "";
    private String digest = "";
    private String content = "";
    private String createtime;
    private int cate;
    private int type;
    private int replynum;
    private int viewnum;
    private int wonderful;
    private int top;
    private int solved;
    private int status = 10;

    private String typename;
    private int hadcollect = -1;

    public int getContentid() {
        return contentid;
    }

    public void setContentid(int contentid) {
        this.contentid = contentid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getHadcollect() {
        return hadcollect;
    }

    public void setHadcollect(int hadcollect) {
        this.hadcollect = hadcollect;
    }


    //-----------
    private UserRecord user;
    @ConvertColumn(ignore = true)
    @Override
    public UserRecord getUser() {
        return user;
    }

    @Override
    public ContentInfo setUser(UserRecord user) {
        this.user = user;
        return this;
    }
}
