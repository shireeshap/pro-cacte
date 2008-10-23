<%@ attribute name="size" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="help" type="java.lang.Boolean" %>

<%@ attribute name="required" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow propertyName="${propertyName}" displayName="${displayName}" categoryName="phone"
                required="${required}" help="${help}" size="${size}"/> 

