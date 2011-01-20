<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<html>
<head>

</head>
<body>


<chrome:box title="Thank you">
    <br/>


    <div id="flash-message" class="${empty flashMessageClass ? 'info' : flashMessageClass}">
        Thanks you for your signup. You can now login by <a href="<c:url value="/public/login"/>">clicking here</a>.
    </div>


</chrome:box>


</body>
</html>