package com.lxyer.bbs.comment;

import com.lxyer.bbs.base.Utils;
import com.lxyer.bbs.base.iface.C;
import lombok.Getter;
import lombok.Setter;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author lxyer
 */
@Setter
@Getter
@Cacheable(interval = 5 * 60)
@Table(catalog = "redbbs", name = "sys_comment", comment = "[评论表]")
public class CommentInfo implements Serializable, C<CommentBean> {

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

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }

    public CommentBean createInfo() {
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
    }
}
