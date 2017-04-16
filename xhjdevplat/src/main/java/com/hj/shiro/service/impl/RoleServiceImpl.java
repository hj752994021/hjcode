package com.hj.shiro.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hj.base.dao.BaseDao;
import com.hj.shiro.model.Role;
import com.hj.shiro.model.RolePermission;
import com.hj.shiro.model.User;
import com.hj.shiro.service.RolePermissionService;
import com.hj.shiro.service.RoleService;
import com.hj.shiro.service.UserService;
@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseDao<Role> implements RoleService<Role> {
	@Resource
	private RolePermissionService<RolePermission> rolePermissionService;
	@Resource(name="userService")
	private UserService<User> userService;
	public void saveRoleWithPermission(Role role,String permissionIds){
		saveOrUpdate(role);
		String[] permissionArr = permissionIds.split(",");
		rolePermissionService.deleteRolePermissionByRoleId(role.getId());
		for (int i = 0; i < permissionArr.length; i++) {
			String permissionId = permissionArr[i];
			RolePermission rolePermission = new RolePermission();
			rolePermission.setPermissionId(Integer.parseInt(permissionId));
			rolePermission.setRoleId(role.getId());
			rolePermissionService.save(rolePermission);
		}
	}
}
