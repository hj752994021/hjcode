package com.hj.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;

public interface IBaseDao<T> {

	public T get(Class<T> c, Integer id);

	public Serializable save(T entity);

	public void delete(T entity);

	public void update(T entity);

	public void saveOrUpdate(T entity);

	public List<T> findByHql(String hql, Object[] values);

	public List<Object[]> findBySql(String sql, Object[] values);

	public List<T> findPageByHql(String hql, int pageNo, int pageSize, Object[] values);

	public List<Object[]> findPageBySql(String hql, int pageNo, int pageSize, Object[] values);
	
	public Long getTotalNumber(String sql, Object[] values);
	
	public SQLQuery createSQLQuery(String sql, Object... values);
	
	public SQLQuery createSQLQuery(String sql, Map<String, Object> values);
}