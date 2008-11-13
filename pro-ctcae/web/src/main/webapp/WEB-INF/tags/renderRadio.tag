<%@ attribute name="propertyName" %>
<%@ attribute name="required" %>
<%@ attribute name="defaultValue" %>
<%@ attribute name="displayName" %>

<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow propertyName="${propertyName}" categoryName="radio" required="${required}" defaultValue="${defaultValue}" displayName="${displayName}"></tags:renderRow>


<%--<form:select path="${path}"--%>
<%--items="${options}"--%>
<%--disabled="${disabled}"--%>
<%--title="${title}"--%>
<%--size="${empty size ? '1' : size}"--%>
<%--cssClass="${validationCss} ${cssClass}"/>--%>
