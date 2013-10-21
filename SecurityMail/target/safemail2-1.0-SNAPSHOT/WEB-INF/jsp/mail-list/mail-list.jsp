<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
	#mailList tr{
		cursor: pointer;
	}
</style>
<div class="mpage" id="mpage">
	<table class="mail_li" border="0" cellspacing="0" cellpadding="0">
				<thead class="mail-box-title">
					<tr>
						<th class="i1" style="border-left: 1px solid #ccc;"><font>类型</font></th>
						<th class="i3"><font>发件人</font></th>
						<th class="i5"><font>主题</font></th>
						<th class="i6"><font>时间</font></th>
					</tr>
				</thead>
				<tbody id="mailList">
					<s:iterator value="mails">
						<tr>
						<td class="i1" style="border-left: 1px solid #ccc;"><font>${typt}</font></td>
						<td class="i3"><font>${from}</font></td>
						<td class="i5"><font><a target="_blank" href="viewMail.action?mail.msgNum=${msgNum}">${subject}</a></font></td>
						<td class="i6"><font><s:date name="date" format="yyyy-MM-dd HH:mm:ss"/></font></td>
					</tr>
					</s:iterator>
				</tbody>
			</table>
</div>