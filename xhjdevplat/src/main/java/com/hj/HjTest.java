package com.hj;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.hj.shiro.model.Permission;
import com.hj.shiro.model.RolePermission;
import com.hj.shiro.model.User;
import com.hj.shiro.service.PermissionService;
import com.hj.shiro.service.RolePermissionService;
import com.hj.shiro.service.UserService;

public class HjTest {
	private static PermissionService<Permission> permissionService;
	public static UserService<User> userService;
	public static RolePermissionService<RolePermission> rolePermissionService;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
			//ApplicationContext act = new FileSystemXmlApplicationContext(new String[]{"classpath:applicationContext.xml"});
			permissionService = (PermissionService)act.getBean("permissionService");
			userService = (UserService)act.getBean("userService");
			rolePermissionService = (RolePermissionService)act.getBean("rolePermissionService");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void get(){
		//Permission permission =  permissionService.get(Permission.class, 1);
		//User user = userService.get(User.class, 1);
		//System.out.println(user.getLoginName());
		//System.out.println(permission.getName());
		//JSONArray array = permissionService.getChildPermissions(-1);
		//System.out.println(array.toJSONString());
		
		List<Permission> permissionsByUserId = rolePermissionService.getPermissionsByUserId2(1);
		for (Permission permission : permissionsByUserId) {
			System.out.println(permission.getName());
		}
	}
}
