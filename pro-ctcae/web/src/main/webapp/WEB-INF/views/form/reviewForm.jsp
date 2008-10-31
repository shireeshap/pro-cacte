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
    <tags:stylesheetLink name="ae"/>


</head>
<body>

<form:form modelAttribute="command" method="post">
    <div class="instructions">

        <div class="summarylabel">Study</div>
        <div class="summaryvalue">${command.studyCrf.study.displayName}</div>
    </div>
    <chrome:box title="Review form">
        <div class="instructions">

            <div class="summarylabel">Title</div>
            <div class="summaryvalue">${command.studyCrf.crf.title}</div>
        </div>
        <c:forEach items="${command.studyCrf.crf.crfItems}" var="crfItem">
            <tags:reviewQuestion crfItem="${crfItem}"></tags:reviewQuestion>

        </c:forEach>
    </chrome:box>


    <tags:flowControls willSave="true" saveAction="save" showBack="true" backAction="back" saveButtonLabel="Save"/>

</form:form>

</body>