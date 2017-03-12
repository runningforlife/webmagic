package us.codecraft.webmagic.scheduler;

import junit.framework.Assert;
import org.junit.Test;
import us.codecraft.webmagic.DownloadRequest;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;

/**
 * @author code4crafter@gmail.com <br>
 */
public class PrioritySchedulerTest {

    private PriorityScheduler priorityScheduler = new PriorityScheduler();

    private Task task = new Task() {
        @Override
        public String getUUID() {
            return "1";
        }

        @Override
        public Site getSite() {
            return null;
        }
    };

    @Test
    public void testDifferentPriority() {
        DownloadRequest request = new DownloadRequest("a");
        request.setPriority(100);
        priorityScheduler.push(request,task);

        request = new DownloadRequest("b");
        request.setPriority(900);
        priorityScheduler.push(request,task);

        request = new DownloadRequest("c");
        priorityScheduler.push(request,task);

        request = new DownloadRequest("d");
        request.setPriority(-900);
        priorityScheduler.push(request,task);

        DownloadRequest poll = priorityScheduler.poll(task);
        Assert.assertEquals("b",poll.getUrl());
        poll = priorityScheduler.poll(task);
        Assert.assertEquals("a",poll.getUrl());
        poll = priorityScheduler.poll(task);
        Assert.assertEquals("c",poll.getUrl());
        poll = priorityScheduler.poll(task);
        Assert.assertEquals("d",poll.getUrl());
    }

    @Test
    public void testNoPriority() {
        DownloadRequest request = new DownloadRequest("a");
        priorityScheduler.push(request,task);

        request = new DownloadRequest("b");
        priorityScheduler.push(request,task);

        request = new DownloadRequest("c");
        priorityScheduler.push(request,task);

        DownloadRequest poll = priorityScheduler.poll(task);
        Assert.assertEquals("a",poll.getUrl());

        poll = priorityScheduler.poll(task);
        Assert.assertEquals("b",poll.getUrl());

        poll = priorityScheduler.poll(task);
        Assert.assertEquals("c",poll.getUrl());
    }
}
