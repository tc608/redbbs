package net.tccn.bbs.base.util;

import net.tccn.bbs.base.Utils;
import org.redkale.convert.json.JsonConvert;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 2018/3/12 14:17.
 */
@SuppressWarnings("Convert2MethodRef")
public class Kv<K, V> extends LinkedHashMap<K, V> {

    protected static final JsonConvert convert = JsonConvert.root();

    public static Kv of() {
        return new Kv();
    }

    public static Kv of(Object k, Object v) {
        return new Kv().set(k, v);
    }

    public static Kv filter(Map<String, String> map, String... fields) {
        Kv kv = Kv.of();
        if (fields == null || fields.length == 0 || map == null) {
            return kv;
        }

        for (String field : fields) {
            if (field.contains("->")) {
                String[] arr = field.split("->");
                kv.put(arr[1], map.get(arr[0]));
                continue;
            }

            kv.put(field, map.get(field));
        }
        return kv;
    }

    public Kv<K, V> set(K k, V v) {
        put(k, v);
        return this;
    }

    public Kv<K, V> putAll(Kv<K, V> kv) {
        kv.forEach((k, v) -> put(k, v));
        return this;
    }

    //  将obj 属性映射到Kv 中
    public static Kv toKv(Object m, String... fields) {
        Kv kv = Kv.of();
        if (m == null) {
            return kv;
        }
        Stream.of(fields).forEach(field -> {
            String filedT = field;
            String filedS = field;

            try {
                if (field.contains("=")) {
                    String[] arr = field.split("=");
                    filedT = arr[0];
                    filedS = arr[1];
                }

                Method method = m.getClass().getMethod("get" + toUpperCaseFirst(filedS));
                if (method != null) {
                    kv.set(filedT, method.invoke(m));
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                new IllegalArgumentException(String.format("Kv.toKv获取 获取参数[%s]失败", field), e);
            }
        });

        return kv;
    }

    public static <T> List<Kv> toKv(Collection<T> datas, String... fields) {
        return datas.stream().map(x -> toKv(x, fields)).collect(Collectors.toList());
    }

    public static Kv toKv(Object m) {
        return toKv(m, Kv.of(), m.getClass());
    }

    private static Kv toKv(Object m, Kv kv, Class clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("get") || method.getParameterCount() > 0 || "getClass".equals(method.getName()))
                continue;

            String k = toLowerCaseFirst(method.getName().replaceFirst("get", ""));
            if (!kv.containsKey(k) || Utils.isEmpty(kv.get(k))) {
                try {
                    kv.set(k, method.invoke(m));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                try {
                    field.setAccessible(true);
                    kv.set("_id", field.get(m));
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        Class superclass = clazz.getSuperclass();
        if (superclass != null) {
            kv = toKv(m, kv, superclass);
        }
        return kv;
    }

    /**
     * 将字符串第一个字母转大写
     *
     * @param str 待转换字符串
     * @return
     */
    private static String toUpperCaseFirst(String str) {
        Objects.requireNonNull(str);
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 将字符串第一个字母转小写
     *
     * @param str 待转换字符串
     * @return
     */
    private static String toLowerCaseFirst(String str) {
        Objects.requireNonNull(str);
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public <T> T toBean(Class<T> type) {
        return toBean(this, type);
    }

    // 首字母大写
    private static final Function<String, String> upFirst = (s) -> s.substring(0, 1).toUpperCase() + s.substring(1);

    private static final Predicate<Class> isNumber = (t) -> t == Integer.class || t == int.class
            || t == Long.class || t == long.class
            || t == Float.class || t == float.class
            || t == Double.class || t == double.class
            || t == Short.class || t == short.class
            || t == Byte.class || t == byte.class;

    public static <T> T toAs(Object v, Class<T> clazz) {
        if (v == null) {
            return null;
        } else if (v.getClass() == clazz) {
            return (T) v;
        } else if (clazz == String.class) {
            return (T) String.valueOf(v);
        }

        Object v1 = v;
        try {

            if (v.getClass() == Long.class) {//多种数值类型的处理: Long => x
                switch (clazz.getSimpleName()) {
                    case "int", "Integer" -> v1 = (int) (long) v;
                    case "short", "Short" -> v1 = (short) (long) v;
                    case "float", "Float" -> v1 = (float) (long) v;
                    case "byte", "Byte" -> v1 = (byte) (long) v;
                }
            } else if (v.getClass() == Double.class) {
                if (isNumber.test(clazz)) {
                    switch (clazz.getSimpleName()) {
                        case "long", "Long" -> v1 = (long) (double) v;
                        case "int", "Integer" -> v1 = (int) (double) v;
                        case "short", "Short" -> v1 = (short) (double) v;
                        case "float", "Float" -> v1 = (float) (double) v;
                        case "byte", "Byte" -> v1 = (byte) (double) v;
                    }
                } else if (clazz == String.class) {
                    v1 = String.valueOf(v);
                }
            } else if (v.getClass() == String.class) {
                switch (clazz.getSimpleName()) {
                    case "Date" -> v1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) v);
                    case "short", "Short" -> v1 = (short) Double.parseDouble((String) v);
                    case "float", "Float" -> v1 = (float) Double.parseDouble((String) v);
                    case "int", "Integer" -> v1 = (int) Double.parseDouble((String) v);
                    case "long", "Long" -> v1 = (long) Double.parseDouble((String) v);
                    case "double", "Double" -> v1 = Double.parseDouble((String) v);
                    case "byte", "Byte" -> v1 = Byte.parseByte((String) v);
                }
            } else if (v.getClass() == Integer.class) {
                switch (clazz.getSimpleName()) {
                    case "long", "Long" -> v1 = (long) (int) v;
                    case "short", "Short" -> v1 = (short) (int) v;
                    case "float", "Float" -> v1 = (float) (int) v;
                    case "byte", "Byte" -> v1 = (byte) (int) v;
                }
            } else if (v.getClass() == Float.class) {
                switch (clazz.getSimpleName()) {
                    case "long", "Long" -> v1 = (long) (float) v;
                    case "int", "Integer" -> v1 = (int) (float) v;
                    case "short", "Short" -> v1 = (short) (float) v;
                    case "byte", "Byte" -> v1 = (byte) (float) v;
                }
            } else {
                v1 = v;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (T) v1;
    }

    public static <T> T toBean(Map map, Class<T> clazz) {
        //按照方法名 + 类型寻找，
        //按照方法名 寻找
        //+
        Object obj = null;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            new IllegalArgumentException("创建对象实列失败", e); // 检查clazz是否有无参构造
        }

        for (String k : (Set<String>) map.keySet()) {
            Object v = map.get(k);
            if (v == null) continue;
            //寻找method
            try {
                String methodName = "set" + upFirst.apply(k);
                Class tClazz = null;
                Method method = null;
                try {
                    method = clazz.getMethod(methodName, tClazz = v.getClass());
                } catch (NoSuchMethodException e) {
                    //e.printStackTrace();
                }
                if (method == null) {
                    for (Method _method : clazz.getMethods()) {
                        if (methodName.equals(_method.getName()) && _method.getParameterCount() == 1) {
                            method = _method;
                            tClazz = _method.getParameterTypes()[0];
                        }
                    }
                }

                if (method == null) {
                    for (Method _method : clazz.getMethods()) {
                        if (methodName.equalsIgnoreCase(_method.getName()) && _method.getParameterCount() == 1) {
                            method = _method;
                            tClazz = _method.getParameterTypes()[0];
                        }
                    }
                }

                if (method != null) {
                    method.invoke(obj, toAs(v, tClazz));
                }

                //没有方法，找属性注解
                /*if (method == null) {
                    Field field = null;
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field _field : fields) {
                        To to = _field.getAnnotation(To.class);
                        if (to != null && k.equals(to.value())) {
                            field = _field;
                            tClazz = _field.getType();
                            break;
                        }
                    }

                    if (field != null) {
                        field.setAccessible(true);
                        field.set(obj, toAs(v, tClazz));
                    }
                }*/
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return (T) obj;
    }

    public String toString() {
        return convert.convertTo(this);
    }

    public int getInt(String key) {
        return toAs(get(key), int.class);
    }

    public int getInt(String key, int defaultValue) {
        V v = get(key);
        if (v == null) {
            return defaultValue;
        }
        return toAs(v, int.class);
    }

    public long getLong(String key) {
        return toAs(get(key), Long.class);
    }

    public long getLong(String key, long defaultValue) {
        V v = get(key);
        if (v == null) {
            return defaultValue;
        }
        return toAs(v, long.class);
    }

    public String getStr(String key) {
        return toAs(get(key), String.class);
    }

    public String getStr(String key, String defaultValue) {
        V v = get(key);
        if (v == null) {
            return defaultValue;
        }
        return toAs(v, String.class);
    }
}