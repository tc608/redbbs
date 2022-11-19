package com.lxyer.bbs.base.user;

import lombok.Getter;
import lombok.Setter;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author lxyer
 */
@Setter
@Getter
public class UserInfo implements java.io.Serializable {

    @Id
    @Column(comment = "[用户id]")
    private int userid;

    private String username = "";
    private int sex = 1;
    private String password = "";
    private String phone = "";
    private String nickname = "";
    private String avatar = "";
    private String relaname = "";
    private String email = "";
    private int roleid = 0;
    private String site = "";
    private String git = "";
    private long createtime;
    private String sign = "";
    private String city = "";
    private int status = 1;
    private String time = "";


    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }

    /**
     * 检查用户权限
     *
     * @param moduleid
     * @param actionid
     * @return
     */
    public boolean checkAuth(int moduleid, int actionid) {

        return !(moduleid == 2 && actionid == 1);
    }
}
