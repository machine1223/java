<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<div id="writeMailPage">
<form action="sendMail.action" method="post">
	<jsp:include page="write-mail-menu.jsp"></jsp:include>
    <div class="write-all">	
	    <div id="contactPersonList">
			<table cellpadding="2" cellspacing="2" border="0">
		    	<tbody id="selectPersonAddress"></tbody>
		    </table>
		</div>
		<div id="bg" style="z-index: 199;position: absolute;display: none;">
			<iframe id="bgIframe" frameborder="0"></iframe>
		</div>
        <div id="mail-xxlb-table">    
          <div class="write-side" id="rightMain">
                <table id="write-mail-form" border="0" cellspacing="0" cellpadding="0" class="write-mail-form">
                  <tr>
                    <th>收件人：</th>
                    <td>
                    	<textarea style="height: 50px;overflow-x: hidden;overflow-y: auto;"
                    		class="xx-input focusBackColor"
                    		name="mail.receipt" id="recipients" 
                    	></textarea>
                    </td>
                  </tr>
                  <tr>
                    <th>主&nbsp;&nbsp;题：</th>
                    <td><input name="mail.subject" id="mailSubject" class="xx-subject"/></td>
                  </tr>
                  <tr>                        
                     <th></th>
                     <td>
                     	<table class="none-padlt">
                     		<tr>
                     			<td valign="middle" style="padding-top:3px !important; * pddding-top:0px !important; _padding-top:0px;">
                     				<input type="checkbox" name="mail.signed" value="1"/>是否签名；
                     				<input type="checkbox" name="mail.encrypted" value="1"/>是否加密
	                        	</td>
                   			</tr>
                     	</table>
                     </td> 	                      
                   </tr>
                  </table>
                 <div class="xxlb-kuang">
                     <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td><textarea name="mail.content" id="content" cols="" rows="" class="xxlb-kuang-textarea"></textarea></td>
                        </tr>
                   </table>
                  </div>  
          </div>
            <div style="clear: both;"></div>
        </div>
    </div>
</form>
<div id="addressMouse" style="display: none;">
</div>
</div>