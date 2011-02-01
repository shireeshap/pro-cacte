<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<table>
    <tr>
        <td style="padding-left:20px">
            <c:choose>
                <c:when test="${fn:length(crfsList) > 1}">
                    Multiple forms have events scheduled for <b>${participant.displayName}</b> on <b>${date}</b>.
                    Please select the forms for which you want to delete the scheduled event(s)<br>
                    <c:forEach items="${crfsList}" var="crfMap">
                        <input type="checkbox" name="selectedForms"<c:if test="${crfMap.value}">disabled="true"</c:if>
                               value="${crfMap.key.id}">${crfMap.key.title}
                        <br/>
                    </c:forEach>
                    <br/>
                </c:when>
                <c:otherwise>
                    You are about to delete the scheduled event(s) on <b>${date}</b> for
                    <b>${participant.displayName}</b> on form: <b>${firstCrf.title}</b>
                    <input type="hidden" name="selectedForms" value="${firstCrf.id}"/>
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