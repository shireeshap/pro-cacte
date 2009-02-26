<%@ attribute name="dateValue" type="java.util.Date" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="size" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotShowFormat" type="java.lang.Boolean" %>

<%@attribute name="doNotshowLabel" type="java.lang.Boolean" %>


<tags:renderRow propertyName="${propertyName}" displayName="${displayName}" categoryName="date"
                required="${required}" noForm="${noForm}"
                dateValue="${dateValue}"
                doNotShowFormat="${doNotShowFormat}" doNotshowLabel="${doNotshowLabel}"
                cssClass="${required ? 'validate-NOTEMPTY&&DATE' : 'validate-DATE'}" size="${size}"/>