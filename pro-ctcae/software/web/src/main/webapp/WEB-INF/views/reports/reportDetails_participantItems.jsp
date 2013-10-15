<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <style type="text/css">
        .shaded {
            background-color: #f1efef;
        }

        td.header-top {
            text-align: left;
            padding-left: 2px;
            white-space: nowrap;

        }

        td.data {
            text-align: left;
            padding-left: 2px;
        }
    </style>
</head>
<body>
<table class="widget" cellspacing="0" align="center" >
	<tr title="childTableRow_${pid}">
	    <td class="header-top"></td>
	    <td class="header-top">
	        Worst Response
	    </td>
	    <td class="header-top">
	        Response date
	    </td>
	    <td class="header-top">
	        Study site
	    </td>
	</tr>
	<c:forEach items="${results}" var="studyParticipantCrfItem" varStatus="status">
	    <c:set var="schedule" value="${studyParticipantCrfItem.studyParticipantCrfSchedule}"/>
	    <c:set var="participant" value="${schedule.studyParticipantCrf.studyParticipantAssignment.participant}"/>
	    <tr id="details_row_${status.index}_${pid}" onmouseover="highlightrow('${status.index}_${pid}');"
	        onmouseout="removehighlight('${status.index}_${pid}');" title="childTableRow_${pid}">
	        <td align="right" class="shaded">
	            <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
	               id="menuActions${schedule.id}"><span
	                    class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
	            <script>showPopUpMenu('${status.index}_${pid}', '${participant.id}', '${schedule.id}');</script>
	        </td>
	        <td class="data shaded">
	                ${studyParticipantCrfItem.proCtcValidValue.value}
	        </td>
	        <td class="data shaded">
	            <tags:formatDate value="${schedule.startDate}"/>
	            <c:if test="${schedule.cycleNumber ne null}">(C${schedule.cycleNumber},D${schedule.cycleDay})</c:if>
	        </td>
	        <td class="data shaded">
	                ${schedule.studyParticipantCrf.studyParticipantAssignment.studySite.displayName}
	        </td>
	    </tr>
	</c:forEach>
</table>
<div id="dropnoteDiv" class="ddnotediv shadowB" style="display:none;left:0;top:0;">
    <div id="dropnoteinnerDiv" class="shadowr">
    </div>
</div>
</body>
</html>



