<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<table>
    <tr>
        <td style="padding-left:20px">
            Event(s) for <b>${date}</b>:
            <hr/>
            <c:forEach items="${schedules}" var="schedule">
                <div class="row">
                    <div class="label">Participant:&nbsp;</div>
                    <div class="value">(${schedule.studyParticipantCrf.studyParticipantAssignment.studyParticipantIdentifier}) ${schedule.studyParticipantCrf.studyParticipantAssignment.participant.firstName} ${schedule.studyParticipantCrf.studyParticipantAssignment.participant.lastName}</div>
                </div>
                <div class="row">
                    <div class="label">Study:&nbsp;</div>
                    <div class="value">${schedule.studyParticipantCrf.studyParticipantAssignment.studySite.study.shortTitle}</div>
                </div>
                <div class="row">
                    <div class="label">Form:&nbsp;</div>
                    <div class="value">${schedule.studyParticipantCrf.crf.title}</div>
                </div>
                <div class="row">
                    <div class="label">Status:&nbsp;</div>
                    <div class="value">${schedule.status}</div>
                </div>
                <div class="row">
                    <div class="label">Start date:&nbsp;</div>
                    <div class="value"><tags:formatDate value="${schedule.startDate}"/></div>
                </div>
                <div class="row">
                    <div class="label">Due date:&nbsp;</div>
                    <div class="value"><tags:formatDate value="${schedule.dueDate}"/></div>
                </div>
                <div class="row">
                    <div class="label">Completion date:&nbsp;</div>
                    <div class="value"><tags:formatDate value="${schedule.completionDate}"/></div>
                </div>
                <hr/>
            </c:forEach>
        </td>
    </tr>
    <tr>
        <td width="100%" align="right">
            <input type="button" value="Ok" onclick="closeWindow()"/>
        </td>
    </tr>
</table>