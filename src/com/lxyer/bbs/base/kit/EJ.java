package com.lxyer.bbs.base.kit;

import org.redkale.net.http.RestService;

import java.text.SimpleDateFormat;

/**
 * enjoy模板引擎使用共享方法类，
 * 更多关于enjoy的共享方法请查阅jfinal使用文档
 * Created by Lxy at 2017/11/26 17:19.
 */
@RestService
public class EJ {

    public String date(long time) {
        return date(time, "yyyy-MM-dd HH:mm:ss");
    }

    public String date(long time, String pattern) {
        return new SimpleDateFormat(pattern).format(time);
    }

}
