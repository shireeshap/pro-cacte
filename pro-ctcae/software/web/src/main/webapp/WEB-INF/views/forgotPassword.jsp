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
        <br/>
        We will generate a new password and send it to your registered email addess.
        <br/>
        <br/>
    </div>
    <c:if test="${not empty Message}">
        <b><spring:message code="${Message}"/></b>
    </c:if>
    <form method="POST" action="password">
        <div class="row">
            <div class="label">Username</div>
            <div class="value"><input type="text" name="username" value=""/>
                <tags:button type="submit" value="Reset Password" color="green"/>
            </div>
        </div>
    </form>
</chrome:box>
</body>
</html>



