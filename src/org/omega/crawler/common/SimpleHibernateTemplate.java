package org.omega.crawler.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.Assert;

/**
 * Hibernate raw basic class 
 *
 * this class can be used in service class directly or be inherited as DAO.
 * reference Spring2.5 Petlinc example,do not use HibernateTemplate, but to use Hibernate raw API.
 * use Hibernate sessionFactory.getCurrentSession() to get the session.
 *
 * @param <E>
 *            DAO operateed bean(Entity bean) type.
 * @param <PK>
 *            PK type
 *
 * @author calvin
 */
@SuppressWarnings("unchecked")
public class SimpleHibernateTemplate<E, PK extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SessionFactory sessionFactory;

	protected Class<E> entityClass;

	public SimpleHibernateTemplate() { 
	}

	public SimpleHibernateTemplate(final SessionFactory sessionFactory, final Class<E> entityClass) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Session openSession(){
		return sessionFactory.openSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

    public void flush() {
        getSession().flush();
    }
    
	public void save(final E entity) {
		Assert.notNull(entity);
		Session session = getSession();

//		log.debug("------------" + session.isOpen() + " aaaa "
//				+ session.getFlushMode());

//		 if(session.getFlushMode().equals(FlushMode.NEVER)){
//		 session.setFlushMode(FlushMode.AUTO);
//		 }
//		 session.flush();
		session.save(entity);
//		 session.flush();
		logger.debug("save entity: {}", entity);
	}
	
	public void update(final E entity) {
		Assert.notNull(entity);
		Session session = getSession();
		session.update(entity);
//		session.flush();
		
//		System.out.println("update entity = " + entity);
		
//		session.merge(entity);
//		session.persist(entity);
		
		logger.debug("save entity: {}", entity);
	}
	
	public void saveOrUpdate(final E entity) {
		Assert.notNull(entity);
		Session session = getSession();

//		log.debug("------------" + session.isOpen() + " aaaa "
//				+ session.getFlushMode());

		// if(session.getFlushMode().equals(FlushMode.NEVER)){
		// session.setFlushMode(FlushMode.AUTO);
		// }
		// session.flush();
		session.saveOrUpdate(entity);
		
		
//		System.out.println("saveOrUpdate entity is " + entity);
		
		 session.flush();
		logger.debug("save entity: {}", entity);
	}
	
	
	
	public void delete(final E entity) {
		Assert.notNull(entity);
		getSession().delete(entity);
		logger.debug("delete entity: {}", entity);
	}

	public void delete(final PK id) {
		Assert.notNull(id);
		delete(get(id));
	}

	public List<E> findAll() {
		return findByCriteria();
	}

	public Page<E> findAll(final Page<E> page) {
		return findByCriteria(page);
	}

	/**
	 * get entity bean by PK
	 */
	public E get(final PK id) {
		return (E) getSession().load(entityClass, id);
	}

	/**
	 * get entity bean by PK
	 */
	public E get(final PK id, Class entityClass) {
		return (E) getSession().load(entityClass, id);
	}
	
	public E load(final PK id) {
		return (E) getSession().load(entityClass, id);
	}

	/**
	 * get entity bean by PK
	 */
	public E load(final PK id, Class entityClass) {
		return (E) getSession().load(entityClass, id);
	}
	

	/**
	 * find list by entity bean 's property value.
	 */
	public List<E> findByProperty(final String propertyName, final Object value) {
		Assert.hasText(propertyName);
		return createCriteria(Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * find a entity bean by entity bean 's property value.
	 */
	public E findUniqueByProperty(final String propertyName, final Object value) {
		Assert.hasText(propertyName);
		return (E) createCriteria(Restrictions.eq(propertyName, value))
				.uniqueResult();
	}

	/**
	 * use HQL to query.
	 * @param hql
	 * @param values
	 * alterable nums parameter values
	 */
	public List find(final String hql, final Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * use hql to query for paging
	 * @param page
	 * @param hql
	 * @param values
	 */
	public Page<E> find(final Page<E> page, final String hql, final Object... values) {
		Assert.notNull(page);

//		String hql1 = hql + " order by " + page.getOrderBy() + " " + page.getOrder();
		
		if (page.isAutoCount()) {
			// logger.warn("now HQL not support to get total count automatic,we have to get by query. hql is{}", hql);
			page.setTotalCount(getResultCount(hql, values));
		}
		
//		System.out.println("Simple Hiber page.getTotalCount() is " + page.getTotalCount());

		Query q = createQuery(hql, values);
		if (page.isFirstSetted()) {
			q.setFirstResult(page.getFirst());
		}
		if (page.isPageSizeSetted()) {
			q.setMaxResults(page.getPageSize());
		}
		
		
		//gavin start
//		List list = q.list();
//		int j = 0;
//		for(int i=0;i<list.size();i++){
//			j = 1 + i;
//			Rowable entity = (Rowable) list.get(i);
//			if(entity instanceof Rowable){
//				entity.setGridRowNumber(j);
//			}
//		}
		
		//gavin end
		page.setResult(q.list());
//		page.setResult(list);
		return page;
	}

	/**
	 * USE raw SQL to query for paging.
	 *
	 * @param page
	 * @param hql
	 * @param values
	 * @return
	 */
	public Page<E> findSql(final Page<E> page, final String sql, Object[] entity, final Object... values) {
		Assert.notNull(page);

		if (page.isAutoCount()) {
			// logger.warn("now HQL not support to get total count automatic,we have to get by query. hql is{}", hql);
			page.setTotalCount(this.getResultCountSql(sql, values));
		}
		SQLQuery q = createSQLQuery(sql, values);//raw sql

		if (entity != null && entity.length > 0) {
			for (Object obj : entity) {
				q.addEntity(obj.getClass());
			}
		}

		if (page.isFirstSetted()) {
			q.setFirstResult(page.getFirst());
		}
		if (page.isPageSizeSetted()) {
			q.setMaxResults(page.getPageSize());
		}

		page.setResult(q.list());
		return page;
	}

	/**
	 * use SQL to query
	 *
	 * @param sql
	 * @param values
	 * @return
	 */
	public List findSql(final String sql, final Object... values) {
		SQLQuery q = createSQLQuery(sql, values);

		return q.list();
	}

	/**
	 * use HQL to query a entity bean.
	 */
	public Object findUnique(final String hql, final Object... values) {
		return createQuery(hql, values).uniqueResult();
	}

	/**
	 * use HQL to get Integer result.
	 */
	public Integer findInt(final String hql, final Object... values) {
		return (Integer) findUnique(hql, values);
	}

	/**
	 * use HQL to get Long result.
	 */
	public Long findLong(final String hql, final Object... values) {
		return (Long) findUnique(hql, values);
	}

	/**
	 * use Criterion to query list.
	 *
	 * @param criterion
	 */
	public List<E> findByCriteria(final Criterion... criterion) {
		return createCriteria(criterion).list();
	}

	/**
	 * use DetachedCriteria to query list.
	 *
	 * @param DetachedCriterion
	 */
	public List<E> findByDetachedCriteria(DetachedCriteria criteria) {
		//log.debug("DetachedCriteria test：session is: " + getSession());
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
	/**
	 * 
	 * @param criteria
	 * @return gavin add for interceptor
	 */
	public List<E> findByDetachedCriteriaInteceptor(DetachedCriteria criteria) {
		//log.debug("DetachedCriteria test：session is: " + getSession());
		Session session = openSession();
		List<E> list = criteria.getExecutableCriteria(session).list();
		session.close();
		return list;
	}
	
	
	/**
	 * use Criterion to query for paging.
	 *
	 * @param page
	 * @param criterion
	 */
	public Page<E> findByCriteria(final Page page, final Criterion... criterion) {
		Assert.notNull(page);

		Criteria c = createCriteria(criterion);
		
		if (page.isAutoCount()) {
			page.setTotalCount(countQueryResult(page, c));
		}
		if (page.isFirstSetted()) {
			c.setFirstResult(page.getFirst());
		}
		if (page.isPageSizeSetted()) {
			c.setMaxResults(page.getPageSize());
		}

		if (page.isOrderBySetted()) {
			if (page.getOrder().equals(Page.ASC)) {
				c.addOrder(Order.asc(page.getOrderBy()));
			} else {
				c.addOrder(Order.desc(page.getOrderBy()));
			}
		}
//		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//gavin start
		List list = c.list();
		int j = 0;
		for(int i=0;i<list.size();i++){
			j = 1 + i;
			Rowable entity = (Rowable) list.get(i);
			if(entity instanceof Rowable){
				entity.setGridRowNumber(j);
			}
		}
		//gavin end
		//page.setResult(c.list());
		page.setResult(list);
		return page;
	}

	

	/**
	 * create Query Assist method
	 */
	public Query createQuery(final String queryString, final Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setCacheable(false);//enable query cache
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
//		queryObject.setFirstResult(3);
//		queryObject.setMaxResults(3);
		
		return queryObject;
	}

	/**
	 * create SQLQuery Assist method
	 */
	public SQLQuery createSQLQuery(final String queryString, final Object... values) {
		Assert.hasText(queryString);
		SQLQuery queryObject = getSession().createSQLQuery(queryString);
//		queryObject.setCacheable(true);//enable query cache
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * create Criteria Assist method
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria.setCacheable(true);//enable query cache;
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * judge the entity bean's property 's value is unique or not,
	 * e.g. open be used by ajax to judge userId is valid.
	 *
	 */
	public boolean isPropertyUnique(final String propertyName,
			final Object newValue, final Object orgValue) {
		if (newValue == null || newValue.equals(orgValue))
			return true;

		Object object = findUniqueByProperty(propertyName, newValue);
		return (object == null);
	}

	/**
	 * query total count.
	 *
	 */
	protected int countQueryResult(final Page<E> page, final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// delete Projection、ResultTransformer、OrderBy
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) BeanUtils.getFieldValue(impl, "orderEntries");
			BeanUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("countQueryResult method occur exception:{}", e.getMessage());
		}

		Object countResult = c.setProjection(Projections.rowCount()).uniqueResult();
		
		int totalCount = 0;
		if (countResult instanceof Number) {
			totalCount =  ((Number) countResult).intValue();
		}

		// set Projection and OrderBy
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}

		try {
			BeanUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("countQueryResult method occur exception:{}", e.getMessage());
		}

		return totalCount;
	}

	protected void checkWriteOperationAllowed(Session session)
			throws InvalidDataAccessApiUsageException {
		if (session.getFlushMode().lessThan(FlushMode.COMMIT)) {
			throw new InvalidDataAccessApiUsageException(
					"Write operations are not allowed in read-only mode (FlushMode.NEVER/MANUAL): "
							+ "Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.");
		}
	}

	private boolean hasDistinctOrGroupBy(String str) {
		Pattern p = Pattern.compile("group\\s*by[\\w|\\W|\\s|\\S]*", 2);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		} else {
			p = Pattern.compile("distinct ", 2);
			m = p.matcher(str);
			return m.find();
		}
	}

	public int getResultCount(String queryString) {
		String hqlstr = removeOrders(queryString);
		if (hasDistinctOrGroupBy(hqlstr)) {
			List lt = find(hqlstr);
			return lt != null ? lt.size() : 0;
		}
		String countQueryString = " select count (*) "
				+ removeSelect(removeOrders(queryString));
		List countlist = find(countQueryString);
		int count = 0;
		if (countlist.size() == 1)
			count = ((Long) countlist.get(0)).intValue();
		else
			count = countlist.size();
		return count;
	}

	public int getResultCount(String queryString, Object args[]) {
		String hqlstr = removeOrders(queryString);
		if (hasDistinctOrGroupBy(hqlstr)) {
			List lt = find(hqlstr, args);
			return lt != null ? lt.size() : 0;
		}
		String countQueryString = " select count (*) "
				+ removeSelect(removeOrders(queryString));
		List countlist = find(countQueryString, args);
		int count = 0;
		if (countlist.size() == 1)
			count = ((Long) countlist.get(0)).intValue();
		else
			count = countlist.size();
		return count;
	}

	public int getResultCountSql(String queryString, Object args[]) {
		String hqlstr = removeOrders(queryString);
		if (hasDistinctOrGroupBy(hqlstr)) {
			List lt = findSql(hqlstr, args);
			return lt != null ? lt.size() : 0;
		}
		String countQueryString = " select count (*) "
				+ removeSelect(removeOrders(queryString));
		List countlist = findSql(countQueryString, args);
		int count = 0;
		if (countlist.size() == 1)
			count = ((java.math.BigDecimal) countlist.get(0)).intValue();
		else
			count = countlist.size();
		return count;
	}

	private String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql
				+ " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		for (; m.find(); m.appendReplacement(sb, ""))
			;
		m.appendTail(sb);
		return sb.toString();
	}
}











