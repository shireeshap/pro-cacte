<%@ attribute name="displayName" %>
<%@ attribute name="required" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<div class="label">
    <c:if test="${required}"><tags:requiredIndicator/></c:if>&nbsp;${displayName}
</div>
