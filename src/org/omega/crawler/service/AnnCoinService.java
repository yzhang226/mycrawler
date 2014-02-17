package org.omega.crawler.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.omega.crawler.bean.AnnCoinBean;
import org.omega.crawler.common.Page;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.omega.crawler.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;

public class AnnCoinService extends SimpleHibernateTemplate<AnnCoinBean, Integer> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = AnnCoinBean.class;
	}
	
	public List<AnnCoinBean> findAnnCoins(Page<AnnCoinBean> page) {
		
		String hql = "from AnnCoinBean ann";
		
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	
}