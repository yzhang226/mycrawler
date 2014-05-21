package org.omega.crawler.offline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.common.Constants;
import org.omega.crawler.common.HtmlPageMatcher;
import org.omega.crawler.common.Utils;
import org.omega.crawler.service.AltCoinService;
import org.omega.crawler.spider.BitcointalkCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MakeupRunner {
	
	@Autowired
	private AltCoinService altCoinService;
	
	
	public void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config-offline.xml"); 
		String p = Utils.getResourcePath("log4j.xml");
		System.out.println("rp is " + p);
		DOMConfigurator.configure(p);
		
		altCoinService = context.getBean(AltCoinService.class);
	}
	
	@SuppressWarnings("unchecked")
	public void downloadUnNameds() throws Exception {
		Session s = altCoinService.openSession();
		Transaction t = s.beginTransaction();
		
		List<AltCoinBean> alts = s.createQuery("from AltCoinBean where name is null").list();
		
		BitcointalkCrawler crawler = new BitcointalkCrawler();
		crawler.fectchAnnTopicsByUrls(alts);
		
		t.commit();
		s.close();
	}
	
	public void makeup() throws Exception {
		File dir = new File(Constants.CRAWL_PAGES_FOLDER) ;
		
		AltCoinBean alt = null;
		StringBuilder content = new StringBuilder();
		HtmlPageMatcher pm = new HtmlPageMatcher();
		for (File f : dir.listFiles()) {
			alt = pm.tryMatch(f.getAbsolutePath());
			content.append(Utils.generateHtml4AltCoin(f.getName(), alt));
		}
		
		String rp = Utils.getResourcePath("checking.html");
		String fileContent = IOUtils.toString(new FileInputStream(rp));
		
		fileContent = fileContent.replace("${content}", content.toString());
		
		IOUtils.write(fileContent.getBytes(), new FileOutputStream(Constants.CRAWL_FOLDER + "/generated_coins.html"));
		
	}
	
	public void testOne(String fileName) throws Exception {
		HtmlPageMatcher pm = new HtmlPageMatcher();
		
		File f = new File(Constants.CRAWL_PAGES_FOLDER + "/" + fileName) ;
		pm.tryMatch(f.getAbsolutePath());
	}
	
	
	public static void main(String[] args) throws Exception {
		MakeupRunner runner = new MakeupRunner();
		
//		runner.init();
//		runner.downloadUnNameds();
		
		runner.makeup();
		
//		runner.testOne("20120819195428-101820-PpCoin-PPC.html");
		
	}
	
	
}
