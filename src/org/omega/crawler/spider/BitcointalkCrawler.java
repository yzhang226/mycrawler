package org.omega.crawler.spider;

import java.util.List;
import java.util.Map;

import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.bean.AltCoinTopicBean;
import org.omega.crawler.common.Constants;
import org.omega.crawler.common.DocIder;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class BitcointalkCrawler {
	
	
	private static final int numberOfCrawlers = 2;
	
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
	
	public List<AltCoinBean> fectchAnnTopics(String baseSeedUrl, int group) throws Exception {
		CrawlController controller = createCrawlController();
		
		int gap = 20;
		int start = gap * group;
		int end = gap * (group + 1);
		int did;
		String url = null;
		for (int i = start; i < end; i++) {
			url = baseSeedUrl + i * 40;
			did = controller.getDocIdServer().getDocId(url);
			if (did == -1) {
				controller.addSeed(url, DocIder.getNext());
			}
		}
		
		controller.start(AltCoinSpider.class, numberOfCrawlers);
		
		return AltCoinSpider.beans;
	}
	
	public Map<Integer, AltCoinBean> fectchAnnTopicsByUrls(List<AltCoinBean> undbAnns) throws Exception {
		CrawlController controller = createCrawlController();
		
		int did;
		for (AltCoinBean alt : undbAnns) {
			did = controller.getDocIdServer().getDocId(alt.getLink());
			if (did == -1) {
				controller.addSeed(alt.getLink(), DocIder.getNext());
			}
		}
		
		controller.start(DetailAltCoinSpider.class, numberOfCrawlers);

		return DetailAltCoinSpider.topicIdAltCoinMap;
	}
	
	public List<AltCoinTopicBean> fectchTalkTopics(String baseSeedUrl, int group) throws Exception {
		CrawlController controller = createCrawlController();

		int gap = 20;
		int start = gap * group;
		int end = gap * (group + 1);
		int did;
		String url = null;
		for (int i = start; i < end; i++) {
			url = baseSeedUrl + i * 40;
			did = controller.getDocIdServer().getDocId(url);
			if (did == -1) {
				controller.addSeed(url, DocIder.getNext());
			}
		}
		
		controller.start(AltCoinTopicSpider.class, numberOfCrawlers);

		return AltCoinTopicSpider.beans;
	}
	
	
}
