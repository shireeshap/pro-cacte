<%@ attribute name="cols" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>

<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotShowFormat" type="java.lang.Boolean" %>
<%@ attribute name="propertyValue" type="java.util.Date" %>

<%@attribute name="doNotshowLabel" type="java.lang.Boolean" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow propertyName="${propertyName}"
                displayName="${displayName}"
                categoryName="textarea"
                required="${required}"
                help="${help}"
                cssClass="${required ? 'validate-NOTEMPTY&&MAXLENGTH2000' : 'validate-MAXLENGTH2000'}"
                cols="${not empty cols ? cols : '30'}"
                rows="${not empty rows ? rows : '2'}"
                noForm="${noForm}"
                propertyValue="${propertyValue}"
                doNotShowFormat="${doNotShowFormat}" doNotshowLabel="${doNotshowLabel}"/>
