<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div id="messageListDiv">
<div class="btndiv">
	<notification:search></notification:search>
	<div class="btn">
		<button class="button button-tbl gray" type="button"
			id="addMessageTemplate">
			<i class="icn_action_add"></i><spring:message code="button.add"/>
		</button>
	</div>

</div>
<form:form method="post" modelAttribute="messageTemplate" align="left">
	<div class="jqgrid">
		<table id="messagetemplate_gridTable"></table>
		<div id="messagetemplate_gridPager"></div>
	</div>
</form:form>
</div>
<script type="text/javascript">
    $(function() {
        var view = EB_View.crossOrgNotification.message.listApp.getInstance({el:$("#messageListDiv")});
        view.render();
//        EB_View.crossOrgNotification.messages.initializeListPage();
    });
</script>