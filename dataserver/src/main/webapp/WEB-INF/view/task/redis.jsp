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

	//setInterval("showTime()", 5000);
	
	function showRedisQueue()
	{
		
		$.ajax( {
			type : "POST",
			url : "<%=request.getContextPath()%>/redis/list",
			dataType: "json",
			success : function(data) {
				for(var key in data){					
					//alert(key+","+data[key]);
					$("#"+key+" td:last").html(data[key]);					
				} 								
				
			},
			error :function(){
				alert("网络连接出错！");
			}
		});	

	}
	
	$(document).ready(function(){
		
		showRedisQueue();
		setInterval("showRedisQueue()", 5000);
	});

</script>

</head>
<body>

<table id="table" align="center" border="1px" >
	<thead>
	    <tr>
	        <th>序号</th>
	        <th>队列名</th>
	        <th>数量</th>
	    </tr>
    </thead>
	<c:forEach items="${queueMap}" var="queue" varStatus="status">
		<tr id="<c:out value="${queue.key}"/>">
			<td><c:out value="${status.index + 1}"/></td>
			<td><c:out value="${queue.value}"/></td>
			<td><c:out value=""/></td>
		</tr>
	</c:forEach>
	
</table>

</body>
</html>