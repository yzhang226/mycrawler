package org.omega.crawler.common;

public final class Constants {
	
	public static final String ATTR_LOGINED_ACCOUNT = "__logined_account__";
	
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	public static final String DATE_PATTERN2 = "yyyyMMdd";
	public static final String TIME_PATTERN_FULL = "yyyyMMddHHmmssSSS";
	
	public static final String TEXT_UNKNOWN = "UNKNOWN";
	
	
	public static final int ONE_HOUR_MILLIS = 24 * 60 * 60 * 1000;
	
	public static final String CRAWL_FOLDER = "/storage/crawler4j";
	public static final String CRAWL_PAGES_FOLDER = CRAWL_FOLDER + "/pages";
	
	public static final String TIMEZONE_SHANGHAI = "Asia/Shanghai";
	
	public static final byte STATUS_ACTIVE = 0;
	public static final byte STATUS_INACTIVE = 1;
	public static final byte STATUS_WATCHED = 11;
	
	
}
