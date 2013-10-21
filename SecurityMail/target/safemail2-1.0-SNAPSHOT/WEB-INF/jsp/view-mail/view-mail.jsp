<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Calendar"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">		
		<title>安全电子邮件系统-查看邮件</title>
		<link rel="shortcut icon" href="images/com/deico.ico">
		<jsp:include page="/common/jquery.jsp"></jsp:include>
	</head>
<body id="body">
<div class="m_right">           
	<div id="viewMailMainDiv" class="mail_cklb">
	  <div class="fj_div" id="fj_div">
	  		<div class="xxlb_div">
		       <table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
				    <td width="65" align="right"><span class="ck_color">主　题：</td>
				    <td width=""><b class="ck_b">${mail.subject}</b></td>
				    <td width="" align="right">&nbsp;</td>
				  </tr>
				</table>
		     </div>
		     <div class="xxlb_div">
		     <table width="100%" border="0" cellspacing="0" cellpadding="0">
				 <tr>
				    <td width="65" align="right"><span class="ck_color">发件人：</span></td>
				    <td width="">
			    		<span class="viewMailFromAddress">${mail.from}</span>
				    </td>
				 </tr>
			 </table>
		     </div>
		     <div class="xxlb_div">
		       <table width="100%" border="0" cellspacing="0" cellpadding="0">
		        <tr>
		         <td width="65" align="right"><span class="ck_color">时　间：</span></td>
		         <td width=""><span id="vm_date"><s:date name="mail.date" format="yyyy-MM-dd HH:mm:ss"></s:date></span></td>
		        </tr>
		       </table>
		     </div>
	      	<div class="xxlb_div" id="vm_recipients_div">
		        <table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
				    <td width="65" align="right"><span class="ck_color">收件人：</span></td>
				    <td width="">
				    	<span class="recipients" id="vm_recipients" style="position: relative;">
				    		${mail.receipt}
			    		</span>
				    </td>
				  </tr>
			    </table> 
	      	</div> 
	  </div>			      
	  <div id="mailContentDiv" class="cklb_kuang" style="word-break:break-all">
	  	${mail.verifyResult}
	  	${mail.content}
	  </div>
	</div>
</div>
</body>
</html>