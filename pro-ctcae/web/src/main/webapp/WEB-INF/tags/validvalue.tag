<%@ attribute name="crfitemindex" type="java.lang.String" required="true" %>
<%@ attribute name="currentId" type="java.lang.String" required="true" %>
<%@ attribute name="selectedId" type="java.lang.String" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@ attribute name="questionType" type="java.lang.String" required="false" %>
<%@ attribute name="scaleValue" type="java.lang.String" required="false" %>

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
    function gonext${crfitemindex}(index) {
        document.myForm.direction.value = 'continue';
        var x = document.getElementsByName('studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemindex}].proCtcValidValue');
        x[index].checked = true;

        <c:if test="${questionType eq 'Severity'}">
           if(x[index].id > 0){
               showQuestions();
           }else{
                hideQuestions();     
           }
        </c:if>
    }
</script>

<td class="norm" onmouseover="javascript:this.className='over';" onmouseout="javascript:this.className='norm';"
    onclick="gonext${crfitemindex}('${index}')">
    <div class="label">
        <c:choose>
            <c:when test="${currentId eq selectedId}">

                <input type="radio"
                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemindex}].proCtcValidValue"
                       value="${currentId}" checked="true" id="${scaleValue}"/> ${title}
            </c:when>
            <c:otherwise>
                <input type="radio"
                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemindex}].proCtcValidValue"
                       value="${currentId}"  id="${scaleValue}"/> ${title}
            </c:otherwise>
        </c:choose>
    </div>
</td>


