package org.omega.crawler.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.bean.AltCoinWatchListBean;
import org.omega.crawler.common.Page;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.omega.crawler.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;

public class AltCoinWatchListService extends SimpleHibernateTemplate<AltCoinWatchListBean, Integer> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = AltCoinWatchListBean.class;
	}
	
	public List<AltCoinBean> getWatchedList(Page<AltCoinWatchListBean> page, Integer interest) {
		String hql = "from AltCoinWatcherBean ann";
		hql = Utils.getOrderHql(page, hql, "ann");
		
		List<AltCoinWatchListBean> wats = find(page, hql).getResult();
		
		List<AltCoinBean> alts = new ArrayList<>(wats.size());
		for (AltCoinWatchListBean wat : wats) {
			alts.add(wat.getAltCoin());
		}
		
		return alts;
	}
	
	
}
