package us.codecraft.webmagic.scheduler;

import org.apache.http.annotation.ThreadSafe;
import us.codecraft.webmagic.DownloadRequest;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.utils.NumberUtils;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Priority scheduler. DownloadRequest with higher priority will poll earlier. <br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.1
 */
@ThreadSafe
public class PriorityScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler {

    public static final int INITIAL_CAPACITY = 5;

    private BlockingQueue<DownloadRequest> noPriorityQueue = new LinkedBlockingQueue<DownloadRequest>();

    private PriorityBlockingQueue<DownloadRequest> priorityQueuePlus = new PriorityBlockingQueue<DownloadRequest>(INITIAL_CAPACITY, new Comparator<DownloadRequest>() {
        @Override
        public int compare(DownloadRequest o1, DownloadRequest o2) {
            return -NumberUtils.compareLong(o1.getPriority(), o2.getPriority());
        }
    });

    private PriorityBlockingQueue<DownloadRequest> priorityQueueMinus = new PriorityBlockingQueue<DownloadRequest>(INITIAL_CAPACITY, new Comparator<DownloadRequest>() {
        @Override
        public int compare(DownloadRequest o1, DownloadRequest o2) {
            return -NumberUtils.compareLong(o1.getPriority(), o2.getPriority());
        }
    });

    @Override
    public void pushWhenNoDuplicate(DownloadRequest request, Task task) {
        if (request.getPriority() == 0) {
            noPriorityQueue.add(request);
        } else if (request.getPriority() > 0) {
            priorityQueuePlus.put(request);
        } else {
            priorityQueueMinus.put(request);
        }
    }

    @Override
    public synchronized DownloadRequest poll(Task task) {
        DownloadRequest poll = priorityQueuePlus.poll();
        if (poll != null) {
            return poll;
        }
        poll = noPriorityQueue.poll();
        if (poll != null) {
            return poll;
        }
        return priorityQueueMinus.poll();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        return noPriorityQueue.size();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return getDuplicateRemover().getTotalRequestsCount(task);
    }
}
