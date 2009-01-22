<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="onClick" required="false" %>
<%@ attribute name="icon" required="false" %>
<%@ attribute name="value" %>
<%@ attribute name="href" required="false" %>
<%@ attribute name="cssClass" required="false" %>
<a class="ctcae-button ${cssClass}" href="${href}" id="${id}" onClick="${onClick}"><img src="
    <c:if test="${icon=='save'}">
        <tags:imageUrl name="disk_icon.png"/>
    </c:if>
    <c:if test="${icon=='add'}">
        <tags:imageUrl name="add_icon.png"/>
    </c:if>
    <c:if test="${icon=='page'}">
        <tags:imageUrl name="page_icon.png"/>
    </c:if>
    <c:if test="${icon=='window'}">
        <tags:imageUrl name="window_icon.png"/>
    </c:if>" alt="" /> ${value}
    <div class="right"></div>
</a>
