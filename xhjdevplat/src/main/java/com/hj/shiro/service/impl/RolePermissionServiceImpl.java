package com.hj.shiro.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hj.base.dao.BaseDao;
import com.hj.shiro.model.Permission;
import com.hj.shiro.model.Role;
import com.hj.shiro.model.RolePermission;
import com.hj.shiro.model.UserRole;
import com.hj.shiro.service.PermissionService;
import com.hj.shiro.service.RolePermissionService;
import com.hj.shiro.service.UserRoleService;
@Service("rolePermissionService")
@Transactional
public class RolePermissionServiceImpl extends BaseDao<RolePermission> implements RolePermissionService<RolePermission> {
	
	@Resource
	private PermissionService<Permission> permissionService;
	private UserRoleService<UserRole> userRoleService;
	
	public int deleteRolePermissionByRoleId(Integer roleId){
		SQLQuery sql = createSQLQuery("delete from role_permission rp where rp.roleId=?", roleId);
		return sql.executeUpdate();
	}
	public List<Permission> getPermissionsByRoleId(Integer roleId){
		List<RolePermission> rpList = findByHql("from RolePermission rp where rp.roleId=?", new Object[]{roleId});
		List<Permission> permissions = null;
		if(rpList!=null){
			permissions = new ArrayList<Permission>();
			for (RolePermission rolePermission : rpList) {
				Permission permission = permissionService.get(Permission.class, rolePermission.getPermissionId());
				permissions.add(permission);
			}
		}
		return permissions;
	}
	public List<Permission>  getPermissionsByUserId(Integer userId){
		List<Permission> permissions = null;
		List<Role> roles = userRoleService.getRolesByUserId(userId);
		if(roles!=null){
			permissions = new ArrayList<Permission>();
			for (Role role : roles) {
				List<Permission> pers = getPermissionsByRoleId(role.getId());
				permissions.addAll(pers);
			}
		}

		return permissions;
	}
	public List<Permission>  getPermissionsByUserId(){
		String hql="select p from Permission  p, User u, UserRole ur,Role r,RolePermission rp where r.id=rp.roleId and rp.permissionId=p.id and r.id=ur.roleId and ur.userId=u.id and r.id=?";
		return null;
	}
}
