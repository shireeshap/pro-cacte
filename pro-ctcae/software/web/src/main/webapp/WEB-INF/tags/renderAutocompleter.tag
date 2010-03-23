<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>
<%@ attribute name="showAllJavascript" %>
<%@attribute name="size" %>
<%@attribute name="showIndicator" type="java.lang.Boolean" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotshowClear" type="java.lang.Boolean" %>
<%@ attribute name="propertyValue" %>

<%@attribute name="doNotshowLabel" type="java.lang.Boolean" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<tags:renderRow propertyName="${propertyName}" displayName="${displayName}" categoryName="autocompleter"
                cssClass="${required ? 'validate-NOTEMPTY' : ''} autocompleter" help="${help}"
                showAllJavascript="${showAllJavascript}" size="${size}"
                noForm="${noForm}"
                required="${required}"
                propertyValue="${propertyValue}"
                doNotshowLabel="${doNotshowLabel}" doNotshowClear="${doNotshowClear}" showIndicator="${showIndicator}"/>