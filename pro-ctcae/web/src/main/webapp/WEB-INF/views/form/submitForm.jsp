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
<c:set var="currentCrfItem"
       value="${command.studyParticipantCrfSchedule.studyParticipantCrfItems[command.currentIndex].crfItem}"/>
<c:if test="${currentCrfItem.crfItemAllignment eq 'Horizontal'}">
    <c:set var="colspan" value="${fn:length(currentQuestion.validValues)}"/>
</c:if>

<c:set var="reviewResponse"
       value="<img src='/ctcae/images/chrome/spacer.gif' height = '1' width='70%' /> (<a href=''>Review responses</a>)"/>


<form:form method="post" name="myForm">
    <chrome:box title="Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title}"
                autopad="true">
        <tags:hasErrorsMessage hideErrorDetails="false"/>
        <chrome:division title="Question ${command.currentIndex + 1} of ${command.totalQuestions} ${reviewResponse}">
            <table align="center">
                <c:if test="${currentCrfItem.instructions ne null}">
                    <tr>
                        <td colspan="${colspan}">
                            <div class="instructions">
                                <div class="summarylabel">Instructions</div>
                                <div class="summaryvalue">${currentCrfItem.instructions}</div>
                            </div>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td colspan="${colspan}">
                        <div class="label">
                            <c:if test="${currentCrfItem.responseRequired == true}">
                                <span class="required-indicator">*</span>
                            </c:if>
                                ${currentQuestion.questionText}
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="${colspan}">
                        <div class="label"></div>
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${currentCrfItem.crfItemAllignment eq 'Horizontal'}">
                        <tr>
                            <c:forEach items="${currentQuestion.validValues}" var="validValue" varStatus="status">
                                <tags:validvalue currentValue="${validValue.id}" title="${validValue.value}"
                                                 selectedValue="${command.studyParticipantCrfSchedule.studyParticipantCrfItems[command.currentIndex].proCtcValidValue.id}"
                                                 crfitemindex="${command.currentIndex}" index="${status.index}"/>
                            </c:forEach>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${currentQuestion.validValues}" var="validValue" varStatus="status">
                            <tr>
                                <tags:validvalue currentValue="${validValue.id}" title="${validValue.value}"
                                                 selectedValue="${command.studyParticipantCrfSchedule.studyParticipantCrfItems[command.currentIndex].proCtcValidValue.id}"
                                                 crfitemindex="${command.currentIndex}" index="${status.index}"/>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
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
                    <c:when test="${command.currentIndex < command.totalQuestions}">
                        <input onclick="document.myForm.direction.value='continue'" type="image"
                               src="/ctcae/images/blue/saveandcontinue_btn.png" alt="continue &raquo;"/>
                    </c:when>
                </c:choose>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>