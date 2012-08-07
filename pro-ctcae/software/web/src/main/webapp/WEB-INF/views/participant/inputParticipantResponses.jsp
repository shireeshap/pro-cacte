<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<html>
<head>
    <script type="text/javascript">
        function saveAndBack(type, id) {
            if (type == 'back') {
                window.location = "../participant/edit?id=" + id + "&tab=3";
            } else {
                document.forms[0].submitType.value = type;
                document.forms[0].submit();
            }
        }

        function saveResponse(type) {
            if (type == 'submit') {
                var confirmStatus = confirm("You will not be able to make any changes to these responses after submitting the form. Press 'OK' to submit the form otherwise press 'Cancel'.");
                if (!confirmStatus) {
                    return;
                }
            }
            document.forms[0].submitType.value = type;
            document.forms[0].submit();
        }

        function playAudio(id) {
            var loc = window.location;
            document.getElementById("audio").src = loc.protocol + "//" + loc.host + ":" + loc.port + "/proctcae/pages/participant/playFile.wav?id=" + id;
        }
    </script>

</head>
<body>
<c:if test="${param['successMessage']}">
    <c:if test="${command.status.displayName eq 'In-progress'}">
        <chrome:flashMessage flashMessage="Form has been saved successfully"/>
    </c:if>

    <c:if test="${command.status.displayName eq 'Completed'}">
        <chrome:flashMessage flashMessage="Form has been submitted successfully"/>
    </c:if>
</c:if>
<c:if test="${command.status.displayName eq 'Completed'}">
    <c:set var="disabled" value="disabled"/>
</c:if>

<chrome:box>
<ctcae:form method="post" name="myForm">
<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}"/>

<input type="hidden" name="submitType" value=""/>

         <span style="float: right;position:relative;bottom:10px">
							    <a style="color:black" href="?id=${command.id}&lang=en">English</a>
		 <span style="color:black">|</span>
							    <a style="color:black" href="?id=${command.id}&lang=es">Spanish</a>
		 			    
		 </span>

<script>
    printDivCSS = new String('<link href="myprintstyle.css" rel="stylesheet" type="text/css">')
    function printDiv(divId) {
        window.frames["print_frame"].document.body.innerHTML = printDivCSS + document.getElementById(divId).innerHTML
        window.frames["print_frame"].window.focus()
        window.frames["print_frame"].window.print()
    }
</script>

<div id="printDiv">
<table>
    <tr>
        <td>
            <div class="label_nomargin"><b>Participant:</b></div>
        </td>
        <td>
            <div class="value_nomargin"> ${participantName}</div>
        </td>
    </tr>
    <tr>
        <td>
            <div class="label_nomargin"><b>Study:</b></div>
        </td>
        <td>
            <div class="value_nomargin">${study}</div>
        </td>
    </tr>
    <tr>
        <td>
            <div class="label_nomargin"><b>Form:</b></div>
        </td>
        <td>
            <div class="value_nomargin">${crf}</div>
        </td>
    </tr>
    <tr>
        <td>
            <div class="label_nomargin"><b>Start date:</b></div>
        </td>
        <td>
            <div class="value_nomargin"><tags:formatDate value="${startDate}"/></div>
        </td>
    </tr>
    <tr>
        <td>
            <div class="label_nomargin"><b>Due date:</b></div>
        </td>
        <td>
            <div class="value_nomargin"><tags:formatDate value="${dueDate}"/></div>
        </td>
    </tr>

