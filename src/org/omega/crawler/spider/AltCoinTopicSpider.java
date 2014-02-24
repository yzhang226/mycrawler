package org.omega.crawler.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.omega.crawler.bean.AltCoinTopicBean;
import org.omega.crawler.common.Utils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class AltCoinTopicSpider extends WebCrawler {

	private static final Log log = LogFactory.getLog(AltCoinTopicSpider.class);

	public final static List<AltCoinTopicBean> beans = new ArrayList<AltCoinTopicBean>();

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
		System.out.println("Visit page url: " + url);

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

				for (Object obj : ns) {
					try {
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

							String link = topicNode.getAttributeByName("href");

							AltCoinTopicBean anncoin = new AltCoinTopicBean();

							anncoin.setTitle(topicTitle);
							anncoin.setLink(topicNode.getAttributeByName("href"));
							anncoin.setTopicid(Integer.valueOf(link.substring(link.indexOf('=') + 1, link.lastIndexOf('.'))));

							TagNode authorNode = (TagNode) cr.get(3).getChildTagList().get(0);
							anncoin.setAuthor(authorNode.getText().toString().trim());

							String replies = cr.get(4).getText().toString().trim();
							if (Utils.isNotEmpty(replies)) anncoin.setReplies(Integer.valueOf(replies));

							if (cr.size() > 5) {
								String views = cr.get(5).getText().toString().trim();
								if (Utils.isNotEmpty(views)) anncoin.setViews(Integer.valueOf(views));
							}

							beans.add(anncoin);
						}
					} catch (Exception e) {
						log.error("extract topic node error.", e);
					}

				}
			}

		}
	}

}