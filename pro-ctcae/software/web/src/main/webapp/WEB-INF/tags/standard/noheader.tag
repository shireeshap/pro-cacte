<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<style>
body {
	background-position:center -98px;
}
#taskbar {
	padding-top:11px;
}
#main{
	top:40px;
}
#taskbar{
	top:0;
}
#logout, #home {
	z-index:2;
}
</style>
<div id="header">

    <div class="background-R">
        <%--<a href="/proctcae" id="logo">ProCtcAE</a>--%>
		
        <div id="taskbar">
        <table width="100%">
        	<tr>
	        	<td width="60%"><spring:message code='label.welcome'/>&nbsp;<b><authz:authentication property="name"></authz:authentication></b>
	        	</td>
	        	<td></td>
        	</tr>
        </table>
        </div>
        <a href="/proctcae" id="home"><spring:message code='label.home'/></a>
        <proctcae:urlAuthorize url="/pages/j_spring_security_logout">
            <a id="logout" href="<c:url value="/pages/j_spring_security_logout"/>"><spring:message code='label.logout'/></a>
        </proctcae:urlAuthorize>
    </div>
</div>
<!-- end header -->
