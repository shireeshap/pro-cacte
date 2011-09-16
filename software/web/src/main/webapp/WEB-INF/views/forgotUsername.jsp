<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
</head>
<body>
<spring:message code="fun.forgotUsername" var="forgotUsername"/>
<chrome:box title="${forgotUsername}" autopad="true">
    <c:choose>
        <c:when test="${showConfirmation}">
        	<spring:message code="fun.confirmation.message.1"/>
        	<a href='<c:url value="login"/>'><spring:message code="fun.confirmation.message.2"/></a>
        	<spring:message code="fun.confirmation.message.3"/>
        	<a href='<c:url value="password"/>'><spring:message code="fun.confirmation.message.4"/></a>
        	<spring:message code="fun.confirmation.message.5"/>
        </c:when>
        <c:otherwise>
            <div style="margin-left:0.1em">
                <spring:message code="fun.instructions"/>
            </div>
            <form method="POST" action="forgotusername">
                <table>
                    <tr>
                        <td style="text-align:right"><b><spring:message code="fun.email"/></b></td>
                        <td><input type="text" name="email" value="" size="37.9"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <spring:message code="submit" var="buttonsubmit"/>
                        <td>&nbsp;&nbsp;&nbsp;<tags:button type="submit" value="${buttonsubmit}" color="green"/></td>
                    </tr>
                </table>
            </form>

            <c:if test="${not empty Message}">
                <b><font color="red"> <spring:message code="${Message}"/></font></b>
            </c:if>
        </c:otherwise>
    </c:choose>
</chrome:box>
</body>
</html>
