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
    default <A extends C> Sheet<I> createInfo(Sheet<A> sheet) {
        Sheet<I> sheet2 = new Sheet<>();

        if (sheet == null || sheet.getTotal() < 1) {
            sheet2.setTotal(0);
            sheet2.setRows(new ArrayList<>());
        } else {
            int total = (int) sheet.getTotal();
            List<I> rows = new ArrayList<>(total);
            sheet.forEach(x -> rows.add((I) x.createInfo()));

            sheet2.setTotal(total);
            sheet2.setRows(rows);
        }

        return sheet2;
    }
}
