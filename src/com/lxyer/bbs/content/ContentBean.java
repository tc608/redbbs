package com.lxyer.bbs.content;

import lombok.Getter;
import lombok.Setter;
import org.redkale.convert.json.JsonConvert;
import org.redkale.source.FilterBean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lxyer
 */
@Setter
@Getter
@Table(catalog = "db_redbbs", name = "content", comment = "[内容表]")
public class ContentBean implements FilterBean, java.io.Serializable {

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

    @Column(comment = "[创建时间]")
    private long createtime;

    @Column(comment = "[类别]")
    private int cate;

    @Column(comment = "[内容类型]1新闻，2作品")
    private int type;

    @Column(comment = "[评论数]")
    private int replynum;

    @Column(comment = "[阅读量]")
    private int viewnum;

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
