package com.lxyer.bbs.base.kit;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lxy at 2017/11/29 15:17.
 */
public final class LxyKit {

    public static String dateFmt(long time){
        /**
         * 刚刚       60秒内        60 * 1000
         * x分钟前     1小时候内    60 * 60*1000
         * x小时前     1天内        24 * 60*60*1000
         * x天前       1周内        7 * 24*60*60*1000
         * 年-月-日    1周前
         */
        long now = System.currentTimeMillis();

        long diff = now - time;
        if (diff < 60 * 1000)
            return "刚刚";
        else if (diff < 60 * 60 *1000)
            return Math.floorDiv(diff, 60 *1000) + "分钟前";
        else if (diff < 24 * 60*60*1000)
            return Math.floorDiv(diff, 60 *60*1000) + "小时前";
        else if (diff > 24 * 60*60*1000 && diff < 7 * 24*60*60*1000)
            return Math.floorDiv(diff, 24 * 60*60*1000) + "天前";
        else
            return new SimpleDateFormat("yyyy-MM-dd").format(time);

    }

    public static String delHTMLTag(String htmlStr){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    public static  <T> T[] listToArray(List list, T[] ts){
        for (int i = 0; i < list.size(); i++) {
            ts[0] = (T) list.get(i);
        }
        return ts;
    }

    public static int[] listToArray(List list, int[] ts){
        for (int i = 0; i < list.size(); i++) {
            ts[0] = (int) list.get(i);
        }
        return ts;
    }


    public static void main(String[] args) {
        Pattern p = Pattern.compile("@*&nbsp;");

        Matcher matcher = p.matcher("@nick&nbsp;[污]&nbsp;");

        int count = 0;
        while (matcher.find()) {
            count++;
        }
    }
}
