<%@ attribute name="method" %>
<%@ attribute name="commandName" %>
<%@ attribute name="willSave" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="willSave" %>
<%@attribute name="title" %>
<%@attribute name="formName" %>
<%@attribute name="formId" %>
<%@attribute name="enctype" %>
<%@attribute name="boxId" %>
<%@attribute name="boxClass" %>
<%@attribute name="pageHelpAnchor" %>
<%@attribute name="instructions" fragment="true" %>
<%@attribute name="localButtons" fragment="true" %>
<%@attribute name="tabControls" fragment="true" %>
<%@attribute name="saveButtonLabel" %>
<%@attribute name="noBackground" required="false" %>
<%@attribute name="hideErrorDetails" type="java.lang.Boolean" %>
<c:if test="${empty willSave}"><c:set var="willSave" value="${true}"/></c:if>
<chrome:box title="${empty title ? tab.shortTitle : title}" id="${boxId}" cssClass="${boxClass}"
            noBackground="${noBackground}">
    <chrome:flashMessage/>
    <form:form name="${formName}" enctype="${enctype}" id="${formId}">
        <tags:tabFields tab="${tab}"/>
        <chrome:division id="single-fields">
            <c:if test="${not empty instructions}"><p class="instructions">
                <jsp:invoke fragment="instructions"/>
            </p></c:if>
            <tags:hasErrorsMessage hideErrorDetails="${hideErrorDetails}"/>
            <jsp:invoke fragment="singleFields"/>
        </chrome:division>
        <jsp:invoke fragment="repeatingFields"/>
        <c:if test="${not empty tabControls}">
            <jsp:invoke fragment="tabControls"/>
        </c:if>
        <c:if test="${empty tabControls}">

        </c:if>

    </form:form>
</chrome:box>
<!-- END tags\tabForm.tag -->
