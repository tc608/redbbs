package com.lxyer.bbs.content;

import com.jfinal.kit.Kv;
import com.lxyer.bbs.base.BaseService;
import com.lxyer.bbs.base.entity.ActLog;
import com.lxyer.bbs.base.iface.UIService;
import com.lxyer.bbs.base.kit.RetCodes;
import com.lxyer.bbs.base.user.UserInfo;
import com.lxyer.bbs.base.user.UserService;
import org.redkale.net.http.HttpScope;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestService;
import org.redkale.net.http.RestUserid;
import org.redkale.service.RetResult;
import org.redkale.source.*;
import org.redkale.util.Comment;
import org.redkale.util.SelectColumn;
import org.redkale.util.Sheet;

import javax.annotation.Resource;

/**
 * Created by Lxy at 2017/11/26 9:33.
 */
@RestService(name = "content", comment = "文章帖子服务")
public class ContentService extends BaseService implements UIService<ContentInfo> {

    @Resource
    protected UserService userService;

    @Comment("根据条件查询帖子数据")
    public Sheet<ContentInfo> contentQuery(Flipper flipper, FilterNode node) {
        Sheet<Content> contents = dataSource.querySheet(Content.class, flipper, node);

        createInfo(contents);
        Sheet<ContentInfo> infos = createInfo(contents);
        setIUser(infos);

        return infos;
    }

    @RestMapping(name = "query", auth = false, comment = "内容列表")
    public Sheet<ContentInfo> query(@RestUserid int userid, Flipper flipper, String actived) {
        FilterNode node = FilterNode.create("status", FilterExpress.NOTEQUAL, -10);
        switch (actived) {
            case "top":
                node.and("top", FilterExpress.GREATERTHANOREQUALTO, 20);
                break;
            case "untop":
                node.and("top", 10);
                break;
            case "unsolved":
                node.and("solved", 10);
                break;
            case "solved":
                node.and("solved", 20);
                break;
            case "wonderful":
                node.and("wonderful", FilterExpress.GREATERTHANOREQUALTO, 20);
                break;
        }

        if (!userService.isAdmin(userid)) {//私密贴：非管理员限制查看
            node.and(FilterNode.create("status", FilterExpress.NOTEQUAL, 30).or(FilterNode.create("status", 30).and("userid", userid)));
        } else if (userid <= 0) {//私密贴：未登录限制查看
            node.and("status", FilterExpress.NOTEQUAL, 30);
        }

        return contentQuery(flipper, node);
    }


    @RestMapping(name = "save", comment = "帖子保存")
    public RetResult save(@RestUserid int userid, Content bean) {
        //数据校验
        if (bean.getTitle().isEmpty() || bean.getTitle().length() > 64) {
            return retError("少年你的文章标题太长啦，精简化标题吧，为了更好的SEO长度请少于64个字节");
        }

        if (bean.getContentid() < 1) {
            synchronized (this) {
                int maxid = dataSource.getNumberResult(Content.class, FilterFunc.MAX, 10_0000, "contentid").intValue();
                bean.setContentid(maxid + 1);
            }
            bean.setCreatetime(System.currentTimeMillis());
            bean.setUserid(userid);
            dataSource.insert(bean);
        } else {
            dataSource.findAsync(Content.class, bean.getContentid()).thenAccept(x -> {
                if (x.getUserid() == userid || userService.isAdmin(userid)) {//身份验证 后修改内容
                    dataSource.updateColumnAsync(bean, SelectColumn.includes("title", "digest", "content", "type", "status"));
                }
            });
        }

        return RET_SUCCESS;
    }

    @RestMapping(name = "info", auth = false, comment = "帖子详情")
    public ContentInfo info(@RestUserid int userid, int contentid) {
        // 异步更新帖子阅读数
        dataSource.updateColumnAsync(Content.class, contentid, ColumnValue.inc("viewnum", 1));

        Content content = dataSource.find(Content.class, contentid);
        if (content == null) {
            return null;
        }

        ContentInfo contentInfo = setIUser(content.createInfo());

        //收藏状态
        if (userid > 0) {
            ActLog actLog = dataSource.find(ActLog.class, FilterNode.create("cate", 20).and("tid", contentid).and("status", 10));
            if (actLog != null) {
                contentInfo.setHadcollect(1);
            }
        }
        return contentInfo;
    }

    @RestMapping(name = "collect", comment = "帖子收藏")
    public RetResult collect(@RestUserid int userid, int contentid, int ok) {

        ActLog actLog = dataSource.find(ActLog.class, FilterNode.create("userid", userid).and("tid", contentid).and("cate", 20));
        if (actLog == null && ok == 1) {
            actLog = new ActLog(20, contentid, userid);//.cate(2).tid(contentId).userId(userId);
            actLog.setCreatetime(System.currentTimeMillis());
            dataSource.insert(actLog);
        } else if (actLog != null && actLog.getStatus() != ok) {
            actLog.setStatus((short) ok);
            actLog.setCreatetime(System.currentTimeMillis());
            dataSource.update(actLog);
        } else {
            return RetCodes.retResult(-1, ok == 1 ? "已收藏" : "已取消收藏");
        }

        return RET_SUCCESS;
    }

    @RestMapping(name = "collectquery", comment = "收藏列表")
    public Sheet<ContentInfo> collectQuery(@RestUserid int userid) {
        Flipper flipper = new Flipper().sort("createtime DESC");
        FilterNode filterNode = FilterNode.create("cate", 20).and("status", 10).and("userid", userid);
        Sheet<ActLog> actLogs = dataSource.querySheet(ActLog.class, SelectColumn.includes("tid", "createtime"), flipper, filterNode);

        int[] contentids = actLogs.stream().mapToInt(x -> x.getTid()).toArray();
        Sheet<Content> contents = dataSource.querySheet(Content.class, SelectColumn.includes("contentid", "title"), flipper.sort(null), FilterNode.create("contentid", FilterExpress.IN, contentids));

        Sheet<ContentInfo> infos = createInfo(contents);

        return infos;
    }

    @RestMapping(name = "set", comment = "便捷的修改内容")
    public RetResult contentSet(UserInfo user,
                                @Comment("帖子id") int id,
                                @Comment("status|top|wonderful") String field,
                                @Comment("目标修改值") short v) {
        int userid = user.getUserid();

        //身份验证 后修改内容
        dataSource.findAsync(Content.class, id).thenAccept(content -> {
            if (userService.isAdmin(userid)) {//管理员可以做更多
                //field: status|top|wonderful
                // update content set {field}={v} where id={id}
                dataSource.updateColumn(Content.class, id, field, v);
            } else if (content.getUserid() == userid && ("status".equals(field))) {//非管理员只能修改状态
                dataSource.updateColumn(Content.class, id, field, v);
            }
        });
        return RET_SUCCESS;
    }

    @RestMapping(name = "t", auth = false, comment = "测试HttpScope 模板使用")
    public HttpScope t(@RestUserid int userid) {
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
        Sheet<ContentInfo> hotReply = contentService.query(userid, flipper3, "");

        //最新加入
        Sheet<UserInfo> lastReg = userService.lastReg();

        Kv kv = Kv.by("top", top)
                .set("contents", contents)
                .set("hotReply", hotReply)
                .set("lastReg", lastReg);

        return HttpScope.refer("index.html").attr(kv);
    }

}
