package com.hj.shiro.service;

import com.hj.base.dao.IBaseDao;
import com.hj.shiro.model.User;

public interface UserService<T> extends IBaseDao<T>{
	/**
	 * 根据登陆名获取登陆对象
	 * @param loginName
	 * @return
	 */
	public User getEntityByLoginName(String loginName);
}
