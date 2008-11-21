<%@ attribute name="items" type="java.util.List" %>
<%@ attribute name="propertyValue" %>
<%@ attribute name="size" %>

<%@ attribute name="propertyName" required="true" %>
<%@ attribute name="displayName" required="true" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="title"><spring:message code='${displayName}' text='${displayName}'/></c:set>

<div id="${propertyName}-row" class="row">
    <div class="label">
        <label for="${propertyName}">${title}
        </label>
    </div>
    <div class="value">
        <input type="hidden" name="${propertyName}" id="${propertyName}"/>
        <c:forEach items="${items}" var="item">
            <c:choose>
                <c:when test="${item.code eq propertyValue}">
                    <input type="radio" class="longselect-radio" name="${propertyName}-radio" id="${propertyName}-radio"
                           value="${item.code}"
                           checked="checked" style="margin:3px"
                           onclick="updateCrfItemroperties('${item.code}','${propertyName}')"/>${item.desc}
                </c:when><c:otherwise>


                <input type="radio" class="longselect-radio" name="${propertyName}-radio" id="${propertyName}-radio"
                       value="${item.code}"
                       onclick="updateCrfItemroperties('${item.code}','${propertyName}')" style="margin:3px"/>${item.desc}
            </c:otherwise>
            </c:choose>
        </c:forEach>


    </div>
</div>



