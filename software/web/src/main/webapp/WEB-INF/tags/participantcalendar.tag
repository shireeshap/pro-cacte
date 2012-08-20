<%@ attribute name="schedule" type="gov.nih.nci.ctcae.core.domain.ParticipantSchedule" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="true" %>
<%@ attribute name="studyParticipantAssignment" type="gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">
    calendarArr[${index}] = new Array();
    scheduleArr[${index}] = new Array();
    forms[${index}] = new Array();
</script>

<c:forEach items="${schedule.currentMonthSchedules}" var="studyParticipantCrfSchedule" varStatus="status">
    <script type="text/javascript">
        var day = '<fmt:formatDate value="${studyParticipantCrfSchedule.startDate}" pattern="d"/>';
        var index = 0;
        if (typeof(scheduleArr[${index}][day]) == 'undefined') {
            scheduleArr[${index}][day] = new Array();
        } else {
            index = scheduleArr[${index}][day].length;
        }
        scheduleArr[${index}][day][index] = new Array();
        scheduleArr[${index}][day][index][0] = '${studyParticipantCrfSchedule.status.displayName}';
        scheduleArr[${index}][day][index][1] = '${studyParticipantCrfSchedule.baseline}';
        scheduleArr[${index}][day][index][2] = '${studyParticipantCrfSchedule.holiday}';
        scheduleArr[${index}][day][index][3] = '${studyParticipantCrfSchedule.id}';
        scheduleArr[${index}][day][index][4] = "${studyParticipantCrfSchedule.studyParticipantCrf.crf.title}";
        forms[${index}][${studyParticipantCrfSchedule.id}] = "${studyParticipantCrfSchedule.studyParticipantCrf.crf.title}";
    </script>
</c:forEach>

<tags:instructions code="schedulecrf.instructions"/>
<c:if test="${studyParticipantAssignment.onHoldTreatmentDate ne null}">
<div style="margin-left:15px">
<font color="red" size="2"><b>Surveys for the participant <i>"${studyParticipantAssignment.participant.displayName}"</i> have been put on hold beginning <tags:formatDate
                        value="${studyParticipantAssignment.onHoldTreatmentDate}"/></font></b>
</div>
    </c:if>
<br/>
<table class="widget" cellspacing="0" cellpadding="0" border="0" align="center" width="100%">
    <tr>
        <td align="right" colspan="2">
            Scheduled =  <br>
            In-progress =
        </td>
        <td>
            <div style="background-color:blue;color:white;margin-bottom:3px;margin-left:3px" align="center">Blue</div>
            <div style="background-color:orange;color:white;margin-bottom:3px;margin-left:3px" align="center">Orange</div>
        </td>

        <td align="right" colspan="2">
            Completed =             <br>
            Past-due =
        </td>
        <td>
            <div style="background-color:green;color:white;margin-bottom:3px;margin-left:3px" align="center">Green</div>
            <div style="background-color:red;color:white;margin-bottom:3px;margin-left:3px" align="center">Red</div>
        </td>
    </tr>
    <tr class="header">
        <td colspan="7" align="left" style="border-bottom:1px solid #77a9ff; font-size:small; color:#000000; ">
            <img height="17" width="29"
                 onmousedown="applyCalendar('${index}','prev');return false;"
                 alt="Earlier"
                 src="/proctcae/images/blank.gif"
                 class="navbutton navBack"/>
            <img height="17" width="29"
                 onmousedown="applyCalendar('${index}','next');return false;" alt="Later"
                 src="/proctcae/images/blank.gif"
                 class="navbutton navForward"/>
            <b> <fmt:formatDate value="${schedule.proCtcAECalendar.time}" pattern="MMM"/> - <fmt:formatDate
                    value="${schedule.proCtcAECalendar.time}" pattern="yyyy"/></b>
        </td>
    </tr>
    <tr class="header">
        <td class="header">Sun</td>
        <td class="header">Mon</td>
        <td class="header">Tue</td>
        <td class="header">Wed</td>
        <td class="header">Thu</td>
        <td class="header">Fri</td>
        <td class="header">Sat</td>
    </tr>
    <c:forEach items="${schedule.proCtcAECalendar.htmlCalendar}" var="week">
        <tr>
            <c:forEach items="${week}" var="day" varStatus="status">
                <td class="data">
                    <div class="grey">${day}&nbsp;&nbsp;</div>
                    <c:choose>
                        <c:when test="${day eq ''}">
                            <div height="50px">&nbsp;</div>
                        </c:when>
                        <c:otherwise>
                            <div id="${index}_schedule_${day}" class="passive" style="text-align:center;" title="">
                            </div>
                            <script type="text/javascript">
                                calendarArr[${index}]['${day}'] = '${day}';
                            </script>
                        </c:otherwise>
                    </c:choose>
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
</table>
<script type="text/javascript">
    initializeCalendar('${index}');
</script>
