<%@ attribute name="flashMessage" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="key" %>
<c:if test="${not empty flashMessage}">
    <div id="flash-message" class="${empty flashMessageClass ? 'info' : flashMessageClass}">
            <spring:message code='${flashMessage}' text='${flashMessage}'/>
    </div>
</c:if>
<c:if test="${not empty key}">
    <div id="flash-message" class="${empty flashMessageClass ? 'info' : flashMessageClass}">
            ${requestScope[key]}
    </div>
</c:if>