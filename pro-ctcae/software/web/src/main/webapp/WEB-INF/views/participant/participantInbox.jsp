<%@ page import="java.util.Date" %>
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
            color: #003E7C;
            position: absolute;
            left: 150px;
            top: 39px;
        }
    </style>

</head>
<body>
<c:set var="todaysdate" value="<%= new Date()%>"/>
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
<img style=" position:relative; left:-10px;" src="<tags:imageUrl name="blue/mailbox.jpg" />" alt="mailbox"/>

<div id="inboxTitle"><span style="font-size:75px; line-height:70px;">Inbox</span><br/>
    <span style="font-size:13pt; margin-left:6px;">You have <c:choose><c:when test="${not empty numberofCrfs}"><span
            style="font-weight:bolder;">${numberofCrfs}</span></c:when><c:otherwise>no</c:otherwise></c:choose> form<c:if
            test="${numberofCrfs != 1}">s</c:if> that need<c:if
            test="${numberofCrfs == 1}">s</c:if> to be completed.</span>
</div>
<div style="text-align:right;font-weight:bold;"><a href="../participant/responseReport">View old responses</a></div>
<chrome:box title="Scheduled forms">
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
                    <c:if test="${studyParticipantCrfSchedule.status eq 'In-progress' || (studyParticipantCrfSchedule.status eq 'Scheduled' &&  studyParticipantCrfSchedule.startDate <= todaysdate)}">
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
                                    <%--<c:if test="${not studyParticipantCrfSchedule.baseline}">--%>
                                <tags:formatDate value="${studyParticipantCrfSchedule.dueDate}"/>
                                    <%--</c:if>--%>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </c:forEach>
        </c:forEach>

    </table>
</chrome:box>


<chrome:box title="Missed forms">
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
                    <c:if test="${studyParticipantCrfSchedule.status eq 'Past-due' ||  studyParticipantCrfSchedule.dueDate < todaysdate}">
                        <tr>
                            <td>
                                    ${studyParticipantCrfSchedule.studyParticipantCrf.crf.title}
                                <c:if test="${studyParticipantCrfSchedule.baseline}">(Baseline)</c:if>

                            </td>

                            <td>
                                <tags:formatDate value="${studyParticipantCrfSchedule.startDate}"/>
                            </td>
                            <td>
                                    <%--<c:if test="${not studyParticipantCrfSchedule.baseline}">--%>
                                <tags:formatDate value="${studyParticipantCrfSchedule.dueDate}"/>
                                    <%--</c:if>--%>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </c:forEach>
        </c:forEach>

    </table>
</chrome:box>
</body>


</html>
