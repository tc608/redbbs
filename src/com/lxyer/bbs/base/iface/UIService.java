package com.lxyer.bbs.base.iface;

import com.lxyer.bbs.base.user.UserRecord;
import org.redkale.net.http.RestMapping;
import org.redkale.source.DataSource;
import org.redkale.source.FilterExpress;
import org.redkale.source.FilterNode;
import org.redkale.util.Sheet;

import java.util.List;

/**
 * Created by liangxianyou at 2018/6/16 18:25.
 */
public interface UIService<I extends UI> extends CService<I> {

    DataSource getSource();

    @RestMapping(ignore = true)
    default Sheet<I> setIUser(Sheet<I> sheet) {
        int[] userids = sheet.stream().mapToInt(I::getUserid).toArray();

        List<UserRecord> users = getSource().queryList(UserRecord.class, FilterNode.create("userid", FilterExpress.IN, userids));
        sheet.forEach(x -> {
            UserRecord user = users.stream().filter(u -> u.getUserid() == x.getUserid()).findAny().orElse(null);
            x.setUser(user);
        });
        return sheet;
    }

    @RestMapping(ignore = true)
    default I setIUser(I i) {
        UserRecord user = getSource().find(UserRecord.class, i.getUserid());

        return (I) i.setUser(user);
    }

}
