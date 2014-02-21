package org.omega.crawler.web;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class TopicPage {
	
	private static final Log log = LogFactory.getLog(TopicPage.class);
	
	private String url;
	private TagNode page;
	private HtmlCleaner cleaner;
	
	public TopicPage(String url) {
		this.url = url;
		
		cleaner = new HtmlCleaner();

		try {
			page = cleaner.clean(new URL(url));
		} catch (Exception e) {
			log.error("Access page url[" + url + "] error. " + e.getMessage());
			try {
				Thread.sleep(1 * 1000);
				page = cleaner.clean(new URL(url));
			} catch (Exception e2) {
				log.error("Access page url[" + url + "] error again. " + e2.getMessage());
			}
		}
		
	}
	
	public String getPublishDate() {
		Object[] ns = null;
		try {
			ns = page.evaluateXPath("//body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/table/tbody/tr/td[2]/div[2]/span");
			if (ns == null || ns.length == 0) {
				ns = page.evaluateXPath("//body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/table/tbody/tr/td[2]/div[2]");
				
				if (ns != null && ns.length > 0) {
//					TagNode n = (TagNode) ns[0];
				} else {
					log.error("No Publish Date Node in html page.");
				}
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		
		String cont = "";
		if (ns != null && ns.length > 0) {
			TagNode n = (TagNode) ns[0];
			cont = n.getText().toString();
		}
		
		return cont;
	}
	
	public String getSubjectContentHtml() {
		Object[] ns = null;
		try {
			ns = page.evaluateXPath("//body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/div[@class='post']");
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		
		String cont = "";
		if (ns != null && ns.length > 0) {
			TagNode n = (TagNode) ns[0];
			cont = cleaner.getInnerHtml(n);
		}
		
		return cont;
	}
	
	
}
