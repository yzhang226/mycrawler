package org.omega.crawler.service;

import java.util.ArrayList;
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
	
	public List<AnnCoinBean> searchAnnCoins(Page<AnnCoinBean> page, String searchField, String searchValue) {
		String hql = "from AnnCoinBean ann ";
		
		String sqlValue = Utils.convertToSqlMatchChars(searchValue);
		
		hql = hql + "where ann." + searchField + " like '" + sqlValue + "'";
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<Integer> findParsedTopicids() {
//		String hql = " select new AnnCoinBean(ann.topicid) from AnnCoinBean ann where ann.isParsed is true";
		String hql = " from AnnCoinBean ann where ann.isParsed is true";
		List<AnnCoinBean> parsedAnns = find(hql);
		
		List<Integer> topicids = new ArrayList<>();
		for (AnnCoinBean ann : parsedAnns) {
			if (ann.getPublishDate() != null) {
				topicids.add(ann.getTopicid());
			}
		}
		
		return topicids;
	}
	
	
	
	
}
