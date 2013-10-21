<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Calendar"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">		
		<title>安全电子邮件系统</title>
		<link rel="shortcut icon" href="images/com/deico.ico">
		<jsp:include page="/common/jquery.jsp"></jsp:include>
	</head>
<body id="body" scroll="no">
<noscript>应启用 JavaScript，才能使用本系统。然而，JavaScript 似乎已被禁用，要么就是您的浏览器不支持 JavaScript。要使用本系统，请更改您的浏览器选项以启用 JavaScript，然后重新登陆。</noscript>
<script type="text/javascript">
	var onceflag = true;
	$(document).ready(function(){
		window.onload = setContentSize;
		$(window).resize(setContentSize);
		loadUploadAndEditor();
	});
</script>
<div id="global">
	<div id="header">
		<div class="welcome">
	        <span><%Calendar cal = Calendar.getInstance();int hour = cal.get(Calendar.HOUR_OF_DAY);if (hour >= 6 && hour < 8) {out.print("早上好");}else if (hour >= 8 && hour < 11) {out.print("上午好");}else if (hour >= 11 && hour < 13) {out.print("中午好");}else if (hour >= 13 && hour < 18) {out.print("下午好");}else {out.print("晚上好");}%>，<c:out value="${sessionScope.user.userEmail}"/>[<a style="border: none;margin: 0;padding: 0" href="logout.action">退出</a>]</span>
	    </div>
	    <div class="right-bg">
	    </div>
	</div>
	<div id="sider">
	<div id="menu">
		<div class="first-layer" id="first-layer">
			<span><a href="javascript:void(0);" onClick="window.location.reload()" class="receive-msg">收信</a></span>
			<span><a href="javascript:void(0);" onClick="toWriteMail();" class="write-msg">写信</a></span>
		</div>
		<div class="second-layer" id="second-layer">
			<ul id="menu-mailbox">
			    <li class="on" id="sReList">
			    	<a onClick="window.location.reload()" title="收件箱" href="javascript:void(0);" onfocus="this.blur()">收件箱</a>
			    </li>
			</ul>
		</div>
	</div>
	</div>
	<div id="mainAll" class="main-div">
		<div id="main" class="main-div-1">
			<div id="viewMailLists">
				<jsp:include page="mail-list/mail-list-menu.jsp"></jsp:include>
				<div id="mailListInclude">
					<jsp:include page="mail-list/mail-list.jsp"></jsp:include>
				</div>
			</div>
		</div>
		<div id="write-main" class="main-div-1" style="display: none;"><jsp:include page="write-mail/write-mail.jsp"></jsp:include></div>
		<div id="viewMailCot" style="display: none">
			<jsp:include page="view-mail/view-mail.jsp"></jsp:include>
		</div>
	</div>
</div>
<script type="text/javascript">
var swfu=null;            //上传控件

function loadUploadAndEditor(){
    var instance = CKEDITOR.instances["content"]; 
    if(instance)
    {
        CKEDITOR.remove(instance); 
    }
    if($('#content').val() == ''){ 
        $('#content').val('<p>&nbsp;</p>');
    }
    $("#content").ckeditor(function(){ $(".cke_toolbar").eq(0).hide();});//创建CKEDITRO实例
}
function wm_init_display(){
	//考虑到转发和回复，所以抄送和密送列表存在不为空的情况
	if($("#cc").val()!=""){
		$("#copyToSend").show();
		$("#copyToSendDisPlay").html("隐藏抄送");
	}
	if($("#bcc").val()!=""){
		$("#secretToSend").show();
		$("#secretToSendDisPlay").html("隐藏密送");	
	}
	//设置上传附件显示列表的高度
	if($("#attachmentsListDiv").height()>68){
		$("#attachmentsListDiv").css("overflow-y","scroll");
		$("#attachmentsListDiv").css("overflow-x","hidden");
		$("#attachmentsListDiv").css("height","68px");
	}
}
</script>
</body>
</html>