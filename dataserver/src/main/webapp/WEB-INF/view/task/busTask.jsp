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
<style>
	table{  border-collapse:collapse;  }
	td{  border:1px solid #f00;  }
</style>


<script type="text/javascript">

function addThread(key){
	
	var  id = '#'+key+"_time";	
	var time = $(id).val();
	
	$.ajax( {
		type : "POST",
		url : "<%=request.getContextPath()%>/bus/start/" + key +"/" + time,
		dataType: "text",
		success : function(data) {
			if(data == "success"){
				window.location.href="<%=request.getContextPath() %>/bus/index";
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


function stopTask(key){
	
	$.ajax( {
		type : "POST",
		url : "<%=request.getContextPath()%>/bus/stop" ,
		dataType: "text",
		success : function(data) {
			if(data == "success"){
				window.location.href="<%=request.getContextPath() %>/bus/index";
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
	        <th>操作</th>
	    </tr>
    </thead>

	<tr id="<c:out value="${task.taskKey}"/>">
		<td><c:out value="1"/></td>
		<td><c:out value="${task.taskKey}"/></td>
		<td>
			<c:choose>
			   <c:when test="${task.running}"> 
				<c:out value="1"/>
			   </c:when>
			   <c:otherwise>  
				<c:out value="0"/>
			   </c:otherwise> 
			</c:choose>
		</td>
		<td>
			<c:if test="${task.running}">
			　　<c:out value="${task.period}"/>秒
			</c:if>
		</td>
		<td>
			<input type="text" value ="30" id ="<c:out value="${task.taskKey}"/>_time" />
			<input type="button" onclick="addThread('<c:out value="${task.taskKey}"/>')" value="增加"/> 
			<input type="button" onclick="stopTask('<c:out value="${task.taskKey}"/>')" value="停止"/>
		    
		</td>
	</tr>
	
</table>

</body>
</html>