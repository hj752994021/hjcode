package com.hj.shiro.service;

import com.hj.base.dao.IBaseDao;
import com.hj.shiro.model.Role;

public interface RoleService<T> extends IBaseDao<T> {
	public void saveRoleWithPermission(Role role,String permissionIds);
}
