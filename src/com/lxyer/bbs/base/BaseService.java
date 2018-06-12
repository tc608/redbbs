package com.lxyer.bbs.base;

import com.lxyer.bbs.base.user.User;
import com.lxyer.bbs.base.user.UserInfo;
import org.redkale.service.Service;
import org.redkale.source.CacheSource;
import org.redkale.source.DataSource;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterNode;
import org.redkale.util.Sheet;
import org.redkalex.cache.RedisCacheSource;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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


    protected Sheet<I> createInfo(Sheet<F> fSheet){
        if (fSheet == null || fSheet.getTotal() < 1){
            Sheet<I> sheet = new Sheet<>();
            sheet.setTotal(0);
            sheet.setRows(new ArrayList<>());
            return sheet;
        }
        List<I> list = new ArrayList<>((int)fSheet.getTotal());
        fSheet.forEach(x->{
            list.add((I)x.createInfo());
        });

        Sheet<I> sheet = new Sheet<>();
        sheet.setTotal(fSheet.getTotal());
        sheet.setRows(list);
        return sheet;
    }

    protected <I extends UI> Sheet<I> setIUser(Sheet<I> ufSheet){
        int[] userIds = ufSheet.stream().mapToInt(I::getUserId).toArray();
        List<User> users = source.queryList(User.class, FilterNode.create("userId", FilterExpress.IN, userIds));
        List<I> infos = new ArrayList((int) ufSheet.getTotal());

        ufSheet.forEach(x->{
            User user = users.stream().filter(u -> u.getUserId() == x.getUserId()).findAny().orElse(null);
            infos.add((I) x.setUser(user));
        });

        Sheet<I> sheet = new Sheet<>();
        sheet.setTotal(ufSheet.getTotal());
        sheet.setRows(infos);

        return sheet;
    }

    /**
     * 将含有用户外键的实体，转为info，并加入用户信息
     * @param uf
     * @return
     */
    protected I setIUser(I uf){
        User user = source.find(User.class, uf.getUserId());

        return (I) uf.setUser(user);
    }
}
