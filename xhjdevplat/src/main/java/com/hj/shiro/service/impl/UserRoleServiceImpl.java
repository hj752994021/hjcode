package com.hj.shiro.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hj.base.dao.BaseDao;
import com.hj.shiro.model.Role;
import com.hj.shiro.model.UserRole;
import com.hj.shiro.service.RoleService;
import com.hj.shiro.service.UserRoleService;

public class UserRoleServiceImpl extends BaseDao<UserRole> implements UserRoleService<UserRole> {
	@Resource
	private RoleService<Role> roleService;
	public List<Role> getRolesByUserId(Integer userId){
		List<Role> roles = null;
		List<UserRole> urList = findByHql("from UserRole ur where ur.userId=?", new Object[]{userId});
		if(urList!=null){
			roles = new ArrayList<Role>();
			for (UserRole userRole : urList) {
				Role role = roleService.get(Role.class, userRole.getRoleId());
				roles.add(role);
			}
		}
		return roles;
	}
}
