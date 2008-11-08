<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<link rel="stylesheet" type="text/css"
      href="<c:url value="/css/extremecomponents.css"/>">
<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:javascriptLink name="extremecomponents"/>
    <style type="text/css">
        .label {
            width: 25em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
            width: 25em;

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
<chrome:flashMessage flashMessage="The form was scheduled successfully"></chrome:flashMessage>
<chrome:box title="Confirmation">

    <div class="row">
        <div class="label">Study</div>
        <div class="value">${command.study.shortTitle} </div>
    </div>
    <div class="row">
        <div class="label">Participant</div>
        <div class="value">${command.participant.displayName}</div>
    </div>

    <c:if test="${not empty command.studyParticipantAssignment.studyParticipantCrfs}">
    <chrome:division title="Scheduled Forms">
        <table class="tablecontent" width="80%">
            <tr>
                <th scope="col">Form</th>
                <th scope="col">Start Date</th>
                <th scope="col">Due Date</th>
            </tr>
            <c:forEach items="${command.studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
                <tr class="results">
                    <td>${studyParticipantCrf.studyCrf.crf.title}</td>
                    <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}" var="schedule">
                        <td>${schedule.startDate}</td>
                        <td>${schedule.dueDate}</td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
        </chrome:division>
    </c:if>
</chrome:box>

</body>
</html>