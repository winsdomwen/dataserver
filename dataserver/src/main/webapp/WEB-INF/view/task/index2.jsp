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
<script type="text/javascript" src="<%=basePath%>js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/lib/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/lib/hoverShow.js"></script>
<style>
	table{  border-collapse:collapse;  }
	td{  border:1px solid #f00;  }
</style>


<script type="text/javascript">
		
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
		
        $('.showItem').hoverShow('text',{
            width:300,
            height:200,
            border:'1px solid blue',
            backgroundColor:'#FFFFFF'
        });
		
		showRedisQueue();
		setInterval("showRedisQueue()", 5000);		
		
	});

</script>

</head>
<body>

	<table align="center" border="1px">
		<thead>
		    <tr>
		        <th>序号</th>
		        <th>类型</th>
		        <th>线程数</th>
		        <th>间隔</th>
		        <th>数量</th>
		        <th>操作</th>
		        <th>介绍</th>
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
					<a  href="#" class="showItem" text='<c:out value="${task.value.introduction}"/>'>介绍</a>
				</td>						
			</tr>
		</c:forEach>	
		<tr>
			<td colspan="5">
				<input type="button"  onclick="uploadFile()" value="添加映射文件"/> 
				
			</td>
			<td colspan="2" style="text-align:right">
				<input type="button"  onclick="startAll()" value="全部启动"/>				
				<input type="button"  onclick="stopAll()" value="全部停止"/> 				
			</td>
		</tr>
	</table>

</body>
</html>