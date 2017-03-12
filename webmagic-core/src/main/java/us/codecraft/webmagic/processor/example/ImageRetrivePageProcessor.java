package us.codecraft.webmagic.processor.example;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.OkHttpDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

public class ImageRetrivePageProcessor implements PageProcessor{

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
	
    private final static String URL_FREE_JPG = "http://en.freejpg.com.ar/free/images/";
    private final static String URL_PIXELS = "https://www.pexels.com/";
    private final static String URL_ALBUM = "http://albumarium.com/";
    
    private final static String REG_FREE_JPG = "http://en\\.freejpg\\.com\\.ar/.*(\\.(gif|jpg|png))$";
        
    private ArrayList<String> imgList = new ArrayList<String>();
    
	@Override
	public void process(Page page) {
		page.addTargetRequests(page.getHtml().links().regex(URL_FREE_JPG + ".*\\w").all());
		
		// here we retrieve all those IMAGE urls
		Document doc = page.getHtml().getDocument();
		Elements images = doc.select("img[src$=.jpg]");
		for(Element img : images){
			if(img.tagName().equals("img")){
				String url = img.attr("abs:src");
				imgList.add(url);
				LOGGER.info("image name{}" + img.attr("alt") + "," + url + ",width{}" + img.attr("width") + ",height{}" + img.attr("height"));
			}
		}
	}

	@Override
	public Site getSite() {
		return site;
	}
	

	public static void main(String[] args){		
		Spider.create(new ImageRetrivePageProcessor())
		.addUrl(URL_FREE_JPG)
		.setDownloader(new OkHttpDownloader())
		.run();	
	}
}
