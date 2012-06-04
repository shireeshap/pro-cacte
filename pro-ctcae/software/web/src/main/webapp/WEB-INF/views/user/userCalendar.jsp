<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<html>
<head>
    <jsp:useBean id="today" class="java.util.Date"/>
    <c:set var="fDate" value='<tags:formatDate value="${today}"/>'/>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:stylesheetLink name="cycledefinitions"/>
    <tags:javascriptLink name="cycledefinitions"/>
    <tags:javascriptLink name="yui"/>
    <script type="text/javascript">


        //function showDetailsWindow(index) {
        //    var request = new Ajax.Request("<c:url value="/pages/participant/detailsFormSchedule"/>", {
        //        onComplete:function(transport) {
        //            showConfirmationWindow(transport, 650, 300);
        //        },
        //        parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&sids=" + sids + "&id=" + pid,
        //        method:'get'
        //    })
        //}
        <%----%>
        <%----%>
        //function getCalendar(index, parameters, pid) {
        //    var request = new Ajax.Request("<c:url value="/pages/participant/displaycalendar"/>", {
        //        onComplete:function(transport) {
        //            showCalendar(index, transport);
        //        },
        //        parameters:<tags:ajaxstandardparams/>+"&id=" + pid +"&index=" + index + "&" + parameters,
        //        method:'get'
        //    })
        //}
        <%----%>
        //function showCalendar(index, transport) {
        //    var items = $('calendar_' + index + '_outer').childElements();
        //    var len = items.length;
        //    for (var i = 0; i < len; i++) {
        //        if (items[i].id != 'calendar_' + index + '_inner') {
        //            items[i].remove();
        //        }
        //    }
        //    new Insertion.After('calendar_' + index + '_inner', transport.responseText);
        //}


        //function showPopUpMenuSchedule(date, index, sid, showDeleteOption) {
        //    var html = '';
        //    var menuindex = date;
        //    var holdDate = date;
        //    if (sid == null) {
        <%----%>
        <%----%>
        //        var html = '<div id="search-engines"><ul>';
        <%--html += '<li><a href="#" onclick="javascript:showDetailsWindow(' + date + ', ' + index + ', \'' + sid + '\', ' + ${command.selectedStudyParticipantAssignment.id} + ');">Show details</a></li>';--%>
        <%----%>
        //        html += '</ul></div>';
        //    }
        //    jQuery('#scheduleActions' + menuindex).menu({
        //        content: html,
        //        maxHeight: 350,
        //        positionOpts: {
        //            directionV: 'down',
        //            posX: 'left',
        //            posY: 'bottom',
        //            offsetX: 0,
        //            offsetY: 0
        //        },
        //        showSpeed: 300
        //    });
        //}


    </script>
    <style type="text/css">


    </style>
</head>
<body>

<table class="widget" cellspacing="0" cellpadding="0" border="0" align="center" width="100%">
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
    <b> <fmt:formatDate value="${proCtcAeCalendar.time}" pattern="MMM"/> - <fmt:formatDate
    value="${proCtcAeCalendar.time}" pattern="yyyy"/></b>
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
    <c:forEach items="${proCtcAeCalendar.htmlCalendar}" var="week">
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
                            <c:forEach items="${schedules}" var="schedule">
                                    <c:if test="${schedule.key eq day}">
                                        <c:set var="hasSchedules" value="true"/>
                                        <c:set var="currentSchedule" value="${schedule}"/>
                                    </c:if>
                                 </c:forEach>
                                    <c:choose>
                                        <c:when test="${hasSchedules eq true}">
                                            <div id="schedule_${day}" class="blue" style="text-align:center;" title="">
                                                                   ${fn:length(currentSchedule.value)}
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
</body>
</html>