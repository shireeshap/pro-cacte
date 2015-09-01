<%@ attribute name="propertyName" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="onclick" %>
<%@ attribute name="onchange" %>
<%@ attribute name="propertyValue" %>
<%@ attribute name="noForm" %>
<%@ attribute name="useRenderInput" type="java.lang.Boolean" %>
<%@attribute name="cssClass" %>
<%@attribute name="values" type="java.util.List" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${useRenderInput}">
        <tags:renderInputs cssClass="${cssClass}" categoryName="checkbox" propertyName="${propertyName}" values="${values}" 
                           displayName="${displayName}" propertyValue="${propertyValue}" onclick="${onclick}" onchange="${onchange}" noForm="${noForm}"/> ${displayName} <br>

    </c:when>
    <c:otherwise>
        <tags:renderRow propertyName="${propertyName}" displayName="${displayName}" categoryName="checkbox"
                        required="${required}" onclick="${onclick}" onchange="${onchange}"
                        propertyValue="${propertyValue}" noForm="${noForm}"/>
    </c:otherwise>
</c:choose>