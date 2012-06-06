<%@ attribute name="userCalendarCommand" type="gov.nih.nci.ctcae.web.user.UserCalendarCommand" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<table class="widget" cellspacing="0" cellpadding="0" border="0" align="center" width="100%">
    <tr class="header">
    <td colspan="7" align="left" style="border-bottom:1px solid #77a9ff; font-size:small; color:#000000; ">
    <img height="17" width="29"
    onmousedown="getCalendar('prev');return false;"
    alt="Earlier"
    src="/proctcae/images/blank.gif"
    class="navbutton navBack"/>
    <img height="17" width="29"
    onmousedown="getCalendar('next');return false;" alt="Later"
    src="/proctcae/images/blank.gif"
    class="navbutton navForward"/>
    <b> <fmt:formatDate value="${userCalendarCommand.proCtcAECalendar.time}" pattern="MMM"/> - <fmt:formatDate
    value="${userCalendarCommand.proCtcAECalendar.time}" pattern="yyyy"/></b>
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
    <%--${proCtcAeCalendar}--%>
    <c:forEach items="${userCalendarCommand.proCtcAECalendar.htmlCalendar}" var="week">
        <tr>
            <c:forEach items="${week}" var="day" varStatus="status">
                <td class="data">
                    <div class="grey">${day}&nbsp;&nbsp;</div>
                    <c:choose>
                        <c:when test="${day eq ''}">
                            <div height="50px">&nbsp;</div>
                        </c:when>
                        <c:otherwise>
                             <c:set var="hasSchedules" value="false"/>
                            <c:forEach items="${userCalendarCommand.scheduleDates}" var="schedule">
                                    <c:if test="${schedule.key eq day}">
                                        <c:set var="hasSchedules" value="true"/>
                                        <c:set var="currentSchedule" value="${schedule}"/>
                                    </c:if>
                                 </c:forEach>
                                    <c:choose>
                                        <c:when test="${hasSchedules eq true}">
                                            <div style="float:right"><a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="scheduleActions${day}" onclick="javascript:showPopUpMenuAlert('${day}')"><span class="ui-icon ui-icon-triangle-1-s"></span></a></div>
                                            <div id="schedule_${day}" class="blue" style="text-align:center;" title="">
                                                                <br><br>   ${fn:length(currentSchedule.value)} schedule<c:if test="${fn:length(currentSchedule.value)>1}">s</c:if>
                                            </div>
                                        </c:when>

                                         <c:otherwise>
                                            <div id="schedule_${day}" class="passive" style="text-align:center;" title=""></div>
                                         </c:otherwise>
                                    </c:choose>
                        </c:otherwise>
                    </c:choose>
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
</table>