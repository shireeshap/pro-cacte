<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <tags:stylesheetLink name="table_menu"/>
</head>
<body>
<chrome:box title="Report">
    <tags:button value="Show chart" color="blue" size="small" markupWithTag="a"
                 onclick="reportResults('${group}');"/>
    <br/>
    <table class="widget" cellspacing="0" align="center">
        <tr>
            <td class="header-top"></td>
            <td class="header-top">
                Participant
            </td>
            <td class="header-top">
                Response date
            </td>
            <td class="header-top">
                Response
            </td>
            <td class="header-top">
                Study site
            </td>
            <c:forEach items="${results}" var="studyParticipantCrfItem" varStatus="status">
                <c:set var="schedule" value="${studyParticipantCrfItem.studyParticipantCrfSchedule}"/>
                <c:set var="participant"
                       value="${schedule.studyParticipantCrf.studyParticipantAssignment.participant}"/>
        <tr id="details_row_${status.index}" onmouseover="highlightrow('${status.index}');"
            onmouseout="removehighlight('${status.index}');">
            <td align="right">
                <div id="img_${status.index}" class="indIcon"
                     onclick="showPopUpMenu('${status.index}','${participant.id}','${schedule.id}',-105,-130)">
                    <img src="../../images/menu.png" alt=""/>
                </div>
            </td>
            <td class="data">
                    ${participant.displayName}
            </td>
            <td class="data">
                <tags:formatDate value="${schedule.startDate}"/>
            </td>
            <td class="data">
                    ${studyParticipantCrfItem.proCtcValidValue.value}
            </td>
            <td class="data">
                    ${schedule.studyParticipantCrf.studyParticipantAssignment.studySite.displayName}
            </td>
        </tr>
        </c:forEach>
    </table>
    <br/>
</chrome:box>
<div id="dropnoteDiv" class="ddnotediv shadowB" style="display:none;left:0;top:0">
    <div id="dropnoteinnerDiv" class="shadowr">
    </div>
</div>
</body>
</html>



