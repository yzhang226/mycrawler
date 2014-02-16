package org.omega.crawler.test;

public class MiscTest {

	
	public static void main(String[] args) {
		String link = "https://bitcointalk.org/index.php?topic=449648.0";
		System.out.println(link.substring(link.indexOf('=')+1));
	}
	
}
