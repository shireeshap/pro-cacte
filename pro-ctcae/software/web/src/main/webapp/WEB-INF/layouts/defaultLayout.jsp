<%-- This is the standard decorator for all caAERS pages --%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>

<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<%--<link type="text/css" rel="stylesheet" href="<c:url value="/resources/dijit/themes/tundra/tundra.css" />"/>--%>

	<%--<script type="text/javascript" src="<c:url value="/resources/dojo/dojo.js" />"></script>--%>
	<%--<script type="text/javascript" src="<c:url value="/resources/spring/Spring.js" />"></script>--%>
	<%--<script type="text/javascript" src="<c:url value="/resources/spring/Spring-Dojo.js" />"></script>--%>

	<title>PRO-CTCAE</title>
	<link rel="shortcut icon" href="<chrome:imageUrl name='../favicon.ico'/>"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<standard:head/>
	<decorator:head/>
</head>
<body>
<div id="all">
	<standard:header/>

	<tiles:insertAttribute name="body"/>

	<div class="tabpane">

		<c:set var="__decorator_title">
			<decorator:title/>
		</c:set>
		<blue:body title="${__decorator_title}">
			<chrome:flashMessage/>
			<decorator:body/>
		</blue:body>

		<standard:footer/>


		<c:set var="__decorator_title">
			<decorator:title/>
		</c:set>
	</div>
</div>
</body>
</html>


