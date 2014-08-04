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
		String hql = "from AltCoinBean ann where ann.topicId = " + topicId;
		return (AltCoinBean) findUnique(hql);
	}
	
	public List<AltCoinBean> findInterestAnnCoins(Page<AltCoinBean> page, Integer interest) {
		String hql = "from AltCoinBean ann";
		
		hql = hql + " where (ann.interest >= " + interest + " or ann.launchTime >= '" + getPreviousDate() + "')";
		hql = appendActiveStatus(hql, "ann");
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	private String appendActiveStatus(String hql, String alias) {
		StringBuilder cond = new StringBuilder();
		
		if (Utils.isNotEmpty(alias)) {
			cond.append(alias).append(".");
		}
		cond.append("status <> ").append(AltCoinBean.STATUS_INACTIVE);
		
		return Utils.appendSql(hql, cond.toString());
	}
	
	public List<AltCoinBean> findAnnCoins(Page<AltCoinBean> page) {
		String hql = "from AltCoinBean ann";
		
		hql = appendActiveStatus(hql, "ann");
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<AltCoinBean> findCoinsInDashboard(Page<AltCoinBean> page, String condition) {
		String hql = "from AltCoinBean ann";
		
		hql = appendActiveStatus(hql, "ann");
		if (Utils.isNotEmpty(condition)) hql = hql + " and " + condition;
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	
	
	public List<AltCoinBean> findAnnCoinsByReply(Page<AltCoinBean> page) {
		String hql = "from AltCoinBean ann";
		
		hql = appendActiveStatus(hql, "ann");
		hql = Utils.getOrderHql(page, hql, "ann");
		
		find(page, hql);
		
		return page.getResult();
	}
	
	public List<AltCoinBean> searchAnnCoins(Page<AltCoinBean> page, String searchField, String searchValue) {
		String hql = "from AltCoinBean ann ";
		
		String sqlValue = Utils.convertToSqlMatchChars(searchValue);
		sqlValue = sqlValue.toUpperCase();
		
		hql = hql + " where upper(ann." + searchField + ") like '" + sqlValue + "'";
		hql = appendActiveStatus(hql, "ann");
		
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
		hql = hql + " and (ann.interest >= " + interest + " or ann.launchTime >= '" + getPreviousDate() + "')";
		hql = appendActiveStatus(hql, "ann");
		
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
		String hql = "select ann.topicId from AltCoinBean ann where ann.publishDate != null";
		List<Integer> allTopicIds = find(hql);
		
		return allTopicIds;
	}
	
	
	
	
}
