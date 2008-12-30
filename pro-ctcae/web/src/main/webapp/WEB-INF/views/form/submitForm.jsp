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
            width: ${(1/5)*150}px;
            height: 15px
        }
    </style>
    <script type="text/javascript">
        var displayRules = new Array();
        var responses = new Array();
        var questions = new Array();
        var nextQuestionIndex = 0;
        var reverseDisplayRules = new Array();

        <c:forEach items="${command.displayRules}" var="displayRule">
            displayRules['${displayRule.key}'] = '${displayRule.value}';
        </c:forEach>

        <c:forEach items="${command.reverseDisplayRules}" var="reverseDisplayRule">
            reverseDisplayRules['${reverseDisplayRule.key}'] = '${reverseDisplayRule.value}';
        </c:forEach>

        <c:forEach items="${command.studyParticipantCrfSchedule.studyParticipantCrfItems}" var="participantCrfItem">
            questions[nextQuestionIndex++] = '${participantCrfItem.crfPageItem.id}';
        </c:forEach>

        function gonext(crfitemindex, index, column) {
            var x = document.getElementsByName('studyParticipantCrfSchedule.studyParticipantCrfItems[' + crfitemindex + '].proCtcValidValue');
            x[index].checked = true;
            responses[x[index].value] = 'Y';
            //column.onmouseout="javascript:this.className='over';";
            for (var i = 0; i < x.length; i++) {
                if (i != index) {
                    responses[x[i].value] = 'N';
                }
            }
            showNextQuestion(crfitemindex);
        }

        function hideAllQuestions() {
            for (var i = 0; i < questions.length; i++) {
                if (questions[i] != '') {
                    hideQuestion(questions[i]);
                }
            }
        }

        function hideQuestion(questionid) {
            $("question_" + questionid).hide();
            var x = document.getElementsByName('studyParticipantCrfSchedule.studyParticipantCrfItems[' + questionid + '].proCtcValidValue');
            for (var i = 0; i < x.length; i++) {
                x[i].checked = false;
            }
        }

        function showQuestion(questionid) {
            $("question_" + questionid).show();
        }

        function showNextQuestion(currentQuestionIndex) {
            nextQuestionIndex = parseInt(currentQuestionIndex) + 1;
            var questionid = questions[nextQuestionIndex];
            if(isDisplay(questionid)){
                showQuestion(questionid);
            }else{
                hideQuestion(questionid);
                showNextQuestion(nextQuestionIndex);
            }
        }

        function isDisplay(questionid){
            var displayRule = displayRules[questionid];
            var rulesSatisfied = false;
            if (displayRule == '') {
                rulesSatisfied = true;
            } else {
                var myRules = displayRule.split('~');
                for (var i = 0; i < myRules.length; i++) {
                    if (responses[myRules[i]] == 'Y') {
                        rulesSatisfied= true;
                    }
                }
            }
            return rulesSatisfied;
        }

        Event.observe(window, "load", function () {
            hideAllQuestions();
            showNextQuestion(-1);
        })
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

    <c:forEach items="${command.studyParticipantCrfSchedule.studyParticipantCrfItems}" var="participantCrfItem"
               varStatus="crfitemstatus">
        <tags:formbuilderBox id="question_${participantCrfItem.crfPageItem.id}">

            <c:set var="crfPageItem" value="${participantCrfItem.crfPageItem}"/>
            <c:if test="${crfPageItem.crfItemAllignment eq 'Horizontal'}">
                <c:set var="colspan" value="${fn:length(crfPageItem.proCtcQuestion.validValues)}"/>
            </c:if>

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
                                ${crfPageItem.proCtcQuestion.formattedQuestionText}
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
                                                 title="${validValue.displayName}"
                                                 selectedId="${participantCrfItem.proCtcValidValue.id}"
                                                 crfitemindex="${crfitemstatus.index}"
                                                 index="${validvaluestatus.index}"/>
                            </c:forEach>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${crfPageItem.proCtcQuestion.validValues}" var="validValue"
                                   varStatus="validvaluestatus">
                            <tr>
                                <tags:validvalue currentId="${validValue.id}"
                                                 title="${validValue.displayName}"
                                                 selectedId="${participantCrfItem.proCtcValidValue.id}"
                                                 crfitemindex="${crfitemstatus.index}"
                                                 index="${validvaluestatus.index}"/>
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
        </tags:formbuilderBox>
    </c:forEach>
</form:form>
</body>
</html>