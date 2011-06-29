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


<html>
<head>
    <script type="text/javascript">
        function saveResponse(type) {
            if (type == 'submit') {
                if (!confirm("You will not be able to make any changes to these responses after submitting the form. Press 'OK' to submit the form otherwise press 'Cancel'.")) {
                    return;
                }
            }
            document.forms[0].submitType.value = type;
            document.forms[0].submit();
        }

     	function playAudio(id){
     		document.getElementById("audio").src="http://localhost:8080/proctcae/pages/participant/wavFile.htm?id="+id;
     	}
    </script>

</head>
<body>
<c:if test="${param['successMessage']}">
    <c:if test="${command.status eq 'In-progress'}">
        <chrome:flashMessage flashMessage="Form has been saved successfully"/>
    </c:if>
    <c:if test="${command.status eq 'Completed'}">
        <chrome:flashMessage flashMessage="Form has been submitted successfully"/>
    </c:if>
</c:if>
<c:if test="${command.status eq 'Completed'}">
    <c:set var="disabled" value="disabled"/>
</c:if>

<chrome:box>
    <form:form method="post" name="myForm">
        <input type="hidden" name="submitType" value=""/>
        <tags:recallPeriodFormatter desc="Please think back ${command.studyParticipantCrf.crf.recallPeriod}"/>
         <span style="float: right;position:relative;bottom:10px">
							    <a style="color:black" href="?id=${command.id}&lang=en">English</a>
		 <span style="color:black">|</span>
							    <a style="color:black" href="?id=${command.id}&lang=es">Spanish</a></span>
        <div id="inputResponses">

            <table width="100%" cellpadding="3px" cellspacing="0px" border="0">
                <c:set var="myindex" value="1"/>
                <%--<c:set var="homeweblanguage"--%>
                       <%--value="${command.studyParticipantCrf.studyParticipantAssignment.homeWebLanguage}"/>--%>
                <%--<c:if test="${homeweblanguage eq null || homeweblanguage eq ''}">--%>
                    <%--<c:set var="homeweblanguage" value="ENGLISH"/>--%>
                <%--</c:if>--%>
                <c:forEach items="${command.crfItemsBySymptom}" var="symptom">
                    <tr>
                        <td colspan="6"><br/></td>
                    </tr>
                    <tr style="background-color:#cccccc;">
                        <td colspan="6">
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
                            <td colspan="6">
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
                        <td colspan="6">
                                <%--<b>${symptom.key.term} </b>--%>
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
                            <td colspan="6">
                                    <%--<b>${myindex}. ${items[0].proCtcQuestion.questionText}</b>--%>
                                <c:if test="${language eq 'en'}">
                                    <b>${myindex}. ${items[0].proCtcQuestion.questionText}</b>
                                </c:if>
                                <c:if test="${language eq 'es'}">
                                    <b>${myindex}. ${items[0].proCtcQuestion.proCtcQuestionVocab.questionTextSpanish}</b>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
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
                                    <%--<span style="${style}">${validValue.value} &nbsp;&nbsp;</span>--%>
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
                <c:forEach items="${command.participantAddedMeddraQuestionsBySymptom}" var="symptom">
                    <tr>
                        <td><br/></td>
                    </tr>
                    <tr style="background-color:#cccccc;">
                        <td colspan="6">
                            <b>${symptom.key} </b>
                        </td>
                    </tr>
                    <c:forEach items="${symptom.value}" var="items">
                        <tr>
                            <td colspan="6">
                                <c:if test="${language eq 'en'}">
                                    <b>${myindex}. ${items[0].meddraQuestion.questionText}</b>
                                </c:if>
                                <c:if test="${language eq 'es'}">
                                    <b>${myindex}. ${items[0].meddraQuestion.meddraQuestionVocab.questionTextSpanish}</b>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
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
                        </tr>
                        <c:set var="myindex" value="${myindex + 1}"/>
                    </c:forEach>
                </c:forEach>
				
				<c:if test="${not empty command.filePath}">
					<tr>
                        <td colspan="6"><br/></td>
                    </tr>
					<tr style="background-color:#cccccc;">
	                    <td colspan="6"> <b><tags:message code="participant.recording.title"/></b>
	                    </td>
	                 </tr>
	                <tr>
		              <td  colspan="6">
		              	<a href="#" onclick="playAudio('${command.id}')"><tags:message code="participant.recording.message"/></a><br/>
		                 <iframe id="audio" src="" frameborder="0" width="400" height="27"  >
						    	<tags:message code="iframe.oops"/>
						</iframe>
					  </td>
					</tr> 
				</c:if>
            </table>
        </div>
        
        <c:if test="${command.status ne 'Completed'}">
            <table width="100%" style="margin-top:10px;">
                <tr>
                    <td align="right">
                        <tags:button color="green" id="flow-update"
                                     cssClass="next" value="Save" icon="save" onclick="saveResponse('save');"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <tags:button color="blue" id="flow-update"
                                     cssClass="next" value="Submit" icon="save" onclick="saveResponse('submit');"/>
                    </td>
                </tr>
            </table>
        </c:if>
    </form:form>
</chrome:box>

</body>
</html>