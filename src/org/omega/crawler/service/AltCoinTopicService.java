package org.omega.crawler.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.omega.crawler.bean.BCTTopicBean;
import org.omega.crawler.common.Page;
import org.omega.crawler.common.SimpleHibernateTemplate;
import org.omega.crawler.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;

public class AltCoinTopicService extends SimpleHibernateTemplate<BCTTopicBean, Integer> {

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.entityClass = BCTTopicBean.class;
	}
	
	public List<BCTTopicBean> findTalkTopics(Page<BCTTopicBean> page) {
		String hql = "from BCTTopicBean ann";
		
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<BCTTopicBean> searchTalkTopics(Page<BCTTopicBean> page, String searchField, String searchValue) {
		String hql = "from BCTTopicBean ann ";
		
		String sqlValue = Utils.convertToSqlMatchChars(searchValue);
		
		hql = hql + "where ann." + searchField + " like '" + sqlValue + "'";
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<Integer> findParsedTopicids() {
		String hql = "select tt.topicid from BCTTopicBean tt where tt.isParsed is true";
		List<Integer> parsedTopicids = find(hql);
		
		List<Integer> topicids = new ArrayList<>();
		for (Integer i : parsedTopicids) {
			topicids.add(i);
		}
		
		return topicids;
	}
	
	
	
	
}
