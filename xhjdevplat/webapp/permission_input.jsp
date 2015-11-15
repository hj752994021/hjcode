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
    
    <title>My JSP 'permission_list.jsp' starting page</title>
    
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
		var flag = $("#flag").val();
		//当新增加一个图层后，刷新树结构并定位新增加的图层
		if(flag=="edit"){
			parent.refreshTree();
		}
	});
</script>
  </head>

<body>
<form action="permission_save.action" method="post">
  <input type="hidden" id="flag" value="${flag }" />
  <input type="hidden" name="permission.id" value="${permission.id }" />
  <input type="hidden" name="permission.parentId" value="${permission.parentId }" />
	<table>
		<tr>
			<td>角色名</td>
			<td><input type="text" name="permission.name" value="${permission.name }" /></td>
		</tr>
		<tr>
			<td>描述</td>
			<td><input type="text" name="permission.description" value="${permission.description }" /></td>
		</tr>
		<tr>
		</tr>	
		<tr>
			<td colspan="2"><input type="submit" value="保存" /></td>
		</tr>
	</table>
</form>
</body>
</html>
