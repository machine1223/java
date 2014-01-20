<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div id="templateListDiv">
    <div class="clearfix">
        <div class="btndiv left">
            <button class="button button-tbl gray btn_disabled canDisabled" disabled="disabled" type="button" id="sendBtn">
                <i class="icn_sentasevent"></i>
                <spring:message code="button.send" htmlEscape="false" />
            </button>
            <label id="errMessage"><font color="red"></font></label>
        </div>
        <notification:search />
    </div>
    <div class="jqgrid">
        <table id="bc_grid_table"></table>
        <div id="bc_grid_pager"></div>
    </div>
    <div id="dialog" style="display: none;" title="<spring:message code='broadcasttemplate.text.sendasanevent' htmlEscape='false' />">
        <form action="" id="withEventForm" name="withEventForm">
        <div class="padding10 margin100-L" style="overflow: hidden;">
            <div><label class="margin5_L" for="nosend"><input type="radio" name="needEvent" value="no" id="nosend"><spring:message code='broadcasttemplate.text.sendasanevent.nosend' htmlEscape='false' /></label></div>
            <div><label class="margin5_L" for="yessend"><input type="radio" name="needEvent" value="yes" checked id="yessend"><spring:message code='broadcasttemplate.text.sendasanevent.yessend' htmlEscape='false' /></label></div>
            <div class="margin20-L margin10-T" id="event_name_div">
                <%--<input id="eventName" name="event" type="hidden" value="" />--%>
                <select id="event" name="event_source" maxlength="120">
                    <option value=""></option>
                    <c:forEach var="event" items="${events}">
                        <option value="${ event.id}"><c:out value="${event.name}"/></option>
                    </c:forEach>
                </select>
                <input id="eventName" class="margin25-L input-invisible {maxlength:120}" type="text" name="event">
            </div>
        </div>
        </form>
    </div>
</div>
<script type="text/javascript">
    $(function() {
        var view = EB_View.crossOrgNotification.template.listApp.getInstance({el:$("#templateListDiv")});
//        EB_View.crossOrgNotification.bcTemplates.initialize();
    });
</script>
