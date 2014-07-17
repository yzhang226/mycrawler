package org.omega.trade.service;

import org.hibernate.SessionFactory;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.omega.trade.entity.TradeStatistics;
import org.springframework.beans.factory.annotation.Autowired;

public class TradeStatisticsService extends SimpleHibernateTemplate<TradeStatistics, Long> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = TradeStatistics.class;
	}
	
	
	
}
