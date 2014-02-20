package org.omega.crawler.spider;

import java.util.List;

import org.omega.crawler.bean.AnnCoinBean;
import org.omega.crawler.bean.TalkTopicBean;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class BitcointalkCrawler {

	public List<AnnCoinBean> fectchAnnTopics(String baseSeedUrl, int group) throws Exception {
		String crawlStorageFolder = "/storage/crawler4j";
		int numberOfCrawlers = 10;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setIncludeHttpsPages(true);
		config.setMaxDepthOfCrawling(0);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		int gap = 10;
		int start = gap * group;
		int end = gap * (group + 1);
		for (int i = start; i < end; i++) {
			int sub = i * 40;
			controller.addSeed(baseSeedUrl + sub);
		}
		
		System.out.println("\t\t AnnTopicSpider.beans is " + AnnTopicSpider.beans);
		
		controller.start(AnnTopicSpider.class, numberOfCrawlers);

		return AnnTopicSpider.beans;
	}
	
	public List<TalkTopicBean> fectchTalkTopics(String baseSeedUrl, int group) throws Exception {
		String crawlStorageFolder = "/storage/crawler4j";
		int numberOfCrawlers = 10;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setIncludeHttpsPages(true);
		config.setMaxDepthOfCrawling(0);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		int gap = 20;
		int start = gap * group;
		int end = gap * (group + 1);
		for (int i = start; i < end; i++) {
			int sub = i * 40;
			controller.addSeed(baseSeedUrl + sub);
		}
		
		System.out.println("\t\t AnnTopicSpider.beans is " + TalkTopicSpider.beans);
		
		controller.start(TalkTopicSpider.class, numberOfCrawlers);

		return TalkTopicSpider.beans;
	}

}
