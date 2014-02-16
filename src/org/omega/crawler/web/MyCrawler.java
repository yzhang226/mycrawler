package org.omega.crawler.web;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.text.html.parser.TagElement;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
														+ "|png|tiff?|mid|mp2|mp3|mp4"
														+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
														+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	// https://bitcointalk.org/index.php?topic=454848.0
	private final static Pattern TALK_PATTER = Pattern.compile("^https.+bitcointalk.org.index.php.topic.\\d+.\\d$");

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		
		return TALK_PATTER.matcher(href).matches();
//		return !FILTERS.matcher(href).matches() && href.startsWith("http://www.ics.uci.edu/");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			
			HtmlCleaner cleaner = new HtmlCleaner();

			TagNode node = cleaner.clean(html);
			
			// /html/body/div[2]/div[2]/table/tbody/tr[2]
			
			Object[] ns = null;
			try {
				// /html/body/div[2]/div[2]/table/tbody/tr
				ns = node.evaluateXPath("//body/div[2]/div[2]/table/tbody/tr");
			} catch (XPatherException e) {
				e.printStackTrace();
			}
			
			if (ns != null && ns.length > 0) {
				System.out.println("ns is " + ns + ", length is " + ns.length);
				for (Object obj : ns) {
					TagNode n = (TagNode) obj;
					List<TagNode> cr = n.getChildTagList();
					
					
					// /html/body/div[2]/div[2]/table/tbody/tr[4]/td[3]/b/span/a
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
						System.out.print("Title: " + topicNode.getText().toString().trim() + ",\t");
						
						String link = topicNode.getAttributeByName("href");
						System.out.print("Link: " + link + ",\t"); 
						
						
						TopicPage cp = new TopicPage(link);
						System.out.print("Public Date: " + cp.getPublishDate() + ",\t");
						
						System.out.print("Topic Id: " + link.substring(link.indexOf('=') + 1) + ",\t");
						
						TagNode authorNode = (TagNode) cr.get(3).getChildTagList().get(0);
						System.out.print("Author: " + authorNode.getText().toString().trim() + ",\t"); 
						
						String replies = cr.get(4).getText().toString().trim();
						System.out.print("Replies: " + replies + ",\t"); 
						
						if (cr.size() > 5)  {
							String views = cr.get(5).getText().toString().trim();
							System.out.print("Views: " + views + ",\t"); 
						}
						
//						if (cr.size() > 6) System.out.print("Last post: " + cr.get(6).getText() + ",\t"); 
						
						System.out.println();
					}
					
					
				}
			}
			
			
//			List<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("\ttitle: " + htmlParseData.getTitle());
//			System.out.println("\tText length: " + text.length());
//			System.out.println("\tHtml length: " + html.length());
//			System.out.println("\tNumber of outgoing links: " + links.size());
		}
	}

}
