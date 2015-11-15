package com.hj.shiro.service;

import com.hj.base.dao.IBaseDao;

public interface RolePermissionService<T> extends IBaseDao<T> {
	public int deleteRolePermissionByRoleId(Integer roleId);
}
