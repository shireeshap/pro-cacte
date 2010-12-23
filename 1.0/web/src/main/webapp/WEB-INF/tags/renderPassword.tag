<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="categoryName" %>
<%@ attribute name="required" %>

<%@attribute name="size" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:renderRow propertyName="${propertyName}" displayName="${displayName}" categoryName="password"
                required="${required}" help="${help}" size="${size}"/>    