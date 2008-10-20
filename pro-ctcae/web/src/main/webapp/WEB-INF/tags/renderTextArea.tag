<%@ attribute name="cols" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>


<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow propertyName="${propertyName}"
                displayName="${displayName}" categoryName="textarea"
                required="${required}" help="${help}" cols="${cols}"/>
