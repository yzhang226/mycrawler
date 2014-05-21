package org.omega.crawler.common;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.omega.crawler.bean.AltCoinBean;

public class HtmlPageMatcher {
	
	private static final Pattern PATTERN_TOPIC_ID = Pattern.compile("\\d+-(\\d+)-");
	
	public AltCoinBean tryMatch(String htmlPath) throws Exception {
		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode page = cleaner.clean(getHtmlPage(htmlPath));
		
		String content = getSubjectContentHtml(page, cleaner);
		String[] lineArr = content.split("<(?i)br />");// 
		List<String> lines = new ArrayList<String>(lineArr.length);
		for (String l : lineArr) {
			lines.add(cleaner.clean(l).getText().toString());
		}
		
		String title = getTitle(page, cleaner);

		long start = System.currentTimeMillis();
		
		ContentMatcher cm = new ContentMatcher(title, lines);
		AltCoinBean alt = cm.buildAndMatch();
		
		long end = System.currentTimeMillis();
		
		alt.setUsedTime((double) (end-start)/1000);
		
		File hf = new File(htmlPath);
		
		alt.setTopicid(getTopicId(hf.getName()));
		
		System.out.println(hf.getName() + " ---- " + alt.toPrintableTxt());
		
		return alt;
	}
	
	private Integer getTopicId(String fileName) {
		Integer topicId = null;
		Matcher m = PATTERN_TOPIC_ID.matcher(fileName);
		if (m.find()) {
			topicId = Integer.parseInt(m.group(1));
		}
		return topicId;
	}
	
	private String getHtmlPage(String path)  {
		String html = "";
		try {
			RandomAccessFile raf = new RandomAccessFile(path, "r");
			int len = (int) raf.length();
			byte[] bs = new byte[len];
			raf.read(bs);
			raf.close();
			
			html = new String(bs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return html;
	}
	
	private String getTitle(TagNode page, HtmlCleaner cleaner) {
		Object[] ns = null;
		try {// //*[@id="top_subject"] 
			ns = page.evaluateXPath("//title");
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
	
	
	private String getSubjectContentHtml(TagNode page, HtmlCleaner cleaner) throws Exception {
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
