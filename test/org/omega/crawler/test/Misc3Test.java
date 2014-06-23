package org.omega.crawler.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.omega.crawler.common.Utils;

public class Misc3Test {

	
	public static void main(String[] args) throws Exception {
//		Pattern p = Pattern.compile("\\[[^\\]]+\\][^\\[]*\\[([^\\]]+)\\]");
//		String xx = "Topic: [PRE-ANN]  [CAR]CARcoin[X11]POS  (Read 136 times)";
//		
//		Matcher m = p.matcher(xx);
//		
//		if (m.find()) {
//			System.out.println(m.group());
//			System.out.println(m.groupCount());
//		}
		
		String line = "0800000000000000000000000000000000000000000000000000000000000000000000000000000 000000000000000000000000000000000000000000000000000";
//		String txt = "080000000000";
		line = "15,000,000 Total Coin Supply                15,000,000 Total Coin Supply";
//		line = "Supply: 10 million; or half the supply of BTC";
//		line = "Supply: 50,000,000";
//		ContentMatcher cm = new ContentMatcher(null, null);
//		
//		cm.buildAltReward(line);
//		cm.buildAltBTime(line);
		// \btotal\b\s?\D*([1-9]((\d{1,3}\D){1,5}|(\d{1,15})))\s([^\d\s]+), \btotal\b\s?\D*([1-9]((\d{1,3}\D){1,5}|(\d{1,15}))), ([1-9]((\d{1,3}\D){1,5}|(\d{1,15})))\s?total, total\s?coins\s?\D+([1-9]((\d{1,3}\D){1,5}|(\d{1,15}))), ([1-9]((\d{1,3}\D){1,5}|(\d{1,15})))\s?max\D+coins?

		String regex = "\\btotal\\b\\s?\\D*([1-9]((\\d{1,3}\\D){1,5}|(\\d{1,3})|(\\d{1,15})))\\s([\\D\\S]+)";
		// \\bsupply\\b\\D+
		regex = "([1-9](\\d{1,3}\\D?){1,5}|(\\d{1,15}))";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(line);
		if (m.find()) {
			System.out.println(m.groupCount());
			System.out.println(m.group(1));
			System.out.println(m.group(2));
			System.out.println(m.group(3));
//			System.out.println(m.group(4));
		}
		
		
//		String ff = "/storage/crawler4j/pages/20140525001628-624041-XGenerationCoin-NA.html";
//		HtmlCleaner cleaner = new HtmlCleaner();
//		TagNode node = cleaner.clean(IOUtils.toString(new FileInputStream(ff)));
		
//		FileOutputStream fos = new FileOutputStream(ff + ".pretty");
//		PrettyHtmlSerializer ser = new PrettyHtmlSerializer(cleaner.getProperties(), " ");
//		ser.writeToStream(node, fos, true);
//		fos.flush();
//		fos.close();
		
//		IOUtils.toString(new FileInputStream(ff));
		
//		PrettyHtmlSerializer ser = new PrettyHtmlSerializer(cleaner.getProperties(), " ");
//		System.out.println(ser.getAsString(IOUtils.toString(new FileInputStream(ff))));
//		System.out.println(ser.getAsString(getContentHtml(node, cleaner)));
//		System.out.println(getContentHtml(node, cleaner));;
		
		Double dbx = 1000.00;
		System.out.println(dbx % 1000 == 0);
		int ii = dbx.intValue();
		System.out.println(dbx == ii);
		
		System.out.println(Utils.extractTotalPagesNumber(Utils.fetchPageByUrl(Utils.ANN_PAGE_URL)));
		
	}
	
	public static String getContentHtml(TagNode page, HtmlCleaner cleaner) {
		Object[] ns = null;
		try {
			ns = page.evaluateXPath("//body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/div[@class='post']");
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		
		String prettyCont = "";
		if (ns != null && ns.length > 0) {
			TagNode n = (TagNode) ns[0];
//			prettyCont = cleaner.getInnerHtml(n);
			
			cleaner.getProperties().setOmitXmlDeclaration(true);
			PrettyHtmlSerializer htmlSer = new PrettyHtmlSerializer(cleaner.getProperties(), " ");
			prettyCont = htmlSer.getAsString(n, true);
		}
		
		return prettyCont;
	}
	
}
