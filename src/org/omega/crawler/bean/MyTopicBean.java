package org.omega.crawler.bean;

import org.omega.crawler.bean.base.BaseMyTopicBean;

public class MyTopicBean extends BaseMyTopicBean {

	private static final long serialVersionUID = -6544340070094216457L;

	public MyTopicBean() { }
	
	public MyTopicBean(Integer topicId) {
		setTopicId(topicId);
	}
	
}
