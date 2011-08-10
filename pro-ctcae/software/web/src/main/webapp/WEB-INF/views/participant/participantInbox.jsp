<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="gov.nih.nci.ctcae.core.domain.ProCtcAECalendar" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
	
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <style type="text/css">
        div.row div.value {
            white-space: normal;
        }

        .label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            padding: 1px;
            font-size: 20px;
        }

        #inboxTable {
            width: 952px;
            border: 0;
            margin-left: -4px;
            border-collapse: collapse;
            margin-bottom: 3px;
        }

        #inboxTable th {
            background: #a4a6a9 url(../../images/table/inboxtable_th.png) repeat-x top;
            padding: 10px 10px 10px 20px;
            font-size: 16px;
            color: #000;
            text-shadow: 0 1px #fff;
            border-top: 1px solid lightgray;
        }

        #inboxTable td {
            padding: 8px 10px 7px 20px;

        }

        #inboxTable tr {
            background: url(../../images/table/inboxtable_tr.png) repeat-x top;
            border-bottom: 1px solid lightgray;
            text-shadow: 0 1px white;
            color: #333;
            font-size: 16px;
        }

        #inboxTable tr:hover {
            background-position: 0 -55px;
        }

        #inboxTitle {
            color: #333;
            height: 75px;
        }

        #inboxTitle .bolder {
            font-weight: bold;
            color: #004a93
        }

        #inboxTitle h1 {
            font-size: 37px;
            color: #004a93;
            margin: 0;
            padding: 0;
        }

        #inboxTitle img {
            float: left;
            margin: 15px 15px 0 10px;
        }

        .current {
            background-position: 0 -62px;

        }


    </style>
    <script type="text/javascript">
        function setCurrent(className) {
            alert(className);
            var x = document.getElementsByClassName('right');
            x.className = 'current';
        }
    </script>

</head>
<body>
<c:set var="todaysdate" value="<%= ProCtcAECalendar.getCalendarForDate(new Date()).getTime()%>"/>
<c:set var="missedFormsAvailable" value="false"/>
<%
    Calendar calendar = new java.util.GregorianCalendar();
    calendar.add(Calendar.DAY_OF_MONTH, -15);
    java.util.Date date = calendar.getTime();
    request.setAttribute("missedFormDate", date);
%>
<%--this loop is the same code as below that renders the forms, but it just gets the number of forms to display under the 'Inbox' text--%>
<c:set var="numberofCrfs" scope="page" value="0"/>
<c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
        <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}" var="studyParticipantCrfSchedule">
            <c:if test="${studyParticipantCrfSchedule.status.displayName eq 'In-progress' || (studyParticipantCrfSchedule.status.displayName eq 'Scheduled' && studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false' && studyParticipantCrfSchedule.startDate <= todaysdate)}">
                <c:set var="numberofCrfs" scope="page" value="${numberofCrfs + 1}"/>
            </c:if>
        </c:forEach>
    </c:forEach>
</c:forEach>

<c:set var="futureNumberofCrfs" scope="page" value="0"/>
<c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
        <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}" var="studyParticipantCrfSchedule">
            <c:if test="${studyParticipantCrfSchedule.status.displayName eq 'In-progress' || (studyParticipantCrfSchedule.status.displayName eq 'Scheduled' &&  studyParticipantCrfSchedule.startDate > todaysdate)}">
                <c:set var="futureNumberofCrfs" scope="page" value="${futureNumberofCrfs + 1}"/>
                <c:if test="${futureNumberofCrfs == 1}">
                    <c:set var="futureSurveyAvailableDate"  scope="page" value="${studyParticipantCrfSchedule.startDate}" />
                </c:if>
                <c:if test="${studyParticipantCrfSchedule.startDate < futureSurveyAvailableDate}">
                    <c:set var="futureSurveyAvailableDate"  scope="page" value="${studyParticipantCrfSchedule.startDate}" />
                </c:if>
            </c:if>
        </c:forEach>
    </c:forEach>
</c:forEach>

<div id="inboxTitle">
    <c:if test="${param.lang eq 'en'}">
        <c:set var="currentEn" value="current"/>
        <c:set var="currentEs" value=""/>
    </c:if>
    <c:if test="${param.lang eq 'es'}">
        <c:set var="currentEn" value=""/>
        <c:set var="currentEs" value="current"/>
    </c:if>
    <div class="language-toggle" style="float:right">
        <ul>
            <li class="left ${currentEn}"><a href="?lang=en">English</a></li>
            <li class="right ${currentEs}"><a href="?lang=es">Español</a></li>
        </ul>
    </div>
    <img src="<tags:imageUrl name="blue/mailbox.jpg" />" alt="mailbox"/>

    <h1><tags:message code="participant.box.inbox"/>(${numberofCrfs})</h1>
	<span style="font-size:13pt; margin-left:10px;">
    <c:choose>
        <c:when test="${numberofCrfs gt 0}">
            <c:if test="${numberofCrfs != 1}"><tags:message
                    code="participant.youHave"/>&nbsp;<span class="bolder">[${numberofCrfs}]</span><tags:message
                    code="participant.messageEndingPlural"/></c:if>
            <c:if test="${numberofCrfs == 1}"><tags:message
                    code="participant.youHave"/>&nbsp;<span class="bolder">[${numberofCrfs}]</span><tags:message
                    code="participant.messageEndingSingular"/></c:if>
        </c:when>
        <c:otherwise><tags:message code="participant.noformsmessage"/>
        </c:otherwise>
    </c:choose>
    </span>
