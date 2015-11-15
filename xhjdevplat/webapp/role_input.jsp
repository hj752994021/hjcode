<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp" %>
<%@ include file="resource/meta_easyui.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'role_list.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
$(function(){
$("#permissionTree").tree({
	url:"permission_easyPermissionsTree.action",
	checkbox:true
});
});
function saveRole(){
	var selEle = $("#permissionTree").tree("getChecked");
	var permissionIds="";
	$.each(selEle,function(i,obj){
		if(i!=selEle.length-1){
			permissionIds += obj.id+",";
		}else{
			permissionIds += obj.id;
		}
	});
	console.info(permissionIds);
	$("#permissionIds").val(permissionIds);
	$("#roleForm").submit();
}
</script>
  </head>
  
<body>
<form id="roleForm" action="role_save.action" method="post">
  <input type="hidden" name="role.id" value="${role.id }" />
  <input type="hidden" id="permissionIds" name="permissionIds" />
	<table>
		<tr>
			<td>角色名</td>
			<td><input type="text" name="role.roleName" value="${role.roleName }" /></td>
		</tr>
		<tr>
			<td>描述</td>
			<td><input type="text" name="role.description" value="${role.description }" /></td>
		</tr>
		<tr>
		</tr>	
		<tr>
			<td colspan="2"><input type="button" value="保存" onclick="saveRole()" /></td>
		</tr>
	</table>
	<div>
	<label>选择权限</label>
	</div>
	<div>
	<ul id="permissionTree"></ul>
	</div>
</form>
</body>
</html>
