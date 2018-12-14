package com.lxyer.bbs.servlet;

import com.lxyer.bbs.base.BaseServlet;
import org.redkale.net.http.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 文件相关
 * Created by liangxianyou at 2018/6/4 13:17.
 */
@WebServlet(value = {"/upload","/upload/*"}, comment = "文件管理入口")
public class FileServlet extends BaseServlet {

    @Resource(name = "property.file.upload_dir")
    private String dir;
    @Resource(name = "property.file.view_path")
    private String view;

    private Function<String, String> fileName = (name) -> {
        String suffix = name.substring(name.lastIndexOf("."));
        String fileName = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", System.currentTimeMillis()) + suffix;
        return fileName;
    };

    @HttpMapping(url = "/upload/img", auth = false, comment = "图片上传,支持批量上传")
    public void uploadImg(HttpRequest request, HttpResponse response){
        try {
            Map ret = new HashMap();
            ret.put("errno", 0);

            List data = new ArrayList();
            for (MultiPart part : request.multiParts()) {
                String name = fileName.apply(part.getName());
                File destFile = new File(dir + name);

                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdir();
                }

                part.save(destFile);
                data.add(view + name);
            }

            ret.put("data", data);
            response.setContentType("application/json; charset=utf-8");
            response.finish(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
