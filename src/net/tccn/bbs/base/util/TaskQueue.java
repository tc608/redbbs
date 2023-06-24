package net.tccn.bbs.base.util;

import net.tccn.bbs.base.BaseService;
import net.tccn.bbs.content.ContentInfo;
import net.tccn.bbs.content.ContentService;
import net.tccn.bbs.user.UserService;
import net.tccn.bbs.vislog.entity.VisLog;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestService;
import org.redkale.source.Flipper;
import org.redkale.util.AnyValue;
import org.redkale.util.Sheet;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liangxianyou at 2018/6/20 22:54.
 */
@RestService(name = "xxx", automapping = true, comment = "日志记录")
public class TaskQueue<T> extends BaseService implements Runnable {

    @Resource
    private ContentService contentService;
    @Resource
    private UserService userService;

    protected static final LinkedBlockingQueue queue = new LinkedBlockingQueue();

    @Override
    public void init(AnyValue config) {

        // 独立线程，用户访问行为记录到数据库
        new Thread(() -> {
            while (true) {
                try {
                    T task = take();
                    //记录访问日志，如果是访问的文章详情：对文章访问数量更新
                    if (task instanceof VisLog) {

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public TaskQueue() {
        new Thread(this).start();
    }

    @RestMapping(ignore = true)
    public T take() throws InterruptedException {
        return (T) queue.take();
    }

    @RestMapping(ignore = true)
    public void put(T t) throws InterruptedException {
        queue.put(t);
    }

    @Override
    @RestMapping(ignore = true)
    public void run() {

    }

    @RestMapping(ignore = true, comment = "访问热帖数据")
    public Sheet<ContentInfo> hotView(String sessionid) {

        return Sheet.empty();
    }
}
