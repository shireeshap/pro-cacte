<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>


<html>
<head>

</head>
<body>

<chrome:box title="Access Denied" autopad="true">

    <div class="row">
        <div class="label">
            <ul class="errors">
                Account Locked:
            </ul>
        </div>
        <div class="value">
            Your account has been locked due to multiple unsuccessful login attempts.
            You can unlock your account by generating a new password using 'Forgot Password' link on the <a
                href="<c:url value="/"/>">login</a> page.
        </div>
    </div>
</chrome:box>

</body>

</html>