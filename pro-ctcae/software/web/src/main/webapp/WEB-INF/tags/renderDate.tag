<%@ attribute name="dateValue" type="java.util.Date" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="size" %>
<%@ attribute name="disabled" %>
<%@ attribute name="onchange" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotShowFormat" type="java.lang.Boolean" %>

<%@attribute name="doNotshowLabel" type="java.lang.Boolean" %>


<tags:renderRow propertyName="${propertyName}" displayName="${displayName}" categoryName="date"
                required="${required}" noForm="${noForm}"
                dateValue="${dateValue}" onchange="${onchange}"
                doNotShowFormat="${doNotShowFormat}" doNotshowLabel="${doNotshowLabel}"
                cssClass="${required ? 'validate-NOTEMPTY&&DATE&DATE_REGEX' : 'validate-DATE'}" size="${size}" disabled="${disabled}"/>