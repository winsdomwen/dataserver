<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Redis 消息队列管理</title>
<!-- javascript lib -->
<link href="${pageContext.request.contextPath}/components/bootstrap/bootstrap.css" type="text/css" media="screen" rel="stylesheet">
<link href="${pageContext.request.contextPath}/components/bootstrap/bootstrap-responsive.css" type="text/css" media="screen" rel="stylesheet">
<link href="${pageContext.request.contextPath}/components/bootstrap/bootstrap-my.css" type="text/css" media="screen" rel="stylesheet">
<link href="${pageContext.request.contextPath}/components/font-awesome/css/font-awesome.css" type="text/css" media="screen" rel="stylesheet">
<link href="${pageContext.request.contextPath}/styles/main.css" type="text/css" media="screen" rel="stylesheet">
<link href="${pageContext.request.contextPath}/styles/modal.css" type="text/css" media="screen" rel="stylesheet">
<link href="${pageContext.request.contextPath}/styles/form.css" type="text/css" media="screen" rel="stylesheet">
<link href="${pageContext.request.contextPath}/styles/editable.css" type="text/css" media="screen" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/components/jquery/jquery-1.8.3.min.js"></script>
<style type="text/css">
.btn-toolbar {
	margin-top: 0;
}

#content-block.full-content {
	margin-left: 0;
}
</style>


<script type="text/javascript">
	function add(){
		window.location.href="<%=request.getContextPath() %>/student.do?method=add";
	}
	
	function del(id){
		$.ajax( {
			type : "POST",
			url : "<%=request.getContextPath()%>/student.do?method=del&id=" + id,
			dataType: "json",
			success : function(data) {
				if(data.del == "true"){
					alert("删除成功！");
					$("#" + id).remove();
				}
				else{
					alert("删除失败！");
				}
			},
			error :function(){
				alert("网络连接出错！");
			}
		});
	}
	
	
	function addThread(key){
		
		//var  id = '#'+key+"_time";	
		//var time = $(id).val();
		
		$.ajax( {
			type : "POST",
			url : "<%=request.getContextPath()%>/task/addThread/" + key ,
			dataType: "text",
			success : function(data) {
				if(data == "success"){
					window.location.href="<%=request.getContextPath() %>/task/index";
				}
				else{
					alert("添加线程失败！");
				}
			},
			error :function(){
				alert("网络连接出错！");
			}
		});	
	}
	
	function startTask(key){
		
		$.ajax( {
			type : "POST",
			url : "<%=request.getContextPath()%>/task/start/" + key ,
			dataType: "text",
			success : function(data) {
				if(data == "success"){
					window.location.href="<%=request.getContextPath() %>/task/index";
				}
				else{
					alert("停止失败！");
				}
			},
			error :function(){
				alert("网络连接出错！");
			}
		});			
	}	
	
	function stopTask(key){
		
		$.ajax( {
			type : "POST",
			url : "<%=request.getContextPath()%>/task/stop/" + key ,
			dataType: "text",
			success : function(data) {
				if(data == "success"){
					window.location.href="<%=request.getContextPath() %>/task/index";
				}
				else{
					alert("停止失败！");
				}
			},
			error :function(){
				alert("网络连接出错！");
			}
		});			
	}
	
	function startAll(){
		$.ajax( {
			type : "POST",
			url : "<%=request.getContextPath()%>/task/startAll",
			dataType: "text",
			success : function(data) {
				if(data == "success"){
					window.location.href="<%=request.getContextPath() %>/task/index";
				}
				else{
					alert("停止失败！");
				}
			},
			error :function(){
				alert("网络连接出错！");
			}
		});					
	}	

	function stopAll(){
		$.ajax( {
			type : "POST",
			url : "<%=request.getContextPath()%>/task/stopAll",
			dataType: "text",
			success : function(data) {
				if(data == "success"){
					window.location.href="<%=request.getContextPath() %>/task/index";
				}
				else{
					alert("停止失败！");
				}
			},
			error :function(){
				alert("网络连接出错！");
			}
		});			
		
	}	
	
	function showRedisQueue()
	{
		
		$.ajax( {
			type : "POST",
			url : "<%=request.getContextPath()%>/redis/list",
			dataType: "json",
			success : function(data) {
				for(var key in data){					
					//alert(key+","+data[key]);
					$("#"+key+" td:eq(4)").html(data[key]);					
				} 												
			},
			error :function(){
				//alert("网络连接出错！");
			}
		});		
	}
		
	function uploadFile(){
		window.location.href="<%=request.getContextPath() %>/task/upload";		
	}	
	
	$(document).ready(function(){
				
		showRedisQueue();
		setInterval("showRedisQueue()", 5000);		
		
	});

</script>

</head>
<body class="change-list"  style="padding-top:60px;padding-left:150px;padding-right:150px;">
	<div id="top-nav" class="navbar navbar-inverse navbar-fixed-top"
		style="position: fixed;">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="brand" href="#">GCI Redis队列入库服务</a> 
			</div>
		</div>
	</div>

<div class="results">
	<table class="table table-bordered table-striped table-hover table-condensed">
	    <caption>队列入库服务</caption>
		<thead>
		    <tr>
		        <th scope="col">序号</th>
		        <th scope="col">类型</th>
		        <th scope="col">线程数</th>
		        <th scope="col">间隔</th>
		        <th scope="col">数量</th>
		        <th scope="col">操作</th>
		        <th scope="col">介绍</th>
		    </tr>
	    </thead>
		<c:forEach items="${taskMap}" var="task" varStatus="status">
			<tr id="<c:out value="${task.key}"/>">
				<td><c:out value="${status.index + 1}"/></td>
				<td><c:out value="${task.value.queueName}"/></td>
				<td><c:out value="${task.value.size()}"/></td>				
				<td>
					<c:out value="${task.value.interval}"/>秒
				</td>
				<td><c:out value=""/></td>
				<td>
					<input type="button" onclick="startTask('<c:out value="${task.key}"/>')" value="启动"/> 
					<input type="button" onclick="addThread('<c:out value="${task.key}"/>')" value="新增线程"/>
					<input type="button" onclick="stopTask('<c:out value="${task.key}"/>')" value="停止"/>				    
				</td>
				<td>
					<button type="button" class="btn btn-link" title="介绍" data-trigger="hover"
					      data-container="body" data-toggle="popover" data-placement="right" 
					      data-content='<c:out value="${task.value.introduction}"/>'>介绍
   					</button>
				</td>						
			</tr>
		</c:forEach>	
		<tr>
			<td colspan="5">
				<div class="btn-group btn-sm">
					<a data-res-uri="<%=basePath%>task/upload"
						data-submit_uri="" title="添加映射文件"
						class="btn btn-primary details-handler"><i
						class="icon-plus icon-white"></i>添加映射文件</a>
				</div>
				<a href="<%=basePath%>task/batchupload"
						title="添加映射文件"
						class="btn btn-primary details-handler"><i
						class="icon-plus icon-white"></i>批量上传</a>
				</div>												
			</td>
			<td  style="text-align:right">
				<input type="button"  class="btn btn-success btn-sm"  onclick="startAll()" value="全部启动"/>				
				<input type="button"  class="btn btn-danger btn-sm" onclick="stopAll()" value="全部停止"/> 				
			</td>
			<td></td>
		</tr>
	</table>
</div>
<footer>
	<p>© GCI 公司所有</p>
</footer>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/components/bootstrap/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/list.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/modal.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/editable.js"></script>
<script>
$(function () 
   { $("[data-toggle='popover']").popover();
   });
</script>
</html>