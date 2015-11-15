package com.hj.shiro.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hj.shiro.model.Role;
import com.hj.shiro.service.RoleService;

@Controller
@Scope("prototype")
public class RoleAction {
	@Resource
	private RoleService<Role> roleService;
	private Integer id;
	private Role role;
	private Integer pageSize;
	private Integer pageNo;
	private List<Role> roles;
	
	public String save() throws Exception{
		String permissionIds = ServletActionContext.getRequest().getParameter("permissionIds");
		if(StringUtils.isEmpty(permissionIds)){
			roleService.saveOrUpdate(role);
		}else{
			roleService.saveRoleWithPermission(role, permissionIds);
		}
		return "showListPage";
	}
	public String input() throws Exception{
		if(id!=null){
			role = roleService.get(Role.class, id);
		}
		return "input";
	}
	public String showListPage(){
		return "showListPage";
	}
	public String easyList(){
		roles =roleService.findPageByHql("from Role", pageNo, pageSize, null);
		Long totalNumber = roleService.getTotalNumber("select count(1) from user", null);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		
		JSONObject res = new JSONObject();
		res.put("totalNumber", totalNumber);
		res.put("records", roles);
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
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
