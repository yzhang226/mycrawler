package org.omega.crawler.common;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.trade.entity.WatchListItem;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;

public final class Utils {
	
	private static final Log log = LogFactory.getLog(Utils.class);
	
	public static int ANN_TOTAL_PAGE_NUMBER = 0;
	public static final String ANN_PAGE_URL = "https://bitcointalk.org/index.php?board=159.0";
	
	private static final String HEX_ALPH = "0123456789ABCDEF";
	private static String WEB_DEPLOY_PATH = null;
	private static String CARD_IMAGE_PATH = null;
	
	public static final String TIME_ZONE_LOCAL = TimeZone.getDefault().getID();
	public static final String TIME_ZONE_GMT = "GMT";
	
	private static final Locale LOCALE_US = Locale.US;
	private static final Pattern TODAY_DATE_PATTERN = Pattern.compile("(\\d{2}+):(\\d{2}+):(\\d{2}+) (\\w{2}+)");
	
	public static final String DATE_FORMAT_FULL = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT_SHORT = "yyMMddHH";
	
	public static final int DAYS_YEAR = 365;
	public static final int DAYS_MONTH = 30;
	public static final int DAYS_WEEK = 7;
	
	/*
	 * Integer.MAX_VALUE 2, 147, 483, 647
	 * Long.MAX_VALUE    9223372036854775807
	 */
	public static final int UNIT_KILO = 1000;
	public static final int UNIT_MILLION = 1000 * UNIT_KILO;
	public static final int UNIT_BILLION = 1000 * UNIT_MILLION;
	public static final long UNIT_TRILLION = 1000l * UNIT_BILLION;
	
	public static final List<String> SQL_OPERATORS = Arrays.asList("=", ">", "<", "between", "like", "in", "<>", ">=", "<=");
	
	private Utils() {}
	
	public static boolean isEmpty(String text) {
		return text == null || text.trim().length() == 0;
	}
	
