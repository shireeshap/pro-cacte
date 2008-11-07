<%@ attribute name="notDisplayInBox" type="java.lang.Boolean" %>
<!-- BEGIN tags\tabForm.tag -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="tab" required="true" type="gov.nih.nci.cabig.ctms.web.tabs.Tab" %>
<%@attribute name="flow" required="true" type="gov.nih.nci.cabig.ctms.web.tabs.Flow" %>
<%@attribute name="willSave" %>
<%@attribute name="title" %>
<%@attribute name="formName" %>
<%@attribute name="formId" %>
<%@attribute name="enctype" %>
<%@attribute name="boxId" %>
<%@attribute name="boxClass" %>
<%@attribute name="pageHelpAnchor" %>
<%@attribute name="instructions" fragment="true" %>
<%@attribute name="singleFields" fragment="true" %>
<%@attribute name="repeatingFields" fragment="true" %>
<%@attribute name="localButtons" fragment="true" %>
<%@attribute name="tabControls" fragment="true" %>
<%@attribute name="saveButtonLabel" %>
<%@attribute name="noBackground" required="false" %>
<%@attribute name="hideErrorDetails" type="java.lang.Boolean" %>
<c:if test="${empty willSave}"><c:set var="willSave" value="${true}"/></c:if>
<form:form name="${formName}" enctype="${enctype}" id="command">

    <tags:tabContent notDisplayInBox="${notDisplayInBox}" tab="${tab}" title="${title}">
        <jsp:attribute name="tabContent">
        <chrome:flashMessage/>

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
            <div class="local-buttons">
                <jsp:invoke fragment="localButtons"/>
            </div>
         
    </jsp:attribute>


    </tags:tabContent>

    <c:if test="${empty tabControls}">
        <tags:tabControls tab="${tab}" flow="${flow}" willSave="${willSave}" saveButtonLabel="${saveButtonLabel}">
        </tags:tabControls>

    </c:if>

</form:form>
<!-- END tags\tabForm.tag -->
