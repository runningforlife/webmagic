package us.codecraft.webmagic.scheduler;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import us.codecraft.webmagic.DownloadRequest;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;

/**
 * @author code4crafter@gmail.com <br>
 */
public class RedisSchedulerTest {

    private RedisScheduler redisScheduler;

    @Before
    public void setUp() {
        redisScheduler = new RedisScheduler("localhost");
    }

    @Ignore("environment depended")
    @Test
    public void test() {
        Task task = new Task() {
            @Override
            public String getUUID() {
                return "1";
            }

            @Override
            public Site getSite() {
                return null;
            }
        };
        DownloadRequest request = new DownloadRequest("http://www.ibm.com/developerworks/cn/java/j-javadev2-22/");
        request.putExtra("1","2");
        redisScheduler.push(request, task);
        DownloadRequest poll = redisScheduler.poll(task);
        System.out.println(poll);

    }
}
