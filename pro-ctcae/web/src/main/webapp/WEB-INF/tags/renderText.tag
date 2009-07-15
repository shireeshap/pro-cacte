<%@ attribute name="size" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="propertyValue" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>
<%@ attribute name="noForm" %>


<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow propertyName="${propertyName}"
                displayName="${displayName}" categoryName="text"
                help="${help}" size="${size}"
                required="${required}"
                cssClass="${required ? 'validate-NOTEMPTY&&MAXLENGTH2000' : 'validate-MAXLENGTH2000'}"
                noForm="${noForm}"
                propertyValue="${propertyValue}"/>
