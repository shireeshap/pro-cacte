<%@page import="gov.nih.nci.ctcae.web.ControllersUtils" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
</head>
<body>
<chrome:box title="Forgot Password" autopad="true">
    <c:if test="${not empty Message}">
        <b><spring:message code="${Message}"/></b>
    </c:if>
    <br/>
    <br/>
    Please <a href="<c:url value="/"/>">Click here</a> to go to the login page.
</chrome:box>
</body>
</html>



