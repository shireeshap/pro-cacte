<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="required" type="java.lang.Boolean" %>
<%@attribute name="sortDir" required="true" %>
<%@attribute name="sort" required="true" %>
<%@attribute name="title" required="true" %>
<%@attribute name="name" required="true" %>

<td class="tableHeader sortable" onclick="sortResults('${name}','${sortDir}')">${title}
<c:if test="${sort eq name}">
<c:if test="${sortDir eq 'asc'}"><img src="../../images/arrow-up.gif"></c:if>
    <c:if test="${sortDir eq 'desc'}"><img src="../../images/arrow-down.gif"></c:if>
</c:if></td>