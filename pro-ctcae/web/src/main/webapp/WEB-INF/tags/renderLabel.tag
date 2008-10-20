<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@attribute name="propertyName" type="java.lang.String" %>
<%@attribute name="displayName" type="java.lang.String" %>
<%@attribute name="required" type="java.lang.Boolean" %>
<%@attribute name="propertyType" type="java.lang.String" %>


<c:choose>
    <c:when test="${propertyType == 'autocompleter'}">
        <label for="${propertyName}-input">
            <c:if test="${required}"><tags:requiredIndicator/></c:if>&nbsp;${displayName}
        </label>
    </c:when>
    <c:otherwise>
        <form:label path="${propertyName}">
            <c:if test="${required}"><tags:requiredIndicator/></c:if>&nbsp;${displayName}
        </form:label>
    </c:otherwise>
</c:choose>



