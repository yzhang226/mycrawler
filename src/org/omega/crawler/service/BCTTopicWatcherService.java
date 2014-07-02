package org.omega.crawler.service;

import org.hibernate.SessionFactory;
import org.omega.crawler.bean.BCTTopicWatcherBean;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class BCTTopicWatcherService extends SimpleHibernateTemplate<BCTTopicWatcherBean, Integer> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = BCTTopicWatcherBean.class;
	}
	
	
	
	
	
}
