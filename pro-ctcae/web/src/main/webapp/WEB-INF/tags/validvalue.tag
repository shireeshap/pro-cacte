<%@ attribute name="crfitemindex" type="java.lang.String" required="true" %>
<%@ attribute name="currentValue" type="java.lang.String" required="true" %>
<%@ attribute name="selectedValue" type="java.lang.String" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .norm {
        background: white;
        cursor: default;
    }

    .over {
        background: #3399ff;
        cursor: pointer;
    }
</style>
<script type="text/javascript">
    function gonext(index) {
        document.myForm.direction.value = 'continue';
        var x = document.getElementsByName('studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemindex}].proCtcValidValue');
        x[index].checked = true;
    }
</script>

<td class="norm" onmouseover="javascript:this.className='over';" onmouseout="javascript:this.className='norm';"
    onclick="gonext('${index}')">
    <div class="label">
        <c:choose>
            <c:when test="${currentValue eq selectedValue}">

                <input type="radio"
                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemindex}].proCtcValidValue"
                       value="${currentValue}" checked="true"/> ${title}
            </c:when>
            <c:otherwise>
                <input type="radio"
                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemindex}].proCtcValidValue"
                       value="${currentValue}"/> ${title}
            </c:otherwise>
        </c:choose>
    </div>
</td>


