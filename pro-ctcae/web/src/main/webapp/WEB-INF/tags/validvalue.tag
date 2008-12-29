<%@ attribute name="crfitemindex" type="java.lang.String" required="true" %>
<%@ attribute name="currentId" type="java.lang.String" required="true" %>
<%@ attribute name="selectedId" type="java.lang.String" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<td class="norm" onmouseover="javascript:this.className='over';" onmouseout="javascript:this.className='norm';"
    onclick="gonext('${crfitemindex}','${index}',this)">
    <div class="label">
        <c:choose>
            <c:when test="${currentId eq selectedId}">
                <input type="radio"
                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemindex}].proCtcValidValue"
                       value="${currentId}" checked="true"/> ${title}
            </c:when>
            <c:otherwise>
                <input type="radio"
                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemindex}].proCtcValidValue"
                       value="${currentId}"/> ${title}
            </c:otherwise>
        </c:choose>
    </div>
</td>


