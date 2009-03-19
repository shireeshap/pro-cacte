<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/ctcae/tags" %>

<html>
<head>
</head>
<body>
<chrome:box title="Initial setup complete" autopad="true">
    <p>
        Initial setup of this ProCtCAE instance is complete. For security reasons, you can't
        repeat this initial setup. You can now login by <ctcae:publicAuthorize>
        <a href="<c:url value="/public/login"/>">clicking here.</a>

    </ctcae:publicAuthorize>


    </p>
</chrome:box>
</body>
</html>