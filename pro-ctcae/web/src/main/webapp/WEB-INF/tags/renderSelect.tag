<%@ attribute name="propertyValue" %>
<%@ attribute name="options" type="java.util.List" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>


<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotshowLabel" type="java.lang.Boolean" %>


<tags:renderRow values="${options}" propertyName="${propertyName}" displayName="${displayName}"
                categoryName="select" required="${required}"
                cssClass="${required ? 'validate-NOTEMPTY' : ''}"
                noForm="${noForm}" propertyValue="${propertyValue}" doNotshowLabel="${doNotshowLabel}" />


<%--<form:select path="${path}"--%>
<%--items="${options}"--%>
<%--disabled="${disabled}"--%>
<%--title="${title}"--%>
<%--size="${empty size ? '1' : size}"--%>
<%--cssClass="${validationCss} ${cssClass}"/>--%>
