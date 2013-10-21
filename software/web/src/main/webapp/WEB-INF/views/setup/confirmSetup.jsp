<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<head>
</head>
<body>
<chrome:box title="Initial setup complete" autopad="true">
    <p>
        <tags:message code="setup.invalid.msg1" />
        <a href="<c:url value="/public/login"/>"><tags:message code="setup.invalid.msg2" /></a>
    </p>
</chrome:box>
</body>
</html>