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

        <select id="${propertyName}">

            <c:forEach items="${items}" var="item">
                <c:choose>
                    <c:when test="${item.code eq propertyValue}">
                        <option value="${item.code}" selected="selected">${item.desc}</option>
                    </c:when><c:otherwise>
                    <option value="${item.code}">${item.desc}</option>
                </c:otherwise>
                </c:choose>

            </c:forEach>
        </select>
    </div>
</div>



