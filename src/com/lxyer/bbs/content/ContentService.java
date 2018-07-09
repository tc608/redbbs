package com.lxyer.bbs.content;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.BaseService;
import com.lxyer.bbs.base.entity.ActLog;
import com.lxyer.bbs.base.iface.UIService;
import com.lxyer.bbs.base.kit.RetCodes;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.base.user.UserService;
import org.redkale.net.http.*;
import org.redkale.service.RetResult;
import org.redkale.source.*;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import javax.annotation.Resource;

/**
 * Created by Lxy at 2017/11/26 9:33.
 */
@RestService(automapping = true, comment = "内容管理")
public class ContentService extends BaseService implements UIService<ContentInfo> {

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

        createInfo(contents);
        Sheet<ContentInfo> infos = createInfo(contents);
        setIUser(infos);

        return infos;
    }

    @RestMapping(name = "query", auth = false, comment = "内容列表")
    public Sheet<ContentInfo> contentQuery(Flipper flipper, String actived, String sessionid){
        UserInfo current = userService.current(sessionid);
        int currentid = current == null ? 0 : current.getUserid();

        FilterNode filterNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -10);
        switch (actived){
            case "top": filterNode.and("top", FilterExpress.GREATERTHANOREQUALTO, 20);break;
            case "untop": filterNode.and("top", 10);break;
            case "unsolved": filterNode.and("solved", 10);break;
            case "solved": filterNode.and("solved", 20);break;
            case "wonderful": filterNode.and("wonderful", FilterExpress.GREATERTHANOREQUALTO, 20);break;
        }

        if (!userService.isAdmin(currentid)){//私密贴：非管理员限制查看
            filterNode.and(FilterNode.create("status", FilterExpress.NOTEQUAL, 30).or(FilterNode.create("status", 30).and("userid", currentid)));
        }else if (currentid <= 0){//私密贴：未登录限制查看
            filterNode.and("status", FilterExpress.NOTEQUAL, 30);
        }

        return contentQuery(flipper, filterNode);
    }


    /*public Sheet<ContentInfo> queryByBean(Flipper flipper, FilterBean bean){
        Sheet<Content> contents = source.querySheet(Content.class, flipper, bean);

        Sheet<ContentInfo> infos = createInfo(contents);

        return infos;
    }*/

    @RestMapping(name = "save", auth = true, comment = "内容保存")
    public RetResult contentSave(@RestParam(name = "bean")Content content, @RestSessionid String sessionid){
        //数据校验
        if (content.getTitle().isEmpty() || content.getTitle().length() > 64){
            return RetCodes.retResult(-1, "少年你的文章标题太长啦，精简化标题吧，为了更好的SEO长度请少于64个字节");
        }

        if (content.getContentid() < 1){
            int maxId = source.getNumberResult(Content.class, FilterFunc.MAX,  10_0000, "contentid").intValue();
            int userId = userService.currentUserId(sessionid);

            content.setContentid(maxId+1);
            content.setCreatetime(System.currentTimeMillis());
            content.setUserid(userId);

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

        ContentInfo contentInfo = setIUser(content.createInfo());

        //收藏状态
        if (userId > 0){
            ActLog actLog = source.find(ActLog.class, FilterNode.create("cate", 20).and("tid", contentid).and("status", 10));
            if (actLog != null) contentInfo.setHadcollect(1);
        }
        return contentInfo;
    }

    @RestMapping(name = "collect", comment = "内容收藏")
    public RetResult collect(@RestSessionid String sessionid, int contentid, int ok){
        int userid = userService.currentUserId(sessionid);//不会为空

        ActLog actLog = source.find(ActLog.class, FilterNode.create("userid", userid).and("tid", contentid).and("cate", 20));
        if (actLog == null && ok == 1){
            actLog = new ActLog(20, contentid, userid);//.cate(2).tid(contentId).userId(userId);
            actLog.setCreatetime(System.currentTimeMillis());
            source.insert(actLog);
        }else if (actLog != null && actLog.getStatus() != ok){
            actLog.setStatus((short) ok);
            actLog.setCreatetime(System.currentTimeMillis());
            source.update(actLog);
        }else {
            return RetCodes.retResult(-1, ok == 1 ? "已收藏" : "已取消收藏");
        }

        return RetResult.success();
    }

    @RestMapping(name = "collectquery", comment = "收藏列表")
    public Sheet<ContentInfo> collectQuery(@RestSessionid String sessionid){
        int userid = currentUserId(sessionid);

        Flipper flipper = new Flipper().sort("createtime DESC");
        FilterNode filterNode = FilterNode.create("cate", 20).and("status", 10).and("userid", userid);
        Sheet<ActLog> actLogs = source.querySheet(ActLog.class, SelectColumn.createIncludes("tid", "createtime"), flipper, filterNode);

        int[] contentids = actLogs.stream().mapToInt(x -> x.getTid()).toArray();
        Sheet<Content> contents = source.querySheet(Content.class, SelectColumn.createIncludes("contentid", "title"), flipper.sort(null), FilterNode.create("contentid", FilterExpress.IN, contentids));

        Sheet<ContentInfo> infos = createInfo(contents);

        return infos;
    }

    @RestMapping(name = "set", comment = "内容操作")
    public RetResult contentSet(int id, String field, short v){
        source.updateColumn(Content.class, id, field, v);
        return RetResult.success();
    }

    @RestMapping(name = "t",auth = false, comment = "测试HttpScope 模板使用")
    public HttpScope t(@RestSessionid String sessionid){
        ContentService contentService = this;
        Flipper flipper = new Flipper().limit(30).sort("top DESC,createtime DESC");
        //置顶贴
        FilterNode topNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -10).and("top", FilterExpress.GREATERTHANOREQUALTO, 20);
        Sheet<ContentInfo> top = contentService.contentQuery(flipper, topNode);

        //非置顶贴
        FilterNode untopNode = FilterNode.create("status", FilterExpress.NOTEQUAL, -10).and("top", 0);
        Sheet<ContentInfo> contents = contentService.contentQuery(flipper, untopNode);

        //热议
        Flipper flipper3 = new Flipper().limit(8).sort("replynum DESC");
        Sheet<ContentInfo> hotReply = contentService.contentQuery(flipper3, "", sessionid);

        //最新加入
        Sheet<UserInfo> lastReg = userService.lastReg();

        Kv kv = Kv.by("top", top)
                .set("contents", contents)
                .set("hotReply", hotReply)
                .set("lastReg", lastReg);

        return HttpScope.refer("index.html").attr(kv);
    }

}
