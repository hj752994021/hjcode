package com.hj.shiro.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hj.base.dao.BaseDao;
import com.hj.shiro.model.Permission;
import com.hj.shiro.model.Role;
import com.hj.shiro.model.UserRole;
import com.hj.shiro.service.PermissionService;
import com.hj.shiro.service.UserRoleService;
@Service("permissionService")
@Transactional
public class PermissionServiceImpl extends BaseDao<Permission> implements
		PermissionService<Permission> {
	private UserRoleService<UserRole> userRoleService;

	public JSONArray getChildPermissions(Integer parnetId){
		List<Permission> permissions = findByHql("from Permission t where t.parentId=?", new Object[]{parnetId});
		JSONArray perJsArr = null;
		if(permissions!=null&&permissions.size()>0){
			perJsArr = new JSONArray(); 
			for (Permission permission : permissions) {
				JSONObject perJson = new JSONObject();
				perJson.put("id", permission.getId());
				perJson.put("text", permission.getName());
				perJson.put("checked", true);
				perJsArr.add(perJson);
				JSONArray childJson = getChildPermissions(permission.getId());
				perJson.put("children", childJson);
			}
		}
		return perJsArr;
	}
	public void getPermissionsByUserId(Integer userId){
		List<Role> roles = userRoleService.getRolesByUserId(userId);
	}
}
