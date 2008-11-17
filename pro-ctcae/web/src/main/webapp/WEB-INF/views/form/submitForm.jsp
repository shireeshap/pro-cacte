<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <style type="text/css">

        div.row div.value {
            white-space: normal;
        }

        .label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            padding: 1px;
            font-size: 20px;
        }
    </style>
</head>
<body>
<c:set var="currentQuestion"
       value="${command.studyParticipantCrfSchedule.studyParticipantCrfItems[command.currentIndex].crfItem.proCtcQuestion}"/>
<c:if test="${!(command.currentIndex == 0 && command.unansweredQuestionIndex == 0)}">
    <c:set var="reviewResponse"
           value="<img src='/ctcae/images/chrome/spacer.gif' height = '1' width='70%' /> (<a href=''>Review responses</a>)"/>
</c:if>


<form:form method="post" name="myForm">
    <chrome:box title="Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title}"
                autopad="true">
        <tags:hasErrorsMessage hideErrorDetails="false"/>

        <chrome:division title="Question ${command.currentIndex + 1} of ${command.totalQuestions} ${reviewResponse}">
            <table align="center">
                <tr>
                    <td>
                        <div class="label">${currentQuestion.questionText}</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="label"></div>
                    </td>
                </tr>

                <c:forEach items="${currentQuestion.validValues}" var="validValue">

                    <tr>
                        <td>
                            <div class="label">
                                <c:choose>
                                    <c:when test="${validValue.id == command.studyParticipantCrfSchedule.studyParticipantCrfItems[command.currentIndex].proCtcValidValue.id}">
                                        <input type="radio"
                                               name="studyParticipantCrfSchedule.studyParticipantCrfItems[${command.currentIndex}].proCtcValidValue"
                                               value="${validValue.id}" checked="true"/> ${validValue.value}

                                    </c:when>
                                    <c:otherwise>
                                        <input type="radio"
                                               name="studyParticipantCrfSchedule.studyParticipantCrfItems[${command.currentIndex}].proCtcValidValue"
                                               value="${validValue.id}"/> ${validValue.value}

                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </chrome:division>
    </chrome:box>
    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="left" width="50%">
                <c:if test="${command.currentIndex > 0}">
                    <input onclick="document.myForm.direction.value='back'" type="image"
                           src="/ctcae/images/blue/back_btn.png" alt="back &raquo;"/>
                </c:if>
            </td>
            <td align="right" width="50%">
                <c:choose>
                    <c:when test="${command.currentIndex < command.totalQuestions-1}">
                        <input onclick="document.myForm.direction.value='continue'" type="image"
                               src="/ctcae/images/blue/continue_btn.png" alt="continue &raquo;"/>
                    </c:when>
                    <c:otherwise>
                        <input onclick="document.myForm.direction.value='review'" type="image"
                               src="/ctcae/images/blue/continue_btn.png" alt="continue"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>