<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="code" required="true" %>
<%@attribute name="codeType" required="true" %>
<c:choose>
    <c:when test="${codeType eq 'condition'}">
        <c:choose>
            <c:when test="${code eq '>'}">
                is greater than
            </c:when>
            <c:when test="${code eq '>='}">
                is greater than or equal to
            </c:when>
            <c:when test="${code eq '=='}">
                is equal to
            </c:when>
            <c:when test="${code eq '<='}">
                is less than or equal to
            </c:when>
            <c:when test="${code eq '<'}">
                is less than
            </c:when>
        </c:choose>
    </c:when>
    <c:when test="${codeType eq 'notify'}">
        <c:choose>
            <c:when test="${code eq 'PrimaryPhysician'}">
                Treating physician
            </c:when>
            <c:when test="${code eq 'PrimaryNurse'}">
                Nurse
            </c:when>
            <c:when test="${code eq 'SiteCRA'}">
                Site CRA
            </c:when>
            <c:when test="${code eq 'SitePI'}">
                Site PI
            </c:when>
            <c:when test="${code eq 'LeadCRA'}">
                Lead CRA
            </c:when>
            <c:when test="${code eq 'PI'}">
                Overall PI
            </c:when>
        </c:choose>
    </c:when>
</c:choose>
