package org.omega.crawler.service;

import org.hibernate.SessionFactory;
import org.omega.crawler.bean.ExchangeSiteBean;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class ExchangeSiteService extends SimpleHibernateTemplate<ExchangeSiteBean, Integer> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = ExchangeSiteBean.class;
	}
	
	
}
