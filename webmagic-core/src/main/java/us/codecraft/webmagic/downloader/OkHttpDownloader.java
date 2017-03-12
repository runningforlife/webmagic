package us.codecraft.webmagic.downloader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.DownloadRequest;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;
/*
 * a http downloader implemented by OkHttp3 <br>
 * @author JasonWang <br>
 * @since 2017-3-12
 */

public class OkHttpDownloader extends AbstractDownloader{
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private final static String REG_IMAGES = "(?m)(?s)<img\\s+(.*)src\\s*=\\s*\"([^\"]+)\"(.*)(\\.(gif|jpg|png))$";

	@Override
	public Page download(DownloadRequest request, Task task) {
        Site site = null;
        if (task != null) {
            site = task.getSite();
        }
        
        int statusCode = 0;
        
		LOGGER.info("download page url{}" + request.getUrl());
				
		Request httpRequest = new Request.Builder()
				.url(request.getUrl())
				.build();
		
		try{
			Response response = handleRequest(site,httpRequest);
			statusCode = response.code();
			
			LOGGER.info("http download response{}" + response);
			
			if(response.isSuccessful()){
				onSuccess(request);
	            request.putExtra(DownloadRequest.STATUS_CODE, statusCode);
	            
				return handleResponse(request,response);
			}else{
				LOGGER.error("http download code{}" + statusCode);
				onError(request);
			}
			
			LOGGER.info("http download code{}" + statusCode);
		}catch(IOException e){
			e.printStackTrace();
			if(site.getCycleRetryTimes() > 0){
				return addToCycleRetry(request,site);
			}
		}
		
		onError(request);
		
		return null;
	}

	@Override
	public void setThread(int threadNum) {
		// TODO Auto-generated method stub
		
	}
	
	private Response handleRequest(Site site,Request req) throws IOException{
		OkHttpClient client = OkHttpProxy.getClient(site, null);

		return client.newCall(req).execute();
	}
	
	private Page handleResponse(DownloadRequest request, Response resp){
		String content;
		Page page = new Page();

		try {
			content = resp.body().string();
			
			page.setRawText(content);
			page.setRequest(request);
			page.setUrl(new PlainText(request.getUrl()));
			page.setStatusCode(resp.code());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.warn("fail to retrive content");
		}
		
		return page;
	}
	
	private boolean isImageUrl(String url){
		Pattern pattern = Pattern.compile(REG_IMAGES);
		Matcher matcher = pattern.matcher(url);
		
		return matcher.matches();
	}

}
