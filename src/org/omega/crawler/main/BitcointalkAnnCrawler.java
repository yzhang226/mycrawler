package org.omega.crawler.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.List;

import org.omega.crawler.bean.AnnCoinBean;
import org.omega.crawler.web.MyCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class BitcointalkAnnCrawler {

	public static void main(String[] args) throws Exception {
        
        BitcointalkAnnCrawler annCrawler = new BitcointalkAnnCrawler();
        annCrawler.fectchAnnCoins(0);
        
//        Collections.sort(MyCrawler.annCoins);
        
        StringBuilder coinsHtml = new StringBuilder();
        
        for (AnnCoinBean coin : MyCrawler.annCoins) {
        	coinsHtml.append(coin.toHtml());
        }
        
        File templateFile = new File(BitcointalkAnnCrawler.class.getResource("/ann-coins.html").getPath());
		BufferedReader br = new BufferedReader(new FileReader(templateFile));
		
		StringBuilder templateBuilder = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			templateBuilder.append(line).append("\n");
		}
		br.close();
		
		String res = templateBuilder.toString().replace("${content}", coinsHtml.toString());
        
        RandomAccessFile raf = new RandomAccessFile("D:/storage/ann-coins.html", "rw");
        raf.seek(0);
        
        raf.write(res.getBytes());
        raf.close();
	}
	
	public List<AnnCoinBean> fectchAnnCoins(int group) throws Exception {
		String crawlStorageFolder = "/storage/crawler4j";
        int numberOfCrawlers = 8;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setIncludeHttpsPages(true);
        config.setMaxDepthOfCrawling(0);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

//        controller.addSeed("https://bitcointalk.org/index.php?board=159.0");
        // https://bitcointalk.org/index.php?board=159.0
        // 39
        int gap = 10;
        int start = gap * group;
        int end = gap * (group + 1);
        for (int i=start; i<end; i++) {
        	int sub = i * 40;
        	controller.addSeed("https://bitcointalk.org/index.php?board=67."+sub);
        }
       

        controller.start(MyCrawler.class, numberOfCrawlers);
        
        return MyCrawler.annCoins;
	}
	
}
