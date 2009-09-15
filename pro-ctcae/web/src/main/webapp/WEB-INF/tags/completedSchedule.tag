<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="completedSchedule" type="gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule"
             required="true" %>
<c:set var="myindex" value="1"/>

<table width="100%" cellpadding="3px" cellspacing="0px" border="0">
    <c:set var="myindex" value="1"/>
    <c:forEach items="${completedSchedule.symptomItems}" var="symptom">
        <tr>
            <td><br/></td>
        </tr>
        <tr style="background-color:#cccccc;">
            <td colspan="5">
                <b>${symptom.key.term} </b>
            </td>
        </tr>
        <c:forEach items="${symptom.value}" var="items">
            <tr>
                <td colspan="5">
                    <b>${myindex}. ${items[0].crfPageItem.proCtcQuestion.questionText}</b>
                </td>
            </tr>
            <tr>

                <td>
                    Answer: <u><i>${items[0].proCtcValidValue.value}</i></u>
                </td>

            </tr>
            <c:set var="myindex" value="${myindex + 1}"/>
        </c:forEach>
    </c:forEach>
</table>
<c:forEach items="${completedSchedule.studyParticipantCrfScheduleAddedQuestions}" var="spCrfAddedQuestion">
    <c:if test="${spCrfAddedQuestion.proCtcQuestion.questionText ne null}">
        <chrome:box>
            <b>${myindex}. ${spCrfAddedQuestion.proCtcQuestion.questionText}</b>
            <br/>
            Answer: <u><i>${spCrfAddedQuestion.proCtcValidValue.value}</i></u>
            <c:set var="myindex" value="${myindex + 1}"/>
        </chrome:box>
    </c:if>
</c:forEach>