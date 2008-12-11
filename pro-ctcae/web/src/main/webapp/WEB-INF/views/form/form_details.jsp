<%-- This is the standard decorator for all caAERS pages --%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>

<tags:stylesheetLink name="tabbedflow"/>
<tags:stylesheetLink name="ae"/>

<tags:includeScriptaculous/>

<tags:includePrototypeWindow/>

<script type="text/javascript">

Event.observe(window, "load", function () {
	sortQustions();
<c:if test="${not empty command.studyCrf.crf.crfItems}">
	reOrderQuestionNumber();
	hideQuestionsFromForm();
	hideProCtcTermFromForm();

</c:if>
	displayReviewLink();

})

Event.observe(window, "load", function () {
	var formNameInPlaceEdit = new Ajax.InPlaceEditor('crfTitle', '/ctcae/pages/form/setName', {
		rows:1,
		cancelControl:false,
		okControl:false,
		size:18,
		submitOnBlur:true,
		onEnterEditMode:function() {
			if ($('crfTitle').innerHTML == 'Click here to name') {
				$('crfTitle').innerHTML = ''

			}

		}  ,
		onComplete:function(transport) {
			$('crfTitle').innerHTML = transport.responseText;
			$('formTitle').value = transport.responseText;
			if ($('formTitle').value == '') {
				$('crfTitle').innerHTML = 'Click here to name';

			}


		},
		callback:function(form, value) {
			return 'crfTitle=' + encodeURIComponent(value)
		}
	});

})


function sortQustions() {
	Sortable.destroy("sortable")
	Sortable.create("sortable", {
		tag:'div',
		only:['sortable'],

		onUpdate:function () {
			updateQuestionsId();

		},
		onChange:function() {
			reOrderQuestionNumber();
		}

	})


}
function displayReviewLink() {
	if ($('totalQuestions').value != '0') {
		$('reviewLink').show();
		//$('reviewAllLink').show();
	} else {
		$('reviewLink').hide();
		//$('reviewAllLink').hide();

	}
}
function reOrderQuestionNumber() {
	var i = 1;
	$$("div.sortableSpan").each(function (item) {
		item.innerHTML = i;
		i = i + 1;


	})

}
function hideQuestionsFromForm() {
	$$("div.makeDraggable").each(function (item) {
		var questionId = item.id.substr(9, item.id.length)
		hideQuestionFromForm(questionId);

	})

}
function hideProCtcTermFromForm() {
	$$("div.selectedProCtcTerm").each(function (item) {
		hideProCtcTermLinkFromForm(item.id);

	})

}

function hideQuestionFromForm(questionId) {
	$('question_' + questionId).hide();

}
function hideProCtcTermLinkFromForm(proCtcTermId) {
	$('proCtcTerm_' + proCtcTermId).hide();

}
function showQuestionInForm(questionId) {
	$('question_' + questionId).show();
}
function addQuestionDiv(transport) {
	var response = transport.responseText;
	new Insertion.Before("hiddenDiv", response);
	sortQustions()
	updateQuestionsId()
	reOrderQuestionNumber();


}
function addProCtcTermDiv(transport) {
	var response = transport.responseText;
	new Insertion.Before("hiddenDiv", response);
	sortQustions()
	updateQuestionsId()
	hideQuestionsFromForm();
	reOrderQuestionNumber();


}
function addQuestion(questionId, proCtcTermId) {
	var displayOrder = parseInt($('totalQuestions').value) + parseInt(1);
	var request = new Ajax.Request("<c:url value="/pages/form/addOneQuestion"/>", {
		parameters:"questionId=" + questionId + "&subview=subview&displayOrder=" + displayOrder,
		onComplete:addQuestionDiv,
		method:'get'
	})
	hideQuestionFromForm(questionId);
	hideProCtcTermLinkFromForm(proCtcTermId);
	$('totalQuestions').value = displayOrder;
	$('totalQuestionDivision').innerHTML = displayOrder;

}
function addProctcTerm(proCtcTermId) {
	var displayOrder = parseInt($('totalQuestions').value) + parseInt(1);
	var request = new Ajax.Request("<c:url value="/pages/form/addOneProCtcTerm"/>", {
		parameters:"proCtcTermId=" + proCtcTermId + "&subview=subview&displayOrder=" + displayOrder,
		onComplete:addProCtcTermDiv,
		method:'get'
	})
	hideProCtcTermLinkFromForm(proCtcTermId);
	//    $('totalQuestions').value = displayOrder;
	//    $('totalQuestionDivision').innerHTML = displayOrder;

}

