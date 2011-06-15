<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
</head>
<body>
<spring:message code="fp.forgotPassword" var="forgotPassword" />
<chrome:box title="${forgotPassword}" autopad="true">
    <c:if test="${not empty Message}">
        <spring:message code="${Message}"/>
    </c:if>
</chrome:box>
</body>
</html>



