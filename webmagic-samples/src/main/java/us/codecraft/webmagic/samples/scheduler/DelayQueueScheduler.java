package us.codecraft.webmagic.samples.scheduler;

import us.codecraft.webmagic.DownloadRequest;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author code4crafter@gmail.com
 */
public class DelayQueueScheduler extends PriorityScheduler {

    private DelayQueue<RequestWrapper> queue = new DelayQueue<RequestWrapper>();

    private Set<String> urls = new HashSet<String>();

    private long time;

    private TimeUnit timeUnit;

    private class RequestWrapper implements Delayed {

        private long startTime = System.currentTimeMillis();

        private DownloadRequest request;

        private RequestWrapper(DownloadRequest request) {
            this.request = request;
        }

        private long getStartTime() {
            return startTime;
        }

        private DownloadRequest getRequest() {
            return request;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long convert = unit.convert(TimeUnit.MILLISECONDS.convert(time, timeUnit) - System.currentTimeMillis() + startTime, TimeUnit.MILLISECONDS);
            return convert;
        }

        @Override
        public int compareTo(Delayed o) {
            return new Long(getDelay(TimeUnit.MILLISECONDS)).compareTo(o.getDelay(TimeUnit.MILLISECONDS));
        }
    }

    public DelayQueueScheduler(long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    @Override
    public synchronized void push(DownloadRequest request, Task task) {
        if (urls.add(request.getUrl())) {
            queue.add(new RequestWrapper(request));
        }

    }

    @Override
    public synchronized DownloadRequest poll(Task task) {
        RequestWrapper take = null;
        while (take == null) {
            try {
                take = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        queue.add(new RequestWrapper(take.getRequest()));
        return take.getRequest();
    }
}
