package org.omega.crawler.common;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

	private static final String HEX_ALPH = "0123456789ABCDEF";
	private static String WEB_DEPLOY_PATH = null;
	private static String CARD_IMAGE_PATH = null;
	
	private static final Locale LOCALE_US = Locale.US;
	private static final Pattern TODAY_DATE_PATTERN = Pattern.compile("(\\d{2}+):(\\d{2}+):(\\d{2}+) (\\w{2}+)");
	
	
	public static boolean isEmpty(String text) {
		return text == null || text.trim().length() == 0;
	}
	
	public static boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}
	
	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.isEmpty();
	}
	
	public static boolean isNotEmpty(Collection<?> list) {
		return !isEmpty(list);
	}
	
	public static Integer getTopicIdByUrl(String link) {
		return Integer.valueOf(link.substring(link.indexOf('=') + 1, link.lastIndexOf('.')));
	}
	
	public static String formatNumber(Number n) {
		if (n == null) {
			return null;
		}
		String txt = "";
		double a = n.doubleValue();
		// String.format("%1$.2f",dd)
		if (a >= 1000000000000l) {
			double b = a/1000000000000.0;
			txt = b + "T";
		} else if (a >= 1000000000) {
			double b = a/1000000000.0;
			txt = b + "B";
		} else if (a >= 1000000) {
			double b = a/1000000.0;
			txt = b + "M";
		} else if (a >= 1000) {
			double b = a/1000.0;
			txt = b + "K";
		} else {
			txt = n.toString();
		}
		
		return txt;
	}
	
	public static String formatDay(Number n) {
		if (n == null) {
			return null;
		}
		String txt = "";
		double a = n.doubleValue();
		
		if (a >= 365) {
			txt = String.format("%1$.1fy", new Double(a/365.0));
		} else if (a >= 30) {
			txt = String.format("%1$.1fm", new Double(a/30.0));
		} else if (a >= 7) {
			txt = String.format("%1$.1fw", new Double(a/7.0));
		} else {
			txt = String.format("%1$.1f", new Double(a));
		}
		
		return txt;
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
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(today);
		
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
				
				int fromIndex = nhql.indexOf("from ") + 5;
				nhql.insert(fromIndex, entity + ", ");
				
				int whereIdx = nhql.indexOf("where ") + 6;
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
	
	public static void main(String[] args) {
		System.out.println(encryptWithMd5("ubi6La5z"));
		String text = "February 17, 2014, 06:16:06 PM";
		
		System.out.println(Utils.parseDateText(text));
		
		System.out.println(formatNumber(1344L));
		System.out.println(formatNumber(134400000L));
		System.out.println(formatNumber(134400000000L));
		
	}
	
}
