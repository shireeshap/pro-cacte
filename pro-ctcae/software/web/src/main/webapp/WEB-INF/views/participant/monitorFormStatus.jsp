<%@ page import="java.util.Date" %>
<%@ page import="gov.nih.nci.ctcae.core.domain.ProCtcAECalendar" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<body>
<div id="formStatusTable">
    <table>
        <tr>
            <td>
                Year = <fmt:formatDate value="${pgStartPrev}" pattern="yyyy"/>
            </td>
        </tr>
    </table>
    <tags:instructions code="participant.monitor.results.instructions"/>
    <c:forEach items="${crfStatusMap}" var="siteCrfStatus">
        <chrome:division title="${siteCrfStatus.key.organization.name}"/>
        <%--<c:choose>--%>
            <%--<c:when test="${fn:length(siteCrfStatus.value) > 0}">--%>
                <%--<c:forEach items="${siteCrfStatus.value}" var="crfStatus">--%>
                <%--<c:if test="${!empty crfStatus}">--%>
                <table class="widget" cellspacing="0">
                    <tr>
                        <td class="data-left"><spring:message code="schedulecrf.label.participant"/>
                        </td>
                        <td class="data-left">Start date</td>
                        <c:forEach items="${calendar}" var="date">
                            <td class="header-top">
                                <fmt:formatDate value="${date}" pattern="MMM-dd"/>
                            </td>
                        </c:forEach>
                    </tr>
                    <c:forEach items="${siteCrfStatus.value}" var="crfStatus">
                        <tr>
                            <td class="data-left">
                                    ${crfStatus.key.displayName}
                            </td>
                            <td class="data-left">
                                <c:set var="startDate"/>
                                <c:forEach items="${crfStatus.value}" var="studyParticipantCrfSchedule">
                                    <c:if test="${!empty studyParticipantCrfSchedule}">
                                        <c:set var="startDate"
                                               value="${studyParticipantCrfSchedule.studyParticipantCrf.startDate}"/>
                                    </c:if>
                                </c:forEach>
                                <tags:formatDate value="${startDate}"/>
                            </td>
                            <c:forEach items="${crfStatus.value}" var="studyParticipantCrfSchedule" varStatus="status">
                                <td class="data ${studyParticipantCrfSchedule.status.displayName}">
                                    <c:choose>
                                    <c:when test="${studyParticipantCrfSchedule.status.displayName eq 'Completed'}">
                                        <a href="javascript:completedForm(${studyParticipantCrfSchedule.id})"
                                           title="Cycle ${studyParticipantCrfSchedule.cycleNumber}, Day ${studyParticipantCrfSchedule.cycleDay}">
                                            <img src="../../images/blue/${studyParticipantCrfSchedule.status.displayName}.png"/>
                                            Results
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                    <c:set var="todaysdate" value="<%= ProCtcAECalendar.getCalendarForDate(new Date())%>"/>
                                    <a class="nolink"
                                       title="Cycle ${studyParticipantCrfSchedule.cycleNumber}, Day ${studyParticipantCrfSchedule.cycleDay}">
                                        <c:choose>
                                        <c:when test="${todaysdate > studyParticipantCrfSchedule.dueDate && (studyParticipantCrfSchedule.status eq 'Scheduled' || studyParticipantCrfSchedule.status eq 'In-progress')}">
                                            <img src="../../images/blue/Past-due.png"/>
                                        </c:when>
                                        <c:when test="${studyParticipantCrfSchedule.status.displayName eq 'OffStudy'}">
                                        </c:when>
                                        <c:when test="${studyParticipantCrfSchedule.status eq 'Scheduled'}">
                                        <div id="img_${status.index}"
                                             onclick="showPopUpMenu('${status.index}', '${studyParticipantCrfSchedule.id}',-105,-130)">
                                            <img src="../../images/blue/Scheduled.png"/>
                                            </c:when>
                                            <c:otherwise>
                                                <%--<img src="../../images/blue/${studyParticipantCrfSchedule.status.displayName}.png"/>--%>
                                            </c:otherwise>
                                            </c:choose>

                                            </c:otherwise>
                                            </c:choose>
                                    </a>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </table>
                <br/>

            <%--</c:when>--%>
            <%--<c:otherwise>--%>
                <%--No data found for this study site.--%>
            <%--</c:otherwise>--%>
        <%--</c:choose>--%>
        <%--</c:if>--%>
        <%--</c:forEach>--%>
    </c:forEach>
    <input type="hidden" id="pgStartDateNext" value='<tags:formatDate value="${pgStartNext}"/>'/>
    <input type="hidden" id="pgStartDatePrev" value='<tags:formatDate value="${pgStartPrev}"/>'/>
    <input type="hidden" id="periodButton" value="${tablePeriod}"/>

</div>
<br>
</body>
</html>