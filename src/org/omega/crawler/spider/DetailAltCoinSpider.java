package org.omega.crawler.spider;

import java.sql.Timestamp;
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
import org.omega.crawler.common.Utils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class DetailAltCoinSpider extends WebCrawler {

	private static final Log log = LogFactory.getLog(DetailAltCoinSpider.class);

	public final static Map<Integer, Timestamp> topicIdTimeMap = new HashMap<Integer, Timestamp>();

	private final static Pattern TALK_PATTER = Pattern.compile("^https.+bitcointalk.org.index.php.topic.\\d+\\..+$");

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();

		return TALK_PATTER.matcher(href).matches();
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {

		String url = page.getWebURL().getURL();
		log.info("Visit page url: " + url);

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
					topicIdTimeMap.put(Utils.getTopicIdByUrl(url), new Timestamp(postDate.getTime()));
//					if (isNeedContent) ann.setPublishContent(cp.getSubjectContentHtml());
				}
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
	
	public String getSubjectContentHtml(TagNode page, HtmlCleaner cleaner) {
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