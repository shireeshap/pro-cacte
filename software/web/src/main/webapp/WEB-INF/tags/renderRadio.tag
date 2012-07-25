<%@ attribute name="values" type="java.util.List" %>
<%@ attribute name="cols" %>
<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>

<%@attribute name="noForm" type="java.lang.Boolean" %>
<%@attribute name="doNotShowFormat" type="java.lang.Boolean" %>
<%@ attribute name="propertyValue" %>
<%@ attribute name="useRenderInput" type="java.lang.Boolean" %>
<%@ attribute name="onclick"%>
<%@attribute name="doNotshowLabel" type="java.lang.Boolean" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${useRenderInput}">
        <tags:renderInputs cssClass="${cssClass}" categoryName="radiobutton" propertyName="${propertyName}"
                           displayName="${displayName}" values="${values}" noForm="${noForm}"
                           propertyValue="${propertyValue}" onclick="${onclick}" onchange="${onchange}" help="${help}"/> ${displayName}<br>
    </c:when>
    <c:otherwise>
        <tags:renderRow propertyName="${propertyName}"
                        displayName="${displayName}"
                        values="${values}"
                        categoryName="radio"
                        required="${required}"
                        help="${help}"
                        noForm="${noForm}"
                        propertyValue="${propertyValue}"
                        doNotShowFormat="${doNotShowFormat}"
                        doNotshowLabel="${doNotshowLabel}" cssClass="${required ? 'validate-NOTEMPTY' : ''}"
                />
    </c:otherwise>
</c:choose>