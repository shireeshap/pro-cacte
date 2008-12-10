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

        .progress-bar-outer{
            border-style:solid;
            width:150px;
            border-color: #0066cc;
            border-width:1px;
            height:15px;
            float:right;
            margin-right:40px;
            margin-top:10px;
        }
        .progress-bar-inner{
            background-color:#0066cc;
            width:${(command.currentPageIndex/command.totalPages)*150}px;
            height:15px
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
            var x = document.getElementsByName('response' + questionid);
            for (var i = 0; i < x.length; i++) {
                x[i].checked = false;
            }
            document.myForm.elements['studyParticipantCrfSchedule.studyParticipantCrfItems[' + questionid + '].proCtcValidValue'].value = '';
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
        })

    </script>
</head>
<body>
<c:set var="currentPage" value="${command.pages[command.currentPageIndex]}"/>

<form:form method="post" name="myForm">
    <div class='progress-bar-outer'><div class='progress-bar-inner'></div></div>
    <chrome:box title="Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title} "
                autopad="true" message="false">
        <tags:hasErrorsMessage hideErrorDetails="false"/>

        <c:forEach items="${currentPage}" var="currentStudyParticipantCrfItem">
            <input type="hidden"
                   name="studyParticipantCrfSchedule.studyParticipantCrfItems[${currentStudyParticipantCrfItem.itemIndex}].proCtcValidValue"
                   value=""/>
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
                                ${currentCrfItem.proCtcQuestion.formattedQuestionText}
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
    </chrome:box>
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