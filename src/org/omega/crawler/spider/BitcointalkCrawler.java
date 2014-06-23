package org.omega.crawler.spider;

import java.util.List;
import java.util.Map;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.bean.BitcointalkTopicBean;
import org.omega.crawler.common.Constants;
import org.omega.crawler.common.DocIder;
import org.omega.crawler.common.Utils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class BitcointalkCrawler {
	
	private static final int numberOfCrawlers = 2;
//	private static final String ANN_PAGE_URL = "https://bitcointalk.org/index.php?board=159.0";
	
	// 
	public List<AltCoinBean> fectchAnnTopics(String baseSeedUrl) throws Exception {
		CrawlController controller = createCrawlController();
		
		String url = null;
		for (int i = 0; i < Utils.ANN_TOTAL_PAGE_NUMBER; i++) {
			url = baseSeedUrl + i * 40;
			addSeed(controller, url);
		}
		controller.start(AltCoinSpider.class, numberOfCrawlers);
		
		return AltCoinSpider.beans;
	}

	public Map<Integer, AltCoinBean> fectchAnnTopicsByUrls(List<AltCoinBean> undbAnns) throws Exception {
		CrawlController controller = createCrawlController();
		
		for (AltCoinBean alt : undbAnns) {
			addSeed(controller, alt.getLink());
		}
		controller.start(DetailAltCoinSpider.class, numberOfCrawlers);

		return DetailAltCoinSpider.topicIdAltCoinMap;
	}
	
	public List<BitcointalkTopicBean> fectchTalkTopics(String baseSeedUrl, int group) throws Exception {
		CrawlController controller = createCrawlController();

		int gap = 20;
		int start = gap * group;
		int end = gap * (group + 1);
		String url = null;
		for (int i = start; i < end; i++) {
			url = baseSeedUrl + i * 40;
			addSeed(controller, url);
		}
		controller.start(AltCoinTopicSpider.class, numberOfCrawlers);

		return AltCoinTopicSpider.beans;
	}
	
	public CrawlController createCrawlController() throws Exception {
		CrawlConfig config = new CrawlConfig();
		
		config.setCrawlStorageFolder(Constants.CRAWL_FOLDER);
		config.setIncludeHttpsPages(true);
		config.setMaxDepthOfCrawling(0);
		config.setPolitenessDelay(1 * 1000);

		PageFetcher pageFetcher = new PageFetcher(config);
		
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		
		return new CrawlController(config, pageFetcher, robotstxtServer);
	}
	
	private void addSeed(CrawlController controller, String url) {
		int did = controller.getDocIdServer().getDocId(url);
		if (did == -1) {
			controller.addSeed(url, DocIder.getNext());
		} else {
			controller.addSeed(url, did);
		}
	}
	
}
