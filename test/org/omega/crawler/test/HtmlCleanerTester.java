package org.omega.crawler.test;

import java.io.File;
import java.net.URL;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class HtmlCleanerTester {
 
	public static void main(String[] args) throws Exception {
		HtmlCleaner cleaner = new HtmlCleaner();

//		TagNode node = cleaner.clean(new URL("https://bitcointalk.org/index.php?topic=449648.0"));
		TagNode node = cleaner.clean(new File("C:/Users/Administrator/Desktop/xx.htm"));
		
//        System.out.println(node.getText());
        
		// /html/body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/div
		Object[] ns = node.evaluateXPath("//body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/div[@class='post']");
		
		System.out.println("ns is " + ns + ", length is " + ns.length);
		
		for (Object obj : ns) {
			TagNode n = (TagNode) obj;
			System.out.println(cleaner.getInnerHtml(n));
			break;
		}
		
		// 
		ns = node.evaluateXPath("//body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/table/tbody/tr/td[2]/div[2]/span");
		System.out.println("ns is " + ns + ", length is " + ns.length);
		for (Object obj : ns) {
			TagNode n = (TagNode) obj;
			System.out.println(n.getText());
			break;
		}
		
		// 
//		ns = node.evaluateXPath("//body/div[2]");
//		System.out.println("ns is " + ns + ", length is " + ns.length);
		
		
	}
}
