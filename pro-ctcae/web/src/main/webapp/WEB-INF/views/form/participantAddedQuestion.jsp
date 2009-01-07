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
         function gonext(crfitemindex, index, column) {
            var x = document.getElementsByName('response' + crfitemindex);
            x[index].checked = true;
            var elementName = 'studyParticipantCrfSchedule.studyParticipantCrfScheduleAddedQuestions[' + crfitemindex + '].proCtcValidValue';
            document.myForm.elements[elementName].value = x[index].value;
        }

        function clearResponse(questionindex) {
            var x = document.getElementsByName('response' + questionindex);
            for (var i = 0; i < x.length; i++) {
                x[i].checked = false;
            }
            var elementName = 'studyParticipantCrfSchedule.studyParticipantCrfScheduleAddedQuestions[' + questionindex + '].proCtcValidValue';
            document.myForm.elements[elementName].value = '';
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

    <c:forEach items="${command.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantCrfAddedQuestions}"
               var="participantCrfItem"
               varStatus="crfitemstatus">
        <tags:formbuilderBox id="question_${participantCrfItem.proCtcQuestion.id}">

            <input type="hidden"
            name="studyParticipantCrfSchedule.studyParticipantCrfScheduleAddedQuestions[${crfitemstatus.index}].proCtcValidValue"
            value=""/>
            <table>
                <tr>
                    <td colspan="${fn:length(participantCrfItem.proCtcQuestion.validValues)}">
                        <div class="label">
                                ${participantCrfItem.proCtcQuestion.formattedQuestionText}
                            (<a href="javascript:clearResponse('${crfitemstatus.index}')">clear
                            this response</a>)
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
                                         title="${validValue.displayName}"                                 
                                         selectedId=""
                                         crfitemindex="${crfitemstatus.index}"
                                         index="${validvaluestatus.index}"/>
                    </c:forEach>
                </tr>
                <tr>
                    <td>
                        <chrome:division title=" " message="false"/>
                    </td>
                </tr>
            </table>
        </tags:formbuilderBox>
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