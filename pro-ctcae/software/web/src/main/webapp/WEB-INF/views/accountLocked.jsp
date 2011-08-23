<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>

</head>
<body>
<spring:message code="user.accessdenied" var="userAccessDenied"/>
<chrome:box title="${userAccessDenied}" autopad="true">

    <div class="row">
        <div class="label">
            <ul class="errors">
                <tags:message code="user.accountlocked.title"/>
            </ul>
        </div>
        <div class="value">
            <tags:message code="user.accountlocked.message1"/>
            <tags:message code="user.accountlocked.message2"/>
            <a href="<c:url value="/"/>"><tags:message code="fun.confirmation.message.2"/></a><tags:message code="user.accountlocked.message3"/>
        </div>
    </div>
</chrome:box>

</body>

</html>