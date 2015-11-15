package com.hj.shiro.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hj.shiro.model.User;
import com.hj.shiro.service.UserService;
import com.shiro.Utils.EncryptUtils;
@Controller
@Scope("prototype")
public class UserAction {
	
	  @Resource
	  private UserService<User> userService;
	  private List<User> users;
	  private Integer id;
	  private User user;
	  private Integer pageSize;
	  private Integer pageNo;
	  
	public String nopermission() {
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser.isPermitted("user_notmyjsp.action")){
			return "notmyjsp";
		}else{
			return "noperms";
		}
	}

	public String save() throws Exception {
		if(user.getId()==null){
			user.setPassword(EncryptUtils.encryptMD5(user.getPassword()));
		}
		this.userService.saveOrUpdate(user);
		return "list";
	}

	public String input() throws Exception {
		if (this.id != null) {
			this.user = ((User) this.userService.get(User.class, this.id));
		}
		return "input";
	}

	public String del() throws Exception {
		if (id != null) {
			userService.delete((User) userService.get(User.class,id));
		}
		return null;
	}

	public String list() {
		//this.users = this.userService.findByHql("from User", null);
		return "list";
	}
	
	public String easyList(){
		users =userService.findPageByHql("from User", pageNo, pageSize, null);
		Long totalNumber = userService.getTotalNumber("select count(1) from user", null);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		
		JSONObject res = new JSONObject();
		res.put("totalNumber", totalNumber);
		res.put("records", users);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		return null;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
}
