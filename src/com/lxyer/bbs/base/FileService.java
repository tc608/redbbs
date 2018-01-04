package com.lxyer.bbs.base;

import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestService;
import org.redkale.net.http.RestUploadFile;
import org.redkale.service.RetResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Lxy at 2017/10/3 13:48.
 */
@RestService(automapping = true, comment = "文件服务")
public class FileService extends BaseService {

    private static final String dir = "/var/www/upload/redbbs/";
    private static final String view = "http://img.1216.top/redbbs/";
    private static final String format = "%1$tY%1$tm%1$td%1$tH%1$tM%1$tS";

    @RestMapping(name = "upload", comment = "文件上传")
    public RetResult upload(@RestUploadFile File tmpFile) throws IOException {
        String name = tmpFile.getName();
        String suffix = name.substring(name.lastIndexOf("."));
        String path = String.format(format, System.currentTimeMillis()) + suffix;
        File destFile = new File((winos ? "E:/wk/redbbs/root/tem/" : dir) + path);
        destFile.getParentFile().mkdir();
        if (!tmpFile.renameTo(destFile)){
            try{
                Files.copy(tmpFile.toPath(), destFile.toPath(), StandardCopyOption.ATOMIC_MOVE);
            }finally {
                tmpFile.delete();//删除临时文件
            }
        }
        RetResult result = RetResult.success();
        result.setRetinfo((winos ? "/tem/": view) + path);
        return result;
    }
}
