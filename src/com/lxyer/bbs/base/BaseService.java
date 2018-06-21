package com.lxyer.bbs.base;

import com.lxyer.bbs.base.iface.UI;
import com.lxyer.bbs.base.user.UserInfo;
import org.redkale.net.http.RestMapping;
import org.redkale.service.Service;
import org.redkale.source.CacheSource;
import org.redkale.source.DataSource;
import org.redkalex.cache.RedisCacheSource;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by Lxy at 2017/10/3 13:50.
 */
public class BaseService<F extends UF,I extends UI> implements Service {

    protected final int sessionExpireSeconds = 7 * 24 * 60 * 60;

    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    @Resource(name = "art123")
    protected DataSource source;

    @Resource(name = "redis")
    protected RedisCacheSource<Integer> sessions;

    @Resource(name = "userInfos")
    protected CacheSource<UserInfo> userInfos;

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    @RestMapping(ignore = true)
    public DataSource getSource() {
        return source;
    }

    /*protected Sheet<I> createInfo(Sheet<F> fSheet){
        Sheet<I> sheet = new Sheet<>();

        if (fSheet == null || fSheet.getTotal() < 1){
            sheet.setTotal(0);
            sheet.setRows(new ArrayList<>());
        }else {
            int total = (int)fSheet.getTotal();
            List<I> rows = new ArrayList<>(total);
            fSheet.forEach(x->rows.add((I)x.createInfo()));

            sheet.setTotal(total);
            sheet.setRows(rows);
        }

        return sheet;
    }*/

    /**
     * 批量设置用户信息
     * @param ufSheet
     * @param <I>
     * @return
     */
    /*protected <I extends UI> Sheet<I> setIUser(Sheet<I> ufSheet){
        int[] userIds = ufSheet.stream().mapToInt(I::getUserid).toArray();

        List<UserRecord> users = source.queryList(UserRecord.class, FilterNode.create("userId", FilterExpress.IN, userIds));
        ufSheet.forEach(x->{
            UserRecord user = users.stream().filter(u -> u.getUserid() == x.getUserid()).findAny().orElse(null);
            x.setUser(user);
        });
        return ufSheet;
    }*/

    /**
     * 将含有用户外键的实体，转为info，并加入用户信息
     * @param uf
     * @return
     */
    /*protected I setIUser(I uf){
        UserRecord user = source.find(UserRecord.class, uf.getUserid());

        return (I) uf.setUser(user);
    }*/

    @RestMapping(ignore = true)
    public int currentUserId(String sessionid){
        if (sessionid == null) return 0;
        Object userid = sessions.getAndRefresh(sessionid, sessionExpireSeconds);
        return userid == null ? 0 : (Integer)userid;
    }
}
