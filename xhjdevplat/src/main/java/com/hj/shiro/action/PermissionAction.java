package com.hj.shiro.action;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hj.shiro.model.Permission;
import com.hj.shiro.model.Role;
import com.hj.shiro.model.RolePermission;
import com.hj.shiro.service.PermissionService;
import com.hj.shiro.service.RolePermissionService;
@Controller
@Scope("prototype")
public class PermissionAction {
	@Resource
	private PermissionService<Permission> permissionService;
	@Resource
	private RolePermissionService<RolePermission> rolePermissionService;

	private Integer pageSize;
	private Integer pageNo;
	private Integer id;
	private Permission permission;
	private List<Permission> permissions;
	public String easyPermissionsTree(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String roleId = request.getParameter("roleId");
		List<Permission> filterPermissions = rolePermissionService.getPermissionsByRoleId(Integer.parseInt(roleId));
		JSONArray childPermissions = permissionService.getChildPermissions(-1,filterPermissions);
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(childPermissions);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		return null;
	}
	public String easyList(){
		permissions = permissionService.findPageByHql("from Permission", pageNo, pageSize, null);
		Long totalNumber = permissionService.getTotalNumber("select count(1) from permission", null);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		
		JSONObject res = new JSONObject();
		res.put("totalNumber", totalNumber);
		res.put("records", permissions);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(res);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		return null;
	}
	public String input(){
		if(id!=null){
			permission = permissionService.get(Permission.class, id);
		}
		return "input";
	}
	public String save(){
		ServletActionContext.getRequest().setAttribute("flag", "edit");
		permissionService.saveOrUpdate(permission);
		return "input";
	}
	public String delete(){
		permissionService.delete(permission);
		return "input";
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
}
