package org.omega.crawler.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Misc2Test {

	public static void main(String[] args) {
//		String url = "https://bitcointalk.org/index.php?topic=471929.0";
		
//		TopicPage t = new TopicPage(url);
		
//		String text = t.getPublishDate();
		
//		System.out.println(Utils.parseTodayText(text));
		
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Long.MAX_VALUE);
		
		System.out.println(String.format("%1$.2fw", new Double(364/365.0)));

		Pattern p1 = Pattern.compile("(\\w+)");
		Matcher wma = p1.matcher("MSS060");
		System.out.println(wma.matches());

		Pattern tp = Pattern.compile("\\w+");
		Matcher tpm = tp.matcher("MSS060  ");
		System.out.println(tpm.matches());

//		  System.out.println(tpm.find());
		
	}
	
}
