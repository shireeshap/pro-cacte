<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="size" %>
<%@ attribute name="help" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:renderRow propertyName="${propertyName}"
                displayName="${displayName}" categoryName="email"
                required="${required}" help="${help}" size="${size}"/>
