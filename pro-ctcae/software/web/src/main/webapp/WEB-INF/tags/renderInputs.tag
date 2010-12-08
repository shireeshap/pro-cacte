<%@ attribute name="uneditable" type="java.lang.Boolean" %>
<%@ attribute name="name" %>
<%@ attribute name="id" %>
<%@ attribute name="dateValue" type="java.util.Date" %>
<%@ attribute name="maxLength" %>

<%@ attribute name="cols" %>
<%@ attribute name="rows" %>
<%@ attribute name="onclick" %>
<%@ attribute name="onchange" %>
<%@attribute name="propertyName" type="java.lang.String" %>
<%@attribute name="itemLabel" type="java.lang.String" %>
<%@attribute name="displayName" type="java.lang.String" %>
<%@attribute name="categoryName" type="java.lang.String" %>
<%@attribute name="defaultValue" type="java.lang.String" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotShowFormat" type="java.lang.Boolean" %>
<%@attribute name="doNotshowClear" type="java.lang.Boolean" %>
<%@attribute name="showIndicator" type="java.lang.Boolean" %>
<%@ attribute name="propertyValue" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<%@ attribute name="values" type="java.util.List" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<%@attribute name="size" %>
<%@attribute name="cssClass" required="true" %>
<%@attribute name="disabled" type="java.lang.Boolean" %>
<%@ attribute name="showAllJavascript" %>
<%@ attribute name="help" type="java.lang.Boolean" %>
<c:set var="title"><spring:message code='${displayName}' text='${displayName}'/></c:set>

<c:choose>
<c:when test="${categoryName == 'text'}">
    <c:choose>
        <c:when test="${noForm}">
            <input name="${propertyName}" type="text" size="${empty size ? attributes.size : size}" title="${title}"
                   class="${cssClass}" value="${propertyValue}" maxlength="${maxLength}"/>
        </c:when>
        <c:otherwise>
            <form:input path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                        title="${title}"
                        cssClass="${cssClass}" maxlength="${maxLength}"/>
        </c:otherwise>

    </c:choose>

</c:when>
<c:when test="${categoryName == 'email'}">
    <form:input path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                title="${title}"
                cssClass="${cssClass}"/>
</c:when>
<c:when test="${categoryName == 'password'}">
    <form:password path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                   title="${title}"
                   cssClass="validate-NOTEMPTY&&MAXLENGTH2000" showPassword="true"/>
</c:when>
<c:when test="${categoryName == 'number'}">
    <form:input path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                title="${title}"
                cssClass="${cssClass}"/>
</c:when>
<c:when test="${categoryName == 'phone'}">
    <form:input path="${propertyName}" disabled="${disabled}" size="${empty size ? attributes.size : size}"
                title="${title}"
                cssClass="${cssClass}"/>

    <span class="phone-number">###-###-####</span>

</c:when>

<c:when test="${categoryName == 'date'}">
    <tags:dateInput path="${propertyName}" displayName="${title}"
                    cssClass="${cssClass}" noForm="${noForm}"
                    dateValue="${dateValue}"
                    doNotShowFormat="${doNotShowFormat}" size="${size}" disabled="${disabled}"/>
</c:when>
<c:when test="${categoryName == 'textarea'}">

    <c:choose>
        <c:when test="${noForm}">
            <textarea id="${propertyName}" cols="${cols}" rows="${rows}"
                      title="${title}" name="${propertyName}">${propertyValue}
            </textarea>

        </c:when>
        <c:otherwise>
            <form:textarea path="${propertyName}" disabled="${disabled}"
                           cols="${cols}"
                           rows="${rows}"
                           title="${title}"
                           cssClass="${cssClass}"/>
        </c:otherwise>
    </c:choose>


</c:when>
<c:when test="${categoryName == 'checkbox'}">
    <c:choose>
        <c:when test="${noForm}">
            <c:if test="${empty values}">
                <input name="${propertyName}" id="${propertyName}" type="checkbox"
                       value="${propertyValue}" onclick="${onclick}"/>
                <input type="hidden" name="_${propertyName}" value="on">
            </c:if>
            <c:if test="${not empty values}">
                <c:set var="_match" value="false"/>
                <c:forEach items="${values}" var="item">
                    <c:if test="${item.code eq propertyValue}">
                        <c:set var="_match" value="true"/>
                    </c:if>
                </c:forEach>
                <input name="${propertyName}" id="${propertyName}" type="checkbox"
                       value="${propertyValue}" onclick="${onclick}" ${_match ? "checked" : " "}/>
                <input type="hidden" name="_${propertyName}" value="on">
            </c:if>

        </c:when>
        <c:otherwise>
            <form:checkbox path="${propertyName}" disabled="${disabled}"
                           onclick="${onclick}" id="${propertyName}"
                           onchange="${onchange}" value="${propertyValue}"/>
        </c:otherwise>
    </c:choose>
</c:when>

