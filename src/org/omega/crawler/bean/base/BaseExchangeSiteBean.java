package org.omega.crawler.bean.base;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BaseExchangeSiteBean implements Serializable {
	
	private static final long serialVersionUID = -5430310744786224042L;
	
	private Integer id;
	private String name;
	private String websiteUrl;
	private String coinStatsUrl;
	private Double fee;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWebsiteUrl() {
		return websiteUrl;
	}
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	public String getCoinStatsUrl() {
		return coinStatsUrl;
	}
	public void setCoinStatsUrl(String coinStatsUrl) {
		this.coinStatsUrl = coinStatsUrl;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
}
