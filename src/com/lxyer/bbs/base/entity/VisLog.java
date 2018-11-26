package com.lxyer.bbs.base.entity;

import org.redkale.convert.json.JsonConvert;

import java.util.Map;

/**
 * 存贮数据到 非关系型数据库
 *
 * @author: liangxianyou at 2018/11/18 8:47.
 */
public class VisLog {
    private String ip;
    private int userid;
    private String ftime;
    private String uri;
    private long time;
    private Map para;
    private Map headers;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map getPara() {
        return para;
    }

    public void setPara(Map para) {
        this.para = para;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
