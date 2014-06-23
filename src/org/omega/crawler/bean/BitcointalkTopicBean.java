package org.omega.crawler.bean;

import org.omega.crawler.bean.base.BaseBitcointalkTopicBean;


public class BitcointalkTopicBean extends BaseBitcointalkTopicBean {

	private static final long serialVersionUID = 1L;
	
	public BitcointalkTopicBean() { }
	
	public BitcointalkTopicBean(Integer topicid) {
		setTopicid(topicid);
	}
	
}
