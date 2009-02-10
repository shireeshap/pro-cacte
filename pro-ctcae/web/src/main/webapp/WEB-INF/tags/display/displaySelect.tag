<%@ attribute name="items" type="java.util.List" required="true" %>
<%@ attribute name="propertyValue" required="true" %>
<%@ attribute name="size" %>

<%@ attribute name="propertyName" required="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


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