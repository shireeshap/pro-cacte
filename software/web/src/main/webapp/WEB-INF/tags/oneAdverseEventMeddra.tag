<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@attribute name="index" required="true" type="java.lang.Integer" %>
<%@attribute name="style" %>
<c:set var="ctcTermGroup">ctcTerm${index}</c:set>
<c:set var="ctcOtherGroup">ctcOther${index}</c:set>
<c:set var="mainGroup">main${index}</c:set>
<c:set var="title"><c:choose>
    <c:when test="${index == 0}">Primary adverse event</c:when>
    <c:otherwise>Adverse event ${index + 1}</c:otherwise>
</c:choose></c:set>
<chrome:division title="${title}" id="ae-section-${index}" cssClass="ae-section" style="${style}">
    <div id="ctc-details-${index}" class="ctc-details">

        <tags:renderRow field="${fieldGroups[ctcTermGroup].fields[0]}"/>

    </div>

    <div id="main-fields-${index}" class="main-fields">
        <c:forEach items="${fieldGroups[mainGroup].fields}" var="field">
            <tags:renderRow field="${field}"/>
        </c:forEach>
    </div>
</chrome:division>
