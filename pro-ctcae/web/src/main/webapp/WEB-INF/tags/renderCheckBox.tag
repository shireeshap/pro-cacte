<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="onclick" %>
<%@ attribute name="onchange" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow propertyName="${propertyName}" displayName="${displayName}" categoryName="checkbox"
                required="${required}" onclick="${onclick}" onchange="${onchange}"/>