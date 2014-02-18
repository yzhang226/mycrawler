package org.omega.crawler.web;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.text.html.parser.TagElement;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.omega.crawler.bean.AnnCoinBean;
import org.omega.crawler.common.Utils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	public final static List<AnnCoinBean> annCoins = new ArrayList<AnnCoinBean>();
	
	// https://bitcointalk.org/index.php?topic=454848.0
	private final static Pattern TALK_PATTER = Pattern.compile("^https.+bitcointalk.org.index.php.topic.\\d+.\\d$");

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
		System.out.print("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();
			
			HtmlCleaner cleaner = new HtmlCleaner();

			TagNode node = cleaner.clean(html);
			
			Object[] ns = null;
			try {
				ns = node.evaluateXPath("//body/div[2]/div[2]/table/tbody/tr");
			} catch (XPatherException e) {
				e.printStackTrace();
			}
			
			if (ns != null && ns.length > 0) {
//				System.out.println("ns is " + ns + ", length is " + ns.length);
				for (Object obj : ns) {
					TagNode n = (TagNode) obj;
					List<TagNode> cr = n.getChildTagList();
					
					TagNode topicNode = null;
					try {
						Object[] ns2 = cr.get(2).evaluateXPath("//span/a");
						if (ns2 != null && ns2.length > 0) {
							topicNode = (TagNode) ns2[0];
						}
					} catch (XPatherException e) {
						e.printStackTrace();
					}
					
					if (topicNode != null) {
						String topicTitle = topicNode.getText().toString().trim();
						
						if (topicTitle.toLowerCase().contains("ann]")) {
							String link = topicNode.getAttributeByName("href");
							
							AnnCoinBean anncoin = new AnnCoinBean();
							
							anncoin.setTitle(topicTitle);
							anncoin.setLink(topicNode.getAttributeByName("href"));
							
							anncoin.setTopicid(Integer.valueOf(link.substring(link.indexOf('=') + 1, link.lastIndexOf('.'))));
							
							TagNode authorNode = (TagNode) cr.get(3).getChildTagList().get(0);
							anncoin.setAuthor(authorNode.getText().toString().trim());
							
							String replies = cr.get(4).getText().toString().trim();
							if (Utils.isNotEmpty(replies)) anncoin.setReplies(Integer.valueOf(replies));
							
							if (cr.size() > 5)  {
								String views = cr.get(5).getText().toString().trim();
								if (Utils.isNotEmpty(views)) anncoin.setViews(Integer.valueOf(views));
							}
							
							annCoins.add(anncoin);
						}
						
						
					}
					
					
				}
			}
			
			System.out.println("\ttitle: " + htmlParseData.getTitle());
		}
	}

}
