package net.tccn.bbs.comment;

import net.tccn.bbs.base.BaseEntity;
import net.tccn.bbs.user.UserInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author lxyer
 */
@Setter
@Getter
@Cacheable(interval = 5 * 60)
@Table(catalog = "redbbs", name = "commentinfo", comment = "[评论表]")
public class CommentInfo extends BaseEntity {

    @Id
    @Column(comment = "[评论id]")
    private int commentid;

    @Column(comment = "[评论用户id]")
    private int userid;

    @Column(comment = "[评论父id]")
    private int pid;

    @Column(comment = "[评论的类型]")
    private short cate = 1;

    @Column(comment = "[被评论内容的id]")
    private int contentid;

    @Column(comment = "[评论内容]")
    private String content = "";

    @Column(updatable = false, comment = "[创建时间]")
    private long createtime;

    @Column(comment = "[支持数]")
    private int supportnum;

    @Column(comment = "[状态]1正常，-1删除")
    private short status = 10;

    /*@Transient
    private CommentBean pCommentInfo;*/

    @Transient
    private String title;
    @Transient
    private int hadsupport = -1;

    @Transient
    private UserInfo user;


    /*public CommentBean createInfo() {
        CommentBean info = new CommentBean();
        info.setCommentid(commentid);
        info.setUserid(userid);
        info.setPid(pid);
        info.setCate(cate);
        info.setContentid(contentid);
        info.setContent(content);
        info.setSupportnum(supportnum);
        info.setStatus(status);

        info.setCreatetime(Utils.dateFmt(createtime));
        return info;
    }*/
}
