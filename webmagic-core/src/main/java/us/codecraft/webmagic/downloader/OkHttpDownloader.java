package us.codecraft.webmagic.downloader;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
/*
 * a http downloader implemented by OkHttp3 <br>
 * @author JasonWang <br>
 * @date 2017-3-12
 */
import us.codecraft.webmagic.utils.WMCollections;

public class OkHttpDownloader extends AbstractDownloader{
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public Page download(Request request, Task task) {
        Site site = null;
        if (task != null) {
            site = task.getSite();
        }
        Set<Integer> acceptStatCode;
        String charset = null;
        Map<String, String> headers = null;
        if (site != null) {
            acceptStatCode = site.getAcceptStatCode();
            charset = site.getCharset();
            headers = site.getHeaders();
        } else {
            acceptStatCode = WMCollections.newHashSet(200);
        }
        
        Response httpRespone = null;
        int statusCode = 0;
		
		LOGGER.info("download page url = " + request.getUrl());
		
		OkHttpClient client = HttpClientFactory.getClient(site, null);
		
		//Request request;
		
		return null;
	}

	@Override
	public void setThread(int threadNum) {
		// TODO Auto-generated method stub
		
	}

}
