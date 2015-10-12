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
	<title>文件上传</title>
	<script type="text/javascript" src="<%=basePath%>js/lib/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/lib/jquery-ui-1.8.18.custom.min.js"></script>

 	<script type="text/javascript">  
		function isvalidatefile(obj) {  
		      
		    var extend = obj.substring(obj.lastIndexOf(".") + 1);  
		    //alert(extend);  
		    if (extend == "") {  
		    } else {  
		        if (!(extend == "csv" )) {  
		            alert("请上传后缀名为csv的文件!");  
		              
		            return false;  
		        }  
		    }  
		    return true;  
		}  
	 
		$(function() {  
						
			 $('#frmupload').submit(function() {  
			        if ($('#file').val() == '') {  
			            alert('请选择上传导入文件!');  
			            $('#file').focus();  
			            return false;  
			        }else{  
			            if(!isvalidatefile($('#file').val()))  
			                  return false;  			                  
			        } 
			        return success;
			    });		
		
		}); 	 
	 
	</script>	

</head>

<body>	

 <div align="center">
	<form id="frmupload" action="uploadfile" method="post" enctype="multipart/form-data" >
		<input id="file" type="file" name="file"/> 
		<input type="submit" value="上传" />
	</form>
 </div>	
 
</body>
</html>