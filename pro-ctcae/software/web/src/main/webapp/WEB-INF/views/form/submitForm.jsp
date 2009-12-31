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

        .formbuilderBox {
            padding-left: 2px;
        }

        .label {
            font-weight: bold;
            margin-left: 0.5em;
            margin-right: 0.5em;
            padding: 1px;
            font-size: 20px;
        }

        .currentPagediv {
            color: #666666;
            font-size: 8pt;
            padding-right: 200px;
            text-align: right;
            margin-bottom: 15px;
        }
    </style>
    <script type="text/javascript">
        var displayRules = new Array();
        var responses = new Array();
        var questions = new Array();
        var nextQuestionIndex = 0;
        var questionIndexes = new Array();
        var questionPage = new Array();

        <c:forEach items="${command.displayRules}" var="displayRule">
        displayRules['${displayRule.key}'] = '${displayRule.value}';
        </c:forEach>

        <c:forEach items="${command.studyParticipantCrfSchedule.studyParticipantCrfItems}" var="participantCrfItem">
        questions[nextQuestionIndex] = '${participantCrfItem.crfPageItem.id}';
        questionIndexes['${participantCrfItem.crfPageItem.id}'] = nextQuestionIndex;
        responses['${participantCrfItem.proCtcValidValue.id}'] = 'Y';
        questionPage['${participantCrfItem.crfPageItem.id}'] = ${participantCrfItem.crfPageItem.crfPage.pageNumber + 1};
        nextQuestionIndex++;
        </c:forEach>

        function gonext(crfitemindex, index, column) {
            var x = document.getElementsByName('response' + crfitemindex);
            x[index].checked = true;
            responses[x[index].value] = 'Y';
            column.onmouseout = function() {
            };
            var elementName = 'studyParticipantCrfSchedule.studyParticipantCrfItems[' + crfitemindex + '].proCtcValidValue';
            document.myForm.elements[elementName].value = x[index].value;
            for (var i = 0; i < x.length; i++) {
                if (i != index) {
                    try {
                        var c = document.getElementById(i + '_column_' + crfitemindex);
                        responses[x[i].value] = 'N';
                        c.className = 'norm';
                        c.onmouseout = function() {
                            this.className = 'norm'
                        };
                    } catch(err) {
                    }
                }
            }
            evaluateAllQuestions();
        }

        function evaluateAllQuestions() {
            for (var i = 0; i < questions.length; i++) {
                showHideQuestion(questions[i]);
            }
        }

        function hideQuestion(questionid) {
            $("question_" + questionid).hide();
            if (questionPage[questionid] == '${command.currentPageIndex}') {
                clearResponse(questionid);
            }
        }
        function clearResponse(questionid) {
            var x = document.getElementsByName('response' + questionIndexes[questionid]);
            for (var i = 0; i < x.length; i++) {
                var c = document.getElementById(i + '_column_' + questionIndexes[questionid]);
                x[i].checked = false;
                c.className = 'norm';
                c.onmouseout = function() {
                    this.className = 'norm'
                };
                responses[x[i].value] = 'N';
            }
            var elementName = 'studyParticipantCrfSchedule.studyParticipantCrfItems[' + questionIndexes[questionid] + '].proCtcValidValue';
            document.myForm.elements[elementName].value = '';
        }

        function showQuestion(questionid) {
            $("question_" + questionid).show();
        }

        function showHideQuestion(questionid) {
            if (isDisplay(questionid)) {
                showQuestion(questionid);
            } else {
                hideQuestion(questionid);
            }
        }

        function isDisplay(questionid) {
            if (questionPage[questionid] != '${command.currentPageIndex}') {
                return false;
            }
            var displayRule = displayRules[questionid];
            var rulesSatisfied = false;
            if (displayRule == '') {
                rulesSatisfied = true;
            } else {
                var myRules = displayRule.split('~');
                for (var i = 0; i < myRules.length; i++) {
                    if (myRules[i] != '' && responses[myRules[i]] == 'Y') {
                        rulesSatisfied = true;
                        break;
                    }
                }
            }
            return rulesSatisfied;
        }

        Event.observe(window, "load", function () {
            evaluateAllQuestions();
        })

        var alreadySubmitted = false;
        function submitForm(direction) {
            if (!alreadySubmitted) {
                alreadySubmitted = true;
                document.myForm.direction.value = direction;
                document.myForm.submit();
            }
        }
    </script>
