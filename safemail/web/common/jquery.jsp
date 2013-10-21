<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%   
String path = request.getContextPath();   
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";   
%>
<link type="text/css" rel="stylesheet" href="css/globle.css?random=2"/>
<link type="text/css" rel="stylesheet" href="css/skinshuxiang.css" id="skin"/>
<script language="javascript" src="js/jquery-1.3.2.min.js?random=2"></script>
<script language="javascript" src="<%=basePath%>/js/ckeditor/ckeditor.js?random=2"></script>
<script language="javascript" src="js/ckeditor/adapters/jquery.js?random=2"></script>
<script language="javascript" src="js/storage/jquery/thirdplugins/formvalidator/formValidator.js?random=2"></script>
<script language="javascript" src="js/storage/jquery/thirdplugins/formvalidator/formValidatorRegex.js?random=2"></script>
<script language="javascript" src="js/setup/setup.js?random=2"></script>
<script type="text/javascript" src="js/swf/swfupload.js?random=2"></script>
<script type="text/javascript" src="js/swf/handlers.js?random=2"></script>
<script language="javascript" src="js/main/main.js?random=2"></script>