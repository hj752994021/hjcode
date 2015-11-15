package com.hj;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hj.shiro.model.Permission;
import com.hj.shiro.model.User;
import com.hj.shiro.service.PermissionService;
import com.hj.shiro.service.UserService;
import com.hj.shiro.service.impl.UserServiceImpl;

public class JunitTest {
	private static PermissionService<Permission> permissionService;
	public static UserService<User> userService;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
			//ApplicationContext act = new FileSystemXmlApplicationContext(new String[]{"classpath:applicationContext.xml"});
			permissionService = (PermissionService)act.getBean("permissionService");
			userService = (UserService)act.getBean("userService");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void get(){
		Permission permission =  permissionService.get(Permission.class, 1);
		//User user = userService.get(User.class, 1);
		//System.out.println(user.getLoginName());
		System.out.println(permission.getName());
		//employeeService.save(new Employee("liming", "123456"));
		//permissionService.get
	}
}
