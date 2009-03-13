<%@ attribute name="schedule" type="gov.nih.nci.ctcae.core.domain.ParticipantSchedule" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:forEach items="${schedule.currentMonthSchedules}" var="studyParticipantCrfSchedule">
    <div id="${index}_temp_<fmt:formatDate value="${studyParticipantCrfSchedule.startDate}" pattern="d" />"
         name="${index}_temp_div" title="${studyParticipantCrfSchedule.holiday}">
        <c:if test="${studyParticipantCrfSchedule.status eq 'Scheduled'}">
            <img height="13" width="12"
                 alt="remove"
                 src="/ctcae/images/blank.gif"
                 class="removebutton" align="right"
                 onclick="showDeleteWindow('<fmt:formatDate value="${studyParticipantCrfSchedule.startDate}" pattern="d" />','${index}');"/>
        </c:if>
            <br/>
            &nbsp;${studyParticipantCrfSchedule.status}
    </div>
</c:forEach>
<table class="widget" cellspacing="0" cellpadding="0" border="0" align="center" width="100%" >
    <tr class="header">
        <td colspan="7" align="left" style="border-bottom:1px solid #77a9ff; font-size:small; color:#000000; ">
            <img height="17" width="29"
                 onmousedown="applyCalendar('${index}','prev');return false;"
                 alt="Earlier"
                 src="/ctcae/images/blank.gif"
                 class="navbutton navBack"/>
            <img height="17" width="29"
                 onmousedown="applyCalendar('${index}','next');return false;" alt="Later"
                 src="/ctcae/images/blank.gif"
                 class="navbutton navForward"/>
            <b> <fmt:formatDate value="${schedule.calendar.time}" pattern="MMM"/> - <fmt:formatDate
                    value="${schedule.calendar.time}" pattern="yyyy"/></b>
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
    <c:forEach items="${schedule.calendar.htmlCalendar}" var="week">
        <tr>
            <c:forEach items="${week}" var="day" varStatus="status">
                <td class="data">
                    <div class="grey">${day}&nbsp;&nbsp;</div>
                    <c:choose>
                        <c:when test="${day eq ''}">
                            <div id="${index}_schedule_${day}"
                                 name="${index}_schedule_div" height="50px">&nbsp;</div>
                        </c:when>
                        <c:otherwise>
                            <div id="${index}_schedule_${day}" name="${index}_schedule_div" class="passive"
                                 onclick="showAddWindow('${day}','${index}');">
                            </div>
                        </c:otherwise>
                    </c:choose>
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
</table>
