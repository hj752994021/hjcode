<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="resource/meta_easyui.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'home.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
	function logout(){
		window.location.href="login_logout.action";
	}
</script>	
  </head>
<body class="easyui-layout">   
<div data-options="region:'north',split:true" style="height:50px;background-color:gray;">
	<a id="btn1" onclick="logout()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">退出</a>
</div>   
<div data-options="region:'west',title:'菜单栏',split:true" style="width:200px;">
<ul class="easyui-tree">
	<li>
		<span>用户管理</span>
		<ul>   
            <li><span><a href="user_list.action" target="contentFrame">用户列表</a></span> </li>
            <li><span><a href="role_showListPage.action" target="contentFrame">角色管理</a></span> </li>
            <li><span><a href="permission_list.jsp" target="contentFrame">权限管理</a></span> </li>
        </ul> 
	</li>
</ul>
</div>   
<div data-options="region:'center',title:'欢迎使用华山论剑的系统'" style="padding:5px;background:#eee;">
<iframe name="contentFrame" style="width: 100%;height: 100%; border:0px solid black;"></iframe>
</div>   
</body>
</html>
