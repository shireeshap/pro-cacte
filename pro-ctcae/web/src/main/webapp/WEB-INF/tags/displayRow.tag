<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="categoryName" %>
<%@ attribute name="name" %>
<%@ attribute name="id" %>

<%@ attribute name="values" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="cssClass" %>
<%@attribute name="style" %>
<%@attribute name="extraParams" %>
<%@attribute name="label" fragment="true" %>
<%@attribute name="value" fragment="true" %>
<%@attribute name="deleteParams" %>


<div class="row ${cssClass}" id="${id}-row" <c:if test="${not empty style}">style="${style}"</c:if>>
    <c:choose>
        <c:when test="${not empty label}">
            <jsp:invoke fragment="label"/>
        </c:when>
        <c:otherwise><tags:displayLabel displayName="${displayName}"
                                        required="${required}"/></c:otherwise>
    </c:choose>
    <div class="value"><c:choose><c:when test="${not empty value}">
        <jsp:invoke fragment="value"/>
    </c:when><c:otherwise><tags:displayInputs id="${id}" displayName="${displayName}"
                                              required="${required}"
                                              categoryName="${categoryName}" name="${name}"
                                              values="${values}"/></c:otherwise></c:choose>
    </div>

</div>