</table>
<br>
<tags:recallPeriodFormatter desc="Please think back ${command.studyParticipantCrf.crf.recallPeriod}"/>
<div id="inputResponses">

    <table width="100%" cellpadding="3px" cellspacing="0px" border="0">
        <c:set var="myindex" value="1"/>
        <c:forEach items="${command.crfItemsBySymptom}" var="symptom">
            <tr>
                <td colspan="7"><br/></td>
            </tr>
            <tr style="background-color:#cccccc;">
                <td colspan="7">
                    <c:if test="${language eq 'en'}">
                        <b>${symptom.key.term} </b>
                    </c:if>
                    <c:if test="${language eq 'es'}">
                        <b>${symptom.key.proCtcTermVocab.termSpanish} </b>
                    </c:if>
                </td>
            </tr>
            <c:forEach items="${symptom.value}" var="items">
                <tr>
                    <td colspan="7">
                        <c:if test="${language eq 'en'}">
                            <b>${myindex}. ${items[0].crfPageItem.proCtcQuestion.questionText}</b>
                        </c:if>
                        <c:if test="${language eq 'es'}">
                            <b>${myindex}. ${items[0].crfPageItem.proCtcQuestion.proCtcQuestionVocab.questionTextSpanish}</b>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <c:forEach items="${items[0].crfPageItem.proCtcQuestion.validValues}" var="validValue">
                        <c:set var="checked" value=""/>
                        <c:set var="style" value=""/>
                        <c:if test="${items[0].proCtcValidValue ne null && items[0].proCtcValidValue.proCtcValidValueVocab.valueEnglish eq validValue.proCtcValidValueVocab.valueEnglish}">
                            <c:set var="checked" value="checked "/>
                            <c:set var="style" value="background-color:blue;color:white"/>
                        </c:if>
                        <td colspan="1">
                            <input name="studyParticipantCrfItems[${items[1]}].proCtcValidValue"
                                   type="radio"
                                   value="${validValue.id}" ${checked} ${disabled}>
                            <c:if test="${language eq 'en'}">
                                <span style="${style}">${validValue.value} &nbsp;&nbsp;</span>
                            </c:if>
                            <c:if test="${language eq 'es'}">
                                <span style="${style}">${validValue.proCtcValidValueVocab.valueSpanish} &nbsp;&nbsp;</span>
                            </c:if>
                        </td>
                    </c:forEach>
                </tr>
                <c:set var="myindex" value="${myindex + 1}"/>
            </c:forEach>
        </c:forEach>
        <c:forEach items="${command.participantAddedProCtcQuestionsBySymptom}" var="symptom">
            <tr>
                <td><br/></td>
            </tr>
            <tr style="background-color:#cccccc;">
                <td colspan="7">
                    <b>${symptom.key} </b>
                </td>
            </tr>
            <c:forEach items="${symptom.value}" var="items">
                <tr>
                    <c:choose>
                        <c:when test="${items[2]}">
                            <td colspan="6">
                                <c:if test="${language eq 'en'}">
                                    <b>${myindex}. ${items[0].proCtcQuestion.proCtcQuestionVocab.questionTextEnglish}</b>
                                </c:if>
                                <c:if test="${language eq 'es'}">
                                    <b>${myindex}. ${items[0].proCtcQuestion.proCtcQuestionVocab.questionTextSpanish}</b>
                                </c:if>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td colspan="7">
                                <c:if test="${language eq 'en'}">
                                    <b>${myindex}. ${items[0].meddraQuestion.meddraQuestionVocab.questionTextEnglish}</b>
                                </c:if>
                                <c:if test="${language eq 'es'}">
                                    <b>${myindex}. ${items[0].meddraQuestion.meddraQuestionVocab.questionTextSpanish}</b>
                                </c:if>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <c:choose>
                        <c:when test="${items[2]}">
                            <c:forEach items="${items[0].proCtcQuestion.validValues}" var="validValue">
                                <c:set var="checked" value=""/>
                                <c:set var="style" value=""/>
                                <c:if test="${items[0].proCtcValidValue ne null && items[0].proCtcValidValue.displayOrder eq validValue.displayOrder}">
                                    <c:set var="checked" value="checked"/>
                                    <c:set var="style" value="background-color:blue;color:white"/>
                                </c:if>
                                <td>
                                    <input name="studyParticipantCrfScheduleAddedQuestions[${items[1]}].proCtcValidValue"
                                           type="radio"
                                           value="${validValue.id}" ${checked} ${disabled}>
                                    <c:if test="${language eq 'en'}">
                                        <span style="${style}">${validValue.value} &nbsp;&nbsp;</span>
                                    </c:if>
                                    <c:if test="${language eq 'es'}">
                                        <span style="${style}">${validValue.proCtcValidValueVocab.valueSpanish} &nbsp;&nbsp;</span>
                                    </c:if>
                                </td>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${items[0].meddraQuestion.validValues}" var="validValue">
                                <c:set var="checked" value=""/>
                                <c:set var="style" value=""/>
                                <c:if test="${items[0].meddraValidValue ne null && items[0].meddraValidValue.displayOrder eq validValue.displayOrder}">
                                    <c:set var="checked" value="checked"/>
                                    <c:set var="style" value="background-color:blue;color:white"/>
                                </c:if>
                                <td>
                                    <input name="studyParticipantCrfScheduleAddedQuestions[${items[1]}].meddraValidValue"
                                           type="radio" value="${validValue.id}" ${checked} ${disabled}>
                                    <c:if test="${language eq 'en'}">
                                        <span style="${style}">${validValue.value} &nbsp;&nbsp;</span>
                                    </c:if>
                                    <c:if test="${language eq 'es'}">
                                        <span style="${style}">${validValue.meddraValidValueVocab.valueSpanish} &nbsp;&nbsp;</span>
                                    </c:if>
                                </td>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <c:set var="myindex" value="${myindex + 1}"/>
            </c:forEach>
        </c:forEach>
        <c:if test="${not empty command.filePath}">
            <tr>
                <td colspan="6"><br/></td>
            </tr>

            <tr style="background-color:#cccccc;">
                <td colspan="7"><b><tags:message code="participant.recording.title"/></b>
                </td>
            </tr>
            <tr>
                <td colspan="7">
                    <a href="#" onclick="playAudio('${command.id}')">
                        <img id="pShowImage_${command.id}" src="../../images/play_audio.png" height="45"
                             style=""/>
                    </a><br/>
                    <iframe id="audio" src="" frameborder="0" width="400" height="27">
                        <tags:message code="iframe.oops"/>
                    </iframe>
                </td>
            </tr>

        </c:if>
        <tr>
            <td colspan="7"><br/></td>
        </tr>
        <tr style="background-color:#cccccc;">
            <td colspan="7"><b><spring:message code="participant.verbatim"/></b></td>
        </tr>
        <tr>
            <td colspan="7">
                <c:choose>
                    <c:when test="${command.status.displayName eq 'Completed'}">
                    	<c:if test="${empty command.verbatim}">
                        	No additional symptoms were reported.
                        </c:if>
                        <c:if test="${!empty command.verbatim}">
                        	${command.verbatim}
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <form:textarea path="verbatim" cols="60" rows="2" id="verbatim"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
    <br>

