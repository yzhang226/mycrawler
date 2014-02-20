package org.omega.crawler.common.thread;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.crawler.bean.TalkTopicBean;

public class TopicBoardCrawlerThread extends Thread {


	private static final Log log = LogFactory.getLog(AnnTopicCrawlerThread.class);
	
	private List<TalkTopicBean> undbAnns;
	private boolean isNeedContent;
	private CountDownLatch counter;
	
	public TopicBoardCrawlerThread(List<TalkTopicBean> undbAnns, boolean isNeedContent, CountDownLatch counter) {
		this.undbAnns = undbAnns;
		this.isNeedContent = isNeedContent;
		this.counter = counter;
	}
	
	@Override
	public void run() {
		
		setName("TopicBoard Crawler");
		
		long startT = System.currentTimeMillis();
		log.info("Start Fectch All Topics. Total Anns' number is " + undbAnns.size() + ".");
		
		try {
			int gap = 20;
			int groups = undbAnns.size() / gap;
			
			int end, start;
			CountDownLatch latch = null;
			for (int i=0; i<=groups; i++) {
				start = i * gap;
				end = (i+1) * gap;
				if (undbAnns.size() < end) { end = undbAnns.size(); }
				
				latch = new CountDownLatch(end - start);
				for (int j=start; j<end; j++) {
					new SubTopicBoardCrawlerThread(j, undbAnns.get(j), isNeedContent, latch).start();;
				}
				
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error("unexcepted error.", e);
		}
		
		counter.countDown();
		
		log.info("End Fectch All Topics. Total Time is " + ((System.currentTimeMillis() - startT) ) + " milliseconds");
	}
	
}
