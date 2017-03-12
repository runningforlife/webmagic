package us.codecraft.webmagic.scheduler;

import org.apache.http.annotation.ThreadSafe;
import us.codecraft.webmagic.DownloadRequest;
import us.codecraft.webmagic.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Basic Scheduler implementation.<br>
 * Store urls to fetch in LinkedBlockingQueue and remove duplicate urls by HashMap.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
@ThreadSafe
public class QueueScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler {

    private BlockingQueue<DownloadRequest> queue = new LinkedBlockingQueue<DownloadRequest>();

    @Override
    public void pushWhenNoDuplicate(DownloadRequest request, Task task) {
        queue.add(request);
    }

    @Override
    public DownloadRequest poll(Task task) {
        return queue.poll();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        return queue.size();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return getDuplicateRemover().getTotalRequestsCount(task);
    }
}
