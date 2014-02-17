package org.omega.crawler.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MiscTest {

	
	public static void main(String[] args) throws Exception {
		String link = "https://bitcointalk.org/index.php?topic=449648.0";
		System.out.println(link.substring(link.indexOf('=')+1));
		
		String time = "January 21, 2014, 09:01:57 PM";
		// MM dd, yyyy, KK:mm:ss a
		// "yyyyy.MMMMM.dd GGG hh:mm aaa"	02001.July.04 AD 12:08 PM
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy, KK:mm:ss a");
		Date d = sdf.parse(time);
		
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
		
		File templateFile = new File("ann-coins.html");
		BufferedReader br = new BufferedReader(new FileReader(templateFile));
		
		StringBuilder templateBuilder = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			templateBuilder.append(line).append("\n");
		}
		br.close();
		
		System.out.println(templateBuilder.toString());
		
	}
	
}
