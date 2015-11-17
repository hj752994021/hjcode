package com.hj.shiro.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.hj.base.dao.IBaseDao;
import com.hj.shiro.model.Permission;

public interface PermissionService<T> extends IBaseDao<T> {
	public JSONArray getChildPermissions(Integer parnetId, List<Permission> filterPermissions);
}
