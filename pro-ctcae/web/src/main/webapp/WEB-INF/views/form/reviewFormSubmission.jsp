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
    <tags:javascriptLink name="submit_btn_animation"/>
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

        #left-panel {
            padding-left: 15px;
            width: 60%;
            float: left;
            font-size: 17px;
        }

        #left-panel a {
            text-decoration: none;
        }

        #left-panel a:hover {
            text-decoration: underline;
        }

        #right-panel {
            width: 35%;
            padding: 5px;
            float: right;
            position: relative;
            height: 265px;
        }

        #submit_btn {
            height: 0px;
            padding-top: 201px;
            overflow: hidden;
            width: 200px;
            background-image: url( ../../images/blue/submit_sprite.png );
            position: relative;
            display: block;
            outline: none;
            left: 80px;
        }

        #submit_btn:hover {
            cursor: pointer;
        }

        h2 {
            font-weight: normal;
        }

        #left-panel h1 {
            font-size: 2em;
        }

    </style>
    <script type="text/javascript">
        var displayRules = new Array();
        var responses = new Array();
        var questions = new Array();
        var nextQuestionIndex = 0;
        var questionIndexes = new Array();

        <c:forEach items="${command.displayRules}" var="displayRule">
        displayRules['${displayRule.key}'] = '${displayRule.value}';
        </c:forEach>

        <c:forEach items="${command.studyParticipantCrfSchedule.studyParticipantCrfItems}" var="participantCrfItem">
        questions[nextQuestionIndex] = '${participantCrfItem.crfPageItem.id}';
        questionIndexes['${participantCrfItem.crfPageItem.id}'] = nextQuestionIndex;
        responses['${participantCrfItem.proCtcValidValue.id}'] = 'Y';
        nextQuestionIndex++;
        </c:forEach>

        function gonext(crfitemindex, index, column) {
            var x = document.getElementsByName('response' + crfitemindex);
            x[index].checked = true;
            responses[x[index].value] = 'Y';
            var elementName = 'studyParticipantCrfSchedule.studyParticipantCrfItems[' + crfitemindex + '].proCtcValidValue';
            document.myForm.elements[elementName].value = x[index].value;
            //column.onmouseout="javascript:this.className='over';";
            for (var i = 0; i < x.length; i++) {
                if (i != index) {
                    responses[x[i].value] = 'N';
                }
            }
            evaluateAllQuestions();
        }

        function evaluateAllQuestions() {
            for (var i = 0; i < questions.length; i++) {
                showHideQuestion(questions[i]);
            }
        }

        function hideAllQuestions() {
            for (var i = 0; i < questions.length; i++) {
                hideQuestion((questions[i]), false);
            }
        }

        function hideQuestion(questionid, eraseanswer) {
            $("question_" + questionid).hide();
            if (eraseanswer) {
                clearResponse(questionid);
            }
        }
        function clearResponseAndEvaluate(questionid) {
            clearResponse(questionid);
            evaluateAllQuestions();
        }
        function clearResponse(questionid) {
            var x = document.getElementsByName('response' + questionIndexes[questionid]);
            for (var i = 0; i < x.length; i++) {
                x[i].checked = false;
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
                hideQuestion(questionid, true);
            }
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

        Event.observe(window, "load", function () {
            hideAllQuestions();
        })
    </script>
</head>
<body>
<form:form method="post" name="myForm">
    <input type="hidden" name="direction" value="save"/>

    <div style="clear:both;">
        <div id="left-panel">
            <h1>Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title}</h1>
            You are about to submit this form.<br/>
            You can not change the responses after submission.
            <br/>
            <br/>
            You can:<br/>
            <a href="javascript:evaluateAllQuestions();"><img src="<tags:imageUrl name="blue/undo.png" />" alt=""/>
                Review your responses and make changes</a>
            <br/>
            <a href="../form/addquestion"><img src="<tags:imageUrl name="blue/edit_add.png" />" alt=""/> Add questions
                relating to other symptoms</a>

        </div>
        <div id="right-panel">
            <a onmousedown="javascript:activate_button()" id="submit_btn">Submit</a>

            <h2 style="margin-left:151px; margin-top:10px; color:#999;">Submit</h2>
        </div>
    </div>
    <table id="formbuilderTable">
        <tr>
            <td id="left">
                <c:forEach items="${command.studyParticipantCrfSchedule.studyParticipantCrfItems}"
                           var="participantCrfItem"
                           varStatus="crfitemstatus">
                <tags:formbuilderBox id="question_${participantCrfItem.crfPageItem.id}">
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
                                    ${crfPageItem.proCtcQuestion.formattedQuestionText}
                                (<a href="javascript:clearResponseAndEvaluate('${crfPageItem.id}')">clear this
                                response</a>)
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
                </table>
                </tags:formbuilderBox>
                </c:forEach>
    </table>
</form:form>
</body>
</html>