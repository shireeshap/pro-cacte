<%@ attribute name="name" %>
<%@ attribute name="id" %>
<%@ attribute name="propertyValue" %>
<%@ attribute name="options" type="java.util.List" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="onchange" %>
<%@ attribute name="disabled" %>
<%@ attribute name="onblur" %>

<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotshowLabel" type="java.lang.Boolean" %>


<tags:renderRow values="${options}" propertyName="${propertyName}" displayName="${displayName}"
                categoryName="select" required="${required}"
                cssClass="${required ? 'validate-NOTEMPTY' : ''}"
                noForm="${noForm}" propertyValue="${propertyValue}" doNotshowLabel="${doNotshowLabel}" id="${id}"
                name="${name}" onchange="${onchange}" disabled="${disabled}" onblur="${onblur}"/>


<%--<form:select path="${path}"--%>
<%--items="${options}"--%>
<%--disabled="${disabled}"--%>
<%--title="${title}"--%>
<%--size="${empty size ? '1' : size}"--%>
<%--cssClass="${validationCss} ${cssClass}"/>--%>
