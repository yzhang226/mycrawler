package org.omega.crawler.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.crawler.bean.AnnCoinBean;
import org.omega.crawler.common.Utils;

public class FetchAnnCoinThread extends Thread {

	private static final Log log = LogFactory.getLog(FetchAnnCoinThread.class);
	
	
	
	private int no;
	private AnnCoinBean ann;
	private CountDownLatch latch;
	
	public FetchAnnCoinThread(int no, AnnCoinBean ann, CountDownLatch latch) {
		this.no = no;
		this.ann = ann;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		setName("FetchAnnCoinThread-" + no);
		
		long startT = System.currentTimeMillis();
		log.info("Start Fectch Ann, url is " + ann.getLink());
		
		try {
			TopicPage cp = null;
			try {
				cp = new TopicPage(ann.getLink());
			} catch (Exception e) {
				log.error("Network connection error.", e);
				Thread.sleep(10 * 1000);
				cp = new TopicPage(ann.getLink());
			}
			
			if (cp != null) {
				String date = cp.getPublishDate();
				Date postDate = null;
				if (Utils.isNotEmpty(date)) {
					// January 21, 2014, 09:01:57 PM
					// MMMMM dd, yyyy, KK:mm:ss aaa
					if (date.toLowerCase().contains("today")) {// Today at 12:39:37 AM
						postDate = Utils.parseTodayText(date);
					} else {
						postDate = Utils.parseDateText(date);
					}
					
					ann.setPublishDate(postDate);
					ann.setPublishContent(cp.getSubjectContentHtml());
				}
			}
			
		} catch (Throwable e) {
			log.error("unexcepted error.", e);
		}
		
		latch.countDown();
		
		log.info("End Fectch Ann. Total Time is " + ((System.currentTimeMillis() - startT) ) + " milliseconds");
	}
	
}
