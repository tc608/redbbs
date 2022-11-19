package com.lxyer.bbs.content;

import com.lxyer.bbs.base.iface.CI;
import com.lxyer.bbs.base.iface.UI;
import com.lxyer.bbs.base.user.UserRecord;
import lombok.Getter;
import lombok.Setter;
import org.redkale.convert.ConvertColumn;

import java.io.Serializable;

/**
 * Created by Lxy at 2017/11/26 20:52.
 */
@Setter
@Getter
public class ContentInfo implements UI<ContentInfo>, Serializable, CI {

    private int contentid;
    private int userid;
    private String title = "";
    private String digest = "";
    private String content = "";
    private String createtime;
    private int cate;
    private int type;
    private int replynum;
    private int viewnum;
    private int wonderful;
    private int top;
    private int solved;
    private int status = 10;

    private String typename;
    private int hadcollect = -1;


    //-----------
    private UserRecord user;

    @ConvertColumn(ignore = true)
    @Override
    public UserRecord getUser() {
        return user;
    }

    @Override
    public ContentInfo setUser(UserRecord user) {
        this.user = user;
        return this;
    }
}
