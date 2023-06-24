package net.tccn.bbs.vislog.entity;

import lombok.Getter;
import lombok.Setter;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lxyer
 */
@Setter
@Getter
@Cacheable(interval = 5 * 60)
@Table(comment = "[动态属性表]")
public class DynAttr implements java.io.Serializable {

    @Id
    @Column(comment = "[目标数据id]")
    private int tid;

    @Column(comment = "[类型]1文章, 2xx, 3...,")
    private short cate;

    @Column(length = 32)
    private String attr = "";

    @Column(comment = "[属性值]")
    private String value = "";

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
