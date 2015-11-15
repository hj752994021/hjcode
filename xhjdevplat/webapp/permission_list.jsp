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
$("#permissionTree").tree({
	url:"permission_easyPermissionsTree.action",
	onClick: function(node){
		console.info(node.text);  // 在用户点击的时候提示
		console.info(node.id);
		console.info($("#contentFrame"));
		$("#contentFrame")[0].src="permission_input.action?id="+node.id;
	},
   	onContextMenu:function(e, node){
   		//禁用默认右键菜单
   		e.preventDefault();
   		//图层组
		$('#menu').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
   	},
   	onLoadSuccess:function(node,data){
   		var idInput = window.frames["contentFrame"].document.getElementById("id");
   		if(idInput!=null){
   			var id = idInput.value;
   			if(id!=null&&id!=""){
   				//找到右边内容窗口对应的节点
   				var newnode = $("#permissionTree").tree("find",id);
   				//扩展到对应的节点并选中
   				$("#permissionTree").tree("expandTo",newnode.target).tree("select",newnode.target);
   			}
   		}
   	}
});
});
function addRootPermission(){
	var url = "permission_input?permission.parentId=-1";
	document.getElementById("contentFrame").src=url;
}
function addPermission(){
	var node = $("#permissionTree").tree("getSelected");
	var nodeId = node.id;
	var url = "permission_input?permission.parentId="+nodeId;
	document.getElementById("contentFrame").src=url;
}
function delPermission(){
	var node = $("#permissionTree").tree("getSelected");
	var id = node.id;
	$.post("permission_delete.action?permission.id="+id,function(){
		$("#permissionTree").tree("remove",node.target);
		$.messager.alert("消息提示", "删除成功！","info");
		document.getElementById("contentframe").src="";
	});
}
//刷新树结构
function refreshTree(){
	$("#permissionTree").tree("reload");
}
//折叠
function collapseAll(){
	$("#permissionTree").tree('collapseAll');
}
//展开
function expandAll(){
	var node = $("#permissionTree").tree("getSelected");
	if(node!=null){
		$("#permissionTree").tree('expandAll',node.target);
	}else{
		$.messager.alert('消息提示', '请选择节点！','info');
	}
}
</script>
  </head>
  
  <body>
<div class="easyui-layout" style="width:100%;height:100%">
    <div data-options="region:'west',title:'权限树结构',split:true" style="width:200px;">
		<a class="easyui-linkbutton" onclick="collapseAll()">折叠全部</a>
		<a class="easyui-linkbutton" onclick="expandAll()">展开全部</a>    
    	<ul id="permissionTree"></ul>
    </div>   
    <div data-options="region:'center',title:'编辑页面'" style="padding:5px;background:#eee;">
    <iframe id="contentFrame" name="contentFrame" style="width: 100%;height: 100%; border:0px solid black;"></iframe>
    </div>
    <!-- 菜单 -->
    <div id="menu" class="easyui-menu" style="width:120px;">
		<div onclick="addRootPermission()" data-options="iconCls:'icon-add'">添加根目录</div>
		<div class="menu-sep"></div>
		<div onclick="addPermission()" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="delPermission()" data-options="iconCls:'icon-remove'">移除</div>
		<div class="menu-sep"></div>
	</div>
</div> 
  </body>
</html>
