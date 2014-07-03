package org.omega.crawler.bean.base;

import java.io.Serializable;

import org.omega.crawler.bean.AltCoinBean;


public class BaseAltCoinWatchListBean implements Serializable {
	
	private static final long serialVersionUID = -8322807790679435192L;
	
	private Integer id;
	private AltCoinBean altCoin;
	private String symbol;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public AltCoinBean getAltCoin() {
		return altCoin;
	}
	public void setAltCoin(AltCoinBean altCoin) {
		this.altCoin = altCoin;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
