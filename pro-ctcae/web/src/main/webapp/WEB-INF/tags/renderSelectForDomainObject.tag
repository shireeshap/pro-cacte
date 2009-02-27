<%@ attribute name="options" type="java.util.List" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="onchange" %>
<%@attribute name="itemLabel" type="java.lang.String" required="true" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<tags:renderRow values="${options}" propertyName="${propertyName}" displayName="${displayName}"
                categoryName="selectdomainobject" required="${required}"
                cssClass="${required ? 'validate-NOTEMPTY' : ''}" onchange="${onchange}" itemLabel="${itemLabel}"/>


<%--<form:select path="${path}"--%>
<%--items="${options}"--%>
<%--disabled="${disabled}"--%>
<%--title="${title}"--%>
<%--size="${empty size ? '1' : size}"--%>
<%--cssClass="${validationCss} ${cssClass}"/>--%>
