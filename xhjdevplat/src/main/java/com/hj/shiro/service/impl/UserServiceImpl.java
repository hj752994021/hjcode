package com.hj.shiro.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hj.base.dao.BaseDao;
import com.hj.shiro.model.Role;
import com.hj.shiro.model.User;
import com.hj.shiro.service.RoleService;
import com.hj.shiro.service.UserService;
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseDao<User> implements UserService<User>{
	@Resource(name="roleService")
	private RoleService<Role> roleService;
	public User getEntityByLoginName(String loginName){
		List<User> list = this.findByHql("from User t where t.loginName=?", new Object[]{loginName});
		if(list!=null){
			return list.get(0);
		}else {
			return null;
		}
	}

	
}
