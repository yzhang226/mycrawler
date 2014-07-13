package org.omega.trade.service;

import org.hibernate.SessionFactory;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.omega.trade.entity.MarketTrade;
import org.springframework.beans.factory.annotation.Autowired;

public class MarketTradeService extends SimpleHibernateTemplate<MarketTrade, Long> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = MarketTrade.class;
	}
	
	
	
}
