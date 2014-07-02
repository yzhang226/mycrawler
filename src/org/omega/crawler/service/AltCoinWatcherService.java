package org.omega.crawler.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.bean.AltCoinWatcherBean;
import org.omega.crawler.common.Page;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.omega.crawler.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;

public class AltCoinWatcherService extends SimpleHibernateTemplate<AltCoinWatcherBean, Integer> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = AltCoinWatcherBean.class;
	}
	
	public List<AltCoinBean> getWatchedList(Page<AltCoinWatcherBean> page, Integer interest) {
		String hql = "from AltCoinWatcherBean ann";
		hql = Utils.getOrderHql(page, hql, "ann");
		
		List<AltCoinWatcherBean> wats = find(page, hql).getResult();
		
		List<AltCoinBean> alts = new ArrayList<>(wats.size());
		for (AltCoinWatcherBean wat : wats) {
			alts.add(wat.getAltCoin());
		}
		
		return alts;
	}
	
	
}
