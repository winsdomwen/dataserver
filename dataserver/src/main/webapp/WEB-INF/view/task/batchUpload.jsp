<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
<title>SWFUpload Demos - Simple Demo</title>
<link href="<%=basePath%>js/swfupload/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=basePath%>js/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=basePath%>js/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=basePath%>js/swfupload/handlers.js"></script>
<script type="text/javascript">
		var swfu;
		window.onload = function() {
			var settings = {
				flash_url : "<%=basePath%>js/swfupload/swfupload.swf",
				upload_url: "<%=basePath%>task/fileupload",
				post_params: {"PHPSESSID" : "<?php echo session_id(); ?>"},
				file_size_limit : "10 MB",
				file_types : "*.csv",
				file_types_description : "csv Files",
				file_upload_limit : 0,
				file_queue_limit : 0,
				file_post_name:"filedata",
				custom_settings : {
					progressTarget : "fsUploadProgress",
					cancelButtonId : "btnCancel"
				},
				debug: false,

				// Button settings
				button_image_url: "<%=basePath%>images/TestImageNoText_65x29.png",
				button_width: "65",
				button_height: "29",
				button_placeholder_id: "spanButtonPlaceHolder",
				button_text: '<span class="theFont">选择</span>',
				button_text_style: ".theFont { font-size: 16; }",
				button_text_left_padding: 12,
				button_text_top_padding: 3,				
				
				//The event handler functions are defined in handlers.js
				file_queued_handler : fileQueued,
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,
				upload_start_handler : uploadStart,
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : this.onUploadSuccess,
				upload_complete_handler : uploadComplete,
				queue_complete_handler : queueComplete	// Queue plugin event
			};

			swfu = new SWFUpload(settings);
	     };
	     
	     function onUploadSuccess(){ 	    	  
	    	 try {
	             //若有多个文件，则上传一个成功后，继续上传，否则显示上传成功！
	             if (this.getStats().files_queued > 0) {
	                  this.startUpload();
	             } else {
	            	 window.location.href ='<%=basePath%>task/inittask';
	             }
	         } catch (ex) {
	             this.debug(ex);
	         }	    	 	    	 
	     }	     
	</script>
</head>
<body>

<div id="content">
	<h2>上传配置文件</h2>
	<form id="form1" action="" method="post" enctype="multipart/form-data">

			<div class="fieldset flash" id="fsUploadProgress">
			<span class="legend">上传文件</span>
			</div>
		<div id="divStatus">0 个文件上传</div>
			<div>
				<span id="spanButtonPlaceHolder"></span>
				<input id="btnCancel" type="button" value="取消上传" onclick="swfu.cancelQueue();" disabled="disabled" style="margin-left: 2px; font-size: 8pt; height: 29px;" />
			</div>
	</form>
</div>
</body>
</html>
