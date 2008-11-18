<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <style type="text/css">
        .label {
            width: 12em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
        }

        #studyDetails td.label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            width: 12em;
            padding: 1px;
        }
    </style>
    <script type="text/javascript">
        function submit(qindex) {
            document.myForm.questionIndex.value = qindex;
            document.myForm.submit();
        }
    </script>
</head>
<body>
<form:form method="post" name="myForm">
    <input type="hidden" name="questionIndex" value="-1">
    <input type="hidden" name="direction" value="jump">
    <chrome:flashMessage flashMessage="${command.flashMessage}"></chrome:flashMessage>

    <chrome:box title="Review">

        <div class="instructions">

            <div class="summarylabel">Title</div>
            <div class="summaryvalue">${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title}</div>
        </div>
        <table id="formbuilderTable">
            <tr>
                <td id="left">
                    Questions
                    <c:forEach items="${command.studyParticipantCrfSchedule.studyParticipantCrfItems}"
                               var="studyParticipantCrfItem" varStatus="status">
                        <tags:formbuilderBox>
                            <c:choose>
                                <c:when test="${studyParticipantCrfItem.proCtcValidValue != null}">
                                    ${studyParticipantCrfItem.crfItem.displayOrder} : <a
                                        href="javascript:submit('${studyParticipantCrfItem.crfItem.displayOrder-1}')">${studyParticipantCrfItem.crfItem.proCtcQuestion.questionText}</a>
                                </c:when>
                                <c:otherwise>
                                    ${studyParticipantCrfItem.crfItem.displayOrder} : ${studyParticipantCrfItem.crfItem.proCtcQuestion.questionText}
                                </c:otherwise>
                            </c:choose>
                            <ul>
                                <c:forEach items="${studyParticipantCrfItem.crfItem.proCtcQuestion.validValues}"
                                           var="proCtcValidValue">
                                    <c:choose>
                                        <c:when test="${proCtcValidValue.id == studyParticipantCrfItem.proCtcValidValue.id}">
                                            <li><b><u>${proCtcValidValue.value}</u></b></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li>${proCtcValidValue.value}</li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </ul>
                        </tags:formbuilderBox>
                        <br>
                    </c:forEach>
                </td>
            </tr>
        </table>
    </chrome:box>
    <table width="100%">
        <tr>
            <c:choose>
                <c:when test="${command.direction == 'continue' || command.unansweredQuestionIndex >= command.totalQuestions}">
                    <td align="right" width="50%">
                        <input onclick="document.myForm.direction.value='save'" type="image"
                               src="/ctcae/images/blue/submit_btn.png" alt="save"/>
                    </td>
                </c:when>
                <c:otherwise>
                    <td align="right" width="50%">
                        <input onclick="document.myForm.questionIndex.value='${command.unansweredQuestionIndex}'"
                               type="image"
                               src="/ctcae/images/blue/continue_btn.png" alt="continue &raquo;"/>
                    </td>
                </c:otherwise>
            </c:choose>
        </tr>
    </table>
</form:form>
</body>
</html>