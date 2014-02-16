package org.omega.crawler.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class TopicPage {
	
	private String url;
	private TagNode page;
	private HtmlCleaner cleaner;
	
	public TopicPage(String url) {
		this.url = url;
		
		cleaner = new HtmlCleaner();

		try {
			page = cleaner.clean(new URL(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getPublishDate() {
		Object[] ns = null;
		try {
			ns = page.evaluateXPath("//body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/table/tbody/tr/td[2]/div[2]/span");
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
	
	public String getSubjectContentHtml() {
		Object[] ns = null;
		try {
			ns = page.evaluateXPath("body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/div[@class='post']");
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
