<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>
<head>
    <script type="text/javascript">
        function saveResponse(type) {
            if (type == 'submit') {
                if (!confirm("You will not be able to make any changes to these responses after submitting the form. Press 'OK' to submit the form otherwise press 'Cancel'.")) {
                    return;
                }
            }
            document.forms[0].submitType.value = type;
            document.forms[0].submit();
        }
    </script>

</head>
<body>
<c:if test="${param['successMessage']}">
    <c:if test="${command.status eq 'In-progress'}">
        <chrome:flashMessage flashMessage="Form has been saved successfully"/>
    </c:if>
    <c:if test="${command.status eq 'Completed'}">
        <chrome:flashMessage flashMessage="Form has been submitted successfully"/>
    </c:if>
</c:if>
<c:if test="${command.status eq 'Completed'}">
    <c:set var="disabled" value="disabled"/>
</c:if>
<chrome:box>
    <form:form method="post" name="myForm">
        <input type="hidden" name="submitType" value=""/>
        <tags:recallPeriodFormatter desc="Please think back ${command.studyParticipantCrf.crf.recallPeriod}"/>
        <div id="inputResponses">
            <table width="100%" cellpadding="3px" cellspacing="0px" border="0">
                <c:set var="myindex" value="1"/>
                <c:forEach items="${command.crfItemsBySymptom}" var="symptom">
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
                            <c:forEach items="${items[0].crfPageItem.proCtcQuestion.validValues}" var="validValue">
                                <c:set var="checked" value=""/>
                                <c:set var="style" value=""/>
                                <c:if test="${items[0].proCtcValidValue ne null && items[0].proCtcValidValue.displayOrder eq validValue.displayOrder}">
                                    <c:set var="checked" value="checked "/>
                                    <c:set var="style" value="background-color:blue;color:white"/>
                                </c:if>
                                <td>
                                    <input name="studyParticipantCrfItems[${items[1]}].proCtcValidValue"
                                           type="radio"
                                           value="${validValue.id}" ${checked} ${disabled}> <span
                                        style="${style}">${validValue.value} &nbsp;&nbsp;</span>
                                </td>
                            </c:forEach>
                        </tr>
                        <c:set var="myindex" value="${myindex + 1}"/>
                    </c:forEach>
                </c:forEach>
                <c:forEach items="${command.participantAddedProCtcQuestionsBySymptom}" var="symptom">
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
                                <b>${myindex}. ${items[0].proCtcQuestion.questionText}</b>
                            </td>
                        </tr>
                        <tr>
                            <c:forEach items="${items[0].proCtcQuestion.validValues}" var="validValue">
                                <c:set var="checked" value=""/>
                                <c:set var="style" value=""/>
                                <c:if test="${items[0].proCtcValidValue ne null && items[0].proCtcValidValue.displayOrder eq validValue.displayOrder}">
                                    <c:set var="checked" value="checked"/>
                                    <c:set var="style" value="background-color:blue;color:white"/>
                                </c:if>
                                <td>
                                    <input name="studyParticipantCrfScheduleAddedQuestions[${items[1]}].proCtcValidValue"
                                           type="radio"
                                           value="${validValue.id}" ${checked} ${disabled}> <span
                                        style="${style}">${validValue.value} &nbsp;&nbsp;</span>
                                </td>
                            </c:forEach>
                        </tr>
                        <c:set var="myindex" value="${myindex + 1}"/>
                    </c:forEach>
                </c:forEach>
                <c:forEach items="${command.participantAddedMeddraQuestionsBySymptom}" var="symptom">
                    <tr>
                        <td><br/></td>
                    </tr>
                    <tr style="background-color:#cccccc;">
                        <td colspan="5">
                            <b>${symptom.key} </b>
                        </td>
                    </tr>
                    <c:forEach items="${symptom.value}" var="items">
                        <tr>
                            <td colspan="5">
                                <b>${myindex}. ${items[0].meddraQuestion.questionText}</b>
                            </td>
                        </tr>
                        <tr>
                            <c:forEach items="${items[0].meddraQuestion.validValues}" var="validValue">
                                <c:set var="checked" value=""/>
                                <c:set var="style" value=""/>
                                <c:if test="${items[0].meddraValidValue ne null && items[0].meddraValidValue.displayOrder eq validValue.displayOrder}">
                                    <c:set var="checked" value="checked"/>
                                    <c:set var="style" value="background-color:blue;color:white"/>
                                </c:if>
                                <td>
                                    <input name="studyParticipantCrfScheduleAddedQuestions[${items[1]}].meddraValidValue"
                                           type="radio"
                                           value="${validValue.id}" ${checked} ${disabled}> <span
                                        style="${style}">${validValue.value} &nbsp;&nbsp;</span>
                                </td>
                            </c:forEach>
                        </tr>
                        <c:set var="myindex" value="${myindex + 1}"/>
                    </c:forEach>
                </c:forEach>


            </table>
        </div>
        <c:if test="${command.status ne 'Completed'}">
            <table width="100%" style="margin-top:10px;">
                <tr>
                    <td align="right">
                        <tags:button color="green" id="flow-update"
                                     cssClass="next" value="Save" icon="save" onclick="saveResponse('save');"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <tags:button color="blue" id="flow-update"
                                     cssClass="next" value="Submit" icon="save" onclick="saveResponse('submit');"/>
                    </td>
                </tr>
            </table>
        </c:if>
    </form:form>
</chrome:box>

</body>
</html>