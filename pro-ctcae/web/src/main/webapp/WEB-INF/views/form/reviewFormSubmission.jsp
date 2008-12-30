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
	<tags:javascriptLink name="submit_btn_animation" />
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
	
<style>
	#left-panel {
		padding-left:15px;
		width:60%;
		float:left;
		font-size:17px;
	}
	#left-panel a{
		text-decoration:none;
	}
	#left-panel a:hover {
		text-decoration:underline;
	}
	#right-panel {
		width:35%;
		padding:5px;
		float:right;
		position:relative;
		height:265px;
	}
	#submit_btn {
		height:0px;
		padding-top:201px;
		overflow:hidden;
		width:200px;
		background-image:url(../../images/blue/submit_sprite.png);
		position:relative;
		display:block;
		outline:none;
		left:80px;
	}
	#submit_btn:hover {
		cursor:pointer;
	}
	h2 {
		font-weight:normal;
	}
	#left-panel h1{
		font-size:2em;
	}
</style>

</head>
<body>
<chrome:flashMessage flashMessage="${command.flashMessage}"></chrome:flashMessage>
<form:form method="post" name="myForm">
		<div style="clear:both;">
            <div id="left-panel">
                    <h1>Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title}</h1>
                    You are about to submit this form.<br/> 
					You can not change the responses after submission.
                    <br/>
					<br/>
                    You can:<br/> 
					<a href="javascript:showReview();"><img src="<tags:imageUrl name="blue/undo.png" />" alt="" /> Review your responses and make changes</a>
                    <br/>
                    <a href="../form/addquestion"><img src="<tags:imageUrl name="blue/edit_add.png" />" alt="" /> Add questions relating to other symptoms</a>
                
            </div>
			<div id="right-panel">
				<a onmousedown="javascript:activate_button()" id="submit_btn">Submit</a>
				<h2 style="margin-left:151px; margin-top:10px; color:#999;">Submit</h2>
			</div>
        </div>
        <table id="formbuilderTable">
            <tr>
                <td id="left">
                    <c:forEach items="${command.pages}" var="page" varStatus="pageindex">
                        <c:forEach items="${page}" var="studyParticipantCrfItem">
                            <tags:formbuilderBox id="question_${studyParticipantCrfItem.itemIndex}">
                                ${studyParticipantCrfItem.crfPageItem.proCtcQuestion.formattedQuestionText}<br/>
                                <input type="hidden"
                                       name="studyParticipantCrfSchedule.studyParticipantCrfItems[${studyParticipantCrfItem.itemIndex}].proCtcValidValue"
                                       value=""/>
                                <c:forEach items="${studyParticipantCrfItem.crfPageItem.proCtcQuestion.validValues}"
                                           var="validValue" varStatus="status">
                                    <div class="norm" onmouseover="javascript:this.className='over';"
                                         onmouseout="javascript:this.className='norm';"
                                         onclick="showHideFor('${pageindex.index}','${studyParticipantCrfItem.crfPageItem.proCtcQuestion.proCtcQuestionType}','${studyParticipantCrfItem.itemIndex}','${status.index}')"
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
                            <c:if test="${studyParticipantCrfItem.crfPageItem.proCtcQuestion.proCtcQuestionType ne 'Severity'}">
                                <script type="text/javascript">
                                    registerToHide('${pageindex.index}', '${studyParticipantCrfItem.itemIndex}', '${studyParticipantCrfItem.proCtcValidValue}');
                                </script>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </td>
            </tr>
        </table>
    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr align="right">
            <td>
                <%--<input onclick="document.myForm.direction.value='save'" type="image"
                       src="/ctcae/images/blue/submit_btn.png" alt="save &raquo;"/>--%>
            </td>
        </tr>
    </table>


</form:form>
</body>
</html>