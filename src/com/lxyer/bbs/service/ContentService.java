package com.lxyer.bbs.service;

import com.lxyer.bbs.base.BaseService;
import com.lxyer.bbs.base.LxyKit;
import com.lxyer.bbs.base.RetCodes;
import com.lxyer.bbs.base.bean.ContentInfo;
import com.lxyer.bbs.base.entity.ActLog;
import com.lxyer.bbs.base.entity.Content;
import com.lxyer.bbs.base.entity.User;
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
    public Sheet<ContentInfo> contentQuery(Flipper flipper, String actived){
        FilterNode filterNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -1);
        switch (actived){
            case "top": filterNode.and("top", 1);break;
            case "untop": filterNode.and("top", 0);break;
            case "unsolved": filterNode.and("solved", 0);break;
            case "solved": filterNode.and("solved", 1);break;
            case "wonderful": filterNode.and("wonderful", 1);break;
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
        if (content.getContentId() < 1){
            int maxId = source.getNumberResult(Content.class, FilterFunc.MAX,  10_0000, "contentId").intValue();
            int userId = userService.currentUserId(sessionid);

            content.setContentId(maxId+1);
            content.setCreateTime(System.currentTimeMillis());
            content.setUserId(userId);

            source.insert(content);
        }else {
            source.updateColumn(content, SelectColumn.createIncludes("title", "digest", "content","type"));
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
        FilterNode filterNode = FilterNode.create("cate", 2).and("status", 1);
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

}
