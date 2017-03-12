package us.codecraft.webmagic.scheduler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import us.codecraft.webmagic.DownloadRequest;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;

/**
 * @author sai
 * Created by sai on 16-7-5.
 */
public class RedisPrioritySchedulerTest
{

    private RedisPriorityScheduler scheduler;

    @Before
    public void setUp()
    {
        scheduler = new RedisPriorityScheduler("localhost");
    }

    @Ignore("environment depended")
    @Test
    public void test()
    {
        Task task = new Task() {
            @Override
            public String getUUID() {
                return "TestTask";
            }

            @Override
            public Site getSite() {
                return null;
            }
        };

        scheduler.resetDuplicateCheck(task);

        DownloadRequest request = new DownloadRequest("https://www.google.com");
        DownloadRequest request1= new DownloadRequest("https://www.facebook.com/");
        DownloadRequest request2= new DownloadRequest("https://twitter.com");

        request.setPriority(1).putExtra("name", "google");
        request1.setPriority(0).putExtra("name", "facebook");
        request2.setPriority(-1).putExtra("name", "twitter");

        scheduler.push(request, task);
        scheduler.push(request1, task);
        scheduler.push(request2, task);

        DownloadRequest GRequest    = scheduler.poll(task);
        DownloadRequest FBRequest   = scheduler.poll(task);
        DownloadRequest TRequest    = scheduler.poll(task);

        Assert.assertEquals(GRequest.getUrl(), request.getUrl());
        Assert.assertEquals(GRequest.getExtra("name"), request.getExtra("name"));

        Assert.assertEquals(FBRequest.getUrl(), request1.getUrl());
        Assert.assertEquals(FBRequest.getExtra("name"), request.getExtra("name"));

        Assert.assertEquals(TRequest.getUrl(), request2.getUrl());
        Assert.assertEquals(TRequest.getExtra("name"), request.getExtra("name"));
    }

}
