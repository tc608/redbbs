package com.lxyer.bbs.base;

import org.redkale.net.http.RestService;

import java.text.SimpleDateFormat;

/**
 * Created by Lxy at 2017/11/26 17:19.
 */
@RestService
public class EJ {

    public String date(long time){
        return date(time, "yyyy-MM-dd HH:mm:ss");
    }
    public String date(long time, String pattern){
        return new SimpleDateFormat(pattern).format(time);
    }


    public static void main(String[] args) {
        /*EJ ej = new EJ();

        System.out.println(ej.date(1511682960591L));*/
        /*List<Kv> list = asList(
                Kv.by("k", 1).set("a", "1+1=?").set("q", 2)
                , Kv.by("k", 2).set("a", "1*1=?").set("q", 1)
                , Kv.by("k", 3).set("a", "3+2-5=?").set("q", 0)
                , Kv.by("k", 4).set("a", "Math.abs(-3)=?").set("q", 3)
        );

        int k = 3;
        Kv kv = list.stream().filter(x -> x.getInt("k") == k).findFirst().orElse(Kv.create());

        System.out.println(System.currentTimeMillis());
        System.out.println(kv.toString());
        System.out.println(kv.getStr("q").equals("0"));*/

    }

}
