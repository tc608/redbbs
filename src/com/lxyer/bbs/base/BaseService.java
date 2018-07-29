package com.lxyer.bbs.base;

import org.redkale.net.http.RestMapping;
import org.redkale.service.Service;
import org.redkale.source.CacheSource;
import org.redkale.source.DataSource;
import org.redkalex.cache.RedisCacheSource;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Lxy at 2017/10/3 13:50.
 */
public class BaseService implements Service {

    protected final int sessionExpireSeconds = 7 * 24 * 60 * 60;

    @Resource(name = "SERVER_ROOT")
    protected File webroot;

    @Resource(name = "art123")
    protected DataSource source;

    @Resource(name = "redis")
    protected RedisCacheSource<Integer> sessions;

    @Resource(name = "cacheSource")
    protected CacheSource cacheSource;

    protected static final boolean winos = System.getProperty("os.name").contains("Window");

    @RestMapping(ignore = true)
    public DataSource getSource() {
        return source;
    }

    @RestMapping(ignore = true)
    public int currentUserid(String sessionid){
        if (sessionid == null) return 0;
        Object userid = null;
        try {
            userid = sessions.getAndRefresh(sessionid, sessionExpireSeconds);
        }catch (Exception e){
            e.printStackTrace();
        }
        return userid == null ? 0 : (Integer)userid;
    }

    /**
     * 文件上传
     */
    private static final String dir = "/var/www/upload/redbbs/";
    private static final String view = "http://img.1216.top/redbbs/";
    private static final String format = "%1$tY%1$tm%1$td%1$tH%1$tM%1$tS";
    protected PicRecord upFile(File tmpFile, IPic bean){
        String name = tmpFile.getName();
        String suffix = name.substring(name.lastIndexOf("."));
        String path = String.format(format, System.currentTimeMillis()) + suffix;
        File destFile = new File((winos ? "root/tem/" : dir) + path);
        destFile.getParentFile().mkdir();
        if (!tmpFile.renameTo(destFile)){
            try{
                Files.copy(tmpFile.toPath(), destFile.toPath(), StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                tmpFile.delete();//删除临时文件
            }
        }

        //存贮资源数据
        String src = (winos ? "/tem/" : view) + path;//资源访问地址
        PicRecord pic = bean.crearePic();
        pic.setSrc(src);
        pic.setName(name);
        pic.setLen((int) destFile.length());
        source.insertAsync(pic);
        return pic;
    }
}
