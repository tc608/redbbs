package com.lxyer.bbs.comment;

import com.lxyer.bbs.base.BaseService;
import com.lxyer.bbs.base.kit.LxyKit;
import com.lxyer.bbs.base.kit.RetCodes;
import com.lxyer.bbs.base.entity.ActLog;
import com.lxyer.bbs.base.user.User;
import com.lxyer.bbs.base.user.UserService;
import com.lxyer.bbs.content.Content;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestParam;
import org.redkale.net.http.RestService;
import org.redkale.net.http.RestSessionid;
import org.redkale.service.RetResult;
import org.redkale.source.*;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lxyer.bbs.base.kit.RetCodes.RET_COMMENT_CONTENT_ILLEGAL;
import static com.lxyer.bbs.base.kit.RetCodes.RET_COMMENT_PARA_ILLEGAL;

/**
 * Created by Lxy at 2017/11/29 10:00.
 */
@RestService(automapping = true, comment = "评论服务")
public class CommentService extends BaseService {

    @Resource
    private UserService userService;

    @RestMapping(name = "save", comment = "评论保存")
    public RetResult commentSave(@RestSessionid String sessionid, @RestParam(name = "bean") Comment comment){
        int contentId = comment.getContentId();

        if (contentId < 1) return RetCodes.retResult(RET_COMMENT_PARA_ILLEGAL, "评论参数无效");
        if (comment.getContent() == null) return RetCodes.retResult(RET_COMMENT_CONTENT_ILLEGAL, "评论内容无效");

        if (comment.getCommentId() < 1) {
            int userId = userService.currentUserId(sessionid);
            comment.setUserId(userId);
            comment.setCreateTime(System.currentTimeMillis());
            source.insert(comment);

            //update replyNum
            int count = source.getNumberResult(Comment.class, FilterFunc.COUNT, "commentId", FilterNode.create("contentId", contentId)).intValue();
            source.updateColumn(Content.class, contentId, ColumnValue.create("replyNum", count));
        }else {
            source.updateColumn(comment, SelectColumn.createIncludes("content"));
        }
        return RetResult.success();
    }

    @RestMapping(name = "query", auth = false,comment = "查询评论")
    public Sheet<CommentInfo> commentQuery(@RestSessionid String sessionid , int contentId, Flipper flipper){
        int userId = userService.currentUserId(sessionid);

        flipper.setSort("supportNum DESC,commentId ASC");
        Sheet<Comment> comments = source.querySheet(Comment.class, flipper, FilterNode.create("contentId", contentId));

        Sheet<CommentInfo> infos = new Sheet<>();
        List<CommentInfo> list = new ArrayList<>();

        if (comments.getRows() == null) return infos;

        //映射用户信息
        int[] userids = comments.stream().mapToInt(x -> x.getUserId()).toArray();
        List<User> users = source.queryList(User.class, SelectColumn.createIncludes("userId","avatar", "nickname"), FilterNode.create("userId", FilterExpress.IN, userids));

        //用户点赞的评论
        List<Integer> hadSupport = new ArrayList<>();
        if (userId > 0){
            FilterNode node = FilterNode.create("cate", 1).and("status", 1).and("userId", userId);
            List<ActLog> actLogs = source.queryList(ActLog.class, SelectColumn.createIncludes("tid"), node);
            actLogs.forEach(x->hadSupport.add(x.getTid()));
        }

        comments.forEach(x->{
            CommentInfo info = x.createCommentInfo();
            User user = users.stream().filter(k -> k.getUserId() == x.getUserId()).findFirst().orElse(new User());
            info.setAvatar(user.getAvatar());
            info.setNickname(user.getNickname());

            info.setHadSupport(hadSupport.contains(x.getCommentId()) ? 1 : -1);//

            list.add(info);
        });

        infos.setRows(list);
        infos.setTotal(comments.getTotal());
        return infos;
    }

    public Sheet<CommentInfo> queryByUserid(int userid){
        Sheet<Comment> comments = source.querySheet(Comment.class, new Flipper().sort("createTime DESC"), FilterNode.create("userId", userid));

        int[] contentIds = comments.stream().mapToInt(x -> x.getContentId()).toArray();

        List<Content> contents = source.queryList(Content.class, SelectColumn.createIncludes("contentId","title"), FilterNode.create("contentId", FilterExpress.IN, contentIds));

        Sheet<CommentInfo> infos = new Sheet<>();
        List<CommentInfo> list = new ArrayList<>();

        comments.forEach(x->{
            CommentInfo info = x.createCommentInfo();
            Content content = contents.stream().filter(k -> k.getContentId() == x.getContentId()).findFirst().orElse(new Content());
            info.setTitle(content.getTitle());
            list.add(info);
        });

        infos.setRows(list);
        infos.setTotal(comments.getTotal());
        return infos;
    }

    @RestMapping(name = "support", comment = "评论点赞")
    public RetResult support(@RestSessionid String sessionid, int commentId, int ok){
        int userId = userService.currentUserId(sessionid);

        ActLog actLog = source.find(ActLog.class, FilterNode.create("userId", userId).and("tid", commentId).and("cate", 1));
        if (actLog == null && ok == 1){
            actLog = new ActLog().cate(1).tid(commentId).userId(userId);
            actLog.setCreateTime(System.currentTimeMillis());
            source.insert(actLog);
        }else if (actLog != null && actLog.getStatus() != ok){
            actLog.setStatus(ok);
            source.update(actLog);
        }else {
            return RetCodes.retResult(-1, ok == 1 ? "已赞" : "已取消赞");
        }

        int count = source.getNumberResult(ActLog.class, FilterFunc.COUNT, 0, "logid", FilterNode.create("tid", commentId).and("status", 1)).intValue();
        source.updateColumn(Comment.class, commentId,"supportNum", count);

        return RetResult.success();
    }

    @RestMapping(name = "rankuser", auth = false, comment = "评论榜")
    public Map<String, Number> commentRank(){
        Flipper flipper = new Flipper().limit(8);
        source.querySheet(Comment.class, flipper, FilterNode.create("userId", FilterExpress.IN));

        Map<String, Number> numberMap = source.getNumberMap(Comment.class, FilterFuncColumn.create(FilterFunc.DISTINCTCOUNT, "userId"));

        return numberMap;
    }
}