</head>
<body>
<form:form method="post" name="myForm">
    <tags:hasErrorsMessage hideErrorDetails="false"/>
    <div class='progress-bar-outer'>
        <div class='progress-bar-inner' style="width: ${(command.currentPageIndex/command.totalPages)*150}px;"></div>
    </div>
    <div class="currentPagediv">
        Progress:
    </div>
    <div class="label" style="margin-bottom:10px;">
        <tags:recallPeriodFormatter
                desc="Please think back ${command.studyParticipantCrfSchedule.studyParticipantCrf.crf.recallPeriod}"/>
    </div>
    <c:forEach items="${command.studyParticipantCrfSchedule.studyParticipantCrfItems}" var="participantCrfItem"
               varStatus="crfitemstatus">
        <tags:formbuilderBox id="question_${participantCrfItem.crfPageItem.id}" style="display:none">

            <c:set var="crfPageItem" value="${participantCrfItem.crfPageItem}"/>
            <c:if test="${crfPageItem.crfItemAllignment eq 'Horizontal'}">
                <c:set var="colspan" value="${fn:length(crfPageItem.proCtcQuestion.validValues)}"/>
            </c:if>
            <input type="hidden"
                   name="studyParticipantCrfSchedule.studyParticipantCrfItems[${crfitemstatus.index}].proCtcValidValue"
                   value="${participantCrfItem.proCtcValidValue.id}"/>
            <table>
                <c:if test="${crfPageItem.instructions ne null}">
                    <tr>
                        <td colspan="${colspan}">
                            <div class="instructions">
                                <div class="summarylabel">Instructions</div>
                                <div class="summaryvalue">${crfPageItem.instructions}</div>
                            </div>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td colspan="${colspan}">
                        <div class="label">
                                ${crfPageItem.proCtcQuestion.questionText}
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="${colspan}">
                        <div class="label"></div>
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${crfPageItem.crfItemAllignment eq 'Horizontal'}">
                        <tr>
                            <c:forEach items="${crfPageItem.proCtcQuestion.validValues}" var="validValue"
                                       varStatus="validvaluestatus">
                                <tags:validvalue currentId="${validValue.id}"
                                                 title="${validValue.value}"
                                                 selectedId="${participantCrfItem.proCtcValidValue.id}"
                                                 crfitemindex="${crfitemstatus.index}"
                                                 index="${validvaluestatus.index}"
                                                 displayOrder="${validValue.displayOrder}"
                                                 questionDisplayOrder="${crfPageItem.proCtcQuestion.displayOrder}"/>
                            </c:forEach>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${crfPageItem.proCtcQuestion.validValues}" var="validValue"
                                   varStatus="validvaluestatus">
                            <tr>
                                <tags:validvalue currentId="${validValue.id}"
                                                 title="${validValue.value}"
                                                 selectedId="${participantCrfItem.proCtcValidValue.id}"
                                                 crfitemindex="${crfitemstatus.index}"
                                                 index="${validvaluestatus.index}"
                                                 displayOrder="${validValue.displayOrder}"
                                                 questionDisplayOrder="${crfPageItem.proCtcQuestion.displayOrder}"/>

                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </table>
        </tags:formbuilderBox>
    </c:forEach>
    <table width="100%" style="margin-top:10px;">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="left" width="50%">
                <c:if test="${command.currentPageIndex gt 1}">
                    <tags:button color="blue" icon="back" onclick="javascript:submitForm('back')" value="Back"/>
                </c:if>
            </td>
            <td align="right" width="50%">
                <c:choose>
                    <c:when test="${command.currentPageIndex le command.totalPages}">
                        <tags:button color="green" icon="next" onclick="javascript:submitForm('continue')"
                                     value="Continue"/>
                    </c:when>
                </c:choose>
            </td>
        </tr>
    </table>

</form:form>
</body>
</html>