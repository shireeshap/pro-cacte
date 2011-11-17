<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="code" required="true" %>
<%@attribute name="heading" %>
<div class="instructions">
    <div class="summarylabel">
        <c:if test="${ not empty heading}">
            ${heading}
        </c:if>
        <c:if test="${empty heading}">
            <spring:message code="instructions"/>
        </c:if>
    </div>
    <div class="summaryvalue"><spring:message code="${code}" text="There are no instructions for this section."/></div>
</div>