package com.hj.shiro.service;

import java.util.List;

import com.hj.base.dao.IBaseDao;
import com.hj.shiro.model.Permission;

public interface RolePermissionService<T> extends IBaseDao<T> {
	public int deleteRolePermissionByRoleId(Integer roleId);
	public List<Permission> getPermissionsByRoleId(Integer roleId);
	public List<Permission>  getPermissionsByUserId(Integer userId);
	public List<Permission>  getPermissionsByUserId2(Integer userId);}
