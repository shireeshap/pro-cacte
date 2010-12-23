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
    <div style="margin-left:0.1em">
        Please enter your username below and press Reset Password.
        We will send a link to your registered email address which must be used to reset the password.
        <br/>
    </div>
    <form method="POST" action="password">
        <b>Username</b>&nbsp;&nbsp;<input type="text" name="username" value=""/>
        <tags:button type="submit" value="Reset Password" color="green"/>
    </form>
    <br>
     <div style="margin-left:0.1em">
        If you don't remember your username you can retrieve it  <a href='<c:url value="forgotusername"/>'>here</a>.
        <br/>
    </div>
    <c:if test="${not empty Message}">
        <b><font color="red"> <spring:message code="${Message}"/></font></b>
    </c:if>
</chrome:box>
</body>
</html>



