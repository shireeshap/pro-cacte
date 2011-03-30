<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<table width="100%">
    <tr>
        <td style="padding-left:20px">
            <c:choose>
                <c:when test="${fn:length(crfs) > 1}">
                    Multiple forms have been assigned to the ${participant.displayName}.
                    Please select the forms for which you want to schedule an event<br>
                    <c:forEach items="${crfs}" var="crf">
                        <input type="checkbox" name="selectedForms" <c:if test="${crf.value}">disabled="true"</c:if>
                               value="${crf.key.id}">${crf.key.title}
                        <br/>
                    </c:forEach>
                    <br/>
                </c:when>
                <c:otherwise>
                    You are about to add a new event for ${participant.displayName} on form: ${firstCrf.title}
                    <input type="hidden" name="selectedForms" value="${firstCrf.id}"/>
                    <br/>
                    <br/>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <td style="padding-left:20px">Would you like to add a new event on ${date}?<br></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td style="padding-left:20px">
            <input type="button" value="Yes"
                   onclick="parent.addRemoveSchedule('${index}','${day}','add' )"/>
            &nbsp;&nbsp;&nbsp;
            <input type="button" value="No"
                   onclick="parent.addRemoveSchedule('${index}','${day}','cancel')"/>
        </td>
    </tr>
</table>