<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<!-- js custom -->
<script type="text/javascript" src="<%=basePath%>js/manager/index.js"></script>

</head>
<body>

<h1>tt</h1>

</body>
</html>