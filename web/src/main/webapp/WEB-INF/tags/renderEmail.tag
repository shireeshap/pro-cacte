<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="size" %>
<%@ attribute name="help" %>
<%@ attribute name="onblur"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:renderRow propertyName="${propertyName}"
                displayName="${displayName}" onblur="${onblur}" categoryName="email"
                required="${required}" help="${help}" size="${size}"
                cssClass="${required ? 'validate-NOTEMPTY&&EMAIL' : 'validate-EMAIL'}"/>
