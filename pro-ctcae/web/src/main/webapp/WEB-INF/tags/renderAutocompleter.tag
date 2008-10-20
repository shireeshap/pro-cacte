<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>
<%@ attribute name="showAllJavascript" %>
<%@attribute name="size" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow propertyName="${propertyName}" displayName="${displayName}" categoryName="autocompleter"
                required="${required}" help="${help}" showAllJavascript="${showAllJavascript}" size="${size}"/>