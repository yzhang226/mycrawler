package org.omega.crawler.bean.base;

import java.io.Serializable;

import org.omega.crawler.bean.BCTTopicBean;


public class BaseBCTTopicWatchListBean implements Serializable {
	
	private static final long serialVersionUID = -5430310744786224042L;
	
	private Integer id;
	private BCTTopicBean bctTopic;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BCTTopicBean getBctTopic() {
		return bctTopic;
	}
	public void setBctTopic(BCTTopicBean bctTopic) {
		this.bctTopic = bctTopic;
	}
	
}
