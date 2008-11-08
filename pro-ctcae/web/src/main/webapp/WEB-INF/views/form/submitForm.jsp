<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <style type="text/css">
        .label {
            width: 12em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
        }

        #studyDetails td.label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            width: 12em;
            padding: 1px;
        }
    </style>
</head>
<body>
<c:set var="currentQuestion"
       value="${submitFormCommand.studyParticipantCrf.studyParticipantCrfItems[0].crfItem.proCtcQuestion}"/>

<form:form method="post" commandName="submitFormCommand">
    <chrome:box title="My Form" autopad="true">
        <tags:hasErrorsMessage hideErrorDetails="false"/>
        <chrome:division title="Question 1.">
            <div class="value">${currentQuestion.questionText}</div>
            <c:forEach items="${currentQuestion.validValues}" var="validValue">
                <div class="value"><tags:renderRadio propertyName="test" defaultValue="${validValue}"/></div>
            </c:forEach>

        </chrome:division>
    </chrome:box>
    <tags:tabControls willSave="true"/>
</form:form>
</body>
</html>