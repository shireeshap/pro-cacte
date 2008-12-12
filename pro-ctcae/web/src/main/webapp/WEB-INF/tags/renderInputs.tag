<%@ attribute name="cols" %>
<%@ attribute name="onclick" %>
<%@attribute name="propertyName" type="java.lang.String" %>
<%@attribute name="displayName" type="java.lang.String" %>
<%@attribute name="categoryName" type="java.lang.String" %>
<%@attribute name="defaultValue" type="java.lang.String" %>

<%@attribute name="required" type="java.lang.Boolean" %>

<%@ attribute name="values" type="java.util.List" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<%@attribute name="size" %>
<%@attribute name="cssClass" %>
<%@attribute name="disabled" type="java.lang.Boolean" %>
<%@ attribute name="showAllJavascript" %>
<%@ attribute name="help" type="java.lang.Boolean" %>
<c:set var="title"><spring:message code='${displayName}' text='${displayName}'/></c:set>

<c:choose>
    <c:when test="${categoryName == 'text'}">
        <form:input path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                    title="${title}"
                    cssClass="${required ? 'validate-NOTEMPTY&&MAXLENGTH2000' : 'validate-MAXLENGTH2000'}"/>
    </c:when>
    <c:when test="${categoryName == 'email'}">
        <form:input path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                    title="${title}"
                    cssClass="${required ? 'validate-NOTEMPTY&&EMAIL' : 'validate-EMAIL'}"/>
    </c:when>
    <c:when test="${categoryName == 'password'}">
        <form:password path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                       title="${title}"
                       cssClass="validate-NOTEMPTY&&MAXLENGTH2000"/>
    </c:when>
    <c:when test="${categoryName == 'number'}">
        <form:input path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                    title="${title}"
                    cssClass="${required ? 'validate-NOTEMPTY&&NUMERIC' : 'validate-NUMERIC'}"/>
    </c:when>
    <c:when test="${categoryName == 'phone'}">
        <form:input path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                    title="${title}"
                    cssClass="${required ? 'validate-NOTEMPTY&&US_PHONE_NO' : 'validate-US_PHONE_NO'}"/>

        <span class="phone-number">###-###-####</span>

    </c:when>

    <c:when test="${categoryName == 'date'}"><tags:dateInput path="${propertyName}" displayName="${title}"
                                                             cssClass="${required ? 'validate-NOTEMPTY&&DATE' : 'validate-DATE'}"/></c:when>
    <c:when test="${categoryName == 'textarea'}"><form:textarea path="${propertyName}" disabled="${disabled}"
                                                                cols="${not empty cols ? cols : ''}"
                                                                rows="${not empty rows ? rows : ''}"
                                                                title="${title}"
                                                                cssClass="${required ? 'validate-NOTEMPTY&&MAXLENGTH2000' : 'validate-MAXLENGTH2000'}"
            /></c:when>
    <c:when test="${categoryName == 'checkbox'}"><form:checkbox path="${propertyName}" disabled="${disabled}"
                                                                onclick="${onclick}" id="${propertyName}"/></c:when>

    <c:when test="${categoryName == 'select'}">
        <form:select path="${propertyName}" items="${values}" disabled="${disabled}" title="${title}"
                     cssClass="${required ? 'validate-NOTEMPTY' : ''}" itemLabel="desc" itemValue="code"/>
    </c:when>
    <c:when test="${categoryName == 'selectdomainobject'}">
        <form:select path="${propertyName}" items="${values}" disabled="${disabled}" title="${title}"
                     cssClass="${required ? 'validate-NOTEMPTY' : ''}" itemValue="id"/>
    </c:when>


    <c:when test="${categoryName == 'label'}"><ui:value propertyName="${propertyName}"/></c:when>
    <c:when test="${categoryName == 'image'}"><img src="<c:url value="/images/chrome/spacer.gif" />"/></c:when>


    <c:when test="${categoryName == 'autocompleter'}">
        <input size="${empty size ? empty attributes.size ? '50' : attributes.size : size}" type="text"
               id="${propertyName}-input" title="${title}" ${disabled ? 'disabled' : ''}
               class="autocomplete ${required ? 'validate-NOTEMPTY' : ''}"/>

        <%--<a href="${showAllJavascript}">Show All</a> --%>

        <input type="image" id="${propertyName}-clear" name="C" value="Clear"
               onClick="javascript:$('${propertyName}-input').clear();$('${propertyName}').clear();"
			   src="/ctcae/images/blue/clear-left-button.png"
			   style="vertical-align:middle;" />
			   
		<tags:indicator id="${propertyName}-indicator"/>

        <div id="${propertyName}-choices" class="autocomplete" style="display: none"></div>

        <form:input path="${propertyName}" id="${propertyName}" cssClass=" ${required ? 'validate-NOTEMPTY' : ''}"
                    title="${title}"
                    cssStyle="display:none;"/>


    </c:when>

    <c:otherwise>
        UNIMPLEMENTED FIELD TYPE ${categoryName} for ${propertyName}
    </c:otherwise>
</c:choose>
<c:if test="${help}">
    <tags:hoverHelp path="${propertyName}">
        <spring:message code="${propertyName}"
                        text="No help available ${propertyName}"/></tags:hoverHelp>
</c:if>

<tags:errors path="${propertyName}"/>
<tags:errors path="${propertyName}.*"/>
