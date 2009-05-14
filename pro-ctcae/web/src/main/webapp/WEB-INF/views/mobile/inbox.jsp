<%@ page import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<% response.setContentType("text/vnd.wap.wml"); %>
<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<c:set var="todaysdate" value="<%= new Date()%>"/>
<c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
        <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}" var="studyParticipantCrfSchedule">
            <c:if test="${studyParticipantCrfSchedule.status eq 'In-progress' || (studyParticipantCrfSchedule.status eq 'Scheduled' &&  studyParticipantCrfSchedule.startDate <= todaysdate)}">
                <c:set var="numberofCrfs" scope="page" value="${numberofCrfs + 1}"/>
            </c:if>
        </c:forEach>
    </c:forEach>
</c:forEach>
<wml>
    <card id="Inbox" title="ProCtcAE Inbox">
        <p align="center">
            You have hh
            <c:choose>
                <c:when test="${not empty numberofCrfs}"> ${numberofCrfs}</c:when>
                <c:otherwise>no</c:otherwise>
            </c:choose>
            form<c:if test="${numberofCrfs != 1}">s</c:if> that need<c:if test="${numberofCrfs == 1}">s</c:if> to be
            completed.
        </p>
        <table columns="3" border="1" width="100%" style="border:1px solid blue;">
            <tr>
                <td><b><tags:message code="participant.label.title"/></b></td>
                <td><b><tags:message code="participant.label.status"/></b></td>
                <td><b><tags:message code="participant.label.dueDate"/></b></td>
            </tr>
            <c:forEach items="${command.studyParticipantAssignments}"
                       var="studyParticipantAssignment">
                <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}"
                           var="studyParticipantCrf">
                    <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}"
                               var="studyParticipantCrfSchedule">
                        <c:if test="${studyParticipantCrfSchedule.status eq 'In-progress' || (studyParticipantCrfSchedule.status eq 'Scheduled' &&  studyParticipantCrfSchedule.startDate <= todaysdate)}">
                            <tr>
                                <td>
                                    <anchor>${studyParticipantCrfSchedule.studyParticipantCrf.crf.title}
                                        <go method="get"
                                            href="/proctcae/mobile/submit?id=${studyParticipantCrfSchedule.id}">
                                        </go>
                                    </anchor>
                                </td>
                                <td>
                                        ${studyParticipantCrfSchedule.status}
                                </td>
                                <td>
                                    <tags:formatDate value="${studyParticipantCrfSchedule.dueDate}"/>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </c:forEach>
        </table>
    </card>
</wml>