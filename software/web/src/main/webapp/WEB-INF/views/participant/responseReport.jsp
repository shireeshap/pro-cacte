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
<chrome:box title="${participant.displayName}">
    <c:choose>
        <c:when test="${completedSchedule == null}">
            No completed form found for the participant.
        </c:when>
        <c:otherwise>
            <table width="100%">
                <tr>
                    <td colspan="2">
                        <tags:instructions code="participant.old.responses"/>
                    </td>
                </tr>
                <tr>
                    <td style="vertical-align:top">

                        <b>Scheduled dates </b>
                        <br/>
                        <c:forEach items="${participant.studyParticipantAssignments}" var="studyParticipantAssignment">
                            <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}"
                                       var="studyParticipantCrf">
                                <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}"
                                           var="studyParticipantCrfSchedule">
                                    <c:if test="${studyParticipantCrfSchedule.status eq 'Completed'}">
                                        <c:choose>
                                            <c:when test="${studyParticipantCrfSchedule.id eq completedSchedule.id}">
                                                <b><tags:formatDate
                                                        value="${studyParticipantCrfSchedule.startDate}"/></b>
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
                            </c:forEach>
                        </c:forEach>
                    </td>
                    <td>
                        <tags:completedSchedule completedSchedule="${completedSchedule}"/>
                    </td>
                </tr>
            </table>
        </c:otherwise>
    </c:choose>
</chrome:box>
</body>
</html>
