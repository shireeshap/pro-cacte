<%-- This is the standard decorator for all caAERS pages --%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <tags:stylesheetLink name="tabbedflow"/>


</head>
<body>

<form:form modelAttribute="command" method="post">
    <chrome:box title="Review form">
        <c:forEach items="${command.crf.crfItems}" var="crfItem">
            <chrome:division>
                ${crfItem.displayOrder}: ${crfItem.proCtcTerm.questionText}
                <ul>
                    <c:forEach items="${crfItem.proCtcTerm.validValues}" var="proCtcValidValue">
                        <li>${proCtcValidValue.value}</li>
                    </c:forEach>
                </ul>
            </chrome:division>
        </c:forEach>
    </chrome:box>


    <tags:flowControls willSave="false" saveAction="save" showBack="true" backAction="back"  saveButtonLabel="Save"/>

</form:form>

</body>