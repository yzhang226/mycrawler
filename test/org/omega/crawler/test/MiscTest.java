package org.omega.crawler.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiscTest {

	
	public static void main(String[] args) throws Exception {
		String link = "https://bitcointalk.org/index.php?topic=449648.0";
		System.out.println(link.substring(link.lastIndexOf('=')+1, link.lastIndexOf('.')));
		
		String time = "January 21, 2014, 09:01:57 PM";
		// MM dd, yyyy, KK:mm:ss a
		// "yyyyy.MMMMM.dd GGG hh:mm aaa"	02001.July.04 AD 12:08 PM
//		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy, hh:mm:ss aaa");
//		Date d = sdf.parse(time);
//		
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
		
//		String p = MiscTest.class.getResource("/ann-coins.html").getPath();
//		
//		File templateFile = new File(p);
//		BufferedReader br = new BufferedReader(new FileReader(templateFile));
//		
//		StringBuilder templateBuilder = new StringBuilder();
//		String line = null;
//		while ((line = br.readLine()) != null) {
//			templateBuilder.append(line).append("\n");
//		}
//		br.close();
//		
//		System.out.println(templateBuilder.toString());
		
		
		String date = "Today at 06:39:37 PM";
		
		Date today = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.clear();
		
		cal.setTime(today);
		
		Pattern p = Pattern.compile("(\\d{2}+):(\\d{2}+):(\\d{2}+) (\\w{2}+)");
		
		Matcher m = p.matcher(date);
		
		if (m.find()) {
			int hh = Integer.valueOf(m.group(1));
			int mm = Integer.valueOf(m.group(2));
			int ss = Integer.valueOf(m.group(3));
			
			String amorpm = m.group(4);
			
			if (amorpm.equalsIgnoreCase("PM")) {
				hh = hh + 12;
			}
			
			cal.set(Calendar.HOUR_OF_DAY, hh);
			cal.set(Calendar.MINUTE, mm);
			cal.set(Calendar.SECOND, ss);
			
			System.out.println("Time is " + cal.getTime());
		}
	}
	
}