</div>
</div>
</ctcae:form>
<c:choose>
    <c:when test="${command.status.displayName ne 'Completed'}">
        <table width="100%" style="margin-top:10px;">
            <tr>
                <td align="left">
                    <tags:button type="submit" color="blue" id="flow-prev"
                                 onclick="saveAndBack('back', ${command.studyParticipantCrf.studyParticipantAssignment.id});"
                                 value="Back" icon="Back"/>
                    <tags:button type="submit" color="blue" id="flow-prev"
                                 onclick="saveAndBack('saveandback', ${command.studyParticipantCrf.studyParticipantAssignment.id});"
                                 value="Save & Back" icon="Back"/>
                </td>
                <td align="left">

                </td>
                <td align="right">
                    <tags:button color="green" id="flow-update"
                                 cssClass="next" value="Save" icon="save" onclick="saveResponse('save');"/>

                    <tags:button color="blue" id="flow-update"
                                 cssClass="next" value="Submit" icon="save" onclick="saveResponse('submit');"/>
                </td>
            </tr>
        </table>
    </c:when>
    <c:otherwise>
        <table width="100%" style="margin-top:10px;">
            <tr>
                <td align="left">
                    <tags:button type="submit" color="blue" id="flow-prev"
                                 onclick="saveAndBack('back', ${command.studyParticipantCrf.studyParticipantAssignment.id});"
                                 value="Back" icon="Back"/>
                </td>
            </tr>
        </table>
    </c:otherwise>
</c:choose>
</chrome:box>
<iframe name=print_frame width=0 height=0 frameborder=0 src=about:blank></iframe>
</body>
</html>