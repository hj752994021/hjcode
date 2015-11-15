package com.hj.shiro.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hj.shiro.model.User;
import com.hj.shiro.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shiro.Utils.EncryptUtils;
@Controller
@Scope("prototype")
public class LoginAction extends ActionSupport{
	@Resource
	private UserService<User> userService;
	private User user;
	
	public String home() {
		System.out.println("home");
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser.isPermitted("user_home.action")){
			return "home";
		}else{
			return "noperms";
		}
	}	
	/**
	 * 用户登录
	 * 
	 * @param user
	   *           　登录用户
	 * @return
	 */
	public String login() {
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(
				user.getLoginName(), EncryptUtils.encryptMD5(user.getPassword()));
		System.err.println(EncryptUtils.encryptMD5(user.getPassword()));
		token.setRememberMe(true);
		try {
			currentUser.login(token);
		} catch (AuthenticationException e) {
			ActionContext.getContext().put("message", "登陆错误");
			//e.printStackTrace();
			System.out.println("用户名或者密码错误");
			return "login";
		}
		if(currentUser.isAuthenticated()){
			user.setLoginName("张三");
			ActionContext.getContext().getSession().put("userinfo", user);
			return "homeaction";
		}else{
			ActionContext.getContext().put("message", "登陆错误");
			return "login";
		}
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public String logout() {
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.logout();
		} catch (AuthenticationException e) {
			e.printStackTrace();

		}
		return "login";
	}

	public String login2() {
		System.out.println("sss");
		ActionContext.getContext().put("message", "登录成功!");
		return "my";
	}

	public String login3() {
		System.out.println("sss");
		ActionContext.getContext().put("message", "登录成功!");
		return "test";
	}
	public String register() throws Exception{
		System.out.println("register");
		if(user.getId()==null){
			user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
			user.setIsEnable(true);
			user.setIsExpired(false);
			user.setCreateDate(new Date());
			userService.save(user);
		}
		ActionContext.getContext().put("message", "注册成功，请登录!");
		return "login";
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
