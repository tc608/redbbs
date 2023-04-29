package net.tccn.bbs.base;

import org.redkale.convert.json.JsonConvert;

/**
 * 所有 entity 上层类
 */
public abstract class BaseEntity {

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
