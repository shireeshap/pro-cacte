<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<table width="100%">
    <tr>
        <td style="padding-left:20px">
            <c:choose>
                <c:when test="${fn:length(crfs) > 1}">
                    Multiple forms have events scheduled for ${participant.displayName} on ${date}.
                    Please select the forms for which you want to delete the scheduled event(s)<br>
                    <c:forEach items="${crfs}" var="crf">
                        <input type="checkbox" name="selectedForms"
                               value="${crf.id}">${crf.title}
                        <br/>
                    </c:forEach>
                    <br/>
                </c:when>
                <c:otherwise>
                    You are about to delete the scheduled events for ${participant.displayName} on form: ${crfs[0].title}
                    <input type="hidden" name="selectedForms" value="${crfs[0].id}"/>
                    <br/>
                    <br/>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <td style="padding-left:20px">Would you like to delete only this event, all events, or this and all
            following
            events?<br></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td style="padding-left:20px">
            <input type="button" value="Only this event"
                   onclick="parent.addRemoveSchedule('${index}','${day}','del' )"/>
            &nbsp;&nbsp;&nbsp;
            <input type="button" value="This and all following events"
                   onclick="parent.addRemoveSchedule('${index}','${day}','delallfuture')"/>
            &nbsp;&nbsp;&nbsp;
            <input type="button" value="All events" onclick="parent.addRemoveSchedule('${index}','${day}','delall' )"/>
            &nbsp;&nbsp;&nbsp;
            <input type="button" value="Cancel" onclick="parent.addRemoveSchedule('${index}','${day}','cancel')"/>
        </td>
    </tr>
</table>