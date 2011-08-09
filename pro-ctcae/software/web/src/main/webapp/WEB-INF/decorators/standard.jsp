<%-- This is the standard decorator for all caAERS pages --%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<head>
	<standard:head/>
    <title>ProCtcAE</title>
    <link rel="icon" href="../../images/ctcae.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <tags:stylesheetLink name="tabbedflow"/>

    <decorator:head/>
</head>
<body>
<div id="proctcae-background"></div>
<div id="all">
    <standard:header/>
    <div class="tabpane">

        <c:set var="__decorator_title">
            <decorator:title/>
        </c:set>
        <blue:body title="${__decorator_title}">
            <decorator:body/>
        </blue:body>

        <standard:footer/>


        <c:set var="__decorator_title">
            <decorator:title/>
        </c:set>
        <!--THE FOLLOWING LINE CLEARS THE .tabpane DIV SO IT WILL STRETCH TO FIT IT'S CONTENT-->
        <hr style="margin: -0.66em 0pt 160px; display: block; clear: left; visibility:hidden;"/>
    </div>
</div>
</body>
</html>


