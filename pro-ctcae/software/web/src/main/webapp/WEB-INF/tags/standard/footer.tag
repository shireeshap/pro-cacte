<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<tags:ssoForm/>
<c:if test="${configuration.map.showDebugInformation}">
    <tags:debugInfo/>
</c:if>
<spring:message var="version" code="version"/>
<spring:message var="date" code="build.date" />
<c:choose>
    <c:when test="${fn:contains(version, 'SNAPSHOT')}">
         <div id="build-name">${version} ${date} </div>
    </c:when>
    <c:otherwise>
         <div id="build-name">${version} ${date} </div>
    </c:otherwise>
</c:choose>

