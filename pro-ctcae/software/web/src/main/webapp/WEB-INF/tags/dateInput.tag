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
<%@attribute name="size" %>
<%@attribute name="disabled" %>
<%@attribute name="onchange" %>

 <c:set var="enabledisable" value="${disabled?'disabled':'enabled'}"/>
 
<c:choose>
    <c:when test="${noForm}">
        <input type="text" id="${path}" class="date ${cssClass}" name="${path}"
               title="${displayName}" onchange="${onchange}"
               value="<tags:formatDate value="${dateValue}"/>"
               size="${empty size ? empty attributes.size ? '20' : attributes.size : size}" ${enabledisable}/>

    </c:when>
    <c:otherwise>
        <form:input path="${path}" cssClass='date ${cssClass}' title="${displayName}"
                    size="${empty size ? empty attributes.size ? '20' : attributes.size : size}" disabled="${disabled}"/>

    </c:otherwise>
</c:choose>
<a href="#" id="${path}-calbutton">
    <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17" height="16" border="0"
         align="absmiddle"/>
</a>
<c:if test="${!doNotShowFormat}">
    <i>(mm/dd/yyyy)</i>
</c:if>