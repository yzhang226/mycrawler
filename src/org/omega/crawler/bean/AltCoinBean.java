package org.omega.crawler.bean;

import org.omega.crawler.bean.base.BaseAltCoinBean;


public class AltCoinBean extends BaseAltCoinBean {

	private static final long serialVersionUID = 1L;
	
	public AltCoinBean() { }
	
	public AltCoinBean(Integer topicid) {
		setTopicid(topicid);
	}
	
}
