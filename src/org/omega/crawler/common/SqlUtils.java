package org.omega.crawler.common;

public final class SqlUtils {
	
	
	private SqlUtils() {}
	/*
	 * CREATE  TABLE market_history_btc_mamm (
  trade_time BIGINT NOT NULL ,
  trade_type BIT NULL ,
  price DOUBLE NULL ,
  total_units DOUBLE NULL ,
  total_cost DOUBLE NULL ,
  PRIMARY KEY (trade_time) );
	 */
	public static String getMarketHistoryCreateSql(String sourceSymbol, String targetSymbol) {
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE market_history_").append(sourceSymbol.toLowerCase()).append("_").append(targetSymbol.toLowerCase()).append(" ( ").append("\n")
		      .append("trade_time BIGINT NOT NULL , ").append("\n")
		      .append("trade_type TINYINT NULL , ").append("\n")
		      .append("price DOUBLE NULL ,").append("\n")
		      .append("total_units DOUBLE NULL ,").append("\n")
		      .append("total_cost DOUBLE NULL ,").append("\n")
		      .append("PRIMARY KEY (trade_time) );");
		
		return create.toString();
	}
	
}
