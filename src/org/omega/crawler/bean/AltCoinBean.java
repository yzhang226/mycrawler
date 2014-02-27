package org.omega.crawler.bean;

import org.omega.crawler.bean.base.BaseAltCoinBean;
import org.omega.crawler.common.Utils;


public class AltCoinBean extends BaseAltCoinBean {

	private static final long serialVersionUID = 1L;
	
	public AltCoinBean() { }
	
	public AltCoinBean(Integer topicid) {
		setTopicid(topicid);
	}
	
	public String getTotalAmountTxt() {
		return Utils.formatNumber(getTotalAmount());
	}
	
	public String getHalfBlocksTxt() {
		return Utils.formatNumber(getHalfBlocks());
	}

	public String getBlockRewardTxt() {
		return Utils.formatNumber(getBlockReward());
	}
	
	public String getPreMinedTxt() {
		return Utils.formatNumber(getPreMined());
	}
	
	public String getMinedPercentageTxt() {
		return getMinedPercentage() != null ? String.format("%1$.2f", getMinedPercentage()) : "";
	}
	
	public String getHalfDaysTxt() {
		return Utils.formatDay(getHalfDays());
	}
	
}
