package com.lxyer.bbs.base.iface;

import org.redkale.net.http.RestMapping;
import org.redkale.util.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangxianyou at 2018/6/16 17:56.
 */
public interface CService<I extends UI> {

    @RestMapping(ignore = true)
    default <A extends C> Sheet<I> createInfo(Sheet<A> fSheet){
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
    }
}
