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

</head>
<body>
<c:if test="${param['successMessage']}">
    <chrome:flashMessage flashMessage="Form has been saved successfully"/>
</c:if>
<chrome:box>
    <form:form method="post" name="myForm">
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
                                <c:choose>
                                    <c:when test="${items[0].proCtcValidValue ne null && items[0].proCtcValidValue.displayOrder eq validValue.displayOrder}">

                                        <td>
                                            <input name="studyParticipantCrfItems[${items[1]}].proCtcValidValue"
                                                   type="radio"
                                                   value="${validValue.id}" checked> ${validValue.value} &nbsp;&nbsp;
                                        </td>
                                    </c:when>
                                    <c:otherwise>

                                        <td>
                                            <input name="studyParticipantCrfItems[${items[1]}].proCtcValidValue"
                                                   type="radio"
                                                   value="${validValue.id}"> ${validValue.value} &nbsp;&nbsp;
                                        </td>
                                    </c:otherwise>
                                </c:choose>
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
                                <c:choose>
                                    <c:when test="${items[0].proCtcValidValue ne null && items[0].proCtcValidValue.displayOrder eq validValue.displayOrder}">

                                        <td>
                                            <input name="studyParticipantCrfScheduleAddedQuestions[${items[1]}].proCtcValidValue"
                                                   type="radio"
                                                   value="${validValue.id}" checked> ${validValue} &nbsp;&nbsp;
                                        </td>
                                    </c:when>
                                    <c:otherwise>

                                        <td>
                                            <input name="studyParticipantCrfScheduleAddedQuestions[${items[1]}].proCtcValidValue"
                                                   type="radio"
                                                   value="${validValue.id}"> ${validValue} &nbsp;&nbsp;
                                        </td>
                                    </c:otherwise>
                                </c:choose>
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
                            <b>${symptom.key.term} </b>
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
                                <c:choose>
                                    <c:when test="${items[0].meddraValidValue ne null && items[0].meddraValidValue.displayOrder eq validValue.displayOrder}">

                                        <td>
                                            <input name="studyParticipantCrfScheduleAddedQuestions[${items[1]}].meddraValidValue"
                                                   type="radio"
                                                   value="${validValue.id}" checked> ${validValue} &nbsp;&nbsp;
                                        </td>
                                    </c:when>
                                    <c:otherwise>

                                        <td>
                                            <input name="studyParticipantCrfScheduleAddedQuestions[${items[1]}].meddraValidValue"
                                                   type="radio"
                                                   value="${validValue.id}"> ${validValue} &nbsp;&nbsp;
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tr>
                        <c:set var="myindex" value="${myindex + 1}"/>
                    </c:forEach>
                </c:forEach>


            </table>
        </div>
        <table width="100%" style="margin-top:10px;">
            <tr>
                <td align="right">
                    <tags:button color="blue" type="submit" id="flow-update"
                                 cssClass="next" value="Submit" icon="save"/>
                </td>
            </tr>
        </table>
    </form:form>
</chrome:box>

</body>
</html>