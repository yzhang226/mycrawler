package org.omega.crawler.bean;

import java.util.concurrent.TimeUnit;

import org.omega.crawler.bean.base.BaseMyTopicBean;
import org.omega.crawler.common.Utils;

public class MyTopicBean extends BaseMyTopicBean {

	private static final long serialVersionUID = -6544340070094216457L;

	public MyTopicBean() { }
	
	public MyTopicBean(Integer topicId) {
		setTopicId(topicId);
	}
	
	public String getPublishTimeTxt() {
		return Utils.formatDate2Short(Utils.convertGmtToLocal(TimeUnit.SECONDS.toMillis(getPublishTime())));
	}
	
	
}
