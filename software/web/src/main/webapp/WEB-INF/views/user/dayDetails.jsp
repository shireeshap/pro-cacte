<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<html>
<head>
    <%--<tags:stylesheetLink name="table_menu"/>--%>

    <script type="text/javascript">
//        function showPopUpMenuAlert() {
            alert(1);
//    var html = '<div id="search-engines"><ul>';
//    html += '<li><a href="#" onclick="">Show details</a></li>';
//    html += '</ul></div>';
//    jQuery('#scheduleAct' + 1).menu({
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
//        }
    </script>
</head>
<body>
Event(s) for <b>${date}</b>:
<table cellspacing="0" align="center" width="100%">
    <tr>
        <td class="header-top" width="17%">
            Participant
        </td>
        <td class="header-top" width="17%">
            Study
        </td>
        <td class="header-top" width="15%">
            Form
        </td>
        <td class="header-top" width="11%">
            Status
        </td>
        <td class="header-top" width="10%">
            Start date
        </td>
        <td class="header-top" width="10%">
            Due date
        </td>
        <td class="header-top" width="10%">
            Completion date
        </td>
        <td class="header-top" width="10%">
            Actions
        </td>
    </tr>
    <c:forEach items="${schedules}" var="schedule">
        <tr>
            <td class="data1">
                (${schedule.studyParticipantCrf.studyParticipantAssignment.studyParticipantIdentifier}) ${schedule.studyParticipantCrf.studyParticipantAssignment.participant.firstName} ${schedule.studyParticipantCrf.studyParticipantAssignment.participant.lastName}
            </td>
            <td class="data1">
                    ${schedule.studyParticipantCrf.studyParticipantAssignment.studySite.study.shortTitle}
            </td>
            <td class="data1">
                    ${schedule.studyParticipantCrf.crf.title}
            </td>
            <td class="data1">
                    ${schedule.status}
            </td>
            <td class="data1">
                <tags:formatDate value="${schedule.startDate}"/>
            </td>
            <td class="data1">
                <tags:formatDate value="${schedule.dueDate}"/>
            </td>
            <td class="data1">
                <tags:formatDate value="${schedule.completionDate}"/>
            </td>
            <td class="data1">
                    <a
                    class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                    id="scheduleAct${schedule.id}"
                    onmouseover="javascript:showPo('${schedule.id}','${schedule.studyParticipantCrf.studyParticipantAssignment.participant.id}', '${schedule.studyParticipantCrf.studyParticipantAssignment.participant.phoneNumber}', '${schedule.studyParticipantCrf.studyParticipantAssignment.participant.emailAddress}', '${schedule.status}')"><span
                    class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
            </td>
        </tr>
    </c:forEach>
    <tr>
        <td width="17%"></td>
        <td width="17%"></td>
        <td width="15%"></td>
        <td width="11%"><br>
            <tags:button value="Close" color="blue" size="big" onclick="closeWindow()"/>
        </td>
        <td width="10%"></td>
        <td width="10%"></td>
        <td width="10%"></td>
        <td width="10%"></td>
    </tr>
</table>
</body>
</html>