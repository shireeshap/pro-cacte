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
        } .label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            padding: 1px;
            font-size: 20px;
        } #taskbar {
            font-weight: bold;
            padding-top: 12px;
        } #taskbar:after {
            content: "Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title}";
        }
		.formbuilderboxTable {
			margin-bottom:30px;
		}
    </style>
    <script type="text/javascript">
        var hiddenIds = '';
        var severityQuestionAnswerId = '';

        function setSeverityQuestionAnswer(answerId) {
            severityQuestionAnswerId = answerId;
        }
        function registerToHide(questionid) {
            hiddenIds = hiddenIds + ',' + questionid;
        }
        function hideQuestion(questionid) {
            $("question_" + questionid).hide();
            var x = document.getElementsByName('studyParticipantCrfSchedule.studyParticipantCrfItems[' + questionid + '].proCtcValidValue');
            for(var i = 0; i < x.length; i++){
                x[i].checked=false;
            }
        }

        function showQuestion(questionid) {
            $("question_" + questionid).show();
        }
        function showQuestions() {
            var idsArray = hiddenIds.split(',');
            for (var i = 0; i < idsArray.length; i++) {
                if (idsArray[i] != '') {
                    showQuestion(idsArray[i]);
                }
            }
        }

        function hideQuestions() {
            var idsArray = hiddenIds.split(',');
            for (var i = 0; i < idsArray.length; i++) {
                if (idsArray[i] != '') {
                    hideQuestion(idsArray[i]);
                }
            }
        }
        function showHideQuestions() {
            if (severityQuestionAnswerId == '' || severityQuestionAnswerId == '0') {
                hideQuestions();
            } else {
                showQuestions();
            }
        }

        Event.observe(window, "load", function () {
            showHideQuestions();
        })    </script>
</head>
<body>
<c:set var="currentPage" value="${command.pages[command.currentPageIndex]}"/>

<form:form method="post" name="myForm">
        <tags:hasErrorsMessage hideErrorDetails="false"/>
        <chrome:division title="Page ${command.currentPageIndex + 1} of ${command.totalPages}" message="false">
           <tags:formbuilderBox> 
			<c:forEach items="${currentPage}" var="currentStudyParticipantCrfItem">
                <c:set var="currentCrfItem" value="${currentStudyParticipantCrfItem.crfItem}"/>

                <c:if test="${currentCrfItem.crfItemAllignment eq 'Horizontal'}">
                    <c:set var="colspan" value="${fn:length(currentCrfItem.proCtcQuestion.validValues)}"/>
                </c:if>

                <table id="question_${currentStudyParticipantCrfItem.itemIndex}">
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
                                    ${currentCrfItem.proCtcQuestion.questionText}
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
                                <c:forEach items="${currentCrfItem.proCtcQuestion.validValues}" var="validValue"
                                           varStatus="status">
                                    <tags:validvalue currentId="${validValue.id}"
                                                     title="${validValue.displayName}"
                                                     selectedId="${currentStudyParticipantCrfItem.proCtcValidValue.id}"
                                                     crfitemindex="${currentStudyParticipantCrfItem.itemIndex}"
                                                     index="${status.index}"
                                                     questionType="${currentCrfItem.proCtcQuestion.proCtcQuestionType}"
                                                     scaleValue="${validValue.value}"/>
                                </c:forEach>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${currentCrfItem.proCtcQuestion.validValues}" var="validValue"
                                       varStatus="status">
                                <tr>
                                    <tags:validvalue currentId="${validValue.id}"
                                                     title="${validValue.displayName}"
                                                     selectedId="${currentStudyParticipantCrfItem.proCtcValidValue.id}"
                                                     crfitemindex="${currentStudyParticipantCrfItem.itemIndex}"
                                                     index="${status.index}"
                                                     questionType="${currentCrfItem.proCtcQuestion.proCtcQuestionType}"
                                                     scaleValue="${validValue.value}"/>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td>
                            <chrome:division title=" " message="false"/>
                        </td>
                    </tr>
                </table>
                <c:choose>
                    <c:when test="${currentCrfItem.proCtcQuestion.proCtcQuestionType eq 'Severity'}">
                        <script type="text/javascript">
                            setSeverityQuestionAnswer('${currentStudyParticipantCrfItem.proCtcValidValue.value}');
                        </script>
                    </c:when>
                    <c:otherwise>
                        <script type="text/javascript">
                            registerToHide(${currentStudyParticipantCrfItem.itemIndex});
                        </script>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
			</tags:formbuilderBox>
        </chrome:division>
    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="left" width="50%">
                <c:if test="${command.currentPageIndex > 0}">
                    <input onclick="document.myForm.direction.value='back'" type="image"
                           src="/ctcae/images/blue/back_btn.png" alt="back &raquo;"/>
                </c:if>
            </td>
            <td align="right" width="50%">
                <c:choose>
                    <c:when test="${command.currentPageIndex < command.totalPages}">
                        <input onclick="document.myForm.direction.value='continue'" type="image"
                               src="/ctcae/images/blue/continue_btn.png" alt="continue &raquo;"/>
                    </c:when>
                </c:choose>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
