<%@attribute name="notDisplayInBox" type="java.lang.Boolean" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="tab" required="true" type="gov.nih.nci.cabig.ctms.web.tabs.Tab" %>
<%@attribute name="noBackground" required="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%@attribute name="willSave" %>
<%@attribute name="title" %>
<%@attribute name="formName" %>
<%@attribute name="formId" %>
<%@attribute name="enctype" %>
<%@attribute name="boxId" %>
<%@attribute name="boxClass" %>
<%@attribute name="pageHelpAnchor" %>
<%@attribute name="tabContent" fragment="true" %>
<%@attribute name="localButtons" fragment="true" %>

<c:choose>
    <c:when test="${!notDisplayInBox}">
        <chrome:box title="${tab.shortTitle}" id="${boxId}" cssClass="${boxClass}"
                    noBackground="${noBackground}">

            <jsp:invoke fragment="tabContent"/>

        </chrome:box>
    </c:when>
    <c:otherwise>
        <jsp:invoke fragment="tabContent"/>


    </c:otherwise>
</c:choose>

