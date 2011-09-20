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
    <div style="margin-left:0.1em">
        <spring:message code="fp.instructions" />
        <br/>
    </div>
    <form method="POST" action="password">
        <b><spring:message code="login.username" /></b>&nbsp;&nbsp;<input type="text" name="username" value=""/>
        <spring:message code="fp.submit" var="fpSubmit" />
        <tags:button type="submit" value="${fpSubmit}" color="green"/>
    </form>
    <br>
     <div style="margin-left:0.1em">
        <spring:message code="fp.message.1" />&nbsp;<a href='<c:url value="forgotusername"/>'><spring:message code="fp.message.2" /></a><spring:message code="fp.message.3" />
        <br/>
    </div>
    <c:if test="${not empty Message}">
        <b><font color="red"> <spring:message code="${Message}"/></font></b>
    </c:if>
</chrome:box>
</body>
</html>



