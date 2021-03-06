package org.omega.crawler.bean;

import java.util.concurrent.TimeUnit;

import org.omega.crawler.bean.base.BaseAltCoinBean;
import org.omega.crawler.common.Utils;

public class AltCoinBean extends BaseAltCoinBean {

	private static final long serialVersionUID = 1L;
	
	public AltCoinBean() { }
	
	public AltCoinBean(Integer topicid) {
		setTopicId(topicid);
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
		return Utils.isPositive(getMinedPercentage()) ? String.format("%1$.2f", getMinedPercentage()) : "";
	}
	
	public String getHalfDaysTxt() {
		return Utils.formatDay(getHalfDays());
	}
	
	public String getPowAmountTxt() {
		return Utils.formatNumber(getPowAmount());
	}
	
	public String getPowHeightTxt() {
		return Utils.formatNumber(getPowHeight());
	}
	
	public String getPowDaysTxt() {
		return Utils.formatDay(getPowDays());
	}
	
	public String getPublishTimeTxt() {
		return Utils.formatDate2Short(Utils.convertGmtToLocal(TimeUnit.SECONDS.toMillis(getMyTopic().getPublishTime())));
	}
	
	public String getLaunchTimeTxt() {
		if (getLaunchTime() != null) {
			return Utils.formatDate2Short(Utils.convertGmtToLocal(getLaunchTime()));
		}
		return "";
	}
	
	public String toPrintableTxt() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName()).append("[").append(getAbbrName()).append("], ")
		  .append("total: ").append(getTotalAmount()).append(", ")
		  .append("algo: ").append(getAlgo()).append(", ")
		  .append("breward: ").append(getBlockReward()).append(", ")
		  .append("btime: ").append(getBlockTime()).append(", ")
		  .append("premine: ").append(getMinedPercentage()).append("%, ")
		  .append("preAmount: ").append(getPreMined()).append(", ")
//		  .append("launch: ").append(pdate).append(", ")
		  .append("launchRaw: ").append(getLaunchRaw()).append(", ");
		return sb.toString();
	}
	
	private double usedTime;
	public double getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(double usedTime) {
		this.usedTime = usedTime;
	}
	
	
}
