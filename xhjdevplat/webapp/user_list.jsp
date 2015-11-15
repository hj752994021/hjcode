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
    
    <title>My JSP 'user_list.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
function add(){
	window.open("user_input.action", "contentFrame");
}
function del(id){
	var sel = $("#userListDg").datagrid("getSelected");
	if(sel){
		$.ajax({
			type:"post",
			url:"user_del.action",
			data:{"id":sel.id},
			success:function(){
				$.messager.alert("提示","删除成功","info",function(){
					$("#userListDg").datagrid("reload");
				});
			}
		});
	}else{
		$.messager.alert("提示","请先选择一条记录","warning");
	}
}  
function edit(){
	var sel = $("#userListDg").datagrid("getSelected");
	if(sel){
		window.open("user_input.action?id="+sel.id,"contentFrame");
	}else{
		$.messager.alert("提示","请先选择一条记录","warning");
	}
	
}
$(function(){
	$("#userListDg").datagrid({    
	    //url:'user_easyList.action',
		columns:[[
	        {field:'ck',checkbox:true},
	        {field:'id',title:'ID值',width:100},
	        {field:'loginName',title:'登录名',width:100}, 
	        {field:'password',title:'密码',width:100}, 
	        {field:'userName',title:'用户名',width:100}, 
	    	]]  
	});
	reload();
	$("#userListpp").pagination({
		onSelectPage:function(pageNumber, pageSize){
		$(this).pagination('loading');
		var param={};
		$("#userListpp").pagination('refresh',{	// 改变选项并刷新分页栏信息
			pageNumber: pageNumber
		});
		param.pageNo=pageNumber;
		param.pageSize=pageSize;
		reload(param);
		$(this).pagination('loaded');
	}
});

	
});
function reload(param){
	if(!param) param={};
	if(!param.pageSize) param.pageSize=10;
	if(!param.pageNo) param.pageNo=1;
	$.ajax({
		type:"post",
		url:"user_easyList.action",
		data:param,
		beforeSend:function(XMLHttpRequest){
		},
		success:function(data){
			console.info(data);
			$("#userListDg").datagrid("loadData",data.records);
			$("#userListpp").pagination('refresh',{	// 改变选项并刷新分页栏信息
				total: data.totalNumber,
			});
		}
	});
}
</script>
  </head>
  
  <body>
<div>
<a id="btn1" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
<a id="btn2" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">编辑</a>
<a id="btn3" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
<a id="btn3" onclick="" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
<a id="btn3" onclick="" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">授权</a>
</div>
<table id="userListDg" title="用户列表" class="easyui-datagrid" style="width:auto;height:auto"   
        data-options="fitColumns:true,singleSelect:true">   
</table>
<div id="userListpp" class="easyui-pagination" data-options="total:2000,pageSize:10" style="background:#efefef;border:1px solid #ccc;"></div> 
  </body>
</html>