function updateQuestionsId() {
	var questionsId = '';
	var i = 0;
	$$("div.sortable").each(function (item) {
		var questionId = item.id.substr(9, item.id.length)
		if (questionsId == '') {
			questionsId = questionId;
		} else {
			questionsId = questionsId + ',' + questionId;
		}
		i = i + 1
	});

	$('questionsIds').value = questionsId;
	$('totalQuestions').value = i;
	$('totalQuestionDivision').innerHTML = i;
	if (i == 1) {
		$('plural1').innerHTML = 'is';
		$('plural2').innerHTML = '';
	} else {
		$('plural1').innerHTML = 'are';
		$('plural2').innerHTML = 's';
	}

	displayReviewLink();
}

function deleteQuestion(questionId) {
	$('sortable_' + questionId).remove();
	sortQustions();
	updateQuestionsId();
	reOrderQuestionNumber()
	showQuestionInForm(questionId);

}


function reviewForm() {
	var firstQuestion = $$('div.sortable')[0].id;
	var firstQuestion = $$('div.sortable')[0].id;
	var nextQuestionIndex = 1;
	if ($('totalQuestions').value == '1') {
		nextQuestionIndex = '';
	}
	showQuestionForReview(firstQuestion, 1, nextQuestionIndex, '');


}
function reviewCompleteForm() {
	var win = Windows.getFocusedWindow();
	if (win == null) {
		win = new Window({ id: '100' , className: "alphacube", closable : true, minimizable : false, maximizable :
			true, title: "Review Form", height:500, width: 750,top:150,left:300});
		win.setDestroyOnClose();
		win.setContent('sortable');
		win.show(true)

	} else {
		win.setContent('sortable');
		win.refresh();
	}

}
function playForm() {

	new PeriodicalExecuter(function(pe) {
		nextQuestionIndex(0);

	}, 3);//display questions in 3 seconds


}
function showNextQuestion(quesitonIndex) {
	nextQuestionIndex(quesitonIndex);
	//  return parseInt(quesitonIndex) + parseInt(1);
}
function showQuestionForReview(firstQuestion, displayOrder, nextQuestionIndex, previousQuestionIndex) {
	var questionId = firstQuestion.substr(9, firstQuestion.length)

	var request = new Ajax.Request("<c:url value="/pages/form/findCrfItem"/>", {
		parameters:"questionId=" + questionId + "&subview=subview&displayOrder=" + displayOrder
			+ "&nextQuestionIndex=" + nextQuestionIndex + "&previousQuestionIndex=" + previousQuestionIndex,
		onComplete:showReviewWindow,
		method:'get'
	})

}
function addCrfItemProperties(questionId) {

	var request = new Ajax.Request("<c:url value="/pages/form/addCrfItemProperties"/>", {
		parameters:"questionId=" + questionId + "&subview=subview",
		onComplete:showCrfItemPropertiesWindow,
		method:'get'
	})

}

function showCrfItemPropertiesWindow(transport) {
	var win = Windows.getFocusedWindow();
	if (win == null) {
		win = new Window({ id: '100' , className: "alphacube", closable : true, minimizable : false, maximizable :
			true, title: "", height:500, width: 550,top:200,left:250});
		win.setDestroyOnClose();
		win.setHTMLContent(transport.responseText);
		win.show(true)

	} else {
		win.setHTMLContent(transport.responseText);
		win.refresh();
	}


}
function updateCrfItemroperties(value, propertyName) {
	if (propertyName == 'crfItem.crfItemAllignment') {
		$('crfItem.crfItemAllignment').value = value;
		if (value == 'Vertical') {
			$('horizontalCrfItems').hide();
			$('verticalCrfItems').show();

		}
		if (value == 'Horizontal') {
			$('verticalCrfItems').hide();
			$('horizontalCrfItems').show();

		}
	}
	if (propertyName == 'crfItem.responseRequired') {
		$('crfItem.responseRequired').value = value;
	}
}
function submitCrfItemPropertiesWindow(questionId) {


	var aElement = '<c:url value="/pages/form/addCrfItemProperties"/>';


	var lastRequest = new Ajax.Request(aElement, {
		method: 'post',
		parameters: {
			'instructions': $('crfItem.instructions').value
			,'crfItemAllignment': $('crfItem.crfItemAllignment').value
			,'responseRequired': $('crfItem.responseRequired').value
			,'subview':'test',
			'questionId':questionId},
		onSuccess: closeCrfItemPropertiesWindow,
		onerror:   function() {
			alert("There was an error with the connection");
		},
		onFailure: function() {
			alert("There was an error with the connection");
		}
	});

}
function closeCrfItemPropertiesWindow(transport) {
	var win = Windows.getFocusedWindow();
	win.close();

}


