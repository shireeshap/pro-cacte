<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="key" %>
<c:if test="${not empty flashMessage}">
    <div id="flash-message" class="${empty flashMessageClass ? 'info' : flashMessageClass}">
        <spring:message code='${flashMessage}' text=''/>
    </div>
</c:if>
<c:if test="${not empty key}">
    <div id="flash-message" class="${empty flashMessageClass ? 'info' : flashMessageClass}">
        <spring:message code='${requestScope[key]}' text=''/>
    </div>
</c:if>