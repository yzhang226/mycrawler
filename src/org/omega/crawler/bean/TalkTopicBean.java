package org.omega.crawler.bean;

import org.omega.crawler.bean.base.BaseTalkTopicBean;


public class TalkTopicBean extends BaseTalkTopicBean {

	private static final long serialVersionUID = 1L;
	
	public TalkTopicBean() { }
	
	public TalkTopicBean(Integer topicid) {
		setTopicid(topicid);
	}
	
}
