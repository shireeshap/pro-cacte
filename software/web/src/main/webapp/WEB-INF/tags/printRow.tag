<%@ attribute name="value" %>
<%@ attribute name="title" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="cssClass" %>
<%@attribute name="style" %>
<%@attribute name="extraParams" %>
<%@attribute name="label" fragment="true" %>


<div class="row ${cssClass}" id="${id}-row" <c:if test="${not empty style}">style="${style}"</c:if>>
    <tags:displayLabel displayName="${title}"
                       required="${required}"/>
    <div class="value">
        ${value}

    </div>

</div>

