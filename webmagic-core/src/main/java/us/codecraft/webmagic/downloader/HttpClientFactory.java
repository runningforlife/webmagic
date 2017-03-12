package us.codecraft.webmagic.downloader;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.proxy.Proxy;

/*
 * a factory class to generate http client　<br>
 * @author JasonWang
 * @date 2017-3-12
 */

public class HttpClientFactory {
		
	private static final int CONNECT_TIMEOUT = 5000;
	
	private final static OkHttpClient.Builder builder = new OkHttpClient.Builder();

	protected static OkHttpClient getClient(Site site, Proxy proxy){
		OkHttpClient client = builder
				.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
				//.readTimeout(site.getTimeOut(), TimeUnit.MILLISECONDS)
				.retryOnConnectionFailure(true)
				.build();
		return client;
	}
}
