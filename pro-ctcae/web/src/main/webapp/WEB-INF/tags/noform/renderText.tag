<%@ attribute name="propertyValue" %>
<%@ attribute name="size" %>

<%@ attribute name="propertyName" required="true" %>
<%@ attribute name="displayName" required="true" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>


<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<div id="${propertyName}-row" class="row">
    <div class="label">
        <label for="${propertyName}">${displayName}
        </label>
    </div>
    <div class="value">
        <input id="${propertyName}" type="text" size="50" value="${propertyValue}"
               title="${displayName}" name="${propertyName}"/>
    </div>
</div>