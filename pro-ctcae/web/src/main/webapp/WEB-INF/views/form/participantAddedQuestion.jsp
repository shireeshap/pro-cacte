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

        .currentPagediv {
            color: #666666;
            font-size: 8pt;
            padding-right: 200px;
            text-align: right;
            margin-bottom: 15px;
        }

        .progress-bar-outer {
            border-style: solid;
            width: 150px;
            border-color: #0066cc;
            border-width: 1px;
            height: 15px;
            float: right;
            margin-right: 40px;
        }

        .progress-bar-inner {
            background-color: #0066cc;
            width: ${(command.currentPageIndex/command.totalPages)*150}px;
            height: 15px
        }
    </style>
    <script type="">

        var displayRules = new Array();
        var questions = new Array();
        var i = 0;
        var responses = new Array();
        var questionindexes = new Array();


        function gonext(crfitemindex, index, column, displayOrder, participantCrfItemId, questionDisplayOrder) {
            var x = document.getElementsByName('response' + crfitemindex);
            x[index].checked = true;
            responses[x[index].value] = 'Y';
            var elementName = 'studyParticipantCrfSchedule.studyParticipantCrfScheduleAddedQuestions[' + crfitemindex + '].proCtcValidValue';
            document.myForm.elements[elementName].value = x[index].value;
            for (var i = 0; i < x.length; i++) {
                if (i != index) {
                    responses[x[i].value] = 'N';
                }
            }
            if (questionDisplayOrder == '1') {
                if (displayOrder == '0') {
                    document.myForm.deletedQuestions.value = participantCrfItemId;
                } else {
                    document.myForm.deletedQuestions.value = '-' + participantCrfItemId;
                }
                evaluateAllQuestions();
            }
        }

        function clearResponse(questionindex) {
            var x = document.getElementsByName('response' + questionindex);
            for (var i = 0; i < x.length; i++) {
                x[i].checked = false;
            }
            var elementName = 'studyParticipantCrfSchedule.studyParticipantCrfScheduleAddedQuestions[' + questionindex + '].proCtcValidValue';
            document.myForm.elements[elementName].value = '';
        }


        Event.observe(window, "load", function () {
            evaluateAllQuestions();
        })


        function evaluateAllQuestions() {
            for (var i = 0; i < questions.length; i++) {
                showHideQuestion(questions[i]);
            }
        }


        function showHideQuestion(questionid) {
            if (isDisplay(questionid)) {
                showQuestion(questionid);
            } else {
                hideQuestion(questionid);
            }
        }

        function showQuestion(questionid) {
            $("question_" + questionid).show();
        }

        function hideQuestion(questionid) {
            $("question_" + questionid).hide();
            clearResponse(questionindexes[questionid]);
        }

        function isDisplay(questionid) {
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


    </script>
</head>
<body>
<form:form method="post" name="myForm">
    <div class='progress-bar-outer'>
        <div class='progress-bar-inner'></div>
    </div>
    <tags:hasErrorsMessage hideErrorDetails="false"/>
    <div class="currentPagediv">
        Progress:
    </div>
    <input type="hidden"
           name="deletedQuestions"
           value=""/>

    <c:forEach items="${command.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantCrfAddedQuestions}"
               var="participantCrfItem"
               varStatus="crfitemstatus">

        <c:if test="${(participantCrfItem.pageNumber + 1)  eq command.currentPageIndex}">
            <script type="text/javascript">
                questions[i] = ${participantCrfItem.proCtcQuestion.id};
                questionindexes['${participantCrfItem.proCtcQuestion.id}'] = i;
                i++;
                displayRules['${participantCrfItem.proCtcQuestion.id}'] = '';
                responses['${command.studyParticipantCrfSchedule.studyParticipantCrfScheduleAddedQuestions[crfitemstatus.index].proCtcValidValue.id}'] = 'Y';
                <c:forEach items="${participantCrfItem.proCtcQuestion.proCtcQuestionDisplayRules}" var="rule">
                displayRules['${participantCrfItem.proCtcQuestion.id}'] = displayRules['${participantCrfItem.proCtcQuestion.id}'] + '~' + ${rule.proCtcValidValue.id};
                </c:forEach>
            </script>
            <tags:formbuilderBox id="question_${participantCrfItem.proCtcQuestion.id}" style="display:none">

                <input type="hidden"
                       name="studyParticipantCrfSchedule.studyParticipantCrfScheduleAddedQuestions[${crfitemstatus.index}].proCtcValidValue"
                       value="${command.studyParticipantCrfSchedule.studyParticipantCrfScheduleAddedQuestions[crfitemstatus.index].proCtcValidValue.id}"/>
                <table>
                    <tr>
                        <td colspan="${fn:length(participantCrfItem.proCtcQuestion.validValues)}">
                            <div class="label">
                                    ${participantCrfItem.proCtcQuestion.questionText}
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="label"></div>
                        </td>
                    </tr>
                    <tr>
                        <c:forEach items="${participantCrfItem.proCtcQuestion.validValues}" var="validValue"
                                   varStatus="validvaluestatus">
                            <tags:validvalue currentId="${validValue.id}"
                                             title="${validValue.value}"
                                             selectedId="${command.studyParticipantCrfSchedule.studyParticipantCrfScheduleAddedQuestions[crfitemstatus.index].proCtcValidValue.id}"
                                             crfitemindex="${crfitemstatus.index}"
                                             index="${validvaluestatus.index}"
                                             displayOrder="${validValue.displayOrder}"
                                             participantCrfItemId="${participantCrfItem.id}"
                                             questionDisplayOrder="${participantCrfItem.proCtcQuestion.displayOrder}"/>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>
                            <chrome:division title=" " message="false"/>
                        </td>
                    </tr>
                </table>
            </tags:formbuilderBox>
        </c:if>
    </c:forEach>
    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="left" width="50%">
                <c:if test="${command.currentPageIndex gt 1}">
                    <input onclick="document.myForm.direction.value='back'" type="image"
                           src="/ctcae/images/blue/back_btn.png" alt="back &raquo;"/>
                </c:if>
            </td>
            <td align="right" width="50%">
                <c:choose>
                    <c:when test="${command.currentPageIndex le command.totalPages}">
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