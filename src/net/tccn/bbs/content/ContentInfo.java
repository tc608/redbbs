package net.tccn.bbs.content;

import com.jfinal.kit.Kv;
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
@Table(comment = "[内容表]")
public class ContentInfo extends BaseEntity {

    @Id
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

    @Transient
    private int collected;

    @Transient
    private UserInfo user;

    public String getTypename() {
        return (String) types.getOrDefault((int)type, "");
    }


    private static final Kv types = Kv.by(10, "求助").set(20, "分享").set(30, "讨论").set(40, "公告").set(50, "动态");
}
