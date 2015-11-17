package com.hj.base.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("baseDao")
@SuppressWarnings("unchecked")
@Transactional
public class BaseDao<T> implements IBaseDao<T> {
	@Autowired
	public SessionFactory sessionFactory;

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional(readOnly = true)
	public T get(Class<T> c, Integer id) {
		return (T) getCurrentSession().get(c, id);
	}

	@Override
	public Serializable save(T entity) {
		Serializable id = getCurrentSession().save(entity);
		return id;
	}

	@Override
	public void delete(T entity) {
		getCurrentSession().delete(entity);
	}

	@Override
	public void update(T entity) {
		getCurrentSession().update(entity);
	}

	@Override
	public void saveOrUpdate(T entity) {
		getCurrentSession().saveOrUpdate(entity);
	}

	@Transactional(readOnly = true)
	public <X> List<X> findByHql(String hql, Object[] values) {
		Query query = getCurrentSession().createQuery(hql);
		if ((values != null) && (values.length > 0)) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[0]);
			}
		}
		return query.list();
	}

	@Transactional(readOnly = true)
	public List<Object[]> findBySql(String sql, Object[] values) {
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		if ((values != null) && (values.length > 0)) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}

	@Transactional(readOnly = true)
	public <X> List<X> findPageByHql(String hql, int pageNo, int pageSize,
			Object[] values) {
		Query query = getCurrentSession().createQuery(hql);
		query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(
				pageNo * pageSize);
		if ((values != null) && (values.length > 0)) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[0]);
			}
		}
		return query.list();
	}

	@Transactional(readOnly = true)
	public List<Object[]> findPageBySql(String sql, int pageNo, int pageSize,
			Object[] values) {
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(
				pageNo * pageSize);
		if ((values != null) && (values.length > 0)) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}

	public Long getTotalNumber(String sql, Object[] values) {
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		if ((values != null) && (values.length > 0)) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		BigInteger totalNumberBd = (BigInteger) query.uniqueResult();
		Long totalNumber = totalNumberBd.longValue();
		return totalNumber;
	}
	public SQLQuery createSQLQuery(String sql, Object... values){
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		if ((values != null) && (values.length > 0)) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	public SQLQuery createSQLQuery(String sql, Map<String, Object> values){
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		if(values != null){
			Iterator<Entry<String, Object>> iterator = values.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, Object> param = iterator.next();
				query.setParameter(param.getKey(), param.getValue());
			}
		}
		return query;
		
	}
}
