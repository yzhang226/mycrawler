package org.omega.crawler.service;

import org.hibernate.SessionFactory;
import org.omega.crawler.bean.BCTTopicWatchListBean;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class BCTTopicWatchListService extends SimpleHibernateTemplate<BCTTopicWatchListBean, Integer> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = BCTTopicWatchListBean.class;
	}
	
	
	
	
	
}
