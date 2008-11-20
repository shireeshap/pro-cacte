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
    </style>
</head>
<body>
<chrome:box title="participant.box.inbox">

    <table width="100%" border="0">
        <tr>
            <th>
                <tags:message code="participant.label.title"/>
            </th>
            <th>
                <tags:message code="participant.label.status"/>
            </th>
            <th>
                <tags:message code="participant.label.dueDate"/>
            </th>
        </tr>
        <c:forEach items="${command.studyParticipantAssignments}"
                   var="studyParticipantAssignment">
            <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}"
                       var="studyParticipantCrf">
                <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}"
                           var="studyParticipantCrfSchedule">
                    <c:if test="${studyParticipantCrfSchedule.status eq 'In-progress' || studyParticipantCrfSchedule.status eq 'Scheduled'}">
                        <tr>
                            <td>
                                <a href="../../pages/form/submit?id=${studyParticipantCrfSchedule.id}"> ${studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title} </a>
                            </td>
                            <td>
                                    ${studyParticipantCrfSchedule.status}
                            </td>
                            <td>
                                    ${studyParticipantCrfSchedule.dueDate}
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