function showReviewWindow(transport) {
	var win = Windows.getFocusedWindow();
	if (win == null) {
		win = new Window({ id: '100' , className: "alphacube", closable : true, minimizable : false, maximizable :
			true, title: "", height:300, width: 600,top:250,left:200});
		win.setDestroyOnClose();
		win.setHTMLContent(transport.responseText);
		win.show(true)

	} else {
		win.setHTMLContent(transport.responseText);
		win.refresh();
	}


}
function nextQuestion(questionIndex) {

	var nextQuestion = $$('div.sortable')[parseInt(questionIndex)].id;
	var displayOrder = parseInt(questionIndex) + parseInt(1);

	var previousQuestionIndex = parseInt(questionIndex) - parseInt(1)
	var nextQuestionIndex = parseInt(questionIndex) + parseInt(1);

	if (nextQuestionIndex == $$('div.sortable').length)
	{
		nextQuestionIndex = '';
	}

	showQuestionForReview(nextQuestion, displayOrder, nextQuestionIndex, previousQuestionIndex)


}

function previousQuestion(questionIndex) {

	var previousQuestion = $$('div.sortable')[parseInt(questionIndex)].id;
	var displayOrder = parseInt(questionIndex) + parseInt(1);

	var nextQuestionIndex = parseInt(questionIndex) + parseInt(1)


	var previousQuestionIndex = '';
	if (questionIndex != 0)
	{
		previousQuestionIndex = parseInt(questionIndex) - parseInt(1)

	}
	showQuestionForReview(previousQuestion, displayOrder, nextQuestionIndex, previousQuestionIndex)


}

</script>
<style type="text/css">

	.makeDraggable {
		cursor: move;
	}
</style>

</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
<jsp:attribute name="singleFields">
             <input type="hidden" name="_finish" value="true"/>

        <div class="instructions">

			<div class="summarylabel"><tags:message code='form.label.study'/></div>
			<div class="summaryvalue">${command.studyCrf.study.displayName}</div>
		</div>


            <table id="formbuilderTable">
				<tr>
					<td id="left">


						<div class="formbuilderHeader"><tags:message code='form.label.question_bank'/></div>

						<ul class="tree">
							<c:forEach items="${ctcCategoryMap}" var="ctcCategory">

								<li>${ctcCategory.key.name}
									<ul>
										<c:forEach items="${ctcCategory.value}" var="proCtcTerm">
											<li class="closed">${proCtcTerm.term}

												<ul><a href="javascript:addProctcTerm(${proCtcTerm.id})"
													   id="proCtcTerm_${proCtcTerm.id}">
													<img src="/ctcae/images/blue/select_question_btn.png"
														 alt="Add" onclick=""/>Add All</a>

													<c:forEach items="${proCtcTerm.proCtcQuestions}"
															   var="proCtcQuestion">

														<li id="question_${proCtcQuestion.id}">
															<tags:formbuilderBox>
																<tags:formbuilderBoxControls add="true"
																							 proCtcQuestionId="${proCtcQuestion.id}"
																							 proCtctermId="${proCtcTerm.id}"/>
																${proCtcQuestion.questionText}
																<ul>
																	<c:forEach items="${proCtcQuestion.validValues}"
																			   var="proCtcValidValue">
																		<li>${proCtcValidValue.value}</li>
																	</c:forEach>
																</ul>
															</tags:formbuilderBox>
														</li>

													</c:forEach>
												</ul>
											</li>
										</c:forEach>

									</ul>
								</li>

							</c:forEach>
						</ul>

					</td>
					<td id="right">
						<a id="reviewLink" href="javascript:reviewForm()" style="display:none">Preview</a>
							<%--<a id="reviewAllLink" href="javascript:reviewCompleteForm()">Review</a>--%>
							<%--<a id="reviewLink" href="javascript:playForm()">Play</a>--%>
						<table style="border-collapse:collapse; height:800px;">
							<tr style="height:100%;">
								<td id="formbuilderTable-middle">
									<div id="formbuilderTable-borderTop">
										<span class="formbuilderHeader" id="crfTitle">${command.title}</span>

										<form:hidden path="studyCrf.crf.title" id="formTitle"/>

                                        <span class="formbuildersubHeader">There <span id="plural1">are</span> <span
											id="totalQuestionDivision">${totalQuestions}</span> question<span
											id="plural2">s</span> in this form.</span>
									</div>
									<div id="sortable">
										<form:hidden path="questionsIds" id="questionsIds"/>
										<input type="hidden" id="totalQuestions" value="${totalQuestions}">
										<c:forEach items="${command.studyCrf.crf.crfItems}" var="crfItem"
												   varStatus="status">
											<tags:oneQuestion proCtcQuestion="${crfItem.proCtcQuestion}"
															  displayOrder="${status.index}">
											</tags:oneQuestion>
										</c:forEach>
										<div id="hiddenDiv"></div>
									</div>
								</td>
							</tr>
							<tr>
								<td id="formbuilderTable-borderBottom">
								</td>
							</tr>
						</table>
					</td>

				</tr>

			</table>
</jsp:attribute>
</tags:tabForm>
</body>