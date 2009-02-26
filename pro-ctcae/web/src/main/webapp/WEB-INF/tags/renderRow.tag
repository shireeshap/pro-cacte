<%@ attribute name="dateValue" type="java.util.Date" %>

<%@ attribute name="cols" %>
<%@ attribute name="onclick" %>
<%@ attribute name="onchange" %>
<%@ attribute name="showAllJavascript" %>
<%@ attribute name="help" type="java.lang.Boolean" %>

<%@ attribute name="rows" %>
<%--
    Can render field or a 'label or value'. The preference is given to label, and value attributes, if they are present the field is kind of ignored.
--%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="cssClass" %>
<%@attribute name="style" %>
<%@attribute name="extraParams" %>
<%@attribute name="label" fragment="true" %>
<%@attribute name="value" fragment="true" %>
<%@attribute name="deleteParams" %>

<%@attribute name="propertyName" type="java.lang.String" %>
<%@attribute name="categoryName" type="java.lang.String" %>
<%@attribute name="size" %>

<%@attribute name="displayName" type="java.lang.String" %>
<%@attribute name="required" type="java.lang.Boolean" %>
<%@ attribute name="name" %>
<%@ attribute name="id" %>

<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotshowLabel" type="java.lang.Boolean" %>
<%@attribute name="doNotshowClear" type="java.lang.Boolean" %>
<%@attribute name="doNotShowFormat" type="java.lang.Boolean" %>
<%@ attribute name="propertyValue" %>
<%@ attribute name="values" type="java.util.List" %>

<%--
	Can render field or a 'label or value'. The preference is given to label, and value attributes, if they are present the field is kind of ignored.
--%>
<c:choose>
    <c:when test="${doNotshowLabel}">
        <tags:renderInputs displayName="${displayName}" propertyName="${propertyName}"
                           categoryName="${categoryName}"
                           help="${help}"
                           showAllJavascript="${showAllJavascript}" size="${size}"
                           onclick="${onclick}" values="${values}"
                           cssClass="${cssClass}"
                           dateValue="${dateValue}"
                           cols="${cols}" noForm="${noForm}"
                           propertyValue="${propertyValue}"
                           doNotShowFormat="${doNotShowFormat}"  id="${id}" name="${name}" rows="${rows}" onchange="${onchange}" doNotshowClear="${doNotshowClear}"/>
    </c:when>
    <c:otherwise>
        <div class="row ${cssClass}" id="${propertyName}-row" <c:if test="${not empty style}">style="${style}"</c:if>>

            <div class="label">
                <c:choose>
                    <c:when test="${not empty label}">
                        <jsp:invoke fragment="label"/>
                    </c:when>
                    <c:otherwise><tags:renderLabel displayName="${displayName}" propertyName="${propertyName}"
                                                   propertyType="${categoryName}" required="${required}"
                                                   noForm="${noForm}"/></c:otherwise>
                </c:choose>
            </div>
            <div class="value"><c:choose><c:when test="${not empty value}">
                <jsp:invoke fragment="value"/>
            </c:when>
                <c:otherwise>
                    <tags:renderInputs displayName="${displayName}" propertyName="${propertyName}"
                                       categoryName="${categoryName}"
                                       cssClass="${cssClass}"
                                       help="${help}"
                                       dateValue="${dateValue}"
                                       showAllJavascript="${showAllJavascript}" size="${size}"
                                       onclick="${onclick}" values="${values}"
                                       cols="${cols}" noForm="${noForm}"
                                       propertyValue="${propertyValue}"
                                       doNotShowFormat="${doNotShowFormat}"  id="${id}" name="${name}" rows="${rows}" onchange="${onchange}" doNotshowClear="${doNotshowClear}"/>
                </c:otherwise>
            </c:choose>
                    <%--<tags:extraParams extraParam="${field.attributes.extraParams}"/>--%>
                <c:if test="${field.attributes.enableDelete}"><input type="button" name="delete" value="Delete"
                                                                     onClick="javascript:fireRowDelete(${deleteParams},'${id}','${cssClass}');"/></c:if>
                <c:if test="${not empty extraParams}">
                    ${extraParams}
                </c:if>
            </div>

        </div>
    </c:otherwise>
</c:choose>




