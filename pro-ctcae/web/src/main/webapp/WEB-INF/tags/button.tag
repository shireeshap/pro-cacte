<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="onClick" required="false" %>
<%@ attribute name="icon" required="false" %>
<%@ attribute name="value" %>
<%@ attribute name="href" required="false" %>
<%@ attribute name="cssClass" required="false" %>
<%@ attribute name="type" required="true" %>
<c:if test="${type=='anchor'}">
    <a class="ctcae-button ${cssClass}"
        <c:if test="${href}">
            href="${href}"
        </c:if>
        id="${id}" 
        <c:if test="${onClick}">
            href="${onClick}"
        </c:if>>
        <c:if test="${icon=='save'||icon=='Save'}">
            <img src="<tags:imageUrl name="disk_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='back'}">
            <img src="<tags:imageUrl name="back_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='Save &amp; Back'}">
            <img src="<tags:imageUrl name="blue/saveback_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='add'}">
            <img src="<tags:imageUrl name="add_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='page'}">
            <img src="<tags:imageUrl name="page_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='window'}">
            <img src="<tags:imageUrl name="window_icon.png"/>" alt="" />
        </c:if>${value}
        <c:if test="${icon=='Save & Continue'}">
            <img src="<tags:imageUrl name="blue/savecontinue_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='Continue'}">
            <img src="<tags:imageUrl name="blue/continue_icon.png"/>" alt="" />
        </c:if>
        <div class="right">
        </div>
    </a>
</c:if>
<c:if test="${type=='button'}">
    <button class="ctcae-button ${cssClass}"
        <c:if test="${href}">
            href="${href}"
        </c:if>
        id="${id}" 
        <c:if test="${onClick}">
            href="${onClick}"
        </c:if>>
        <c:if test="${icon=='save'||icon=='Save'}">
            <img src="<tags:imageUrl name="disk_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='Back'}">
            <img src="<tags:imageUrl name="blue/back_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='Save &amp; Back'}">
            <img src="<tags:imageUrl name="blue/saveback_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='add'}">
            <img src="<tags:imageUrl name="add_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='page'}">
            <img src="<tags:imageUrl name="page_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='window'}">
            <img src="<tags:imageUrl name="window_icon.png"/>" alt="" />
        </c:if>${value}
        <c:if test="${icon=='Save & Continue'}">
            <img src="<tags:imageUrl name="blue/savecontinue_icon.png"/>" alt="" />
        </c:if>
        <c:if test="${icon=='Continue'}">
            <img src="<tags:imageUrl name="blue/continue_icon.png"/>" alt="" />
        </c:if>
        <div class="right">
        </div>
    </button>
</c:if>
