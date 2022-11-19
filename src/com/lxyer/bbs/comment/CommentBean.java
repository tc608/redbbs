package com.lxyer.bbs.comment;

import com.lxyer.bbs.base.iface.CI;
import com.lxyer.bbs.base.iface.UI;
import com.lxyer.bbs.base.user.UserRecord;
import lombok.Getter;
import lombok.Setter;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author lxyer
 */
@Setter
@Getter
public class CommentBean implements UI<CommentBean>, Serializable, CI<CommentBean> {

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

    private CommentBean pCommentInfo;

    private String title;
    private int hadsupport = -1;

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
    public CommentBean setUser(UserRecord user) {
        this.user = user;
        return this;
    }
}