<c:when test="${categoryName == 'select'}">
    <c:choose>
        <c:when test="${noForm}">
            <c:set var="enabledisableselect" value="${disabled?'disabled':'enabled'}"/>
            <select id="${not empty propertyName?propertyName:id}" class="${cssClass}" title="${title}"
                    name="${not empty propertyName?propertyName:name}"
                    onchange="${onchange}" ${enabledisableselect}>

                <c:forEach items="${values}" var="item">
                    <c:choose>
                        <c:when test="${item.code eq propertyValue}">
                            <option value="${item.code}" selected="selected">${item.desc}</option>
                        </c:when><c:otherwise>
                        <option value="${item.code}">${item.desc}</option>
                    </c:otherwise>
                    </c:choose>

                </c:forEach>
            </select>

        </c:when>
        <c:otherwise>
            <form:select path="${propertyName}" items="${values}" disabled="${disabled}" title="${title}"
                         cssClass="${cssClass}" itemLabel="desc" itemValue="code" onchange="${onchange}"/>
        </c:otherwise>
    </c:choose>

</c:when>

<c:when test="${categoryName == 'radio'}">
    <c:choose>
        <c:when test="${noForm}">
            <c:forEach items="${values}" var="item" varStatus="status">
                <c:choose>
                    <c:when test="${item.code eq propertyValue}">
                        <input type="radio" class="longselect-radio" name="${propertyName}"
                               id="${propertyName}-radio-${status.index}"
                               value="${item.code}"
                               checked="checked" style="margin:3px"/>${item.desc}
                    </c:when>
                    <c:otherwise>
                        <input type="radio" class="longselect-radio" name="${propertyName}"
                               id="${propertyName}-radio-${status.index}"
                               value="${item.code}"
                               style="margin:3px"/>${item.desc}
                    </c:otherwise>
                </c:choose>
            </c:forEach>

        </c:when>
        <c:otherwise>
            <form:radiobuttons path="${propertyName}" items="${values}" disabled="${disabled}" title="${title}"
                               cssClass="${cssClass}" itemLabel="desc" itemValue="code"/>
        </c:otherwise>
    </c:choose>
</c:when>

<c:when test="${categoryName == 'radiobutton'}">
    <c:choose>
        <c:when test="${noForm}">
            <c:if test="${empty values}">
                <input type="radio" class="longselect-radio" name="${propertyName}" id="${propertyName}"
                       value="${propertyValue}"
                       style="margin:3px" onclick="${onclick}"/>
                <input type="hidden" name="_${propertyName}" value="on">
            </c:if>
            <c:if test="${not empty values}">
                <c:set var="_match" value="false"/>
                <c:forEach items="${values}" var="item">
                    <c:if test="${item.code eq propertyValue}">
                        <c:set var="_match" value="true"/>
                        <c:if test="${item.code eq 'IVRS'}">
                            <c:set var="show_time" value="true"/>
                        </c:if>
                    </c:if>
                </c:forEach>
                <input type="radio" class="longselect-radio" name="${propertyName}" id="${propertyName}"
                       value="${propertyValue}"
                       style="margin:3px" onclick="${onclick}" ${_match ? "checked" : " "}/>
                <input type="hidden" name="_${propertyName}" value="on">
            </c:if>
        </c:when>
        <c:otherwise>
            <form:radiobuttons path="${propertyName}" items="${values}" disabled="${disabled}" title="${title}"
                               cssClass="${cssClass}" itemLabel="desc" itemValue="code"/>
        </c:otherwise>
    </c:choose>


</c:when>

<c:when test="${categoryName == 'selectdomainobject'}">
    <form:select path="${propertyName}" items="${values}" disabled="${disabled}" title="${title}"
                 cssClass="${cssClass}" itemValue="id" onchange="${onchange}" itemLabel="${itemLabel}"/>
</c:when>


<c:when test="${categoryName == 'label'}"><ui:value propertyName="${propertyName}"/></c:when>
<c:when test="${categoryName == 'image'}"><img src="<c:url value="/images/chrome/spacer.gif" />"/></c:when>


<c:when test="${categoryName == 'autocompleter'}">
    <input size="${empty size ? empty attributes.size ? '50' : attributes.size : size}" type="text"
           id="${propertyName}-input" title="${title}" ${disabled ? 'disabled' : ''}
           class="autocomplete ${cssClass}"/>

    <%--<a href="${showAllJavascript}">Show All</a> --%>
    <c:if test="${!doNotshowClear}">
        <input type="image" id="${propertyName}-clear" name="C" value="Clear"
               onClick="javascript:$('${propertyName}-input').clear();$('${propertyName}').clear();return false;"
               src="/proctcae/images/blue/clear-left-button.png"
               style="vertical-align:top;"/>
    </c:if>

    <c:if test="${showIndicator}">

        <tags:indicator id="${propertyName}-indicator"/>
    </c:if>

    <div id="${propertyName}-choices" class="autocomplete" style="display: none"></div>

    <c:choose>
        <c:when test="${noForm}">
            <input id="${propertyName}" class="${cssClass}" type="text" value=""
                   title="${title}" style="display: none;" name="${propertyName}"/>


        </c:when>
        <c:otherwise>
            <form:input path="${propertyName}" id="${propertyName}" cssClass="${cssClass}"
                        title="${title}"
                        cssStyle="display:none;"/>
        </c:otherwise>
    </c:choose>


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
<c:if test="${not noForm}">
    <tags:errors path="${propertyName}"/>
    <tags:errors path="${propertyName}.*"/>
</c:if>
