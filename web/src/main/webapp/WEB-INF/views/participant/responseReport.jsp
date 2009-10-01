<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>

</head>
<body>
<div align="right"><a href="../participant/participantInbox">Inbox</a></div>
<chrome:box
        title="${completedSchedule.studyParticipantCrf.studyParticipantAssignment.participant.displayName}">
    <table width="100%">
        <tr>
            <td style="vertical-align:top">
                <br/>
                <c:forEach items="${completedSchedule.studyParticipantCrf.studyParticipantCrfSchedules}"
                           var="studyParticipantCrfSchedule">
                    <c:if test="${studyParticipantCrfSchedule.status eq 'Completed'}">
                        <c:choose>
                            <c:when test="${studyParticipantCrfSchedule.id eq param['id']}">
                                <b><tags:formatDate value="${studyParticipantCrfSchedule.startDate}"/></b>
                            </c:when>
                            <c:otherwise>
                                <a href="../participant/responseReport?id=${studyParticipantCrfSchedule.id}">
                                    <tags:formatDate value="${studyParticipantCrfSchedule.startDate}"/>
                                </a>
                            </c:otherwise>
                        </c:choose>
                        <br/>
                    </c:if>
                </c:forEach>
            </td>
            <td>
                <tags:completedSchedule completedSchedule="${completedSchedule}"/>
            </td>
        </tr>
    </table>


</chrome:box>
</body>
</html>
