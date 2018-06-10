package com.lxyer.bbs.content;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.BaseService;
import com.lxyer.bbs.base.kit.LxyKit;
import com.lxyer.bbs.base.kit.RetCodes;
import com.lxyer.bbs.base.entity.ActLog;
import com.lxyer.bbs.base.user.User;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.base.user.UserService;
import org.redkale.net.http.*;
import org.redkale.service.RetResult;
import org.redkale.source.*;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lxy at 2017/11/26 9:33.
 */
@RestService(automapping = true, comment = "内容管理")
public class ContentService extends BaseService{

    @Resource
    protected UserService userService;

    /**
     *
     * @param flipper
     * @param filterNode
     * @return
     */
    public Sheet<ContentInfo> contentQuery(Flipper flipper, FilterNode filterNode){
        Sheet<Content> contents = source.querySheet(Content.class, flipper, filterNode);

        int[] userids = contents.stream().mapToInt(x -> x.getUserId()).distinct().toArray();

        Sheet<ContentInfo> infos = new Sheet<>();
        List<ContentInfo> list = new ArrayList<>();

        List<User> users = source.queryList(User.class, SelectColumn.createIncludes("userId","avatar", "nickname"), FilterNode.create("userId", FilterExpress.IN, userids));
        contents.forEach(x->{
            ContentInfo contentInfo = x.createContentInfo();
            User user = users.stream().filter(k -> k.getUserId() == x.getUserId()).findFirst().orElse(new User());
            contentInfo.setAvatar(user.getAvatar());
            contentInfo.setNickname(user.getNickname());
            list.add(contentInfo);
        });

        infos.setRows(list);
        infos.setTotal(contents.getTotal());
        return infos;
    }

    @RestMapping(name = "query", auth = false, comment = "内容列表")
    public Sheet<ContentInfo> contentQuery(Flipper flipper, String actived, int currentId){
        FilterNode filterNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -1);
        switch (actived){
            case "top": filterNode.and("top", 1);break;
            case "untop": filterNode.and("top", 0);break;
            case "unsolved": filterNode.and("solved", 0);break;
            case "solved": filterNode.and("solved", 1);break;
            case "wonderful": filterNode.and("wonderful", 1);break;
        }

        if (!userService.isAdmin(currentId)){//私密贴：非管理员限制查看
            filterNode.and(FilterNode.create("status", FilterExpress.NOTEQUAL, 3).or(FilterNode.create("status", 3).and("userId", currentId)));
        }else if (currentId <= 0){//私密贴：未登录限制查看
            filterNode.and("status", FilterExpress.NOTEQUAL, 3);
        }