</div>

<tags:instructions  code="participant.instruction"/><br/>

<%--<div style="text-align:right;font-weight:bold;"><a href="../participant/responseReport">View old responses</a></div>--%>
<spring:message code="label.scheduledForms" var="labelScheduledForms"/>

<chrome:box title="${labelScheduledForms}">
    <c:if test="${numberofCrfs eq 0}">
        <c:if test="${futureNumberofCrfs eq 0}">
                 <tags:message code="participant.noSurveyAvailable"/>
        </c:if>
        <c:if test="${futureNumberofCrfs gt 0}">
                  <tags:message code="participant.noSurveyAvailable"/> <tags:message code="participant.nextSurveyAvailable"/> <tags:formatDate value="${futureSurveyAvailableDate}"/>.
        </c:if>
    </c:if>
    <c:if test="${numberofCrfs gt 0}">
        <table id="inboxTable">
            <tr>
                <th>
                    <tags:message code="participant.label.title"/>
                </th>
                <th>
                    <tags:message code="participant.label.status"/>
                </th>
          <%--  <th>
                    <tags:message code="participant.label.scheduleDate"/>
                </th> --%>
                <th>
                    <tags:message code="participant.label.dueDate"/>
                </th>
                <th>

                </th>
            </tr>
            <c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
                <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
                    <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}"
                               var="studyParticipantCrfSchedule">
                        <c:if test="${studyParticipantCrfSchedule.status.displayName eq 'In-progress' || (studyParticipantCrfSchedule.status.displayName eq 'Scheduled' && studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false' && studyParticipantCrfSchedule.startDate <= todaysdate)}">
                            <tr>
                                <td>
                                        ${studyParticipantCrfSchedule.studyParticipantCrf.crf.title}
                                    <%--<c:if test="${studyParticipantCrfSchedule.baseline}">(Baseline)</c:if> --%>
                                </td>
                                <td>
                                    <tags:message code="crfStatus_${studyParticipantCrfSchedule.status.code}"/>
                                </td>
                          <%--  <td>
                                    <tags:formatDate value="${studyParticipantCrfSchedule.startDate}"/>
                                </td> --%>
                                <td>
                                    <c:set scope="page" var="remainingDays" value="${(studyParticipantCrfSchedule.dueDate.time - todaysdate.time) / (1000 * 60 * 60 * 24)}"/>

                                    <c:if test="${(studyParticipantCrfSchedule.dueDate.time eq todaysdate.time)}">
                                             <tags:message code="participant.today"/>
                                    </c:if>
                                    <c:if test="${(studyParticipantCrfSchedule.dueDate.time gt todaysdate.time)}">
                                        <c:if test="${remainingDays eq 1}">
                                               <tags:message code="participant.tomorrow"/>
                                        </c:if>
                                        <c:if test="${remainingDays gt 1}">
                                            <tags:message code="participant.in"/> <fmt:formatNumber type="number" maxFractionDigits="0" value="${remainingDays}"/> <tags:message code="participant.days"/>
                                        </c:if>
                                    </c:if>
                                     <c:if test="${(studyParticipantCrfSchedule.dueDate.time lt todaysdate.time)}">
                                        <tags:message code="participant.expired"/> <fmt:formatNumber type="number" maxFractionDigits="0" value="${(todaysdate.time - studyParticipantCrfSchedule.dueDate.time) / (1000 * 60 * 60 * 24)}"/> <tags:message code="participant.days.ago"/>
                                    </c:if>

                                </td>
                                <td>
                                    <a href="../../pages/form/submit?id=${studyParticipantCrfSchedule.id}"
                                       class="btn small-green"><span><tags:message code="label.start"/></span></a>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </c:forEach>

        </table>
    </c:if>
</chrome:box>

   <%-- <spring:message code="label.missedForms" var="labelMissedForms"/>
    <c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
        <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
            <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}"
                       var="studyParticipantCrfSchedule">
                <c:if test="${studyParticipantCrfSchedule.status.displayName eq 'Past-due' && studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false'
                            && studyParticipantCrfSchedule.dueDate ge missedFormDate}">
                    <c:set var="missedFormsAvailable" value="true"/>
                </c:if>
            </c:forEach>
        </c:forEach>
    </c:forEach>
    <c:if test="${missedFormsAvailable}">
        <chrome:box title="${labelMissedForms}">
            <table id="inboxTable">
                <tr>
                    <th>
                        <tags:message code="participant.label.title"/>
                    </th>

                    <th>
                        <tags:message code="participant.label.scheduleDate"/>
                    </th>
                    <th>
                        <tags:message code="participant.label.dueDate"/>
                    </th>
                </tr>
                <c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
                    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
                        <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}"
                                   var="studyParticipantCrfSchedule">
                            <c:if test="${studyParticipantCrfSchedule.status.displayName eq 'Past-due' && studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false'
                                && studyParticipantCrfSchedule.dueDate ge missedFormDate}">
                                <tr>
                                    <td>
                                            ${studyParticipantCrfSchedule.studyParticipantCrf.crf.title}
                                        <c:if test="${studyParticipantCrfSchedule.baseline}">(Baseline)</c:if>

                                    </td>

                                    <td>
                                        <tags:formatDate value="${studyParticipantCrfSchedule.startDate}"/>
                                    </td>
                                    <td>
                                        <tags:formatDate value="${studyParticipantCrfSchedule.dueDate}"/>
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </c:forEach>
            </table>
        </chrome:box>
       </c:if>--%>

</body>


</html>
