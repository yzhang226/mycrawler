package org.omega.crawler.bean.base;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BaseAltCoinBean implements Serializable {
	
	private static final long serialVersionUID = -5430310744786224042L;
	
	private Integer id;
	private Integer topicid;
	private String author;
	private String title;
	private String link;
	private Timestamp publishDate;
	private String publishContent;
	private Integer replies;
	private Integer views;
	private Boolean isParsed;
	
	private Timestamp createTime;
	
	private String algo;
	private Date launchTime;
	private String launchRaw;
	private String name;
	private String abbrName;
	private Integer interestLevel;
	
	private String proof;
	private Boolean cpuMinable;
	private Boolean gpuMinable;
	private Boolean asicMinable;
	
	private Long totalAmount;
	private Integer blockTime;
	private Integer halfBlocks;
	private Integer halfDays;
	private Double blockReward;
	private String difficultyAdjust;
	
	private Boolean isShow;
	
	private Long preMined;
	private Double minedPercentage;
	
	private Integer powDays;
	private Integer powHeight;
	private Long posAmount;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTopicid() {
		return topicid;
	}
	public void setTopicid(Integer topicid) {
		this.topicid = topicid;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Timestamp getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Timestamp publishDate) {
		this.publishDate = publishDate;
	}
	public String getPublishContent() {
		return publishContent;
	}
	public void setPublishContent(String publishContent) {
		this.publishContent = publishContent;
	}
	public Integer getReplies() {
		return replies;
	}
	public void setReplies(Integer replies) {
		this.replies = replies;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	public Boolean getIsParsed() {
		return isParsed;
	}
	public void setIsParsed(Boolean isParsed) {
		this.isParsed = isParsed;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getAlgo() {
		return algo;
	}
	public void setAlgo(String algo) {
		this.algo = algo;
	}
	public Date getLaunchTime() {
		return launchTime;
	}
	public void setLaunchTime(Date launchTime) {
		this.launchTime = launchTime;
	}
	public String getLaunchRaw() {
		return launchRaw;
	}
	public void setLaunchRaw(String launchRaw) {
		this.launchRaw = launchRaw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbrName() {
		return abbrName;
	}
	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}
	public Integer getInterestLevel() {
		return interestLevel;
	}
	public void setInterestLevel(Integer interestLevel) {
		this.interestLevel = interestLevel;
	}
	
	public String getProof() {
		return proof;
	}
	public void setProof(String proof) {
		this.proof = proof;
	}
	public Boolean getCpuMinable() {
		return cpuMinable;
	}
	public void setCpuMinable(Boolean cpuMinable) {
		this.cpuMinable = cpuMinable;
	}
	public Boolean getGpuMinable() {
		return gpuMinable;
	}
	public void setGpuMinable(Boolean gpuMinable) {
		this.gpuMinable = gpuMinable;
	}
	public Boolean getAsicMinable() {
		return asicMinable;
	}
	public void setAsicMinable(Boolean asicMinable) {
		this.asicMinable = asicMinable;
	}
	
	public Long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getBlockTime() {
		return blockTime;
	}
	public void setBlockTime(Integer blockTime) {
		this.blockTime = blockTime;
	}
	public Integer getHalfBlocks() {
		return halfBlocks;
	}
	public void setHalfBlocks(Integer halfBlocks) {
		this.halfBlocks = halfBlocks;
	}
	public Integer getHalfDays() {
		return halfDays;
	}
	public void setHalfDays(Integer halfDays) {
		this.halfDays = halfDays;
	}
	public Double getBlockReward() {
		return blockReward;
	}
	public void setBlockReward(Double blockReward) {
		this.blockReward = blockReward;
	}
	public String getDifficultyAdjust() {
		return difficultyAdjust;
	}
	public void setDifficultyAdjust(String difficultyAdjust) {
		this.difficultyAdjust = difficultyAdjust;
	}
	
	public Boolean getIsShow() {
		return isShow;
	}
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
	
	public Long getPreMined() {
		return preMined;
	}
	public void setPreMined(Long preMined) {
		this.preMined = preMined;
	}
	public Double getMinedPercentage() {
		return minedPercentage;
	}
	public void setMinedPercentage(Double minedPercentage) {
		this.minedPercentage = minedPercentage;
	}
	public Integer getPowDays() {
		return powDays;
	}
	public void setPowDays(Integer powDays) {
		this.powDays = powDays;
	}
	public Integer getPowHeight() {
		return powHeight;
	}
	public void setPowHeight(Integer powHeight) {
		this.powHeight = powHeight;
	}
	public Long getPosAmount() {
		return posAmount;
	}
	public void setPosAmount(Long posAmount) {
		this.posAmount = posAmount;
	}
	public String toHtml() {
		StringBuilder sb = new StringBuilder("<tr>");
		sb.append("<td>").append(null != publishDate ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publishDate) : "").append("</td>");
		sb.append("<td>").append(title).append("</td>");
		sb.append("<td>").append(author).append("</td>");
		sb.append("<td>").append(replies).append("</td>");
		sb.append("<td>").append(views).append("</td>");
		sb.append("</tr>\n");
		
		return sb.toString();
	}
	
}
