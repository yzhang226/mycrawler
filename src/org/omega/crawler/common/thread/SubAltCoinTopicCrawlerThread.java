package org.omega.crawler.common.thread;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.crawler.bean.AltCoinTopicBean;
import org.omega.crawler.common.Utils;
import org.omega.crawler.web.TopicPage;

public class SubAltCoinTopicCrawlerThread extends Thread {

	private static final Log log = LogFactory.getLog(SubAltCoinTopicCrawlerThread.class);
	
	private int no;
	private AltCoinTopicBean bean;
	private boolean isNeedContent;
	private CountDownLatch latch;
	
	public SubAltCoinTopicCrawlerThread(int no, AltCoinTopicBean ann, boolean isNeedContent, CountDownLatch latch) {
		this.no = no;
		this.bean = ann;
		this.isNeedContent = isNeedContent;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		setName("TopicBoard SubCrawler " + no);
		
		long startT = System.currentTimeMillis();
		log.info("Start Fectch TopicBoard, url is " + bean.getLink());
		
		try {
			TopicPage cp = null;
			try {
				cp = new TopicPage(bean.getLink());
			} catch (Exception e) {
				log.error("Network connection error.", e);
				Thread.sleep(10 * 1000);
				cp = new TopicPage(bean.getLink());
			}
			
			if (cp != null) {
				String date = cp.getPublishDate();
				Date postDate = null;
				if (Utils.isNotEmpty(date)) {
					if (date.toLowerCase().contains("today")) {
						postDate = Utils.parseTodayText(date);
					} else {
						postDate = Utils.parseDateText(date);
					}
					
					bean.setPublishDate(postDate);
					if (isNeedContent) bean.setPublishContent(cp.getSubjectContentHtml());
				}
			}
			
		} catch (Throwable e) {
			log.error("unexcepted error.", e);
		}
		
		latch.countDown();
		
		log.info("End Fectch TopicBoard Crawler. Total Time is " + ((System.currentTimeMillis() - startT) ) + " milliseconds");
	}
	
}
