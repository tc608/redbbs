package com.lxyer.bbs.comment;

import com.lxyer.bbs.base.BaseService;
import com.lxyer.bbs.base.entity.ActLog;
import com.lxyer.bbs.base.iface.UIService;
import com.lxyer.bbs.base.kit.LxyKit;
import com.lxyer.bbs.base.kit.RetCodes;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.content.Content;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestParam;
import org.redkale.net.http.RestService;
import org.redkale.service.RetResult;
import org.redkale.source.*;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import java.util.List;
import java.util.Map;

import static com.lxyer.bbs.base.kit.RetCodes.RET_COMMENT_CONTENT_ILLEGAL;
import static com.lxyer.bbs.base.kit.RetCodes.RET_COMMENT_PARA_ILLEGAL;

/**
 * Created by Lxy at 2017/11/29 10:00.
 */
@RestService(name = "comment", comment = "评论服务")
public class CommentService extends BaseService implements UIService<CommentInfo> {

    @RestMapping(name = "save", comment = "评论保存")
    public RetResult save(UserInfo user, @RestParam(name = "bean") Comment comment) {
        int contentid = comment.getContentid();

        //数据校验
        if (contentid < 1)
            return RetCodes.retResult(RET_COMMENT_PARA_ILLEGAL, "评论参数无效");
        if (comment.getContent() == null)
            return RetCodes.retResult(RET_COMMENT_CONTENT_ILLEGAL, "评论内容无效");
        String content = LxyKit.delHTMLTag(comment.getContent());
        if (content.isEmpty())
            return RetCodes.retResult(RET_COMMENT_CONTENT_ILLEGAL, "评论内容无效");

        if (comment.getCommentid() < 1) {
            comment.setUserid(user.getUserid());
            comment.setCreatetime(System.currentTimeMillis());
            //todo:@用户处理
            source.insert(comment);

            //update replyNum
            int count = source.getNumberResult(Comment.class, FilterFunc.COUNT, "commentid", FilterNode.create("contentid", contentid)).intValue();
            source.updateColumn(Content.class, contentid, ColumnValue.create("replynum", count));
        } else {
            source.updateColumn(comment, SelectColumn.includes("content"));
        }
        return RetResult.success();
    }

    @RestMapping(name = "query", auth = false, comment = "查询评论")
    public Sheet<CommentInfo> query(UserInfo user, int contentId, Flipper flipper) {
        flipper.setSort("supportnum DESC,commentid ASC");
        Sheet<Comment> comments = source.querySheet(Comment.class, flipper, FilterNode.create("contentid", contentId));

        Sheet<CommentInfo> infos = createInfo(comments);
        setIUser(infos);

        //用户点赞的评论
        if (user != null) {
            int[] commentids = comments.stream().mapToInt(Comment::getCommentid).toArray();
            FilterNode node = FilterNode.create("cate", 10).and("status", 10).and("userid", user.getUserid()).and("tid", FilterExpress.IN, commentids);
            List<Integer> hadSupport = source.queryColumnList("tid", ActLog.class, node);

            infos.forEach(x -> {
                x.setHadsupport(hadSupport.contains(x.getCommentid()) ? 1 : -1);//
            });
        }

        return infos;
    }

    @org.redkale.util.Comment("查询用户评论数据")
    public Sheet<CommentInfo> queryByUserid(int userid) {
        Sheet<Comment> comments = source.querySheet(Comment.class, new Flipper().sort("createtime DESC"), FilterNode.create("userid", userid));

        int[] contentIds = comments.stream().mapToInt(x -> x.getCommentid()).toArray();
        List<Content> contents = source.queryList(Content.class, SelectColumn.includes("contentid", "title"), FilterNode.create("contentid", FilterExpress.IN, contentIds));

        Sheet<CommentInfo> infos = createInfo(comments);
        infos.forEach(x -> {
            Content content = contents.stream().filter(k -> k.getContentid() == x.getContentid()).findFirst().orElse(new Content());
            x.setTitle(content.getTitle());
        });
        return infos;
    }

    @RestMapping(name = "support", comment = "点赞")
    public RetResult support(UserInfo user, int commentid, int ok) {
        source.findAsync(ActLog.class, FilterNode.create("userid", user.getUserid()).and("tid", commentid).and("cate", 10)).thenAccept(actLog -> {
            if (actLog == null && ok == 1) {
                actLog = new ActLog(10, commentid, user.getUserid());
                actLog.setCreatetime(System.currentTimeMillis());
                source.insert(actLog);
            } else if (actLog != null && actLog.getStatus() != ok) {
                actLog.setStatus((short) -10);
                source.update(actLog);
            }/*else {
                return RetCodes.retResult(-1, ok == 1 ? "已赞" : "已取消赞");
            }*/

            int count = source.getNumberResult(ActLog.class, FilterFunc.COUNT, 0, "logid", FilterNode.create("tid", commentid).and("status", 10)).intValue();
            source.updateColumn(Comment.class, commentid, "supportnum", count);
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
        Flipper flipper = new Flipper().limit(8);
        source.querySheet(Comment.class, flipper, FilterNode.create("userid", FilterExpress.IN));

        Map<String, Number> numberMap = source.getNumberMap(Comment.class, FilterFuncColumn.create(FilterFunc.DISTINCTCOUNT, "userid"));

        return numberMap;
    }
}
