<%@ attribute name="values" type="java.util.List" %>
<%@ attribute name="cols" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>

<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotShowFormat" type="java.lang.Boolean" %>
<%@ attribute name="propertyValue" %>

<%@attribute name="doNotshowLabel" type="java.lang.Boolean" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow propertyName="${propertyName}"
                displayName="${displayName}"
                values="${values}"
                categoryName="radio"
                required="${required}"
                help="${help}"
                noForm="${noForm}"
                propertyValue="${propertyValue}"
                doNotShowFormat="${doNotShowFormat}"
                doNotshowLabel="${doNotshowLabel}" cssClass="${required ? 'validate-NOTEMPTY' : ''}"
        />
