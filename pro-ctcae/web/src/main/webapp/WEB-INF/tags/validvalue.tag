<%@ attribute name="currentValue"  type="java.lang.String" required="true"%>
<%@ attribute name="selectedValue" type="java.lang.String"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<td>
    <div class="label">
        <c:choose>
            <c:when test="${currentValue eq selectedValue}">
                <input type="radio"
                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${command.currentIndex}].proCtcValidValue"
                       value="${currentValue}" checked="true"/> ${title}
            </c:when>
            <c:otherwise>
                <input type="radio"
                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${command.currentIndex}].proCtcValidValue"
                       value="${currentValue}"/> ${title}
            </c:otherwise>
        </c:choose>
    </div>
</td>


