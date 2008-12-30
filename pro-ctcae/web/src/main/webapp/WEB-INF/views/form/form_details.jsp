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
	sortQuestions();
<%--<c:if test="${not empty command.studyCrf.crf.crfItems}">--%>
<%--postProcessFormChanges();--%>
<%--hideQuestionsFromForm();--%>
<%--hideProCtcTermFromForm();--%>

<%--<c:forEach items="${command.studyCrf.crf.crfItems}" var="crfItem" varStatus="status">--%>
<%--var id = '${crfItem.proCtcQuestion.id}';--%>
<%--addConditionalDisplayToQuestion(id);--%>
<%--</c:forEach>--%>
<%--</c:if>--%>
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
	var i = 0;
	$$("div.sortableSpan").each(function (item) {
		item.innerHTML = i + 1;
		//			var orderNumberAtCrfItemPropertiesPage = $$("span.sortableSpan")[parseInt(item.id) - 1];
		//			orderNumberAtCrfItemPropertiesPage.innerHTML = i + 1;
		i = i + 1;


	})


}
function updateOrderId() {
	var i = 0;
	$$("div.sortableSpan").each(function (item) {
		item.id = i + 1;

	})


}
function hideQuestionsFromForm() {
	$$("div.makeDraggable").each(function (item) {
		if (item.id != '') {
			var questionId = item.id.substr(9, item.id.length)
			hideQuestionFromForm(questionId);
		}

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

function addProCtcTermDiv(transport) {
	var response = transport.responseText;
	new Insertion.Before("hiddenDiv", response);
	sortQuestions()
	updateQuestionsId()
	hideQuestionsFromForm();
	postProcessFormChanges()


}
function postProcessFormChanges() {

	reOrderQuestionNumber()
	showHideQuestionUpDownLink();
	updateTotalNumberOfQuestionsInEachPage();
}
function addCrfItem(questionId, proCtcTermId) {
	var displayOrder = parseInt($('totalQuestions').value) + parseInt(1);
	var request = new Ajax.Request("<c:url value="/pages/form/addOneCrfPageItem"/>", {
		parameters:"questionId=" + questionId + "&subview=subview&displayOrder=" + displayOrder,
		onComplete:function (transport) {
			var response = transport.responseText;
			new Insertion.Before("hiddenDiv", response);
			sortQuestions()
			updateQuestionsId()
			updateSelectedCrfItems(questionId)
			addCrfItemPropertiesHtml(questionId);
			postProcessFormChanges();


		},
		method:'get'
	})
	hideQuestionFromForm(questionId);
	hideProCtcTermLinkFromForm(proCtcTermId);
	$('totalQuestions').value = displayOrder;
	$('totalQuestionDivision').innerHTML = displayOrder;


}
function addCrfPage() {
	var request = new Ajax.Request("<c:url value="/pages/form/addOneCrfPage"/>", {
		parameters:"subview=subview",
		onComplete:function (transport) {
			var response = transport.responseText;
			new Insertion.Before("hiddenCrfPageDiv", response);
			sortQuestions()
			updateQuestionsId()
			//			updateSelectedCrfItems(questionId)
			//			addCrfItemPropertiesHtml(questionId);
			postProcessFormChanges();


		},
		method:'get'
	})
	//hideQuestionFromForm(questionId);
	//hideProCtcTermLinkFromForm(proCtcTermId);
	//	$('totalQuestions').value = displayOrder;
	//	$('totalQuestionDivision').innerHTML = displayOrder;


}
function updateSelectedCrfItems(questionId) {
	var selectedCrfItems = $('selectedCrfItems_' + questionId)
	$$('select.selectedCrfItems').each(function (item) {
		item.innerHTML = selectedCrfItems.innerHTML;
	});
}

function addProctcTerm(proCtcTermId) {
	var displayOrder = parseInt($('totalQuestions').value) + parseInt(1);
	var request = new Ajax.Request("<c:url value="/pages/form/addOneProCtcTerm"/>", {
		parameters:"proCtcTermId=" + proCtcTermId + "&subview=subview&displayOrder=" + displayOrder,
		onComplete:addProCtcTermDiv,
		method:'get'
	})
	hideProCtcTermLinkFromForm(proCtcTermId);
}


function updateQuestionsId() {
	var questionsId = '';
	var i = 0;
	$$("div.sortable").each(function (item) {
		if (item.id != '') {
			var questionId = item.id.substr(9, item.id.length)
			if (questionsId == '') {
				questionsId = questionId;
			} else {
				questionsId = questionsId + ',' + questionId;
			}
			i = i + 1
		}
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
function updateTotalNumberOfQuestionsInEachPage() {

	var formPages = $$('div.formpages');
	var numberOfQuestionsInEachPage = '';
	var sortableDivs = [];
	formPages.each(function(item) {
		var index = item.id.substr(11, item.id.length);
		numberOfQuestionsInEachPage = numberOfQuestionsInEachPage + parseInt($$('#' + item.id + ' div.sortable').length - 1)

	})
	numberOfQuestionsInEachPage = numberOfQuestionsInEachPage.toArray();
	$('numberOfQuestionsInEachPage').value = numberOfQuestionsInEachPage;
}
function sortQuestions() {

	var formPages = $$('div.formpages');
	var sortableDivs = [];
	formPages.each(function(item) {
		var index = item.id.substr(11, item.id.length);
		sortableDivs.push('sortable_' + index)

	})
	sortableDivs.each(function (item) {
		Sortable.destroy(item)
	})
	sortableDivs.each(function (item) {
		Sortable.create(item, {
			containment:sortableDivs,
			tag	:'div',
			only:['sortable'],
			dropOnEmpty:true,

			onUpdate:function () {
				updateQuestionsId();
				updateTotalNumberOfQuestionsInEachPage();

			}
			,
			onChange:function(item) {
				reOrderQuestionNumber();
				var questionId = item.id.substr(9, item.id.length);
				//showCrfItemPropertiesTab(questionId);
				showHideQuestionUpDownLink();
			}

		})
	})


}

function deleteQuestion(questionId) {
	$('sortable_' + questionId).remove();
	$('questionProperties_' + questionId).remove();

	sortQuestions();
	updateQuestionsId();
	updateOrderId();
	postProcessFormChanges()
	showQuestionInForm(questionId);

}

function moveQuestionUp(selectedQuestionId) {
	var previousQuestionIdSearch = selectedQuestionId;
	var previousQuestionId = selectedQuestionId;

	$$('div.sortable').each(function (item) {
		if (item.id != '') {

			var questionId = item.id.substr(9, item.id.length)
			if (questionId == selectedQuestionId) {
				previousQuestionId = previousQuestionIdSearch;
			}
			previousQuestionIdSearch = questionId;
		}
	})
	if (selectedQuestionId != previousQuestionId) {
		Element.insert($('sortable_' + previousQuestionId), {before:$('sortable_' + selectedQuestionId)})
		updateQuestionsId();
		updateOrderId();
		postProcessFormChanges()
	}


}
function moveQuestionDown(selectedQuestionId) {
	var nextQuestionId = selectedQuestionId;
	var i = 0;
	$$('div.sortable').each(function (item) {
		if (item.id != '') {
			var questionId = item.id.substr(9, item.id.length)
			if (questionId == selectedQuestionId) {
				nextQuestionId = $$('div.sortable')[parseInt(i) + parseInt(1)].id;
			}
			i = i + 1;
		}
	})
	if (selectedQuestionId != nextQuestionId) {
		Element.insert($(nextQuestionId), {after:$('sortable_' + selectedQuestionId)})
		updateQuestionsId();
		updateOrderId();
		postProcessFormChanges()
	}


}

function showHideQuestionUpDownLink() {
	var allCrfItems = $$('div.sortable');

	var i = 0
	allCrfItems.each(function(item) {
		if (item.id != '') {
			var questionId = item.id.substr(9, item.id.length)
			if (i == 0) {
				$('moveQuestionUpLink_' + questionId).hide();
				$('moveQuestionDownLink_' + questionId).show();
			} else if (i == parseInt(allCrfItems.length) - parseInt(1)) {
				$('moveQuestionDownLink_' + questionId).hide();
				$('moveQuestionUpLink_' + questionId).show();
			} else {
				$('moveQuestionDownLink_' + questionId).show();
				$('moveQuestionUpLink_' + questionId).show();
			}

			i = i + 1;
		}
	})


}


function reviewForm() {
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


//function updateCrfItemroperties(value, propertyName, questionId) {
//	if (propertyName.include('crfItemAllignment')) {
//		if (value == 'Vertical') {
//
//			$('horizontalCrfItems_' + questionId).hide();
//			$('verticalCrfItems_' + questionId).show();
//
//		}
//		if (value == 'Horizontal') {
//			$('verticalCrfItems_' + questionId).hide();
//			$('horizontalCrfItems_' + questionId).show();
//
//		}
//	}
//
//}


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
function addConditionalQuestion(questionId, selectedValidValues) {


	var request = new Ajax.Request("<c:url value="/pages/form/addConditionalQuestion"/>", {
		parameters:"questionId=" + questionId + "&subview=subview&selectedValidValues=" + selectedValidValues,
		onComplete:function(transport) {
			var response = transport.responseText;

			new Insertion.Before("conditions_" + questionId, response)
			addConditionalDisplayToQuestion(questionId);
		},
		method:'get'
	})


}
function addConditionalDisplayToQuestion(questionId) {
	var conditions = 'crfItemDisplayRule_' + questionId + '-condition';
	if ($$('tr.' + conditions).length > 0) {

		$('conditionsImage_' + questionId).show();
		$('conditionsTable_' + questionId).show();

		$("sortable_" + questionId).addClassName('conditional-question')
	}
}
function deleteConditions(questionId, proCtcValidValueId) {


	var request = new Ajax.Request("<c:url value="/pages/form/removeConditionalQuestion"/>", {
		parameters:"questionId=" + questionId + "&subview=subview&proCtcValidValueId=" + proCtcValidValueId,
		onComplete:function(transport) {
			var inputName = 'crfItemDisplayRule_' + questionId + '_' + proCtcValidValueId;
			$(inputName + '-row').remove();
			removeConditionalDisplayFromQuestion(questionId)
		},
		method:'get'
	})


}
function removeConditionalDisplayFromQuestion(questionId) {
	var conditions = 'crfItemDisplayRule_' + questionId + '-condition';
	if ($$('tr.' + conditions).length == 0) {
		$("sortable_" + questionId).removeClassName('conditional-question');
		$('conditionsImage_' + questionId).hide();
		$('conditionsTable_' + questionId).hide();

	}
}

function showForm() {
	$('questionBank').show();
	hideQuestionSettings();
	document.getElementById("firstlevelnav_1").className = "selected_4thlvl";
	document.getElementById("firstlevelnav_3").className = "";
	document.getElementById("firstlevelnav_2").className = "";
}
function showQuestionSettings() {
	showQuestionSettingsTab();

	if ($$('div.sortable').size() != 0) {
		var firstQuestion = $$('div.sortable')[1].id;
		var questionId = firstQuestion.substr(9, firstQuestion.length)
		showCrfItemProperties(questionId);
	}
}
function showQuestionSettingsTab() {
	hideQuestionBank();
	hideCrfItemProperties();
	document.getElementById("firstlevelnav_2").className = "selected_4thlvl";
	document.getElementById("firstlevelnav_3").className = "";
	document.getElementById("firstlevelnav_1").className = "";


}
function hideQuestionSettings() {
	hideCrfItemProperties();
}
function hideCrfItemProperties() {
	$$('div.questionProperties').each(function (item) {
		item.hide();
	})
}
function showCrfItemProperties(questionId) {
	addCrfItemPropertiesHtml(questionId);
	removeEditingDisplayFromQuestions();
	$('questionProperties_' + questionId).show();

	//	$('questionProperties_' + questionId).addClassName('editing');
	//	$('questionProperties_' + questionId).addClassName('focused');
	addEditingDisplayToQuestion(questionId)
}
function addEditingDisplayToQuestion(questionId) {
	$('sortable_' + questionId).addClassName('editing');
	//$('sortable_' + questionId).addClassName('focused');
	$('arrow_' + questionId).show();

}
function removeEditingDisplayFromQuestions() {
	$$('div.sortable').each(function (item) {
		if (item.id != '') {
			item.removeClassName('editing');
			//item.removeClassName('focused');
			var questionId = item.id.substr(9, item.id.length)
			$('arrow_' + questionId).hide();

		}
	})
}
function addCrfItemPropertiesHtml(questionId) {

	var response = $('questionPropertiesDiv_' + questionId).innerHTML;

	if (response != '') {
		$('questionPropertiesDiv_' + questionId).innerHTML = '';
		new Insertion.Before("questionProperties0", response);
	}
}
function showCrfItemPropertiesTab(questionId) {
	showQuestionSettingsTab()
	showCrfItemProperties(questionId);
}
function hideQuestionBank() {
	$('questionBank').hide();
}
function showFormSettings() {
	document.getElementById("firstlevelnav_3").className = "selected_4thlvl";
	document.getElementById("firstlevelnav_2").className = "";
	document.getElementById("firstlevelnav_1").className = "";
}


</script>
<style type="text/css">

	.makeDraggable {
		cursor: move;
	}

	.instructions .summaryvalue {
		font-weight: normal;
		margin-left: 90px;
		text-align: left;
		width: 60%;
	}

	#form-tabs {
		position: relative;
	}

	#firstlevelnav_1 {
		position: absolute;
		left: 0;
		top: 0;
		display: block;
		height: 0px;
		padding-top: 45px;
		width: 145px;
		background-image: url( ../../images/blue/formbuilder_4thlvl_btns.png );
		overflow: hidden;
	}

	#firstlevelnav_2 {
		position: absolute;
		left: 145px;
		top: 0;
		display: block;
		height: 0px;
		padding-top: 45px;
		width: 138px;
		background-image: url( ../../images/blue/formbuilder_4thlvl_btns.png );
		overflow: hidden;
		background-position: -145px 0;
	}

	#firstlevelnav_3 {
		position: absolute;
		left: 283px;
		top: 0;
		display: block;
		height: 0px;
		padding-top: 45px;
		width: 145px;
		background-image: url( ../../images/blue/formbuilder_4thlvl_btns.png );
		overflow: hidden;
		background-position: -283px 0;
	}

	#questionBank {
		padding: 1px;
		background-color: #E5E9F3;
	}

	#firstlevelnav_1.selected_4thlvl {
		background-position: 0px -45px;
	}

	#firstlevelnav_2.selected_4thlvl {
		background-position: -145px -45px;
	}

	#firstlevelnav_3.selected_4thlvl {
		background-position: -283px -45px;
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
<a id="reviewLink" href="javascript:reviewForm()" style="display:none">Preview</a>

            <table id="formbuilderTable">
				<tr>
					<td id="left">
						<ul id="form-tabs" class="tabs">
							<li>
								<a id="firstlevelnav_1" href="javascript:showForm()" class="selected_4thlvl">
									<tags:message code='form.add_question'/>
									|</a>
							</li>
							<li class="">
								<a id="firstlevelnav_2" href="javascript:showQuestionSettings()">
									<tags:message code="form.question_settings"/> |</a>
							</li>

								<%--<li class="">--%>
								<%--<a id="firstlevelnav_3" href="javascript:showFormSettings()">--%>
								<%--<tags:message code='form.form_settings'/> </a>--%>
								<%--</li>--%>

							<li class="">
								<a id="firstlevelnav_3" href="javascript:addCrfPage()">
									<tags:message code='form.add_page'/> </a>
							</li>
						</ul>
						<br>
						<br>
						<br>

						<div id="questionBank">
							<ul class="tree">
								<c:forEach items="${ctcCategoryMap}" var="ctcCategory">

									<li>${ctcCategory.key.name}
										<ul>
											<c:forEach items="${ctcCategory.value}" var="proCtcTerm">
												<li class="closed">${proCtcTerm.term}


													<ul>
														<a href="javascript:addProctcTerm(${proCtcTerm.id})"
														   id="proCtcTerm_${proCtcTerm.id}">
															<img src="/ctcae/images/blue/add_all_btn.png"
																 alt="Add" onclick=""/></a>
														<c:forEach items="${proCtcTerm.proCtcQuestions}"
																   var="proCtcQuestion">

															<li id="question_${proCtcQuestion.id}">
																<tags:formbuilderBox>
																	<tags:formbuilderBoxControls add="true"
																								 proCtcQuestionId="${proCtcQuestion.id}"
																								 proCtctermId="${proCtcTerm.id}"/>
																	${proCtcQuestion.formattedQuestionText}
																	<ul>
																		<c:forEach items="${proCtcQuestion.validValues}"
																				   var="proCtcValidValue">
																			<li>${proCtcValidValue.displayName}</li>
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

						</div>


						<div id="questionProperties0" style="display:none;"></div>
					</td>
					<td id="right">

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

									<form:hidden path="questionsIds" id="questionsIds"/>
									<form:hidden path="numberOfQuestionsInEachPage" id="numberOfQuestionsInEachPage"/>
									<input type="hidden" id="totalQuestions" value="${totalQuestions}">
									<c:forEach items="${command.studyCrf.crf.crfPages}" var="selectedCrfPage"
											   varStatus="status">
										<tags:oneCrfPage crfPage="${selectedCrfPage}"
														 index="${status.index}">
										</tags:oneCrfPage>

									</c:forEach>

									<div id="hiddenCrfPageDiv"></div>

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
