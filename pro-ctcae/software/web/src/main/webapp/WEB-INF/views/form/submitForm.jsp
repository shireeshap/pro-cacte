<%@ page import="gov.nih.nci.ctcae.web.participant.ParticipantCommand" %>
<%@ page import="gov.nih.nci.ctcae.web.form.SubmitFormCommand" %>
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
            font-size: 12px;
            padding-right: 0px;
            text-align: right;
            margin-bottom: 10px;
        }

        .pagesLeftdiv {
            color: #666666;
            font-size: 11px;
            padding-right: 43px;
            text-align: right;
            margin-bottom: 15px;
            float: right;
        }

        .pagesLeftdiv div {
            float: left;
            margin-left: 15px;
        }

        .formbuilderboxTable .selected {
            display: table-cell;

        }

        .selected .label {
            background:#477e20 url(/proctcae/images/green-selected.png) no-repeat 100% 0;
            cursor: pointer;
            color: white;
            text-shadow: 0 -1px #245808;
            border: 1px solid #477e20;
            vertical-align: middle;
            display: table-cell;
            padding: 0 10px;
            text-align: center;
            line-height: 23px;
            height: 47px;
            max-height: 47px;
            -moz-border-radius: 6px;
            -webkit-border-radius: 6px;
            border-radius: 6px;
            -moz-box-shadow: 0 1px 1px white inset;
            -webkit-box-shadow: 0 1px 1px white inset;
            box-shadow: 0 1px 1px white inset;
            width: 98px;

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
//            alert(questionIndexOnPage);
//            alert(validValueIndexForQuestion);

            var x = document.getElementsByName('response' + questionIndexOnPage);
            x[validValueIndexForQuestion].checked = true;
            column.onmouseout = function() {
                column.className = 'selected';
            };
            var elementName = 'currentPageQuestions[' + questionIndexOnPage + '].selectedValidValueId';
            document.myForm.elements[elementName].value = x[validValueIndexForQuestion].value;
            column.className = 'selected';
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

        function getClassName(elId, notNullClass, nullClass) {
            if ($F(elId)) return notNullClass;
            return nullClass;
        }

    </script>
</head>
<body>
<form:form method="post" name="myForm">
    <tags:hasErrorsMessage hideErrorDetails="false"/>

   <%-- <div style="padding-left:796px;font-size:12px">
        <spring:message code="current.page"/>: ${command.newPageIndex}
        <spring:message code="pages.left"/> ${command.totalPages}
    </div>

    <div class="val" style="margin-bottom:10px; float:left;font-size:12px">
        <tags:recallPeriodFormatter desc="Please think back ${command.schedule.studyParticipantCrf.crf.recallPeriod}"/>
    </div>

    <div class="currentPagediv">
       <div> <spring:message code="progress"/>: </div>
        <div class='progress-bar-outer'>
            <div class='progress-bar-inner' style="width: ${(command.newPageIndex/command.totalPages)*150}px;"></div>
        </div>
    </div>--%>
    <div style="padding-left:835px;font-size:12px;color: #666666;">
        <spring:message code="current.page"/>: ${command.newPageIndex}
        <spring:message code="pages.left"/> ${command.totalPages}
    </div>
    <table cellspacing="0">
        <tr>
            <td width="80%">
                 <div class="val" style="float:left;font-size:18px;margin-left:10px">
                    <tags:recallPeriodFormatter desc="Please think back ${command.schedule.studyParticipantCrf.crf.recallPeriod}"/>
                </div>
            </td>
            <td width="4%">
                <div style="font-size:12px;color: #666666;">
                 <spring:message code="progress"/>:
                </div>
            </td>
            <td valign="middle" width="20%">
                 <div class='progress-bar-outer'>
                    <div class='progress-bar-inner' style="width: ${(command.newPageIndex/command.totalPages)*150}px;"></div>
                </div>
            </td>
        </tr>
    </table>


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
                    <td colspan="${colspan}" >
                        <div class="label">
                            <c:set var="lang"
                                   value="${sessionScope['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE']}"/>
                            <c:if test="${lang eq null || lang eq ''}">
                                <c:set var="lang" value="en"/>
                            </c:if>
                            <c:set var="homeweblanguage"
                                   value="${command.schedule.studyParticipantCrf.studyParticipantAssignment.homeWebLanguage}"/>
                            <c:set var="ivrslanguage"
                                   value="${command.schedule.studyParticipantCrf.studyParticipantAssignment.ivrsLanguage}"/>
                                <%--<c:set var="_command" value="${command}"/>--%>

                            <c:if test="${lang eq 'en'}">
                                ${displayQuestion.questionText}?<br/>
                            </c:if>
                                <%--<%  pageContext.getAttribute("_command"); %>--%>
                            <c:if test="${lang eq 'es' }">
                                &#191;${displayQuestion.questionTextSpanish}?<br/>
                            </c:if>
                                <%--<c:if test="${homeweblanguage eq null || homeweblanguage eq ''}">--%>
                                <%--<c:if test="${ivrslanguage eq 'ENGLISH'}">--%>
                                <%--${displayQuestion.questionText}?<br/>--%>
                                <%--</c:if>--%>
                                <%--<c:if test="${ivrslanguage eq 'SPANISH'}">--%>
                                <%--&#191;${displayQuestion.questionTextSpanish}?<br/>--%>
                                <%--</c:if>--%>
                                <%--<c:if test="${ivrslanguage eq null || ivrslanguage eq ''}">--%>
                                <%--${displayQuestion.questionText}?<br/>--%>
                                <%--</c:if>--%>
                                <%--</c:if>--%>
                        </div>
                    </td>
                </tr>
                </table>
                <table align="center">
                    <tr>
                    <c:forEach items="${displayQuestion.validValues}" var="validValue"
                               varStatus="validvaluestatus">
                        <c:if test="${lang eq 'en'}">
                            <tags:validvalue validValueId="${validValue.id}"
                                             title="${validValue.value}"
                                             selectedId="${displayQuestion.selectedValidValue.id}"
                                             displayOrder="${validValue.displayOrder}"
                                             questionIndexOnPage="${varStatus.index}"
                                             validValueIndexForQuestion="${validvaluestatus.index}"
                                    />
                        </c:if>
                        <c:if test="${lang eq 'es'}">
                            <tags:validvalue validValueId="${validValue.id}"
                                             title="${validValue.valueSpanish}"
                                             selectedId="${displayQuestion.selectedValidValue.id}"
                                             displayOrder="${validValue.displayOrder}"
                                             questionIndexOnPage="${varStatus.index}"
                                             validValueIndexForQuestion="${validvaluestatus.index}"
                                    />
                        </c:if>
                        <%--<c:if test="${homeweblanguage eq null || homeweblanguage eq ''}">--%>
                        <%--<c:if test="${ivrslanguage eq 'ENGLISH'}">--%>
                        <%--<tags:validvalue validValueId="${validValue.id}"--%>
                        <%--title="${validValue.value}"--%>
                        <%--selectedId="${displayQuestion.selectedValidValue.id}"--%>
                        <%--displayOrder="${validValue.displayOrder}"--%>
                        <%--questionIndexOnPage="${varStatus.index}"--%>
                        <%--validValueIndexForQuestion="${validvaluestatus.index}"--%>
                        <%--/>--%>
                        <%--</c:if>--%>
                        <%--<c:if test="${ivrslanguage eq 'SPANISH'}">--%>
                        <%--<tags:validvalue validValueId="${validValue.id}"--%>
                        <%--title="${validValue.valueSpanish}"--%>
                        <%--selectedId="${displayQuestion.selectedValidValue.id}"--%>
                        <%--displayOrder="${validValue.displayOrder}"--%>
                        <%--questionIndexOnPage="${varStatus.index}"--%>
                        <%--validValueIndexForQuestion="${validvaluestatus.index}"--%>
                        <%--/>--%>
                        <%--</c:if>--%>
                        <%--<c:if test="${ivrslanguage eq null || ivrslanguage eq ''}">--%>
                        <%--<tags:validvalue validValueId="${validValue.id}"--%>
                        <%--title="${validValue.value}"--%>
                        <%--selectedId="${displayQuestion.selectedValidValue.id}"--%>
                        <%--displayOrder="${validValue.displayOrder}"--%>
                        <%--questionIndexOnPage="${varStatus.index}"--%>
                        <%--validValueIndexForQuestion="${validvaluestatus.index}"--%>
                        <%--/>--%>
                        <%--</c:if>--%>
                        <%--</c:if>--%>
                    </c:forEach>
                </tr>
            </table>
        </tags:formbuilderBox>

    </c:forEach>
    <table width="100%" style="margin-top:10px;" cellspacing="10">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="right" width="50%">
                <spring:message code="back" var="back"/>
                <c:if test="${command.newPageIndex gt 1}">
                    <a href="#" class="btn big-blue-left"
                       onclick="javascript:submitForm('back')"><span>${back}</span></a>
                </c:if>
            </td>
            <td align="left" width="50%">
                <spring:message code="next" var="next"/>
                <c:choose>
                    <c:when test="${command.newPageIndex le command.totalPages}">
                        <a href="#" class="btn huge-green"
                           onclick="javascript:submitForm('continue')"><span>${next}</span></a>
                    </c:when>
                </c:choose>
            </td>
        </tr>
    </table>

</form:form>
</body>
</html>