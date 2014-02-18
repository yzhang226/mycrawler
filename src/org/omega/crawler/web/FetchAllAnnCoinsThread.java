package org.omega.crawler.web;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.crawler.bean.AnnCoinBean;

public class FetchAllAnnCoinsThread extends Thread {
	
	private static final Log log = LogFactory.getLog(FetchAllAnnCoinsThread.class);
	
	private List<AnnCoinBean> undbAnns;
	private CountDownLatch counter;
	
	public FetchAllAnnCoinsThread(List<AnnCoinBean> undbAnns, CountDownLatch counter) {
		this.undbAnns = undbAnns;
		this.counter = counter;
	}
	
	@Override
	public void run() {
		
		setName("FetchAllAnnCoinsThread");
		
		long startT = System.currentTimeMillis();
		log.info("Start Fectch All Anns. Total Anns' number is " + undbAnns.size() + ".");
		
		try {
			int gap = 10;
			int groups = undbAnns.size() / gap;
			
			int end, start;
			CountDownLatch latch = null;
			for (int i=0; i<=groups; i++) {
				start = i * gap;
				end = (i+1) * gap;
				if (undbAnns.size() < end) { end = undbAnns.size(); }
				
				latch = new CountDownLatch(end - start);
				for (int j=start; j<end; j++) {
					new FetchAnnCoinThread(j, undbAnns.get(j), latch).start();;
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
		
		log.info("End Fectch All Anns. Total Time is " + ((System.currentTimeMillis() - startT) ) + " milliseconds");
	}
	
}
