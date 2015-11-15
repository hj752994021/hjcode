package com.hj.shiro.service;

import com.alibaba.fastjson.JSONArray;
import com.hj.base.dao.IBaseDao;

public interface PermissionService<T> extends IBaseDao<T> {
	public JSONArray getChildPermissions(Integer parnetId);
}
