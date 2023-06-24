package net.tccn.bbs.file;

import net.tccn.bbs.base.BaseService;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestService;
import org.redkale.net.http.RestUploadFile;
import org.redkale.util.Utility;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestService(name = "file")
public class FileService extends BaseService {

    @Resource(name = "property.file.upload_dir")
    private String dir = "./root/upload/";
    @Resource(name = "property.file.view_path")
    private String view = "/upload/";
    private String format = "%1$tY%1$tm%1$td%1$tH%1$tM%1$tS";

    @RestMapping(name = "img")
    public Map img(@RestUploadFile File[] parts) {
        Map ret = new HashMap();
        ret.put("errno", 0);
        List data = new ArrayList();

        for (File part : parts) {
            String name = part.getName();
            String suffix = name.substring(name.lastIndexOf("."));
            String path = String.format(format, System.currentTimeMillis()) + suffix;
            File destFile = new File(dir + Utility.todayYYMMDD() + "/" + path);
            destFile.getParentFile().mkdirs();
            part.renameTo(destFile);


            data.add(view + Utility.todayYYMMDD() + "/" + path);
        }
        ret.put("data", data);
        return ret;
    }
}
