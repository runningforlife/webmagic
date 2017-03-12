package us.codecraft.webmagic.samples.scheduler;

import org.junit.Ignore;
import org.junit.Test;
import us.codecraft.webmagic.DownloadRequest;

import java.util.concurrent.TimeUnit;

/**
 * @author code4crafter@gmail.com
 */
public class DelayQueueSchedulerTest {

    @Ignore("infinite")
    @Test
    public void test() {
        DelayQueueScheduler delayQueueScheduler = new DelayQueueScheduler(1, TimeUnit.SECONDS);
        delayQueueScheduler.push(new DownloadRequest("1"), null);
        while (true){
            DownloadRequest poll = delayQueueScheduler.poll(null);
            System.out.println(System.currentTimeMillis()+"\t"+poll);
        }
    }
}
