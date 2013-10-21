<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!--工具栏-->
<div class="lb-tit">
  <div class="tit-side">
  	<input type="submit" style="margin-top: 5px;margin-left: 10px;" value="发送"/>
  </div>
</div>
<!--工具栏-->
<div id="sendSuccess" style="display: none">
	<div>
		<div class="lb-tit">&nbsp;</div>
		<div class="ls-text">
		  <table border="0" cellspacing="0" cellpadding="0">
		    <tr>
		      <td valign="top">
		     	  <h2>邮件发送成功<font>此邮件已经成功保存至已发送邮件夹</font></h2>
		        <p><input type="button" onclick="recevieMails('收件箱')" value="返回收件箱" class="input_return" />
		        <input name="" type="button" onclick="writeMail()" value="继续写信" class="input_write" />
		        </p>
		      </td>
		    </tr>
		  </table>
		</div>
	</div>
</div>