package com.lxyer.bbs.comment;

import com.lxyer.bbs.base.BaseService;
import com.lxyer.bbs.base.Utils;
import com.lxyer.bbs.base.entity.ActLog;
import com.lxyer.bbs.base.iface.UIService;
import com.lxyer.bbs.base.kit.RetCodes;
import com.lxyer.bbs.content.Content;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestParam;
import org.redkale.net.http.RestService;
import org.redkale.net.http.RestUserid;
import org.redkale.service.RetResult;
import org.redkale.source.*;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import java.util.List;
import java.util.Map;

import static com.lxyer.bbs.base.kit.RetCodes.RET_COMMENT_CONTENT_ILLEGAL;

/**
 * Created by Lxy at 2017/11/29 10:00.
 */
@RestService(name = "comment", comment = "评论服务")
public class CommentService extends BaseService implements UIService<CommentBean> {

    @RestMapping(name = "save", comment = "评论保存")
    public RetResult save(@RestUserid int userid, @RestParam(name = "bean") CommentInfo comment) {
        int contentid = comment.getContentid();

        //数据校验
        if (contentid < 1) {
            return retError("评论参数无效");
        }
        if (Utils.isEmpty(comment.getContent())) {
            return retError("评论内容无效");
        }
        String content = Utils.delHTMLTag(comment.getContent());
        if (content.isEmpty()) {
            return RetCodes.retResult(RET_COMMENT_CONTENT_ILLEGAL, "评论内容无效");
        }

        if (comment.getCommentid() < 1) {
            comment.setUserid(userid);
            comment.setCreatetime(System.currentTimeMillis());
            //todo:@用户处理
            dataSource.insert(comment);

            //update replyNum
            int count = dataSource.getNumberResult(CommentInfo.class, FilterFunc.COUNT, "commentid", FilterNode.create("contentid", contentid)).intValue();
            dataSource.updateColumn(Content.class, contentid, ColumnValue.create("replynum", count));
        } else {
            dataSource.updateColumn(comment, SelectColumn.includes("content"));
        }
        return RetResult.success();
    }

    @RestMapping(name = "query", auth = false, comment = "查询评论")
    public Sheet<CommentBean> query(@RestUserid int userid, int contentid, Flipper flipper) {
        flipper.setSort("supportnum DESC,commentid ASC");
        Sheet<CommentInfo> comments = dataSource.querySheet(CommentInfo.class, flipper, FilterNode.create("contentid", contentid));

        Sheet<CommentBean> infos = createInfo(comments);
        setIUser(infos);

        //用户点赞的评论
        if (userid > 0) {
            int[] commentids = comments.stream().mapToInt(CommentInfo::getCommentid).toArray();
            FilterNode node = FilterNode.create("cate", 10).and("status", 10).and("userid", userid).and("tid", FilterExpress.IN, commentids);
            List<Integer> hadSupport = dataSource.queryColumnList("tid", ActLog.class, node);

            infos.forEach(x -> {
                x.setHadsupport(hadSupport.contains(x.getCommentid()) ? 1 : -1);//
            });
        }

        return infos;
    }

    @org.redkale.util.Comment("查询用户评论数据")
    public Sheet<CommentBean> queryByUserid(int userid) {
        Sheet<CommentInfo> comments = dataSource.querySheet(CommentInfo.class, new Flipper().sort("createtime DESC"), FilterNode.create("userid", userid));

        int[] contentIds = comments.stream().mapToInt(x -> x.getCommentid()).toArray();
        List<Content> contents = dataSource.queryList(Content.class, SelectColumn.includes("contentid", "title"), FilterNode.create("contentid", FilterExpress.IN, contentIds));

        Sheet<CommentBean> infos = createInfo(comments);
        infos.forEach(x -> {
            Content content = contents.stream().filter(k -> k.getContentid() == x.getContentid()).findFirst().orElse(new Content());
            x.setTitle(content.getTitle());
        });
        return infos;
    }

    @RestMapping(name = "support", comment = "点赞")
    public RetResult support(@RestUserid int userid, int commentid, int ok) {
        dataSource.findAsync(ActLog.class, FilterNode.create("userid", userid).and("tid", commentid).and("cate", 10)).thenAccept(actLog -> {
            if (actLog == null && ok == 1) {
                actLog = new ActLog(10, commentid, userid);
                actLog.setCreatetime(System.currentTimeMillis());
                dataSource.insert(actLog);
            } else if (actLog != null && actLog.getStatus() != ok) {
                actLog.setStatus((short) -10);
                dataSource.update(actLog);
            }/*else {
                return RetCodes.retResult(-1, ok == 1 ? "已赞" : "已取消赞");
            }*/

            int count = dataSource.getNumberResult(ActLog.class, FilterFunc.COUNT, 0, "logid", FilterNode.create("tid", commentid).and("status", 10)).intValue();
            dataSource.updateColumn(CommentInfo.class, commentid, "supportnum", count);
        });

        return RetResult.success();
    }

    /**
     * todo:用户评论榜 待完成
     *
     * @return
     */
    @org.redkale.util.Comment("用户评论榜")
    public Map<String, Number> commentRank() {
        dataSource.querySheet(CommentInfo.class, new Flipper(8), FilterNode.create("userid", FilterExpress.IN));

        Map<String, Number> numberMap = dataSource.getNumberMap(CommentInfo.class, FilterFuncColumn.create(FilterFunc.DISTINCTCOUNT, "userid"));

        return numberMap;
    }
}
