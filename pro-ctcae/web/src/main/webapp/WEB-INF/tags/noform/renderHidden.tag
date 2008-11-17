<%@ attribute name="propertyValue" required="true" %>
<%@ attribute name="size" %>

<%@ attribute name="propertyName" required="true" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>


<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<div class="value">
    <input id="${propertyName}" type="hidden" value="${propertyValue}"
           title="${displayName}" name="${propertyName}"/>
</div>