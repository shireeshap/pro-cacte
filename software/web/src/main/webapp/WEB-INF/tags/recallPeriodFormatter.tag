<%@ attribute name="desc" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:choose>
    <c:when test="${fn:contains(desc,'past' )}">
    <c:set var="tenure" value="${fn:substring(desc,0,fn:indexOf(desc,'over')+4 )}${fn:substring(desc,fn:indexOf(desc,'over')+4,fn:length(desc) )}" />
    </c:when>
    <c:when test="${fn:contains(desc,'last cancer treatment' )}">
        <c:set var="tenure" value="${fn:substring(desc,0,fn:indexOf(desc,'since')+5 )}${fn:substring(desc,fn:indexOf(desc,'since')+5,fn:length(desc) )}" />
    </c:when>

</c:choose>

<c:choose>
	<c:when test="${fn:contains(tenure,'7 days')}">
		<tags:message code="past7days.I"/><b><u><tags:message code="past7days.II"/></u></b>
	</c:when>
	<c:when test="${fn:contains(tenure,'30 days')}">
		<tags:message code="past30days.I"/><b><u><tags:message code="past30days.II"/></u></b>
	</c:when>
	<c:when test="${fn:contains(tenure,'2 weeks')}">
		<tags:message code="past2weeks.I"/><b><u><tags:message code="past2weeks.II"/></u></b>
	</c:when>
	<c:when test="${fn:contains(tenure,'3 weeks')}">
		<tags:message code="past3weeks.I"/><b><u><tags:message code="past3weeks.II"/></u></b>
	</c:when>
	<c:when test="${fn:contains(tenure,'4 weeks')}">
		<tags:message code="past4weeks.I"/><b><u><tags:message code="past4weeks.II"/></u></b>
	</c:when>
	<c:otherwise>
		<tags:message code="lastTreatment.I"/><b><u><tags:message code="lastTreatment.II"/></u></b>
	</c:otherwise>
</c:choose>


<%--<b>${desc}</b>--%>