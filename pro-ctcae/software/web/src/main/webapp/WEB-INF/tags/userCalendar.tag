<%@ attribute name="userCalendarCommand" type="gov.nih.nci.ctcae.web.user.UserCalendarCommand" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id='ajaxLoadingImgDiv' style="display:none;left:400px;position:absolute;top:270px;">
    <img src="<tags:imageUrl name="ajax-loading.gif"/>"/>
</div>
<table class="widget" cellspacing="0" cellpadding="0" border="0" align="center" width="50%">
    <tr>
        <td align="right" colspan="2">
            Scheduled = <br>
            In-progress = <br>
            Cancelled/N/A =
        </td>
        <td>
            <div style="background-color:blue;color:white;margin-bottom:3px;margin-left:3px" align="center">Blue</div>
            <div style="background-color:orange;color:white;margin-bottom:3px;margin-left:3px" align="center">Orange
            </div>
            <div style="background-color:#d3d3d3;color:black;margin-bottom:3px;margin-left:3px" align="center">Light
                gray
            </div>
        </td>

        <td align="right" colspan="2">
            Completed = <br>
            Past-due = <br>
            On-hold =
        </td>
        <td>
            <div style="background-color:green;color:white;margin-bottom:3px;margin-left:3px" align="center">Green</div>
            <div style="background-color:red;color:white;margin-bottom:3px;margin-left:3px" align="center">Red</div>
            <div style="background-color:yellow;color:black;margin-bottom:3px;margin-left:3px" align="center">Yellow
            </div>
        </td>
    </tr>

    <tr>
        <td>

        </td>
    </tr>
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
                            <c:set var="class" value="blue"/>
                            <c:set var="break" value="false"/>
                            <c:set var="inprog" value="false"/>
                            <c:set var="sched" value="false"/>
                            <c:set var="onhold" value="false"/>
                            <c:forEach items="${currentSchedule.value}" var="sch">
                                <c:if test="${break eq false}">
                                    <c:if test="${sch.status.displayName eq 'Past-due'}">
                                        <c:set var="class" value="red"/>
                                        <c:set var="break" value="true"/>
                                    </c:if>
                                    <c:if test="${sched eq false}">
                                        <c:if test="${inprog eq false}">
                                            <c:if test="${sch.status.displayName eq 'Completed'}">
                                                <c:set var="class" value="green"/>
                                            </c:if>
                                            <c:if test="${sch.status.displayName eq 'In-progress'}">
                                                <c:set var="class" value="orange"/>
                                                <c:set var="inprog" value="true"/>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${sch.status.displayName eq 'Scheduled'}">
                                            <c:set var="class" value="blue"/>
                                            <c:set var="sched" value="true"/>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${sch.status.displayName eq 'On-hold' && sched eq false && inprog eq false}">
                                        <c:set var="class" value="yellow"/>
                                    </c:if>
                                </c:if>

                            </c:forEach>
                            <c:choose>
                                <c:when test="${hasSchedules eq true}">
                                    <div id="schedule_${day}" class="${class}" style="text-align:center;" title="">
                                     <div style="float:right;"><a
                                            class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                                            id="scheduleActions${day}"
                                            onmouseover="javascript:showPopUpMenuAlert('${day}')"><span
                                            class="ui-icon ui-icon-triangle-1-s"></span></a></div>
                                        <br><br> <c:if
                                            test="${fn:length(currentSchedule.value) eq 1}">Form (${fn:length(currentSchedule.value)})</c:if>
                                        <c:if test="${fn:length(currentSchedule.value)>1}">Multiple forms (${fn:length(currentSchedule.value)})</c:if>
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
<script type="text/javascript">
if (isIE()){
	if(getIEVersion()==8){
		jQuery('a[id^="scheduleActions"]').css("height","6");
		jQuery('a[id^="scheduleActions"]').css("width","30");
	}
	if(getIEVersion()==9){
		jQuery('a[id^="scheduleActions"]').attr("style","height:6px; width:30px;");
	}
}	
</script>