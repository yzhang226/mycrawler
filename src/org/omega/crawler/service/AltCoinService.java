package org.omega.crawler.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.common.Page;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.omega.crawler.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;

public class AltCoinService extends SimpleHibernateTemplate<AltCoinBean, Integer> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = AltCoinBean.class;
	}
	
	public List<AltCoinBean> findAnnCoins(Page<AltCoinBean> page) {
		String hql = "from AltCoinBean ann";
		
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<AltCoinBean> searchAnnCoins(Page<AltCoinBean> page, String searchField, String searchValue) {
		String hql = "from AltCoinBean ann ";
		
		String sqlValue = Utils.convertToSqlMatchChars(searchValue);
		sqlValue = sqlValue.toLowerCase();
		
		hql = hql + "where lower(ann." + searchField + ") like '" + sqlValue + "'";
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<Integer> findParsedTopicids() {
//		String hql = " select new AltCoinBean(ann.topicid) from AltCoinBean ann where ann.isParsed is true";
		String hql = " from AltCoinBean ann where ann.isParsed is true";
		List<AltCoinBean> parsedAnns = find(hql);
		
		List<Integer> topicids = new ArrayList<>();
		for (AltCoinBean ann : parsedAnns) {
			if (ann.getPublishDate() != null) {
				topicids.add(ann.getTopicid());
			}
		}
		
		return topicids;
	}
	
	
	
	
}
