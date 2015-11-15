package com.hj.shiro.service;

import java.util.List;

import com.hj.base.dao.IBaseDao;
import com.hj.shiro.model.Role;

public interface UserRoleService<T> extends IBaseDao<T> {
	public List<Role> getRolesByUserId(Integer userId);
}