        return contentQuery(flipper, filterNode);
    }


    public Sheet<ContentInfo> queryByBean(Flipper flipper, FilterBean bean){
        Sheet<Content> contents = source.querySheet(Content.class, flipper, bean);

        Sheet<ContentInfo> infos = new Sheet<>();
        List<ContentInfo> list = new ArrayList<>();

        contents.forEach(x->{
            list.add(x.createContentInfo());
        });

        infos.setRows(list);
        infos.setTotal(contents.getTotal());

        return infos;
    }

    @RestMapping(name = "save", auth = true, comment = "内容保存")
    public RetResult contentSave(@RestParam(name = "bean")Content content, @RestSessionid String sessionid){
        //数据校验
        if (content.getTitle().isEmpty() || content.getTitle().length() > 64){
            return RetCodes.retResult(-1, "少年你的文章标题太长啦，精简化标题吧，为了更好的SEO长度请少于64个字节");
        }

        if (content.getContentId() < 1){
            int maxId = source.getNumberResult(Content.class, FilterFunc.MAX,  10_0000, "contentId").intValue();
            int userId = userService.currentUserId(sessionid);

            content.setContentId(maxId+1);
            content.setCreateTime(System.currentTimeMillis());
            content.setUserId(userId);

            source.insert(content);
        }else {
            source.updateColumn(content, SelectColumn.createIncludes("title", "digest", "content","type", "status"));
        }

        return RetResult.success();
    }

    @RestMapping(name = "info", auth = false, comment = "内容详情")
    public ContentInfo contentInfo(@RestSessionid String sessionid, int contentid){
        int userId = userService.currentUserId(sessionid);

        Content content = source.find(Content.class, contentid);
        if (content == null) return null;

        ContentInfo contentInfo = content.createContentInfo();
        User user = source.find(User.class, content.getUserId());
        contentInfo.setAvatar(user.getAvatar());
        contentInfo.setNickname(user.getNickname());

        //收藏状态
        if (userId > 0){
            ActLog actLog = source.find(ActLog.class, FilterNode.create("cate", 2).and("tid", contentid).and("status", 1));
            if (actLog != null) contentInfo.setHadCollect(1);
        }
        return contentInfo;
    }

    @RestMapping(name = "upview", comment = "增加文章1个访问量")
    public void incrViewNum(int contentId){
        source.updateColumn(Content.class, contentId, ColumnValue.inc("viewNum", 1));
    }

    @RestMapping(name = "collect", comment = "内容收藏")
    public RetResult collect(@RestSessionid String sessionid, int contentId, int ok){
        int userId = userService.currentUserId(sessionid);//不会为空

        ActLog actLog = source.find(ActLog.class, FilterNode.create("userId", userId).and("tid", contentId).and("cate", 2));
        if (actLog == null && ok == 1){
            actLog = new ActLog().cate(2).tid(contentId).userId(userId);
            actLog.setCreateTime(System.currentTimeMillis());
            source.insert(actLog);
        }else if (actLog != null && actLog.getStatus() != ok){
            actLog.setStatus(ok);
            actLog.setCreateTime(System.currentTimeMillis());
            source.update(actLog);
        }else {
            return RetCodes.retResult(-1, ok == 1 ? "已收藏" : "已取消收藏");
        }

        return RetResult.success();
    }

    @RestMapping(name = "collectquery", comment = "收藏列表")
    public Sheet<ContentInfo> collectQuery(@RestSessionid String sessionid){
        int userId = userService.currentUserId(sessionid);

        Flipper flipper = new Flipper().sort("createTime DESC");
        FilterNode filterNode = FilterNode.create("cate", 2).and("status", 1).and("userId", userId);
        Sheet<ActLog> actLogs = source.querySheet(ActLog.class, SelectColumn.createIncludes("tid", "createTime"), flipper, filterNode);

        int[] contentids = actLogs.stream().mapToInt(x -> x.getTid()).toArray();
        Sheet<Content> contents = source.querySheet(Content.class, SelectColumn.createIncludes("contentId", "title"), flipper.sort(null), FilterNode.create("contentId", FilterExpress.IN, contentids));

        Sheet<ContentInfo> infos = new Sheet<>();
        ArrayList<ContentInfo> list = new ArrayList<>();

        actLogs.forEach(x->{
            Content content = contents.stream().filter(k -> k.getContentId() == x.getTid()).findFirst().orElse(null);
            if (content != null){
                ContentInfo info = content.createContentInfo();
                info.setCreateTime(LxyKit.dateFmt(x.getCreateTime()));
                list.add(info);
            }
        });

        infos.setRows(list);
        infos.setTotal(actLogs.getTotal());

        return infos;
    }

    @RestMapping(name = "set", comment = "内容操作")
    public RetResult contentSet(int id, String field, int v){
        source.updateColumn(Content.class, id, field, v);
        return RetResult.success();
    }

    @RestMapping(name = "t",auth = false, comment = "测试HttpScope 模板使用")
    public HttpScope t(){
        ContentService contentService = this;
        Flipper flipper = new Flipper().limit(30).sort("top DESC,createTime DESC");
        //置顶贴
        FilterNode topNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -1).and("top", FilterExpress.GREATERTHAN, 0);
        Sheet<ContentInfo> top = contentService.contentQuery(flipper, topNode);

        //非置顶贴
        FilterNode untopNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -1).and("top", 0);
        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, untopNode);

        //热帖
        /*Flipper flipper2 = new Flipper().limit(8).sort("viewNum DESC");
        Sheet<ContentInfo> hotView = contentService.contentQuery(flipper2, "");*/

        //热议
        Flipper flipper3 = new Flipper().limit(8).sort("replyNum DESC");
        Sheet<ContentInfo> hotReply = contentService.contentQuery(flipper3, "", 0);

        //最新加入
        Sheet<UserInfo> lastReg = userService.lastReg();

        Kv kv = Kv.by("top", top).set("contents", contents).set("hotReply", hotReply).set("lastReg", lastReg);

        return HttpScope.refer("index.html").attr(kv);
    }

}
