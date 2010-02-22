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
            padding-right: 10px;
            text-align: right;
            margin-bottom: 15px;
        }
    </style>
    <script type="text/javascript">
        var alreadySubmitted = false;
        var totalQuestionsOnPage = ${fn:length(command.currentPageQuestions)};
        function submitForm(direction) {
            if (!alreadySubmitted) {
                alreadySubmitted = true;
                document.myForm.direction.value = direction;
                document.myForm.submit();
            }
        }
        function selectValidValue(column, validValueDisplayOrder, questionIndexOnPage, validValueIndexForQuestion) {
            var x = document.getElementsByName('response' + questionIndexOnPage);
            x[validValueIndexForQuestion].checked = true;
            column.onmouseout = function() {
            };
            var elementName = 'currentPageQuestions[' + questionIndexOnPage + '].selectedValidValueId';
            document.myForm.elements[elementName].value = x[validValueIndexForQuestion].value;
            for (var i = 0; i < x.length; i++) {
                if (i != validValueIndexForQuestion) {
                    try {
                        var c = document.getElementById(i + '_column_' + questionIndexOnPage);
                        c.className = 'norm';
                        c.onmouseout = function() {
                            this.className = 'norm'
                        };
                    } catch(err) {
                    }
                }
            }
            if (questionIndexOnPage == 0) {
                if (validValueDisplayOrder > 0) {
                    showOtherQuestions();
                } else {
                    hideOtherQuestions();
                    clearResponsesForOtherQuestions();
                }
            }
        }

        function showOtherQuestions() {
            for (var i = 1; i < totalQuestionsOnPage; i++) {
                $('question_' + i).show();
            }
        }
        function hideOtherQuestions() {
            for (var i = 1; i < totalQuestionsOnPage; i++) {
                $('question_' + i).hide();
            }
        }
        function clearResponsesForOtherQuestions() {
            for (var i = 1; i < totalQuestionsOnPage; i++) {
                var x = document.getElementsByName('response' + i);
                for (var j = 0; j < x.length; j++) {
                    try {
                        var c = document.getElementById(j + '_column_' + i);
                        x[j].checked = false;
                        c.className = 'norm';
                        c.onmouseout = function() {
                            this.className = 'norm'
                        };
                        var elementName = 'currentPageQuestions[' + i + '].selectedValidValueId';
                        document.myForm.elements[elementName].value = '';
                    } catch(err) {
                    }
                }


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
                desc="Please think back ${command.schedule.studyParticipantCrf.crf.recallPeriod}"/>
    </div>
    <c:set var="showConditionalQuestions" value="false"/>
    <c:forEach items="${command.currentPageQuestions}" var="displayQuestion" varStatus="varStatus">
        <c:if test="${varStatus.index == 0 and displayQuestion.selectedValidValue.displayOrder > 0}">
            <c:set var="showConditionalQuestions" value="true"/>
        </c:if>
        <c:if test="${!showConditionalQuestions and varStatus.index > 0}">
            <c:set var="cssstyle" value="display:none"/>
        </c:if>
        <tags:formbuilderBox id="question_${varStatus.index}" style="${cssstyle}">
            <c:set var="colspan" value="${fn:length(displayQuestion.validValues)}"/>
            <input type="hidden"
                   name="currentPageQuestions[${varStatus.index}].selectedValidValueId"
                   value="${displayQuestion.selectedValidValue.id}"/>

            <table>
                <tr>
                    <td colspan="${colspan}">
                        <div class="label">
                                ${displayQuestion.questionText}:<br/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <c:forEach items="${displayQuestion.validValues}" var="validValue"
                               varStatus="validvaluestatus">
                        <tags:validvalue validValueId="${validValue.id}"
                                         title="${validValue.value}"
                                         selectedId="${displayQuestion.selectedValidValue.id}"
                                         displayOrder="${validValue.displayOrder}"
                                         questionIndexOnPage="${varStatus.index}"
                                         validValueIndexForQuestion="${validvaluestatus.index}"
                                />
                    </c:forEach>
                </tr>
            </table>
        </tags:formbuilderBox>
    </c:forEach>
    <table width=" 100%" style="margin-top:10px;">
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