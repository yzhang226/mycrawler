package org.omega.crawler.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.common.ContentMatcher;
import org.omega.crawler.common.Utils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class DetailAltCoinSpider extends WebCrawler {

	private static final Log log = LogFactory.getLog(DetailAltCoinSpider.class);
	
	private static final boolean IS_DOWNLOADING = false;

	public final static Map<Integer, Timestamp> topicIdTimeMap = new HashMap<Integer, Timestamp>();
	public final static Map<Integer, AltCoinBean> topicIdAltCoinMap = new HashMap<Integer, AltCoinBean>();
	

	private final static Pattern TALK_PATTER = Pattern.compile("^https.+bitcointalk.org.index.php.topic.\\d+\\..+$");

	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return TALK_PATTER.matcher(href).matches();
	}

	public void visit(Page page) {

		String url = page.getWebURL().getURL();
		log.info("Visit page url for detail: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();

			HtmlCleaner cleaner = new HtmlCleaner();

			TagNode node = cleaner.clean(html);
			
			if (node != null) {
				String date = getPublishDate(node);
				Date postDate = null;
				if (Utils.isNotEmpty(date)) {
					// January 21, 2014, 09:01:57 PM
					// MMMMM dd, yyyy, KK:mm:ss aaa
					if (date.toLowerCase().contains("today")) {// Today at 12:39:37 AM
						postDate = Utils.parseTodayText(date);
					} else {
						postDate = Utils.parseDateText(date);
					}
					
//					ann.setPublishDate(new Timestamp(postDate.getTime()));
					Integer topicId = Utils.getTopicIdByUrl(url);
					
					topicIdTimeMap.put(topicId, new Timestamp(postDate.getTime()));
					
					AltCoinBean alt = buildAltCion(node, cleaner);
					alt.setTopicid(topicId);
					alt.setPublishDate(new Timestamp(postDate.getTime()));
					
					topicIdAltCoinMap.put(topicId, alt);
					
					if (IS_DOWNLOADING) {
						downloadHtmlPage(alt, html);
					}
					
				}
			}
		}
	}
	
	public void downloadHtmlPage(AltCoinBean alt, String html) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String pdate = sdf.format(alt.getPublishDate());
		
		File htmlPath = new File(BitcointalkCrawler.crawl_storage_folder + "/" + pdate + "-" + alt.getTopicid() + "-" + alt.getName() + "-" + alt.getAbbrName() + ".html");
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(htmlPath);
			byte[] bs = html.getBytes();
			fos.write(bs);
			fos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public String getPublishDate(TagNode page) {
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
	
	public String getContentHtml(TagNode page, HtmlCleaner cleaner) {
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
	
	public String getTitle(TagNode page, HtmlCleaner cleaner) {
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

	public AltCoinBean buildAltCion(TagNode page, HtmlCleaner cleaner) {
		String content = getContentHtml(page, cleaner);
		String title = getTitle(page, cleaner);
		
		String[] lineArr = content.toLowerCase().split("<br />");
		List<String> lines = new ArrayList<String>(lineArr.length);
		for (String l : lineArr) {
			lines.add(cleaner.clean(l).getText().toString());
		}

		ContentMatcher cm = new ContentMatcher(title, lines);
		AltCoinBean alt = cm.buildAndMatch();
		
		return alt;
	}
	
}