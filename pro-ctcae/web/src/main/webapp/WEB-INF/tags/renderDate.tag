<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow propertyName="${propertyName}" displayName="${displayName}" categoryName="date"
                required="${required}"/>