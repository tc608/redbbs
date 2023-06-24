package net.tccn.bbs.vislog.entity;

import lombok.Getter;
import lombok.Setter;
import org.redkale.convert.json.JsonConvert;

import java.util.Map;

/**
 * 存贮数据到 非关系型数据库
 *
 * @author liangxianyou at 2018/11/18 8:47.
 */
@Setter
@Getter
public class VisLog {
    private String ip;
    private int userid;
    private String ftime;
    private String uri;
    private long time;
    private Map para;
    private Map headers;

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
