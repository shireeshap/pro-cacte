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
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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
            width: 80%;
            border: 0;
            margin-left: 20px;
            border-collapse: collapse;
        }

        #inboxTable th {
            background-color: #CBD9E4;
        }

        #inboxTitle {
            color: #333;
            height: 75px;
        }
        
        #inboxTitle .bolder {
        	font-weight: bold;
        	color:#004a93
        }
        
        #inboxTitle h1 {
        	font-size:37px;
        	color: #004a93; 
        	margin: 0;
        	padding: 0;
        }
        
        #inboxTitle img {
        float: left;
        margin: 15px 15px 0 10px;
        }
    </style>

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
<c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
        <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}" var="studyParticipantCrfSchedule">
            <c:if test="${studyParticipantCrfSchedule.status eq 'In-progress' || (studyParticipantCrfSchedule.status eq 'Scheduled' &&  studyParticipantCrfSchedule.startDate <= todaysdate)}">
                <c:set var="numberofCrfs" scope="page" value="${numberofCrfs + 1}"/>
            </c:if>
        </c:forEach>
    </c:forEach>
</c:forEach>


<div id="inboxTitle">
<img src="<tags:imageUrl name="blue/mailbox.jpg" />" alt="mailbox"/>

<h1><tags:message code="participant.box.inbox"/></h1>
	<span style="font-size:13pt; margin-left:10px;">
    <c:choose>
        <c:when test="${not empty numberofCrfs}">
            <c:if test="${numberofCrfs != 1}"><tags:message
                    code="participant.youHave"/>&nbsp;<span class="bolder">${numberofCrfs}</span><tags:message
                    code="participant.messageEndingPlural"/></c:if>
            <c:if test="${numberofCrfs == 1}"><tags:message
                    code="participant.youHave"/>&nbsp;<span class="bolder">${numberofCrfs}</span><tags:message
                    code="participant.messageEndingSingular"/></c:if>
        </c:when>
        <c:otherwise><tags:message code="participant.noformsmessage"/>
        </c:otherwise>
    </c:choose>
    </span>
</div>


<%--<div style="text-align:right;font-weight:bold;"><a href="../participant/responseReport">View old responses</a></div>--%>
<spring:message code="label.scheduledForms" var="labelScheduledForms"/>
<chrome:box title="${labelScheduledForms}">
    <table id="inboxTable">
        <tr>
            <th>
                <tags:message code="participant.label.title"/>
            </th>
            <th>
                <tags:message code="participant.label.status"/>
            </th>
            <th>
                <tags:message code="participant.label.scheduleDate"/>
            </th>
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
                    <c:if test="${studyParticipantCrfSchedule.status eq 'In-progress' || (studyParticipantCrfSchedule.status eq 'Scheduled' && studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false' && studyParticipantCrfSchedule.startDate <= todaysdate)}">
                        <tr>
                            <td>
                                <a href="../../pages/form/submit?id=${studyParticipantCrfSchedule.id}">${studyParticipantCrfSchedule.studyParticipantCrf.crf.title}</a>
                                <c:if test="${studyParticipantCrfSchedule.baseline}">(Baseline)</c:if>
                            </td>
                            <td>
                                    ${studyParticipantCrfSchedule.status}
                            </td>
                            <td>
                                <tags:formatDate value="${studyParticipantCrfSchedule.startDate}"/>
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
</chrome:box>

<spring:message code="label.missedForms" var="labelMissedForms"/>
<c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
        <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}"
                   var="studyParticipantCrfSchedule">
            <c:if test="${studyParticipantCrfSchedule.status eq 'Past-due' && studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false'
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
                        <c:if test="${studyParticipantCrfSchedule.status eq 'Past-due' && studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false'
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
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </c:forEach>

        </table>
    </chrome:box>
</c:if>
</body>


</html>
