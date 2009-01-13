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
<chrome:flashMessage flashMessage="schedulecrf.flash.saved"></chrome:flashMessage>
<chrome:box title="schedulecrf.label.confirmation">

    <div class="row">
        <div class="label"><spring:message code="schedulecrf.label.study"/></div>
        <div class="value">${command.study.shortTitle} </div>
    </div>
    <div class="row">
        <div class="label"><spring:message code="schedulecrf.label.participant"/></div>
        <div class="value">${command.participant.displayName}</div>
    </div>
    <div class="row">
        <div class="label"><a href="../participant/participantInbox?participantId=${command.participant.id}">Participant
            Inbox</a></div>
    </div>

    <c:if test="${not empty command.studyParticipantAssignment.studyParticipantCrfs}">
        <chrome:division title="schedulecrf.label.scheduled_forms">
            <table class="tablecontent" width="80%">
                <tr>
                    <th scope="col"><spring:message code="schedulecrf.label.form"/></th>
                    <th scope="col"><spring:message code="schedulecrf.label.start_date"/></th>
                    <th scope="col"><spring:message code="schedulecrf.label.due_date"/></th>
                    <th scope="col"><spring:message code="schedulecrf.label.status"/></th>
                </tr>
                <c:forEach items="${command.studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
                    <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}" var="schedule">
                        <tr class="results">
                            <td>${studyParticipantCrf.crf.title}</td>
                            <td><tags:formatDate value="${schedule.startDate}"/></td>
                            <td><tags:formatDate value="${schedule.dueDate}"/></td>
                            <td>${schedule.status}</td>
                        </tr>
                    </c:forEach>
                </c:forEach>
            </table>
        </chrome:division>
    </c:if>
</chrome:box>

</body>
</html>