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

        function hideme(divid) {
            $(divid).hide();
        }
        function showanswers(ischecked, divid) {
            if (ischecked) {
                $(divid).show();
            } else {
                hideme(divid);
            }
        }

    </script>


</head>
<body>
<chrome:flashMessage flashMessage="${command.flashMessage}"></chrome:flashMessage>
<form:form method="post" name="myForm">
    <chrome:box title="Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title}"
                autopad="true" message="false">
        <p>
            <b>Please select additional questions from below</b>
        </p>

        <c:forEach items="${command.arrangedQuestions}" var="term">
            <chrome:division title=" ${term.key}">

                <c:forEach items="${term.value}" var="question">
                    <tags:formbuilderBox>
                        <input type="checkbox" name="questionsByParticipants"
                               value="${question.id}"
                               onchange="showanswers(this.checked,'answer-div-${question.id}')"/> ${question.formattedQuestionText}
                        <br/>

                        <div id="answer-div-${question.id}">
                            <c:forEach items="${question.validValues}" var="validValue" varStatus="status">
                                <div class="norm" onmouseover="javascript:this.className='over';"
                                     onmouseout="javascript:this.className='norm';"
                                     onclick="document.getElementsByName('answer${question.id}')[${status.index}].checked=true"
                                     width="20%">
                                    <input type="radio" name="answer${question.id}"
                                           value="${validValue.id}"/>${validValue.displayName}
                                    <br/>
                                </div>
                            </c:forEach>
                        </div>
                    </tags:formbuilderBox>
                    <script type="text/javascript">
                        hideme('answer-div-${question.id}');
                    </script>
                </c:forEach>
            </chrome:division>
        </c:forEach>
    </chrome:box>
    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="left" width="50%">
                <input onclick="document.myForm.direction.value='back'" type="image"
                       src="/ctcae/images/blue/back_btn.png" alt="back &raquo;"/>
            </td>
            <td align="right" width="50%">
                <input onclick="document.myForm.direction.value='continue'" type="image"
                       src="/ctcae/images/blue/continue_btn.png" alt="continue &raquo;"/>
            </td>
        </tr>
    </table>

</form:form>
</body>
</html>