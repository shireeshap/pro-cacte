<%@ attribute name="propertyValue" %>
<%@ attribute name="size" %>

<%@ attribute name="propertyName" required="true" %>
<%@ attribute name="displayName" required="true" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>


<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="title"><spring:message code='${displayName}' text=''/></c:set>


<div id="${propertyName}-row" class="row">
    <div class="label">
        <label for="${propertyName}">${title}
        </label>
    </div>
    <div class="value">
        <textarea id="${propertyName}" cols="30" rows="2"
                  title="${title}" name="${propertyName}">${propertyValue}</textarea>
    </div>
</div>