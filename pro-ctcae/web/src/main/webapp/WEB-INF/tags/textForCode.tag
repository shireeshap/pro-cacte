<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="code" required="true" %>
<c:choose>
    <c:when test="${code eq 'gt'}">
        is greater than
    </c:when>
    <c:when test="${code eq 'ge'}">
        is greater than or equal to
    </c:when>
    <c:when test="${code eq 'eq'}">
        is equal to
    </c:when>
    <c:when test="${code eq 'le'}">
        is less than or equal to
    </c:when>
    <c:when test="${code eq 'lt'}">
        is less than
    </c:when>
</c:choose>