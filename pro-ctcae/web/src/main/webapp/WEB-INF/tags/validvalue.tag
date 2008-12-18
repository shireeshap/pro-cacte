<%@ attribute name="crfitemindex" type="java.lang.String" required="true" %>
<%@ attribute name="currentId" type="java.lang.String" required="true" %>
<%@ attribute name="selectedId" type="java.lang.String" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@ attribute name="questionType" type="java.lang.String" required="false" %>
<%@ attribute name="scaleValue" type="java.lang.String" required="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    function gonext${crfitemindex}(index) {
        document.myForm.direction.value = 'continue';
        var x = document.getElementsByName('response${crfitemindex}');
        x[index].checked = true;
        document.myForm.elements['studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemindex}].proCtcValidValue'].value = x[index].value;
    <c:if test="${questionType eq 'Severity'}">
        if (x[index].id > 0) {
            showQuestions();
        } else {
            hideQuestions();
        }
    </c:if>
    }
    function setValue(itemindex, value) {
        document.myForm.elements['studyParticipantCrfSchedule.studyParticipantCrfItems[' + itemindex + '].proCtcValidValue'].value = value;
    }

</script>

<td class="norm" onmouseover="javascript:this.className='over';" onmouseout="javascript:this.className='norm';"
    onclick="gonext${crfitemindex}('${index}')">
    <div class="label">
        <c:choose>
            <c:when test="${currentId eq selectedId}">

                <input type="radio"
                       name="response${crfitemindex}"
                       value="${currentId}" checked="true" id="${scaleValue}"/> ${title}
                <script type="text/javascript">
                    setValue('${crfitemindex}', '${currentId}');
                </script>
            </c:when>
            <c:otherwise>
                <input type="radio"
                       name="response${crfitemindex}"
                       value="${currentId}" id="${scaleValue}"/> ${title}
            </c:otherwise>
        </c:choose>
    </div>
</td>


