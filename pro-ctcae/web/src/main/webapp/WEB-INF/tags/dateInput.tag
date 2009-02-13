<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@attribute name="path" type="java.lang.String" %>
<%@attribute name="cssClass" type="java.lang.String" %>
<%@attribute name="displayName" type="java.lang.String" %>

<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotShowFormat" type="java.lang.Boolean" %>
<%@ attribute name="dateValue" type="java.util.Date" %>


<c:choose>
    <c:when test="${noForm}">
        <input type="text" id="${id}" class="date ${cssClass}" name="${path}"
               title="${displayName}"
               value="<tags:formatDate value="${dateValue}"/>"/>

    </c:when>
    <c:otherwise>
        <form:input path="${path}" cssClass='date ${cssClass}' title="${displayName}"/>

    </c:otherwise>
</c:choose>
<a href="#" id="${path}-calbutton">
    <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17" height="16" border="0"
         align="absmiddle"/>
</a>
<c:if test="${!doNotShowFormat}">
    <i>(mm/dd/yyyy)</i>
</c:if>