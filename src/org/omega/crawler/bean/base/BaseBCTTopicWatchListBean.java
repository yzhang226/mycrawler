package org.omega.crawler.bean.base;

import java.io.Serializable;

import org.omega.crawler.bean.MyTopicBean;


public class BaseBCTTopicWatchListBean implements Serializable {
	
	private static final long serialVersionUID = -5430310744786224042L;
	
	private Integer id;
	private MyTopicBean bctTopic;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MyTopicBean getBctTopic() {
		return bctTopic;
	}
	public void setBctTopic(MyTopicBean bctTopic) {
		this.bctTopic = bctTopic;
	}
	
}
