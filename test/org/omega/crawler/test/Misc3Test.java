package org.omega.crawler.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Misc3Test {

	
	public static void main(String[] args) {
		Pattern p = Pattern.compile("\\[[^\\]]+\\][^\\[]*\\[([^\\]]+)\\]");
		String xx = "Topic: [PRE-ANN]  [CAR]CARcoin[X11]POS  (Read 136 times)";
		
		Matcher m = p.matcher(xx);
		
		if (m.find()) {
			System.out.println(m.group());
			System.out.println(m.groupCount());
		}
		
	}
	
}
