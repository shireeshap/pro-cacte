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
		var id = item.id;
		if (!id.include('dummySortable_')) {
			var questionId = id.substr(9, id.length)
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

function postProcessFormChanges() {

	reOrderQuestionNumber()
	showHideQuestionUpDownLink();
	updateTotalNumberOfQuestionsInEachPage();
}
function addCrfPageItem(questionId, proCtcTermId) {
	var displayOrder = parseInt($('totalQuestions').value) + parseInt(1);

	var crfPageNumber = ''
	$$('div.formpagesselected').each(function(item) {
		crfPageNumber = item.id.substr(11, item.id.length);

	})
	var request = new Ajax.Request("<c:url value="/pages/form/addOneCrfPageItem"/>", {
		parameters:"questionId=" + questionId + "&subview=subview&crfPageNumber=" + crfPageNumber,
		onComplete:function (transport) {
			var response = transport.responseText;
			if (crfPageNumber == '') {
				addCrfPageDiv(transport);

			} else {
				addCrfPageItemDiv(transport, crfPageNumber);
				updateSelectedCrfItems(questionId)
				addCrfItemPropertiesHtml(questionId);
				postProcessFormChanges();
			}

		},
		method:'get'
	})
	hideQuestionFromForm(questionId);
	hideProCtcTermLinkFromForm(proCtcTermId);
	$('totalQuestions').value = displayOrder;
	$('totalQuestionDivision').innerHTML = displayOrder;


}
function addCrfPageItemDiv(transport, crfPageNumber) {
	var response = transport.responseText;

	new Insertion.Before("hiddenDiv_" + crfPageNumber, response);
	sortQuestions()
	updateQuestionsId();
	updateCrfPageNumberAndShowHideUpDownLink();
}
function addProctcTerm(proCtcTermId) {
	var displayOrder = parseInt($('totalQuestions').value) + parseInt(1);
	var crfPageNumber = ''
	$$('div.formpagesselected').each(function(item) {
		crfPageNumber = item.id.substr(11, item.id.length);

	})
	var request = new Ajax.Request("<c:url value="/pages/form/addOneProCtcTerm"/>", {
		parameters:"proCtcTermId=" + proCtcTermId + "&subview=subview&crfPageNumber=" + crfPageNumber,
		onComplete:function(transport) {
			if (crfPageNumber == '') {
				addCrfPageDiv(transport);
				hideQuestionsFromForm();


			} else {
				addCrfPageItemDiv(transport, crfPageNumber)
				hideQuestionsFromForm();
				postProcessFormChanges()

			}
		},
		method:'get'
	})
	hideProCtcTermLinkFromForm(proCtcTermId);
}

function selectPage(pageIndex) {
	unselectAllSelectedPage();
	$('form-pages_' + pageIndex).addClassName('formpagesselected')
	$('form-pages-image_' + pageIndex).show();
}
function unselectAllSelectedPage() {
	$$('div.formpagesselected').each(function(item) {
		var pageIndex = item.id.substr(11, item.id.length);

		unselectPage(pageIndex)
	})
}
function unselectPage(pageIndex) {
	$('form-pages_' + pageIndex).removeClassName('formpagesselected')
	$('form-pages-image_' + pageIndex).hide();
}
function addCrfPageDiv(transport) {
	var response = transport.responseText;
	new Insertion.Before("hiddenCrfPageDiv", response);
	sortQuestions()
	updateQuestionsId()
	//			updateSelectedCrfItems(questionId)
	//			addCrfItemPropertiesHtml(questionId);
	updateCrfPageNumberAndShowHideUpDownLink();
	postProcessFormChanges();
	initSearchField();


}
function addCrfPage() {
	var request = new Ajax.Request("<c:url value="/pages/form/addOneCrfPage"/>", {
		parameters:"subview=subview",
		onComplete:function (transport) {
			addCrfPageDiv(transport);


		},
		method:'get'
	})
	//hideQuestionFromForm(questionId);
	//hideProCtcTermLinkFromForm(proCtcTermId);
	//	$('totalQuestions').value = displayOrder;
	//	$('totalQuestionDivision').innerHTML = displayOrder;


}
function updateSelectedCrfItems(questionId) {
	var selectedCrfPageItems = $('selectedCrfPageItems_' + questionId)
	$$('select.selectedCrfPageItems').each(function (item) {
		item.innerHTML = selectedCrfPageItems.innerHTML;
	});
}


function updateQuestionsId() {
	var questionsId = '';
	var i = 0;
	$$("div.sortable").each(function (item) {
		var id = item.id;
		if (!id.include('dummySortable_')) {
			var questionId = id.substr(9, id.length)
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
function updateCrfPageNumberAndShowHideUpDownLink() {
	var crfPageNumbers = '';
	var formPages = $$('div.formpages');
	var i = 0;
	formPages.each(function (item) {

		var index = item.id.substr(11, item.id.length);

		if (crfPageNumbers == '') {
			crfPageNumbers = index;
		} else {
			crfPageNumbers = crfPageNumbers + ',' + index;
		}
		if (i == 0) {
			$('crfPageUpLink_' + index).hide();
			$('crfPagDownLink_' + index).show();
		} else if (i == formPages.length - 1)
		{
			$('crfPagDownLink_' + index).hide();
			$('crfPageUpLink_' + index).show();
		} else {
			$('crfPageUpLink_' + index).show();
			$('crfPagDownLink_' + index).show();

		}

		i++;
	});

	$('crfPageNumbers').value = crfPageNumbers;


}

function moveCrfPageUp(selectedCrfPageNumber) {

	var formPages = $$('div.formpages');

	var sortableDivs = [];
	var i = 0;
	var previousCrfPage = '';
	formPages.each(function(item) {


		if (item.id == 'form-pages_' + selectedCrfPageNumber) {

			previousCrfPage = formPages[i - 1];

		}

		i++;


	});
	if (previousCrfPage != '') {
		Element.insert(previousCrfPage, {before:$('form-pages_' + selectedCrfPageNumber)})

		updateCrfPageNumberAndShowHideUpDownLink();
		postProcessFormChanges()
	}
}
function moveCrfPageDown(selectedCrfPageNumber) {

	var formPages = $$('div.formpages');

	var sortableDivs = [];
	var i = 0;
	var nextCrfPage = '';
	formPages.each(function(item) {


		if (item.id == 'form-pages_' + selectedCrfPageNumber) {

			nextCrfPage = formPages[i + 1];

		}

		i++;


	});
	if (nextCrfPage != '') {
		Element.insert(nextCrfPage, {after:$('form-pages_' + selectedCrfPageNumber)})

		updateCrfPageNumberAndShowHideUpDownLink();
		postProcessFormChanges()
	}
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
			scroll:window,

			onUpdate:function () {
				updateQuestionsId();
				updateTotalNumberOfQuestionsInEachPage();
				showHideQuestionUpDownLink();

			}
			,
			onChange:function(item) {
				reOrderQuestionNumber();
				var questionId = item.id.substr(9, item.id.length);
				//showCrfItemPropertiesTab(questionId);

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
	$$('optgroup.conditions').each(function(item) {
		if (item.id == 'condition_' + questionId) {
			item.remove();
		}
	})


}

function moveQuestionUp(selectedQuestionId) {

	var formPages = $$('div.formpages');

	var sortableDivs = [];
	var j = 0;
	var previousItemId = '';
	formPages.each(function(item) {
		var i = 1;


		var crfPageItems = $$('#' + item.id + ' div.sortable');

		crfPageItems.each(function(crfPageItem) {
			var id = crfPageItem.id;
			if (!id.include('dummySortable_'))
			{
				if (id == 'sortable_' + selectedQuestionId) {

					if (i == 1) {
						//move to previous page
						var pageIndex = parseInt(j - 1);
						previousItemId = 'hiddenDiv_' + pageIndex;
						selectPage(pageIndex);

					} else {
						var previousItem = crfPageItems[i - 1];
						previousItemId = previousItem.id;

					}

				}
				i++;
			}
		});

		j++;

	});
	if (previousItemId != '') {
		Element.insert($(previousItemId), {before:$('sortable_' + selectedQuestionId)})

		updateQuestionsId();
		updateOrderId();
		postProcessFormChanges()
	}
}
function moveQuestionDown(selectedQuestionId) {
	var formPages = $$('div.formpages');

	var sortableDivs = [];
	var j = 0;
	var nextItemId = ''
	formPages.each(function(item) {
		var i = 1;
		var crfPageItems = $$('#' + item.id + ' div.sortable');
		crfPageItems.each(function(crfPageItem)
		{
			if (!crfPageItem.id.include('dummySortable_')) {
				if (crfPageItem.id == 'sortable_' + selectedQuestionId) {

					if (i == crfPageItems.length - 1) {
						//move to next page
						var pageIndex = parseInt(j + 1);
						nextItemId = 'dummySortable_' + pageIndex;
						selectPage(pageIndex)

					} else {
						var nextItem = crfPageItems[i + 1];
						nextItemId = nextItem.id;
					}

				}
				i++;
			}
		});


		j++;
	});
	if (nextItemId != '') {
		Element.insert($(nextItemId), {after:$('sortable_' + selectedQuestionId)})
		updateQuestionsId();
		updateOrderId();
		postProcessFormChanges()
	}

}

function showHideQuestionUpDownLink() {

	var formPages = $$('div.formpages');

	var j = 0;
	var nextItemId = ''
	formPages.each(function(item) {
		var i = 1;
		var crfPageItems = $$('#' + item.id + ' div.sortable');
		crfPageItems.each(function(crfPageItem)
		{
			if (!crfPageItem.id.include('dummySortable_')) {
				var questionId = crfPageItem.id.substr(9, crfPageItem.id.length)
				if (j == 0) {
					if (parseInt(crfPageItems.length) == 2) {
						$('moveQuestionUpLink_' + questionId).hide();
						$('moveQuestionDownLink_' + questionId).hide();
					}
					if (i == 1) {

					} else if (i == parseInt(crfPageItems.length)) {
						$('moveQuestionDownLink_' + questionId).hide();
						$('moveQuestionUpLink_' + questionId).show();
					} else {

					}

				} else {
					$('moveQuestionDownLink_' + questionId).show();
					$('moveQuestionUpLink_' + questionId).show();
				}
				i++;
			}
		});


		j++;
	});

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
			addRemoveConditionalTriggeringDisplayToQuestion();
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
			removeConditionalDisplayFromQuestion(questionId);
			addRemoveConditionalTriggeringDisplayToQuestion();
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
	hideFormSettings();
	$("firstlevelnav_1").addClassName('selected_4thlvl')
	$("firstlevelnav_3").removeClassName('selected_4thlvl')
	$("firstlevelnav_2").removeClassName('selected_4thlvl')

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
	hideFormSettings();
	$("firstlevelnav_2").addClassName('selected_4thlvl')
	$("firstlevelnav_3").removeClassName('selected_4thlvl')
	$("firstlevelnav_1").removeClassName('selected_4thlvl')


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
	$('questionProperties_' + questionId).show();

	//	$('questionProperties_' + questionId).addClassName('editing');
	//	$('questionProperties_' + questionId).addClassName('focused');
	addEditingDisplayToQuestion(questionId)
}
function addEditingDisplayToQuestion(questionId) {
	removeEditingDisplayFromQuestions();
	$('sortable_' + questionId).addClassName('editing');
	//$('sortable_' + questionId).addClassName('focused');
	//$('arrow_' + questionId).show();

}
function removeEditingDisplayFromQuestions() {
	$$('div.sortable').each(function (item) {
		var id = item.id;
		if (!id.include('dummySortable_')) {
			item.removeClassName('editing');
			//item.removeClassName('focused');
			var questionId = id.substr(9, id.length)
			//$('arrow_' + questionId).hide();

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
	$("firstlevelnav_3").addClassName("selected_4thlvl");
	$("firstlevelnav_2").removeClassName('selected_4thlvl');
	$("firstlevelnav_1").removeClassName('selected_4thlvl');
	hideQuestionBank();
	hideQuestionSettings();
	$('formSettings').show();
}
function hideFormSettings() {
	$('formSettings').hide();
}


</script>
<script type="text/javascript">
	function shrinkQuestionBank() {
		Effect.SlideUp('left', {
			scaleX:true,
			scaleY:false
		});
		$('shrinkQuestionBankUrl').hide();
		$('expandQuestionBankUrl').show();
	}
	function expandQuestionBank() {
		Effect.SlideDown('left', {
			scaleX:true,
			scaleY:false
		});

		$('shrinkQuestionBankUrl').show();
		$('expandQuestionBankUrl').hide();
	}
	function shrinkForm() {
		Effect.SlideUp('right', {
			scaleX:true,
			scaleY:false
		});
		$('shrinkFormUrl').hide();
		$('expandFormUrl').show();
	}
	function expandForm() {
		Effect.SlideDown('right', {
			scaleX:true,
			scaleY:false
		});

		$('shrinkFormUrl').show();
		$('expandFormUrl').hide();
	}
	function addRemoveConditionalTriggeringDisplayToQuestion() {

		$$("div.sortable").each(function (item) {
			var id = item.id;
			if (!id.include('dummySortable_')) {
				var questionId = id.substr(9, id.length)
				if ($$('td.conditionalTriggering_' + questionId).length > 0) {
					$('conditionalTriggeringImage_' + questionId).show();
					$("sortable_" + questionId).addClassName('conditional-triggering');
				} else {
					$('conditionalTriggeringImage_' + questionId).hide();
					$("sortable_" + questionId).removeClassName('conditional-triggering');
				}
			}
		});

	}

	function deleteCrfPage(selectedCrfPageNumber) {
		var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
			parameters:"confirmationType=deleteCrf&subview=subview&selectedCrfPageNumber=" + selectedCrfPageNumber,
			onComplete:showConfirmationWindow,
			method:'get'
		});

	}
	function showConfirmationWindow(transport) {
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

	function deleteCrfPageConfirm(selectedCrfPageNumber) {
		closeWindow();
		$('form-pages_' + selectedCrfPageNumber).remove();

		var crfPageNumbersToRemove = $('crfPageNumbersToRemove').value;
		if (crfPageNumbersToRemove.blank()) {
			crfPageNumbersToRemove = selectedCrfPageNumber;
		} else {
			crfPageNumbersToRemove = crfPageNumbersToRemove + ',' + selectedCrfPageNumber;
		}
		$('crfPageNumbersToRemove').value = crfPageNumbersToRemove;
		updateQuestionsId();
		updateCrfPageNumberAndShowHideUpDownLink();
		postProcessFormChanges();
	}
</script>
<style type="text/css">

	.makeDraggable {
		cursor: move;
	}

	.formpagesselected {
		background-color: #edc0ac;
		border: 1px solid #da4d01;
	}

	.instructions .summaryvalue {
		font-weight: normal;
		margin-left: 90px;
		text-align: left;
		width: 60%;
	}

	#form-tabs {
		left: 5px;
		position: relative;
		top: 13px;
	}

	#firstlevelnav_1 {
		position: absolute;
		left: 0;
		top: 0;
		display: block;
		height: 0px;
		padding-top: 41px;
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
		padding-top: 41px;
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
		padding-top: 41px;
		width: 160px;
		background-image: url( ../../images/blue/formbuilder_4thlvl_btns.png );
		overflow: hidden;
		background-position: -283px 0;
	}

	.leftBox {
		padding: 10px;
		background-color: #e7eaf3;
		margin-right: 3px;
		position: relative;
	}

	#firstlevelnav_1.selected_4thlvl {
		background-position: 0px -41px;
	}

	#firstlevelnav_2.selected_4thlvl {
		background-position: -145px -41px;
	}

	#firstlevelnav_3.selected_4thlvl {
		background-position: -283px -41px;
	}

	.propertiesHeader {
		color: #00207E;
		display: block;
		font-size: 25px;
	}

	.arrow {
		vertical-align: top;
	}

	#newPageBtn {
		position: absolute;
		top: 5px;
		right: 10px;
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
	<a id="expandQuestionBankUrl" href="javascript:expandQuestionBank()" style="display:none;">Maximize</a>
	<a id="expandFormUrl" href="javascript:expandForm()" style="display:none;">Maximize</a>

            <table id="formbuilderTable">
				<tr>
					<td id="left">
						<a id="shrinkQuestionBankUrl" href="javascript:shrinkQuestionBank()">Minimize</a>

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
								<a id="firstlevelnav_3" href="javascript:showFormSettings()">
									<tags:message code='form.form_settings'/> </a>
							</li>
						</ul>
						<br>
						<br>
						<br>

						<div id="questionBank" class="leftBox">
							<a id="newPageBtn" href="javascript:addCrfPage()"><img
								src="<tags:imageUrl name="blue/new_page_button.png" />" alt="New Page"/></a>
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
						<div id="formSettings" style="display:none;">
							<tags:renderTextArea propertyName="studyCrf.crf.description"
												 displayName="form.label.description" cols="35"/>
						</div>
					</td>
					<td id="right">
						<a id="shrinkFormUrl" href="javascript:shrinkForm()">Minimize</a>

							<%--<a id="reviewAllLink" href="javascript:reviewCompleteForm()">Review</a>--%>
							<%--<a id="reviewLink" href="javascript:playForm()">Play</a>--%>
						<table style="border-collapse:collapse; height:800px;">
							<tr style="height:100%;">
								<td id="formbuilderTable-middle">
									<div id="formbuilderTable-borderTop">
										<span class="formbuilderHeader" id="crfTitle">${command.title}</span>

										<form:hidden path="studyCrf.crf.title" id="formTitle"/>

                                        <span class="formbuildersubHeader">There <span id="plural1">are</span> <span
											id="totalQuestionDivision"><c:choose>
											<c:when test="${totalQuestions}">${totalQuestions}</c:when>
											<c:otherwise>0</c:otherwise>
										</c:choose>
										</span> question<span id="plural2">s</span> in this form.</span>
									</div>

									<form:hidden path="questionsIds" id="questionsIds"/>
									<form:hidden path="crfPageNumbers" id="crfPageNumbers"/>
									<form:hidden path="crfPageNumbersToRemove" id="crfPageNumbersToRemove"/>
									<form:hidden path="numberOfQuestionsInEachPage" id="numberOfQuestionsInEachPage"/>
									<input type="hidden" id="totalQuestions" value="${totalQuestions}">
									<c:forEach items="${command.studyCrf.crf.crfPages}" var="selectedCrfPage"
											   varStatus="status">
										<tags:oneCrfPage crfPage="${selectedCrfPage}"
														 crfPageNumber="${status.index}">
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
