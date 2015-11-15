<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'register.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  
  <body>
    <form method="post" action="login_register.action">
    <table>
    <tr><td>id：</td><td><input type="text" readonly="readonly" name="user.id" /></td></tr>
    <tr><td>登录名：</td><td><input type="text" name="user.loginName" /></td></tr>
    <tr><td>密码：</td><td><input type="text" name="user.password" /></td></tr>
    <tr><td>名称：</td><td><input type="text" name="user.userName" /></td></tr>
    <tr><td><input type="submit" value="注册" /></td></tr>
    </table>
    </form>
  </body>
</html>
