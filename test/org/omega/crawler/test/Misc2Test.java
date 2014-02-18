package org.omega.crawler.test;

import org.omega.crawler.common.Utils;
import org.omega.crawler.web.TopicPage;

public class Misc2Test {

	public static void main(String[] args) {
		String url = "https://bitcointalk.org/index.php?topic=471929.0";
		
		TopicPage t = new TopicPage(url);
		
		String text = t.getPublishDate();
		
		System.out.println(Utils.parseTodayText(text));
		
	}
	
}
