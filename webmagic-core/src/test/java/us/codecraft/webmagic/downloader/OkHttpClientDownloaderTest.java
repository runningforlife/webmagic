package us.codecraft.webmagic.downloader;

import static org.junit.Assert.*;

import org.junit.Test;

import us.codecraft.webmagic.selector.Html;

public class OkHttpClientDownloaderTest {
	
    public static final String PAGE_ALWAYS_NOT_EXISTS = "http://localhost:13421/404";

    @Test
    public void testDownloader(){
    	OkHttpDownloader downloader = new OkHttpDownloader();
    	Html html = downloader.download("https://www.baidu.com/?tn=97661637_hao_pg");
    	assertTrue(!html.getFirstSourceText().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalUrl(){
    	OkHttpDownloader downloader = new OkHttpDownloader();
    	downloader.download("<<?https://www.baidu.com/>>");
    }
}
