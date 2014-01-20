<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- set variable for current page --%>
<c:set var="lang" value="${util:getCurrentLocale(pageContext.request)}"/>
<head>
</head>
<div id="ajax_bg" class="ajax-bg"></div>
<div id="ajax_loading" class="ajax-loading"><p>Working</p></div>
<div id="wrapper">
    <jsp:include page="header.jsp"></jsp:include>
    <div id="main">
        <tiles:insertAttribute name="body" />
    </div>
    <jsp:include page="footer.jsp"></jsp:include>
</div>
</body>
</html>
