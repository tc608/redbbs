/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tccn.bbs.base;

import net.tccn.bbs.base.util.Kv;
import org.redkale.util.Comment;
import org.redkale.util.Reproduce;
import org.redkale.util.Sheet;
import org.redkale.util.Utility;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Utils {

    private Utils() {
    }

    public static String dateFmt(long time) {
        /*
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
        else if (diff < 60 * 60 * 1000)
            return Math.floorDiv(diff, 60 * 1000) + "分钟前";
        else if (diff < 24 * 60 * 60 * 1000)
            return Math.floorDiv(diff, 60 * 60 * 1000) + "小时前";
        else if (diff > 24 * 60 * 60 * 1000 && diff < 7 * 24 * 60 * 60 * 1000)
            return Math.floorDiv(diff, 24 * 60 * 60 * 1000) + "天前";
        else
            return new SimpleDateFormat("yyyy-MM-dd").format(time);

    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    public static <T> T[] listToArray(List list, T[] ts) {
        for (Object o : list) {
            ts[0] = (T) o;
        }
        return ts;
    }

    public static int[] listToArray(List list, int[] ts) {
        for (Object o : list) {
            ts[0] = (int) o;
        }
        return ts;
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 待判断的对象
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return ((String) obj).trim().isEmpty();
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        } else if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }

        return false;
    }

    private static final Map<String, Reproduce> reproduceMap = new HashMap<>();

    /**
     * @param d   目标对象
     * @param s   源对象
     * @param <D> 目标对象的数据类型
     * @param <S> 源对象的数据类型
     * @return
     */
    public static <D, S> D copy(D d, S s) {
        String reproductKey = d.getClass().getName() + "_" + s.getClass().getName();

        Reproduce<D, S> reproduce = reproduceMap.get(reproductKey);
        if (reproduce == null) {
            reproduceMap.put(reproductKey, reproduce = (Reproduce<D, S>) Reproduce.create(d.getClass(), s.getClass()));
        }

        return reproduce.apply(d, s);
    }


    public static List<String> parseHtmlImage(String html) {
        Pattern pattern = Pattern.compile("(?<=(<img\\s?src\\s?=\\s?\"))\\S+\\.[A-Za-z]+");
        Matcher match = pattern.matcher(html);
        List<String> ls = new ArrayList<>();
        while (match.find()) {
            ls.add(match.group());
        }
        return ls;
    }

    /**
     * @param rs
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> queryList(ResultSet rs, Class<T> type) {
        try {
            List list = new ArrayList();
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            while (rs.next()) {
                Kv row = Kv.of();
                for (int i = 1; i <= count; i++) {
                    String columnTypeName = metaData.getColumnTypeName(i);
                    //String columnName = metaData.getColumnName(i);
                    String columnLabel = metaData.getColumnLabel(i);
                    row.put(columnLabel, null);

                    if (rs.getObject(i) != null) {
                        switch (columnTypeName) {
                            case "DATETIME", "TIMESTAMP", "DATE" -> row.put(columnLabel, rs.getTimestamp(i).getTime());
                            default -> row.put(columnLabel, rs.getObject(i));
                        }
                    }
                }
                list.add((Map.class == type || Kv.class == type) ? row : Kv.toBean(row, type));
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询 第一条的 第一列数据 值
     *
     * @param rs
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T findColumn(ResultSet rs, Class<T> type) {
        try {
            Object v = null;
            while (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int count = metaData.getColumnCount();

                for (int i = 1; i <= count; i++) {
                    String columnTypeName = metaData.getColumnTypeName(i);
                    if (rs.getObject(i) != null) {
                        switch (columnTypeName) {
                            case "DATETIME", "TIMESTAMP", "DATE" -> v = rs.getTimestamp(i).getTime();
                            default -> v = rs.getObject(i);
                        }
                    }
                    break;
                }
            }

            return Kv.toAs(v, type);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * List 混排
     *
     * @param list
     * @return
     */
    public static <T> List<T> mix(List<T> list) {
        int len = list.size();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int r = random.nextInt(len);
            if (i == r) continue;

            T x = list.get(i);
            list.set(i, list.get(r));
            list.set(r, x);
        }
        return list;
    }

    @Comment("获取集合随机元素")
    public static <T> List<T> randomItems(List<T> list, int len) {
        List<Integer> randoms = getRandoms(list.size(), len);
        List<T> items = new ArrayList<>(randoms.size());

        randoms.forEach(x -> items.add(list.get(x)));
        return items;
    }

    @Comment("获取随机数字符")
    public static String randomNumber(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }

    @Comment("获取随机数集合")
    private static List<Integer> getRandoms(int max, int len) {
        Set<Integer> randoms = new HashSet<>();
        Random random = new Random();
        while (randoms.size() < len && randoms.size() < max) {
            randoms.add(random.nextInt(max));
        }
        List<Integer> list = randoms.stream().collect(Collectors.toList());
        return mix(list);
    }

    /**
     * unicode转中文
     *
     * @param str
     * @return
     */
    public static String unicodeToCn(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * 计算字符串的字符长度
     *
     * @param value
     * @return
     */
    public static int strLength(String value) {
        int valueLength = 0;
        String chinese = "[一-龥]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static String randomIP() {
        // aaa.aaa.aaa.aaa
        StringBuilder buf = new StringBuilder();

        Random r = new Random();
        buf.append("x").append(".");
        buf.append(r.nextInt(255)).append(".");
        buf.append(r.nextInt(255)).append(".");
        buf.append(r.nextInt(255));

        return buf.toString();
    }

    public static String fmt36(int n) {
        return Integer.toString(n, 36);
    }

    public static String fmt36(long n) {
        return Long.toString(n, 36);
    }

    public static <T, V> Map<T, V> toMap(Collection<V> list, Function<V, T> fun) {
        Map<T, V> map = new HashMap<>(list.size());
        for (V v : list) {
            if (v == null) {
                continue;
            }
            map.put(fun.apply(v), v);
        }
        return map;
    }

    public static <T, V, T2> Map<T, T2> toMap(Collection<V> list, Function<V, T> fun, Function<V, T2> fun2) {
        Map<T, T2> map = new HashMap<>(list.size());
        for (V v : list) {
            if (v == null) {
                continue;
            }
            map.put(fun.apply(v), fun2.apply(v));
        }
        return map;
    }

    public static <T, V> List<V> toList(Collection<T> list, Function<T, V> fun) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<V> list1 = new ArrayList<>();
        list.forEach(x -> list1.add(fun.apply(x)));
        return list1;
    }

    public static <T, V> List<V> toList(Sheet<T> sheet, Function<T, V> fun) {
        List<V> list = new ArrayList<>();
        sheet.forEach(x -> list.add(fun.apply(x)));
        return list;
    }

    public static <T, V> Set<V> toSet(Collection<T> list, Function<T, V> fun) {
        if (list == null || list.isEmpty()) {
            return new HashSet<>();
        }
        Set<V> set = new HashSet<>(list.size());
        list.forEach(x -> set.add(fun.apply(x)));
        return set;
    }

    public static <T, V> Set<V> toSet(Sheet<T> sheet, Function<T, V> fun) {
        Set<V> set = new HashSet<>();
        sheet.forEach(x -> set.add(fun.apply(x)));
        return set;
    }

    public static <T> List<T> filter(Collection<T> list, Predicate<T> predicate) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> list1 = new ArrayList<>(list.size());
        list.forEach(x -> {
            if (!predicate.test(x)) {
                return;
            }

            list1.add(x);
        });

        return list1;
    }

    public static <T, V> List<V> filterToList(Collection<T> list, Predicate<T> predicate, Function<T, V> fun) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<V> list1 = new ArrayList<>(list.size());

        list.forEach(x -> {
            if (!predicate.test(x)) {
                return;
            }

            list1.add(fun.apply(x));
        });

        return list1;
    }

    public static <T, V> Map<V, List<T>> group(Collection<T> list, Function<T, V> fun) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.groupingBy(fun));
    }

    public static <T, V, K> Map<V, List<K>> group(Collection<T> list, Function<T, V> fun, Function<T, K> fun2) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }
        Map<V, List<T>> group = group(list, fun);
        Map<V, List<K>> _group = new HashMap<>();
        group.forEach((k, v) -> _group.put(k, toList(v, fun2)));
        return _group;
    }

    public static String getHtmlBody(String html) {
        String s = html.replaceAll("\n", "");
        int bodyIndex = s.indexOf("<body>");
        if (bodyIndex > -1) {
            bodyIndex = bodyIndex + 6;
            int lastIndexOf = s.lastIndexOf("</body>");
            if (lastIndexOf < bodyIndex) lastIndexOf = s.length();
            s = s.substring(bodyIndex, lastIndexOf);
        }
        return s;
    }

    public static String getHtmlText(String html) {
        return html.replaceAll("<([^ \\f\\n\\r\\t\\v<]| )+>", "");
    }

    //获取指定日期的 前/后 几天日期
    public static long plusDays(long datetime, int daynum) {
        if (daynum == 0 || datetime == 0) {
            return datetime;
        }

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(datetime), zoneId);
        time = time.plusDays(daynum);
        return time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    // -----------------
    private static final MessageDigest sha1;
    private static final MessageDigest md5;
    private static final String AES_KEY = "HAOGAME_20200721";
    private static final Cipher aesEncrypter; //加密
    private static final Cipher aesDecrypter; //解密

    static {
        MessageDigest d = null;
        try {
            d = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new Error(ex);
        }
        sha1 = d;
        try {
            d = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new Error(ex);
        }
        md5 = d;

        Cipher cipher = null;
        final SecretKeySpec aesKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        } catch (Exception e) {
            throw new Error(e);
        }
        aesEncrypter = cipher;  //加密
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
        } catch (Exception e) {
            throw new Error(e);
        }
        aesDecrypter = cipher; //解密
    }

    //AES加密
    public static String encryptAES(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        try {
            synchronized (aesEncrypter) {
                return Utility.binToHexString(aesEncrypter.doFinal(value.getBytes()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //AES解密
    public static String decryptAES(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        byte[] hex = Utility.hexToBin(value);
        try {
            synchronized (aesEncrypter) {
                return new String(aesDecrypter.doFinal(hex));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Comment("GET请求参数转换为字符，结果：p1=v1&p2=v2&p3=v3")
    public static String convertHttpParams(Map map, boolean encode) {
        if (map == null || map.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (Utils.isEmpty(k) || Utils.isEmpty(v)) {
                return;
            }
            String value = String.valueOf(v);
            if (encode) {
                value = URLEncoder.encode(String.valueOf(v), StandardCharsets.UTF_8);
            }
            sb.append("&").append(k).append("=").append(value);
        });
        return sb.length() > 0 ? sb.substring(1) : "";
    }

    @Comment("对象转GET请求参数转换为字符，结果：p1=v1&p2=v2&p3=v3")
    public static String convertHttpParams(Object o, List<String> removeFields, boolean encode) {
        Class<?> c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        Map<String, Object> map = new TreeMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (!Utils.isEmpty(removeFields) && removeFields.contains(name)) {
                continue;
            }
            Object value = null;
            try {
                value = field.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (value != null) {
                map.put(name, value);
            }
        }
        return convertHttpParams(map, encode);
    }

    @Comment("GET请求参数转换为字符，结果：p1=v1&p2=v2&p3=v3")
    public static String convertHttpParams(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        Set<String> sortSet = new TreeSet<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sortSet.add(entry.getKey());
        }

        // 参数名按ASCII码从小到大排序（字典序）,然后使用( & )连接排好序的key=value集合
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String key : sortSet) {
            if (isFirst) {
                sb.append(key + "=" + URLEncoder.encode(String.valueOf(map.get(key)), StandardCharsets.UTF_8));
                isFirst = false;
            } else {
                sb.append("&" + key + "=" + URLEncoder.encode(String.valueOf(map.get(key)), StandardCharsets.UTF_8));
            }
        }

        return sb.toString();
    }

    @Comment("对象转map")
    public static Map<String, Object> convertToMap(Object o, List<String> removeFields) {
        Class<?> c = o.getClass();
        List<Field> fields = Stream.of(c.getDeclaredFields()).collect(Collectors.toList());
        if (c.getSuperclass() != null) {
            Field[] superFields = c.getSuperclass().getDeclaredFields();
            if (superFields.length > 0) {
                fields.addAll(Arrays.asList(superFields));
            }
        }

        Map<String, Object> map = new TreeMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (!Utils.isEmpty(removeFields) && removeFields.contains(name)) {
                continue;
            }
            Object value = null;
            try {
                value = field.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (value != null) {
                map.put(name, value);
            }
        }
        return map;
    }

    @Comment("key字典排序")
    public static LinkedHashMap sortedMap(Map<String, ?> map, String... excludeKeys) {
        List<String> excludeList = List.of(excludeKeys);
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        map.entrySet().stream()
                .filter(f -> !excludeList.contains(f.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .forEach(x -> linkedHashMap.put(x.getKey(), x.getValue()));
        return linkedHashMap;
    }

    @Comment(" string转化为jsonstring")
    public static String convertParamStrToJsonStr(String paramStr) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(paramStr.split("&")).forEach(x -> {
            //防止有地址传参时造成的错误解析
            x = URLDecoder.decode(x, StandardCharsets.UTF_8);
            int index = x.indexOf("=");
            char c = x.charAt(index + 1);
            builder.append(String.format("\"%s\":\"%s\",", x.substring(0, index), c == '[' || c == '{' ? x.substring(index + 1).replaceAll("\"", "\\\\\"") : x.substring(index + 1)));
        });
        return String.format("{%s}", builder.deleteCharAt(builder.length() - 1).toString());
    }

    @Comment("获取次日0点0分0秒时间戳")
    public static long getNextDayZero() {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusDays(1);
        LocalDateTime dateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0);
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String formatTime(long time, String pattern) {
        if (Utils.isEmpty(pattern)) pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(time));
    }

    @Comment("获取字符串中某个字符的数量")
    public static int strCountOfChar(String str, String code) {
        int fromIndex = 0;
        int count = 0;
        while (true) {
            int index = str.indexOf(code, fromIndex);
            if (-1 != index) {
                fromIndex = index + 1;
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private static final Random random = new Random();

    public static int randomNum(int len) {
        int rs = random.nextInt(9);
        if (rs == 0) rs = 1;
        for (int i = 0; i < len - 1; i++) {  //总长度为6
            rs = rs * 10 + random.nextInt(9);
        }
        return rs;
    }

    public static String genMd5(String info) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] infoBytes = info.getBytes();
        md5.update(infoBytes);
        byte[] sign = md5.digest();
        return byteArrayToHex(sign);
    }

    public static String byteArrayToHex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toLowerCase());
        }
        return sign.toString();
    }

}
