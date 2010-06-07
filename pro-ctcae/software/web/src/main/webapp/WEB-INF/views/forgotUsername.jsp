<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
</head>
<body>
<chrome:box title="Forgot Username" autopad="true">
    <c:choose>
        <c:when test="${showConfirmation}">
            Your username has been emailed to you. You can <a href='<c:url value="login"/>'>login</a> to the system or
            <a href='<c:url value="password"/>'>reset</a> the password using that username.
        </c:when>
        <c:otherwise>
            <div style="margin-left:0.1em">
                Please enter your last name and email address below.
                We will send the username to your registered email address.
                <br/>
            </div>
            <form method="POST" action="forgotusername">
                <table>
                    <tr>
                        <td style="text-align:right"><b>Last name</b></td>
                        <td><input type="text" name="lastName" value=""/></td>
                    </tr>
                    <tr>
                        <td  style="text-align:right"><b>Email</b></td>
                        <td><input type="text" name="email" value=""/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>&nbsp;&nbsp;&nbsp;<tags:button type="submit" value="Submit" color="green"/></td>
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
