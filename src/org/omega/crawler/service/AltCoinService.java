package org.omega.crawler.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	
	public AltCoinBean getByTopicId(int topicId) {
		String hql = "from AltCoinBean ann where ann.topicid = " + topicId;
		return (AltCoinBean) findUnique(hql);
	}
	
	public List<AltCoinBean> findInterestAnnCoins(Page<AltCoinBean> page, Integer interest) {
		String hql = "from AltCoinBean ann";
		
		hql = hql + " where ann.isShow is true and (ann.interestLevel >= " + interest + " or ann.launchTime >= '" + getPreviousDate() + "')";
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<AltCoinBean> findAnnCoins(Page<AltCoinBean> page) {
		String hql = "from AltCoinBean ann";
		
		hql = hql + " where ann.isShow is true";
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<AltCoinBean> findAnnCoinsByReply(Page<AltCoinBean> page) {
		String hql = "from AltCoinBean ann";
		
		hql = hql + " where ann.isShow is true";
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<AltCoinBean> searchAnnCoins(Page<AltCoinBean> page, String searchField, String searchValue) {
		
		page.setPageNo(0);
		
		String hql = "from AltCoinBean ann ";
		
		String sqlValue = Utils.convertToSqlMatchChars(searchValue);
		sqlValue = sqlValue.toUpperCase();
		
		hql = hql + " where upper(ann." + searchField + ") like '" + sqlValue + "'";
		hql = hql + " and ann.isShow is true ";
		
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<AltCoinBean> searchInterestAnnCoins(Page<AltCoinBean> page, String searchField, String searchValue, Integer interest) {
		
		page.setPageNo(0);
		
		String hql = "from AltCoinBean ann ";
		
		String sqlValue = Utils.convertToSqlMatchChars(searchValue);
		sqlValue = sqlValue.toUpperCase();
		
		hql = hql + " where upper(ann." + searchField + ") like '" + sqlValue + "'";
		hql = hql + " and ann.isShow is true and (ann.interestLevel >= " + interest + " or ann.launchTime >= '" + getPreviousDate() + "')";
		
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	private String getPreviousDate() {
		Date now = new Date(System.currentTimeMillis());
		
		Date prev = Utils.substractDays4Date(now, 4);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sqlText = sdf.format(prev);
		return sqlText;
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
