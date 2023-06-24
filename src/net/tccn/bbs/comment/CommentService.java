package net.tccn.bbs.comment;

import net.tccn.bbs.base.BaseService;
import net.tccn.bbs.base.Utils;
import net.tccn.bbs.base.util.RetCodes;
import net.tccn.bbs.content.ContentInfo;
import net.tccn.bbs.user.UserInfo;
import net.tccn.bbs.user.UserService;
import net.tccn.bbs.vislog.entity.ActLog;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestParam;
import org.redkale.net.http.RestService;
import org.redkale.net.http.RestUserid;
import org.redkale.service.RetResult;
import org.redkale.source.*;
import org.redkale.util.Comment;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.tccn.bbs.base.util.RetCodes.RET_COMMENT_CONTENT_ILLEGAL;
import static org.redkale.source.FilterExpress.IN;

/**
 * Created by Lxy at 2017/11/29 10:00.
 */
@RestService(name = "comment", comment = "评论服务")
public class CommentService extends BaseService {

    @Resource
    private UserService userService;

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
            dataSource.updateColumn(ContentInfo.class, contentid, ColumnValue.create("replynum", count));
        } else {
            dataSource.updateColumn(comment, SelectColumn.includes("content"));
        }
        return RetResult.success();
    }

    @RestMapping(name = "query", auth = false, comment = "查询评论")
    public Sheet<CommentInfo> query(@RestUserid int userid, Flipper flipper, int contentid) {
        flipper.setSort("supportnum DESC, commentid ASC"); // 按照 点赞数排序，相同点赞数按照 id（时间先后排序）
        Sheet<CommentInfo> comments = dataSource.querySheet(CommentInfo.class, flipper, FilterNode.create("contentid", contentid));

        // 设置用户信息
        Set<Integer> userids = Utils.toSet(comments, x -> x.getUserid());
        Map<Integer, UserInfo> userMap = userService.queryUserMap(userids);
        comments.forEach(x -> x.setUser(userMap.get(x.getUserid())));

        // 访客访问：不需要设置用户状态信息，返回
        if (userid == 0) {
            return comments;
        }

        // 当前用户为登录用户：处理用户点赞状态
        List<Integer> commentids = Utils.toList(comments, x -> x.getCommentid());
        FilterNode node = FilterNode.create("cate", 10).and("status", 10).and("userid", userid).and("tid", IN, (Serializable) commentids);
        List<Integer> supports = dataSource.queryColumnList("tid", ActLog.class, node);
        comments.forEach(x -> {
            x.setHadsupport(supports.contains(x.getCommentid()) ? 1 : 0);
        });

        return comments;
    }

    @Comment("查询用户评论数据")
    public Sheet<CommentInfo> queryByUserid(int userid) {
        Sheet<CommentInfo> comments = dataSource.querySheet(CommentInfo.class, new Flipper("createtime DESC"), FilterNode.create("userid", userid));

        Set<Integer> contentids = Utils.toSet(comments, x -> x.getContentid());

        List<ContentInfo> contents = dataSource.queryList(ContentInfo.class, SelectColumn.includes("contentid", "title"), FilterNode.create("contentid", IN, (Serializable) contentids));
        Map<Integer, ContentInfo> contentMap = Utils.toMap(contents, x -> x.getContentid());

        // 设置用户信息
        Set<Integer> userids = Utils.toSet(comments, x -> x.getUserid());
        Map<Integer, UserInfo> userMap = userService.queryUserMap(userids);
        comments.forEach(x -> x.setUser(userMap.get(x.getUserid())));

        comments.forEach(x -> {
            ContentInfo content = contentMap.get(x.getContentid());
            if (content == null) {
                return;
            }

            x.setTitle(content.getTitle());
        });
        return comments;
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
    @Comment("用户评论榜")
    public Map<String, Number> commentRank() {
        dataSource.querySheet(CommentInfo.class, new Flipper(8), FilterNode.create("userid", IN));

        Map<String, Number> numberMap = dataSource.getNumberMap(CommentInfo.class, FilterFuncColumn.create(FilterFunc.DISTINCTCOUNT, "userid"));

        return numberMap;
    }
}
