<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<html>
<head>
</head>
<body>
<chrome:box title="Initial setup complete" autopad="true">
    <p>
        Initial setup of this ProCtCAE instance is complete. For security reasons, you can't
        repeat this initial setup. You can now login by
        <a href="<c:url value="/public/login"/>">clicking here.</a>
    </p>
</chrome:box>
</body>
</html>