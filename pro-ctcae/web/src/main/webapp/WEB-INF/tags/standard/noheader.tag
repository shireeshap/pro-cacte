<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/ctcae/tags" %>

<div id="header">

    <div class="background-R">
        <a href="/ctcae/pages/form/createForm" id="logo">ProCtcAE</a>
        <div id="taskbar">Welcome <b><authz:authentication property="name"></authz:authentication></b></div>
        <ctcae:urlAuthorize url="/pages/j_spring_security_logout">
            <a id="logout" href="<c:url value="/pages/j_spring_security_logout"/>">Log out</a>
        </ctcae:urlAuthorize>
        
    </div>
</div>
<!-- end header -->
