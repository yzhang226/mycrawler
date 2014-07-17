package org.omega.trade.service;

import org.hibernate.SessionFactory;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.omega.trade.entity.WatchListItem;
import org.springframework.beans.factory.annotation.Autowired;

public class WatchListItemService extends SimpleHibernateTemplate<WatchListItem, Integer> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = WatchListItem.class;
	}
	
	
	
}