	public static boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}
	
	public static boolean isNotEmpty(Object[] objs) {
		return objs != null && objs.length != 0;
	}
	
	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.isEmpty();
	}
	
	public static boolean isNotEmpty(Collection<?> list) {
		return !isEmpty(list);
	}
	
	public static boolean isPositive(Integer i) {
		return i != null && i.intValue() > 0;
	}
	
	public static boolean isPositive(Long i) {
		return i != null && i.longValue() > 0;
	}
	
	public static boolean isPositive(Double i) {
		return i != null && i.doubleValue() > 0;
	}
	
	public static Integer getTopicIdByUrl(String link) {
		return Integer.valueOf(link.substring(link.indexOf('=') + 1, link.lastIndexOf('.')));
	}
	
	public static String formatNumber(Number n) {
		if (n == null) {
			return null;
		}
		if (n.doubleValue() == 0) {
			return "0";
		}
		String txt = "";
		Double a = n.doubleValue();
		if (a >= UNIT_TRILLION) {
			Double b = Arith.divide(a, UNIT_TRILLION);
			txt = isInteger(b) ? b.intValue() + "T" : String.format("%1$.2fT", b);
		} else if (a >= UNIT_BILLION) {
			Double b = Arith.divide(a, UNIT_BILLION);
			txt = isInteger(b) ? b.intValue() + "B" : String.format("%1$.2fB", b);
		} else if (a >= UNIT_MILLION) {
			Double b = Arith.divide(a, UNIT_MILLION);
			txt = isInteger(b) ? b.intValue() + "M" : String.format("%1$.2fM", b);
		} else if (a >= UNIT_KILO) {
			Double b = Arith.divide(a, UNIT_KILO);
			txt = isInteger(b) ? b.intValue() + "K" : String.format("%1$.2fK", b);
		} else {
			txt = isInteger(a) ? a.intValue() + "" : String.format("%1$.2f", a);
		}
		
		return txt;
	}
	
	public static boolean isInteger(Double dou) {
		return dou.intValue() == dou;
	}
	
	public static String formatDay(Number n) {
		if (n == null) {
			return null;
		}
		String txt = "";
		Double a = n.doubleValue();
		
		if (a >= DAYS_YEAR) {
			Double b = Arith.divide(a, DAYS_YEAR);
			txt = isInteger(b) ? b.intValue() + "y" : String.format("%1$.2fy", b);
		} else if (a >= DAYS_MONTH) {
			Double b = Arith.divide(a, DAYS_MONTH);
			txt = isInteger(b) ? b.intValue() + "m" : String.format("%1$.2fm", b);
		} else if (a >= DAYS_WEEK) {
			Double b = Arith.divide(a, DAYS_WEEK);
			txt = isInteger(b) ? b.intValue() + "w" : String.format("%1$.2fw", b);
		} else {
			txt = isInteger(a) ? a.intValue() + "" : String.format("%1$.2f", n);
		}
		
		return txt;
	}
	
	
	
	public static Date convertDateZone(Date sourceDate, String srcTimeZone, String destTimeZone) {
		return convertDateZone(sourceDate.getTime(), srcTimeZone, destTimeZone);
	}
	
	public static Date convertDateZone(long mills, String srcTimeZone, String destTimeZone) {
		TimeZone srcZone = TimeZone.getTimeZone(srcTimeZone);
		TimeZone destZone = TimeZone.getTimeZone(destTimeZone);
		long targetTime = mills - srcZone.getRawOffset() + destZone.getRawOffset();
		return new Date(targetTime);
	}
	
	public static Date convertDateZone(Date sourceDate, String destTimeZone) {
		return convertDateZone(sourceDate, TIME_ZONE_LOCAL, destTimeZone);
	}
	
	public static Date convertLocalToGmt(Date sourceDate) {
		return convertDateZone(sourceDate, TIME_ZONE_LOCAL, TIME_ZONE_GMT);
	}
	
	public static Date convertGmtToLocal(Date sourceDate) {
		return convertDateZone(sourceDate, TIME_ZONE_GMT, TIME_ZONE_LOCAL);
	}
	
	public static Date convertGmtToLocal(long mills) {
		return convertDateZone(mills, TIME_ZONE_GMT, TIME_ZONE_LOCAL);
	}
    
	public static String formatDate2Short(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_SHORT);
		return format.format(d);
	}
	
	public static boolean containsChar(String str) {
		if (isEmpty(str)) return false;
		boolean contained = false;
		for (char c : str.toCharArray()) {
			if (c < '0' || c > '9') {
				contained = true;
			}
		}
		return contained;
	}
	
	public static String convertToSqlMatchChars(String searchValue) {
		String xchar = searchValue;
		if (xchar.contains("*")) {
			while (xchar.startsWith("*")) {
				xchar = xchar.substring(1);
			}
			while (xchar.endsWith("*")) {
				xchar = xchar.substring(0, xchar.length() - 1);
			}
			xchar = xchar.replaceAll("\\*", "%");
		}
		
		xchar = "%" + xchar + "%";
		
		return xchar;
	}
	
	public static Date substractDays4Date(Date d, int days) {
		Calendar res = Calendar.getInstance();
		res.clear();
		res.setTime(d);
		res.set(Calendar.DAY_OF_MONTH, res.get(Calendar.DAY_OF_MONTH) - days);
		
		return res.getTime();
	}
	
	public static Date parseDateText(String dateText) {
		Date postDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy, hh:mm:ss aaa", LOCALE_US);
		try {
			postDate = sdf.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return postDate;
	}
	
	public static Date parseTodayText(String dateText) {
		Date today = new Date(System.currentTimeMillis());
		
		Date gmtToday = convertDateZone(today, TIME_ZONE_LOCAL, TIME_ZONE_GMT);
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(gmtToday);
		
		Matcher m = TODAY_DATE_PATTERN.matcher(dateText);
		
		Date postDate = null;
		if (m.find()) {
			int hh = Integer.valueOf(m.group(1));
			int mm = Integer.valueOf(m.group(2));
			int ss = Integer.valueOf(m.group(3));
			
			String amorpm = m.group(4);
			if (amorpm.equalsIgnoreCase("PM")) { hh = hh + 12; }
			
			cal.set(Calendar.HOUR_OF_DAY, hh);
			cal.set(Calendar.MINUTE, mm);
			cal.set(Calendar.SECOND, ss);
			
			postDate = cal.getTime();
		}
		
		return postDate;
	}
	
	
	public static String convertToString(List<Integer> list) {
		StringBuilder sb = new StringBuilder();
		for (Integer i : list) {
			sb.append(i).append(", ");
		}
		if (sb.length() > 0) sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getOrderHql(Page page,String hql,String alias){
		StringBuilder nhql = new StringBuilder(hql);
		
		if (Utils.isNotEmpty(page.getOrderBy())) {
			alias = Utils.isEmpty(alias) ? "" : alias+".";
			String order = Utils.isNotEmpty(page.getOrder()) ? page.getOrder() : Page.ASC;
			
			String orderBy = page.getOrderBy();// op.operatorName|Operator op|op.id
			String orderProperties = orderBy;
			if (orderBy.contains("|")) {
				String[] ss = orderBy.split("\\|");
				orderProperties = ss[0]; 
				String entity = ss[1];
				String key = ss[2];
				String secKey = ss[3];
				
				int fromIndex = hql.toLowerCase().indexOf("from ") + 5;
				nhql.insert(fromIndex, entity + ", ");
				
				int whereIdx = hql.toLowerCase().indexOf("where ") + 6;
				StringBuilder keyClause = new StringBuilder();
				if (whereIdx > 6) {
					keyClause.append(alias).append(secKey).append(" = ").append(key).append(" and (");
					nhql.insert(whereIdx, keyClause.toString());
					nhql.append(")");
				} else {
					keyClause.append(" where ").append(alias).append(secKey).append(" = ").append(key);
					nhql.insert(nhql.length(), keyClause.toString());
				}
			}
			
			String property = orderProperties.replaceAll(" ", "");
			if (property.contains(",")) {
				nhql.append(" order by ");
				
				String[] pros = property.split(",");
				
				for (String pro : pros) {
					if (Utils.isNotEmpty(pro)) {
						nhql.append(" ").append(pro.contains(".") ? "" : alias).append(pro).append(" ").append(order).append(", ");
					}
				}
				if (nhql.lastIndexOf(", ") == nhql.length() - 2) nhql.delete(nhql.length() - 2, nhql.length());
			} else {
				nhql.append(" order by ").append(property.contains(".") ? "" : alias).append(property).append(" ").append(order);
			}
			
		}
		
//		System.out.println("Utils.getOrderSql nhql is [" + nhql + "]");
		
		return nhql.toString();
	}
	
	public static String appendSql(String hql, String condition){
		StringBuilder nhql = new StringBuilder(hql);
				
		int fromIndex = hql.toLowerCase().indexOf("where ");
		
		if (fromIndex > 0) {
			if (existSqlOperator(nhql.toString())) {// there is some condition already
				nhql.insert(fromIndex+6, " and ");
			}
			nhql.insert(fromIndex+6, condition);
		} else {
			nhql.append(" where ").append(condition);
		}
		
		return nhql.toString();
	}
	
	public static boolean existSqlOperator(String sql) {
		sql = sql.toLowerCase();
		for (String op : SQL_OPERATORS) {
			if (sql.indexOf(op) > 0) {
				return true;
			}
		}
		return false;
	}
	
	public static String getTodayText() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_PATTERN2);
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public static Date getPreviousDay(Date current) {
		Calendar previousDay = Calendar.getInstance();
		previousDay.clear();
		previousDay.setTime(current);
		previousDay.set(Calendar.DAY_OF_MONTH, previousDay.get(Calendar.DAY_OF_MONTH) - 1);
		
		return previousDay.getTime();
	}
	
	public static String getImageServerPath(String fileName) {
		StringBuilder sb = new StringBuilder(Utils.getCardImagePath());
		sb.append(Utils.getTodayText()).append("/").append(System.currentTimeMillis()).append("_").append(fileName);
		return sb.toString();
	}
	
	public static String getJsonMessage(boolean success, String message) {
		StringBuilder json = new StringBuilder();
		
		// "{\"success\":\"ture\", \"message\":\"ok\"}"
		json.append("{").append(quoteAround("success")).append(":").append(quoteAround(success)).append(",");
		json.append(quoteAround("message")).append(":").append(quoteAround(message)).append("}");
		
		return json.toString();
	}

	public static String quoteAround(Object text) {
		return new StringBuilder("\"").append(text).append("\"").toString();
	}
	static void setWebDeployPath(String path) {
		WEB_DEPLOY_PATH = path;
		CARD_IMAGE_PATH = WEB_DEPLOY_PATH + "/card_image/";
	}
	
	public static String getCardImagePath() {
		return CARD_IMAGE_PATH;
	}
	
	public static String encryptWithMd5(String text) {
		if (isEmpty(text)) {
			return "";
		}
		
		String encrypted = "";
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte[] md5 = md.digest();
			encrypted = byteToHex(md5);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return encrypted;
	}
	
	public static String byteToHex(byte[] bs) {
		if (bs == null || bs.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder(bs.length * 2);
		for (byte b : bs) {
			sb.append(byteToHex(b));
		}
		return sb.toString();
	}
	
	public static String byteToHex(byte b) {
		return new StringBuilder(2).append(HEX_ALPH.charAt(b >> 4 & 0xF)).append(HEX_ALPH.charAt(b & 0xF)).toString();
	}
	
	public static String getResourcePath(String resource) {
		URL url = Utils.class.getClassLoader().getResource(resource);
		return url == null ? "" : url.getPath();
	}
	
	public static String removeChars(String s) {
		return  replaceAll(s, "\\D+", "");
	}
	
	public static String replaceAll(String s, String regex, String replacement) {
		if (s == null) {
			return "";
		}
		Pattern p = Pattern.compile(regex);
		
		Matcher m = p.matcher(s);
		
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, replacement);
		}
		m.appendTail(sb);
		
		return sb.toString();
	}
	
	public static String generateHtml4AltCoin(String fileName, AltCoinBean alt) {
		StringBuilder sb = new StringBuilder();
		
		// <tr style='font-weight: bold;font-size: 9px;'>
		// 		<td>File Name</td><td>Name</td><td>Abbr</td><td>Algo</td><td>Total</td><td>Block Time</td><td>Block Reward</td><td>PreMined</td><td>Percentage</td>
		//      <td>Launch Raw</td><td>Post Date</td><td>Used Time</td>
		// </tr>
		// <a href='file://localhost/storage/crawler4j/pages/' target='_blank'></a>
		sb.append("<tr>").append("\n");
		sb.append("\t").append("<td>").append(getHref(fileName)).append("</td><td>").append(alt.getName()).append("</td><td>").append(alt.getAbbrName()).append("</td>");
		sb.append("\t").append("<td>").append(trimNull(alt.getAlgo())).append("</td>");
		sb.append("\t").append("<td>").append(trimNull(alt.getTotalAmount())).append("</td><td>").append(trimNull(alt.getBlockTime())).append("</td><td>").append(trimNull(alt.getBlockReward())).append("</td>");
		sb.append("\t").append("<td>").append(trimNull(alt.getPreMined())).append("</td><td>").append(trimNull(alt.getMinedPercentage())).append("</td><td>").append(trimNull(alt.getLaunchRaw())).append("</td>");
		sb.append("\t").append("<td>").append(trimNull(alt.getLaunchTime())).append("</td><td>").append(alt.getUsedTime()).append("</td>").append("\n");
		
		sb.append("</tr>").append("\n");;
		
		return sb.toString();
	}
	
	private static String getHref(String fileName) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<a href='file://localhost").append(Constants.CRAWL_PAGES_FOLDER).append("/").append(fileName).append("' target='_blank'>").append(fileName).append("</a>");
		
		return sb.toString();
	}
	
	private static Object trimNull(Object txt) {
		return txt == null ? "" : txt;
	}
	
	public static String fetchPageByUrl(String pageUrl) {
		String canonicalUrl = URLCanonicalizer.getCanonicalURL(pageUrl);
		WebURL webUrl = new WebURL();
		webUrl.setURL(canonicalUrl);
		
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(Constants.CRAWL_FOLDER);
		config.setIncludeHttpsPages(true);
		config.setMaxDepthOfCrawling(0);
		config.setPolitenessDelay(1 * 1000);

		PageFetcher pageFetcher = createPageFetcher();
		
		edu.uci.ics.crawler4j.crawler.Page page = new edu.uci.ics.crawler4j.crawler.Page(webUrl);
		
		PageFetchResult fetchResult = pageFetcher.fetchHeader(webUrl);
		
		if (!fetchResult.fetchContent(page)) {
			return null;
		}
		
		String content = null;
		try {
			if (page.getContentCharset() == null) {
				content = new String(page.getContentData());
			} else {
				content = new String(page.getContentData(), page.getContentCharset());
			}
		} catch (Exception e) {
			log.error("get page content error", e);
		}
		
		return content;
	}
	
	private static PageFetcher createPageFetcher() {
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(Constants.CRAWL_FOLDER);
		config.setIncludeHttpsPages(true);
		config.setMaxDepthOfCrawling(0);
		config.setPolitenessDelay(1 * 1000);

		return new PageFetcher(config);
	}
	
	public static int extractTotalPagesNumber(String html) {
		
		if (isEmpty(html)) {
			return 100;
		}
		
		HtmlCleaner cleaner = new HtmlCleaner();

		TagNode node = cleaner.clean(html);

		Object[] nodes = null;
		try {
			nodes = node.evaluateXPath("//body/div[2]/table/tbody/tr/td/a");
		} catch (XPatherException e) {
			e.printStackTrace();
		}

		if (Utils.isNotEmpty(nodes)) {
			TagNode n = (TagNode) nodes[nodes.length-1];
			return Integer.valueOf(n.getText().toString().trim()).intValue();
		}
		
		return 99;
	}
	

	public static long getOneMinuteRangeEnd(long baseMillis) {
		return getRangeEndMillis(baseMillis, 1);
	}
	
	public static long getOneMinuteRangeStart(long baseMillis) {
		return getRangeStartMillis(baseMillis, 1);
	}
	
	public static long getRangeEndMillis(long baseMillis, int interval) {
		DateTime base = new DateTime(baseMillis, DateTimeZone.UTC);// 
		if (interval == 1) {
			int baseSec = base.getSecondOfMinute();
			if (baseSec != 0) {
				base = base.withMillisOfSecond(0).plusSeconds(60 - baseSec);
			}
		} else {
			int baseMinute = base.getMinuteOfHour();
			if (baseMinute != 0) {
				int mod = baseMinute % interval;
				int left = mod == 0 ? 0 : interval - mod;
				base = base.withSecondOfMinute(0).withMillisOfSecond(0).plusMinutes(left);
			}
		}
		
		return base.getMillis();
	}
	
	public static long getRangeStartMillis(long baseMillis, int interval) {
		DateTime base = new DateTime(baseMillis, DateTimeZone.UTC);// 
		if (interval == 1) {
			int baseSec = base.getSecondOfMinute();
			if (baseSec == 0) {
				base = base.withMillisOfSecond(0).minusMinutes(1);
			} else {
				base = base.withMillisOfSecond(0).minusSeconds(baseSec);
			}
		} else {
			int baseMinute = base.getMinuteOfHour();
			if (baseMinute == 0) {
				base = base.withSecondOfMinute(0).withMillisOfSecond(0).minusMinutes(interval);
			} else {
				int mod = baseMinute % interval;
				int left = mod == 0 ? 0 : interval - mod;
				base = base.withSecondOfMinute(0).withMillisOfSecond(0).minusMinutes(left);
			}
		}
		
		return base.getMillis();
	}
	
	public static String getMarketTradeTable(WatchListItem item) {
		return new StringBuilder("trade_")
		.append(item.getOperator().toLowerCase()).append("_")
		.append(item.getExchangeSymbol().toLowerCase()).append("_")
		.append(item.getWatchedSymbol().toLowerCase()).toString();
	}
	
	public static void main(String[] args) throws ParseException, Exception {
		System.out.println(encryptWithMd5("ubi6La5z"));
		String text = "February 17, 2014, 06:16:06 PM";
		
		System.out.println(Utils.parseDateText(text));
		
		System.out.println(formatNumber(1344L));
		System.out.println(formatNumber(134400000L));
		System.out.println(formatNumber(134400000000L));
		
		
		String currTxt = "2014-06-15 21:09:00";
		Date curr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currTxt);
		System.out.println(convertDateZone(curr, "GMT", "Asia/Shanghai"));
		System.out.println(formatDate2Short( convertDateZone(curr, "GMT", "Asia/Shanghai")));
		
		Date curr2 = new Date();
		System.out.println(convertDateZone(curr2, "GMT", "Asia/Shanghai"));
		System.out.println(formatDate2Short(convertDateZone(curr2, "GMT", "Asia/Shanghai")));
		
		String todayText = "Today at 06:07:42 AM";
		System.out.println(parseTodayText(todayText));
		
		System.out.println(TimeZone.getDefault().getID());
		
//		System.out.println(fetchPageByUrl("https://bitcointalk.org/index.php?board=159.0"));
		
//		String html = IOUtils.toString(new FileInputStream("/Users/cook/Downloads/ann_alts.html"));
//		System.out.println(extractTotalPagesNumber(html));
		
//		String hql = "ann from AltCoin ann where x = 0 and b = y";
//		String hql = "ann from AltCoin ann where x like 'x'";
//		String hql = "ann from AltCoin ann ";
		String hql = "ann from AltCoin ann order by x";
		String condition = "status = 0";
		System.out.println(appendSql(hql, condition));
		
		System.out.println(new Date());
//		System.out.println(getLastFiveMinuteTime());
//		System.out.println(getSecondFiveMinuteTime());
		
		
//		System.out.println(getLastFiveMinuteTime(1404995649540l));
//		System.out.println(getSecondFiveMinuteTime(1404995649540l));
		
	}
	
}
