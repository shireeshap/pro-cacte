<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
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
            font-size: 12px;
        }

        .norm {
            cursor: default;
            width: 15%;
        }

        .over {
            background: #3399ff;
            cursor: pointer;
            width: 15%;
        }
    </style>
    <script type="text/javascript">
        var hiddenIds = '';
        var pages = new Array();


        Event.observe(window, "load", function () {
            hideReview();
        })
        function hideReview() {
            $('formbuilderTable').hide();
        }

        function showReview() {
            $('formbuilderTable').show();
            hideQuestions(hiddenIds);
        }

        function registerToHide(pageindex, itemindex, response) {
            if (response == '') {
                hiddenIds = hiddenIds + ',' + itemindex;
            }
            if (typeof(pages[pageindex]) == 'undefined') {
                pages[pageindex] = itemindex;
            } else {
                pages[pageindex] = pages[pageindex] + ',' + itemindex;
            }
        }


        function hideQuestions(hidethem) {
            if (typeof(hidethem) != 'undefined') {
                var idsArray = hidethem.split(',');
                for (var i = 0; i < idsArray.length; i++) {
                    if (idsArray[i] != '') {
                        hideQuestion(idsArray[i]);
                    }
                }
            }
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
        function showQuestions(showthem) {
            if (typeof(showthem) != 'undefined') {
                var idsArray = showthem.split(',');
                for (var i = 0; i < idsArray.length; i++) {
                    if (idsArray[i] != '') {
                        showQuestion(idsArray[i]);
                    }
                }
            }
        }

        function showHideFor(pageindex, type, itemindex, responseindex) {
            var x = document.getElementsByName('response' + itemindex);
            x[responseindex].checked = true;
            document.myForm.elements['studyParticipantCrfSchedule.studyParticipantCrfItems[' + itemindex + '].proCtcValidValue'].value = x[responseindex].value;


            if (type == 'Severity') {
                if (x[responseindex].id > 0) {
                    showQuestions(pages[pageindex]);
                } else {
                    hideQuestions(pages[pageindex]);
                }
            }
        }

        function setValue(itemindex, value) {
            document.myForm.elements['studyParticipantCrfSchedule.studyParticipantCrfItems[' + itemindex + '].proCtcValidValue'].value = value;
        }

    </script>


</head>
<body>
<chrome:flashMessage flashMessage="${command.flashMessage}"></chrome:flashMessage>
<form:form method="post" name="myForm">
    <chrome:box title="Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title}"
                autopad="true" message="false">

        <chrome:division title="Submit">
            <p>
                <b>
                    You are about to submit the form. You can not change the responses after submission.
                    <br/>
                    You can <a href="javascript:showReview();">click here</a> to review your responses and make changes.
                    <br/>
                    If you want to add questions relating to any other symptom, please <a href="../form/addquestion">click
                    here.</a>
                </b>
            </p>
        </chrome:division>
        <table id="formbuilderTable">
            <tr>
                <td id="left">
                    <c:forEach items="${command.pages}" var="page" varStatus="pageindex">
                        <c:forEach items="${page}" var="studyParticipantCrfItem">
                            <tags:formbuilderBox id="question_${studyParticipantCrfItem.itemIndex}">
                                ${studyParticipantCrfItem.crfItem.proCtcQuestion.formattedQuestionText}<br/>
                                <input type="hidden"
                                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${studyParticipantCrfItem.itemIndex}].proCtcValidValue"
                                       value=""/>
                                <c:forEach items="${studyParticipantCrfItem.crfItem.proCtcQuestion.validValues}"
                                           var="validValue" varStatus="status">
                                    <div class="norm" onmouseover="javascript:this.className='over';"
                                         onmouseout="javascript:this.className='norm';"
                                         onclick="showHideFor('${pageindex.index}','${studyParticipantCrfItem.crfItem.proCtcQuestion.proCtcQuestionType}','${studyParticipantCrfItem.itemIndex}','${status.index}')"
                                         width="20%">

                                        <c:choose>
                                            <c:when test="${studyParticipantCrfItem.proCtcValidValue.id eq validValue.id}">
                                                <input type="radio"
                                                       name="response${studyParticipantCrfItem.itemIndex}"
                                                       value="${validValue.id}" checked="true"
                                                       id="${validValue.value}"/> ${validValue.displayName}
                                                <script type="text/javascript">
                                                    setValue('${studyParticipantCrfItem.itemIndex}', '${validValue.id}');
                                                </script>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio"
                                                       name="response${studyParticipantCrfItem.itemIndex}"
                                                       value="${validValue.id}"
                                                       id="${validValue.value}"/> ${validValue.displayName}
                                            </c:otherwise>
                                        </c:choose>
                                        <br/>
                                    </div>
                                </c:forEach>
                            </tags:formbuilderBox>
                            <c:if test="${studyParticipantCrfItem.crfItem.proCtcQuestion.proCtcQuestionType ne 'Severity'}">
                                <script type="text/javascript">
                                    registerToHide('${pageindex.index}', '${studyParticipantCrfItem.itemIndex}', '${studyParticipantCrfItem.proCtcValidValue}');
                                </script>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </td>
            </tr>
        </table>
    </chrome:box>
    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr align="right">
            <td>
                <input onclick="document.myForm.direction.value='save'" type="image"
                       src="/ctcae/images/blue/submit_btn.png" alt="save &raquo;"/>
            </td>
        </tr>
    </table>


</form:form>
</body>
</html>