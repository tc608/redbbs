package com.lxyer.bbs.servlet;

import com.lxyer.bbs.base.BaseServlet;
import org.redkale.net.http.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件相关
 * Created by liangxianyou at 2018/6/4 13:17.
 */
@WebServlet(value = {"/upload", "/upload/*"}, comment = "文件管理入口")
public class FileServlet extends BaseServlet {

    private static final String dir = "/var/www/upload/redbbs/";
    private static final String view = "http://img.1216.top/redbbs/";
    private static final String format = "%1$tY%1$tm%1$td%1$tH%1$tM%1$tS";

    @HttpMapping(url = "/upload/img", auth = false, comment = "图片上传")
    public void uploadImg(HttpRequest request, HttpResponse response) {
        try {
            Map ret = new HashMap();
            ret.put("errno", 0);
            List data = new ArrayList();

            for (MultiPart part : request.multiParts()) {
                String name = part.getName();
                String suffix = name.substring(name.lastIndexOf("."));
                String path = String.format(format, System.currentTimeMillis()) + suffix;
                File destFile = new File((winos ? "E:/wk/own/redbbs/root/tem/" : dir) + path);
                destFile.getParentFile().mkdir();

                part.save(destFile);

                data.add((winos ? "/tem/" : view) + path);
            }
            ret.put("data", data);

            response.setContentType("application/json; charset=utf-8");
            response.finish(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
