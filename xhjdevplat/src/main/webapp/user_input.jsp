<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'user_list.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  	<script type="text/javascript" src="resource/jquery-easyui-1.4.1/jquery.min.js"></script>
  </head>
  
<body>
<form action="user_save.action" method="post">
  <input type="hidden" name="user.id" value="${user.id }" />
	<table>
		<tr>
			<td>登录名</td>
			<td><input type="text" name="user.loginName" value="${user.loginName }" /></td>
		</tr>
		<tr>
			<td>密码</td>
			<td><input type="text" name="user.password" value="${user.password }" /></td>
		</tr>
		<tr>
			<td>姓名</td>
			<td><input type="text" name="user.userName" value="${user.userName }" /></td>
		</tr>
		<tr>
			<td>创建时间</td>
			<td><input type="text" name="user.createDate" value="${user.createDate }" /></td>
		</tr>
		<tr>
			<td>是否禁用</td>
			<td><input type="text" name="user.isEnable" value="${user.isEnable }" /></td>
		</tr>
		<tr>
			<td>是否过期</td>
			<td><input type="text" name="user.isExpired" value="${user.isExpired }" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="保存" /></td>
		</tr>
	</table>
</form>
</body>
</html>
