<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="hideErrorDetails" type="java.lang.Boolean" %>
<style>
	ul.errors li{
	    padding-top: 5px;
	}
</style>
<form:errors path="*">
    <div class="errors">
        <c:if test="${not empty messages}">
            <c:if test="${not hideErrorDetails}">
                <ul class="errors">
                    <c:forEach items="${messages}" var="msg">
                        <li>${msg}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </c:if>
    </div>
</form:errors>
