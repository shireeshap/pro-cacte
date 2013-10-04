<%-- This is the standard decorator for all caAERS pages --%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
<tags:stylesheetLink name="tabbedflow"/>
<tags:includeScriptaculous/>
<tags:includePrototypeWindow/>
<script type="text/javascript">

    Event.observe(window, "load", function () {
        var selectedItemBank = jQuery("#itemBank option:selected").val();

        <c:if test="${not empty command.crf.crfPagesSortedByPageNumber}">
	
	        updateQuestionsId();
	        addRemoveConditionalTriggeringDisplayToQuestion();
            updateCrfPageNumberAndShowHideUpDownLink();
	        reOrderQuestionNumber()
	
	        hideQuestionsFromForm();
	        <c:forEach items="${selectedProCtcTerms}" var="selectedProCtcTerms" varStatus="status">
	            hideProCtcTermLinkFromForm('${selectedProCtcTerms}')
	        </c:forEach>
        </c:if>
        jQuery('#displayPrefsMenu').menu({
            content: jQuery('#displayOptionsMenu').html(),
            width:250,
            positionOpts:{directionV: 'down',posX: 'left',posY: 'bottom',offsetX: 0,offsetY: 0},
            showSpeed: 300
        });
        //jQuery('#displaySympsMenu').menu({
        //    content: jQuery('#displaySymptomsMenu').html(),
        //    width:133,
        //    positionOpts:{directionV: 'down',posX: 'left',posY: 'bottom',offsetX: 0,offsetY: 0},
        //    showSpeed: 300
        //});
        //set the itemBank DD.
        selectItemBank(selectedItemBank);
    })

    function selectItemBank(selectedItemFromBank){
        if(selectedItemFromBank === 'EQ5D-5L'){
            jQuery("ul.tree > li").each( function (index, elem){
                if(elem.id === 'EQ5D-5L'){
                    $(elem).show();
                } else {
                    $(elem).hide();
                }
            });
        }
        if(selectedItemFromBank === 'EQ5D-3L'){
            jQuery("ul.tree > li").each( function (index, elem){
                if(elem.id === 'EQ5D-3L'){
                    $(elem).show();
                } else {
                    $(elem).hide();
                }
            });
        }
        if(selectedItemFromBank === 'PRO-CTCAE'){
            jQuery("ul.tree > li").each( function (index, elem){
                if(elem.id === 'EQ5D-5L' || elem.id === 'EQ5D-3L'){
                    $(elem).hide();
                } else {
                    $(elem).show();
                }
            });
        }
        updateEq5dSettings(selectedItemFromBank)
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

    function hideQuestionFromForm(questionId) {
        $('question_' + questionId).hide();

    }
    function hideProCtcTermLinkFromForm(proCtcTermId) {
        $('proCtcTerm_' + proCtcTermId).hide();

    }
    function showProCtcTermLinkFromForm(proCtcTermId) {
        $('proCtcTerm_' + proCtcTermId).show();

    }
    function showQuestionInQuestionBank(questionId) {
        $('question_' + questionId).show();

    }

    function postProcessFormChanges() {
        reOrderQuestionNumber()
        addRemoveConditionalTriggeringDisplayToQuestion();
    }
</script>
<script type="text/javascript">

function selectPage(pageIndex) {

<c:if test="${command.crf.advance}">
    unselectAllSelectedPage();
    $('form-pages_' + pageIndex).addClassName('formpagesselected')
    $('form-pages-image_' + pageIndex).show();
</c:if>
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

function updateSelectedCrfItems(questionId) {
    var selectedCrfPageItems = $('selectedCrfPageItems_' + questionId)
    if (selectedCrfPageItems != null) {
        $$('select.selectedCrfPageItems').each(function (item) {
            item.innerHTML = selectedCrfPageItems.innerHTML;
        });
    }

}

function showForm() {
    $('questionBank').show();
    hideQuestionSettings();
    $("firstlevelnav_1").addClassName('selected_4thlvl');
    $("firstlevelnav_2").removeClassName('selected_4thlvl');
    removeEditingDisplayFromQuestions();
}
function showQuestionSettings() {
    showQuestionSettingsTab();

    if ($$('div.sortable').size() != 0) {
        var firstQuestion = $$('div.sortable')[1].id;
        var questionId = firstQuestion.substr(9, firstQuestion.length);
        //showCrfItemProperties(questionId);
        if($('pages_' + questionId) != null){
        	$('pages_' + questionId).style.display = "none";
        }
    }
}
function showQuestionSettingsTab() {
    hideQuestionBank();
    hideCrfItemProperties();
    $("firstlevelnav_2").addClassName('selected_4thlvl');
    $("firstlevelnav_1").removeClassName('selected_4thlvl');


}
function hideQuestionSettings() {
    hideCrfItemProperties();
}
function hideChildren(id) {
    var childnodes = $(id).childNodes;
    for (var i = 0; i < childnodes.length; i++) {
        if (typeof(childnodes[i].id) != 'undefined') {
            $(childnodes[i]).hide();
        }
    }
}
function hideCrfItemProperties() {
    hideChildren('questionProperties');
}

function addEditingDisplayToQuestion(questionId) {
    removeEditingDisplayFromQuestions();
    $('sortable_' + questionId).addClassName('editing');

}
function removeEditingDisplayFromQuestions() {
    $$('div.editing').each(function (item) {
        item.removeClassName('editing');
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
    if (questionId != '') {

        showQuestionSettingsTab();
        showCrfItemProperties(questionId);
    }
}
function hideQuestionBank() {
    $('questionBank').hide();
}
function showFormSettings() {
    $("firstlevelnav_2").removeClassName('selected_4thlvl');
    $("firstlevelnav_1").removeClassName('selected_4thlvl');
    hideQuestionBank();
    hideQuestionSettings();
    $('formSettings').show();
}

function addCrfPageItemDiv(response, crfPageNumber) {
    var children = $('sortablePage_' + crfPageNumber).childElements();
    new Insertion.After(children[children.length - 1].id, response);
    updateQuestionsId();
    updateCrfPageNumberAndShowHideUpDownLink();

}
function addProctcTerm(proCtcTermId, categoryName) {
    var crfPageNumber = ''
    var obj = document.getElementsByName("question_forterm_" + proCtcTermId);
    for (var i = 0; i < obj.length; i++) {
        obj[i].hide();
    }
    getCrfPageNumbersForProCtcTerm(proCtcTermId).each(function(item) {
        crfPageNumber = item;
    })

    addProCtcTermForCrfPage(proCtcTermId, crfPageNumber, categoryName);
    if(categoryName === 'EQ5D-3L' || categoryName === 'EQ5D-5L'){
        updateEq5dSettings(categoryName);
    }
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
    $('totalQuestionDivision').innerHTML = i + '<!--[if IE]>&nbsp;<![endif]-->';
    if (i == 1) {
        $('plural1').innerHTML = 'is';
        $('plural2').innerHTML = '';
    } else {
        $('plural1').innerHTML = 'are';
        $('plural2').innerHTML = 's';
    }

}

function updateConditions() {
    var request = new Ajax.Request("<c:url value="/pages/form/allConditions"/>", {
        parameters:<tags:ajaxstandardparams/>+"&questionsIds=" + $('questionsIds').value,
        onComplete:function(transport) {
            $$('select.selectedCrfPageItems').each(function (item) {
                item.innerHTML = transport.responseText
            })
            var questionId = '';
            $$('div.editing').each(function (item) {
                var id = item.id
                questionId = id.substr(9, id.length);
            })
            showCrfItemPropertiesTab(questionId);
        } ,
        method:'get'
    });

}

function updateCrfPageNumberAndShowHideUpDownLink() {
    var crfPageNumbers = '';
    var formPages = $$('div.formpages');
    var i = 0;
    var currentPageOrder = [];
    formPages.each(function (item) {

        var index = item.id.substr(11, item.id.length);
        currentPageOrder.push(index);
        if(jQuery('#crfPageUpLink_' + index).length > 0 && jQuery('#crfPagDownLink_' + index).length > 0){
            if (i == 0) {
                $('crfPageUpLink_' + index).hide();
                $('crfPagDownLink_' + index).show();
            } else if (i == formPages.length - 1) {
                $('crfPagDownLink_' + index).hide();
                $('crfPageUpLink_' + index).show();
            } else {
                $('crfPageUpLink_' + index).show();
                $('crfPagDownLink_' + index).show();
            }
            i++;
        }
    });
    
    var crfPages = [];
    for(i=0; i<currentPageOrder.length; i++){
    	crfPages[currentPageOrder[i]] = i;
    }
    
    for(i=0; i<crfPages.length; i++){
    	if(i==0){
    		crfPageNumbers = crfPages[i];
    	} else {
    		crfPageNumbers = crfPageNumbers + ',' + crfPages[i];
    	}
    }
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
        updateConditions();
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
        postProcessFormChanges();
        updateConditions();
    }
}

function showCrfItemProperties(selectedQuestionId) {
    addCrfItemPropertiesHtml(selectedQuestionId);
    var questionIdsOfConditionsToDisplay = []
    var addQuestionIdOfConditionsToDisplay = true
    $$("div.sortable").each(function (item) {
        var id = item.id;
        if (!id.include('dummySortable_')) {
            var questionId = id.substr(9, id.length)
            if (questionId == selectedQuestionId) {
                addQuestionIdOfConditionsToDisplay = false;
            }
            if (addQuestionIdOfConditionsToDisplay) {
                questionIdsOfConditionsToDisplay.push(questionId)
            }
        }
    });
    //hide all conditions
    if($$('.conditions').length != 0){
 		var allConditions = $$('.conditions');
 			for (var i = 0; i < allConditions.length; i++) {
 	     		$(allConditions[i]).hide();
 	    	}
    }

    $('questionProperties_' + selectedQuestionId).show();
    questionIdsOfConditionsToDisplay.each(function(item) {
    	if($$('.conditions_' + item).length != 0){
    		 var conditions = $$('.conditions_' + item);
    	     for (var i = 0; i < conditions.length; i++) {
    	     	$(conditions [i]).show();
    	     }
    	}
    });


    addEditingDisplayToQuestion(selectedQuestionId);
    var sortablePosition = parseInt($('sortable_' + selectedQuestionId).viewportOffset()[1]);
    var questionPropertyPosition = parseInt($('questionProperties_' + selectedQuestionId).viewportOffset()[1]);
    var yPosition = sortablePosition - questionPropertyPosition;
    if (sortablePosition < 0) {
        $('questionProperties_' + selectedQuestionId).hide();
    }

    var firstnavposition = (parseInt($('firstlevelnav_2').viewportOffset()[1]));
    if (yPosition < 0) {
         new Effect.Move($('questionProperties_' + selectedQuestionId), { y: firstnavposition-questionPropertyPosition+40});
    } else {
    new Effect.Move($('questionProperties_' + selectedQuestionId), { y: yPosition, mode: 'relative' });
    }
}


function deleteCrfPage(selectedCrfPageNumber, proTermId) {
    var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
        parameters:<tags:ajaxstandardparams/>+"&confirmationType=deleteCrf&selectedCrfPageNumber=" + selectedCrfPageNumber + "&proTermId=" + proTermId,
        onComplete:function(transport) {
            var isAnyConditionalTriggeringQuestion = false;
            var crfPageItems = $$('#form-pages_' + selectedCrfPageNumber + ' div.sortable');
            crfPageItems.each(function (item) {
                var id = item.id;
                if (!id.include('dummySortable_')) {
                    var conditionalTriggeredQuestionId = id.substr(9, id.length);
                    var conditions = $$('tr.conditionalTriggering_' + conditionalTriggeredQuestionId);
                    conditions.each(function(item) {
                        isAnyConditionalTriggeringQuestion = true;
                    })
                }

            })
            showConfirmationWindow(transport);
            if (isAnyConditionalTriggeringQuestion) {
                $('conditionsWarningForCrfPage_' + selectedCrfPageNumber).show();

            }
        },
        method:'get'
    });
}

function deleteQuestion(questionId, proCtcTermId) {

    var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
        parameters:<tags:ajaxstandardparams/>+"&confirmationType=deleteQuestion&questionId=" + questionId + "&proCtcTermId=" + proCtcTermId,
        onComplete:function(transport) {
            showConfirmationWindow(transport);
            if ($$("#sortable_" + questionId + '.conditional-triggering').length > 0) {
                $('conditionsWarning_' + questionId).show();
            }
        } ,
        method:'get'
    });
}

function deleteQuestionConfirm(questionId, proCtcTermId) {
    closeWindow();
    $('questionIdToRemove').value = questionId;
    refreshPage();
}

function refreshQuestionDiv(questionId){
	 var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
	        parameters:<tags:ajaxstandardparams/>+"&confirmationType=deleteCrfPostConfirm" + "&pageNumberToRemove="+questionId +
	         "&crfPageNumbers="+$('crfPageNumbers').value, 
	        onComplete:function(transport) {
	           closeWindow();
	           jQuery("#formBuildersDiv").empty();
	           jQuery("#formBuildersDiv").append(transport.responseText);
	        } ,
	        method:'get'
	    });
}

</script>
<script type="text/javascript">
    function getQuestionIdsInAPage(selectedCrfPageNumber) {
        var questionIds = [];
        var crfPageItems = $$('#form-pages_' + selectedCrfPageNumber + ' div.sortable');
        crfPageItems.each(function (item) {
            var id = item.id;
            if (!id.include('dummySortable_')) {
                var questionId = id.substr(9, id.length);
                questionIds.push(questionId);

            }

        })
        return questionIds;
    }

    function getCrfPageNumbersForProCtcTerm(proCtcTermId) {
        var crfPageNumbers = [];

        var formPages = $$('div.formpages');
        formPages.each(function(item) {
            var crfPageNumber = item.id.substr(11, item.id.length);
            if ($$('#form-pages_' + crfPageNumber + ' div.selectedProCtcTerm_' + proCtcTermId).length > 0) {
                crfPageNumbers.push(crfPageNumber);
            }
        });
        return crfPageNumbers;

    }
    function deleteCrfPageConfirm(selectedCrfPageNumber) {
       refreshQuestionDiv(selectedCrfPageNumber);
    }

    function addCrfPageItemDiv(response, crfPageNumber) {
        var children = $('sortablePage_' + crfPageNumber).childElements();
        new Insertion.After(children[children.length - 1].id, response);
        updateQuestionsId();
        updateCrfPageNumberAndShowHideUpDownLink();

    }

    function addCrfPageDiv(transport) {
        var response = transport.responseText;
        new Insertion.Before("hiddenCrfPageDiv", response);
        updateQuestionsId()
        //			updateSelectedCrfItems(questionId)
        //			addCrfItemPropertiesHtml(questionId);
        updateCrfPageNumberAndShowHideUpDownLink();


        var formPages = $$('div.formpages');
        var item = formPages[formPages.length - 1];
        var crfPageNumber = item.id.substr(11, item.id.length);
        hideQuestionsFromForm();

        postProcessFormChanges();
        updateConditions();
    }
	
	function addProCtcTermForCrfPage(proCtcTermId, crfPageNumber, categoryName) {
	    var request = new Ajax.Request("<c:url value="/pages/form/addCrfComponent"/>", {
	        parameters:<tags:ajaxstandardparams/>+"&isConfirmation=true&proCtcTermId=" + proCtcTermId + "&crfPageNumber=" + crfPageNumber + "&componentType=proCtcTerm" + "&categoryName=" + categoryName,
	        onComplete:function(transport) {
	            var response = transport.responseText;
	            
                if(response.indexOf("isConfirm") != -1){
                    showConfirmationWindow(transport);   
                } else {
                    if (crfPageNumber == '') {
                        addCrfPageDiv(transport);
                    } else {
                        addCrfPageItemDiv(response, crfPageNumber)
                        hideQuestionsFromForm();
                        postProcessFormChanges();
                        updateConditions();
                    }
                }
	        },
	        method:'get'
	    })
	    hideProCtcTermLinkFromForm(proCtcTermId);
	}
	
	function deleteExistingAndAddItemsForQuestion(crfPageNumber,proCtcTermId, componentType, categoryName){
	    closeWindow();
	    var crfPageNumbers = jQuery("#crfPageNumbers").val();
	    var request = new Ajax.Request("<c:url value="/pages/form/addCrfComponent"/>", {
	        parameters:<tags:ajaxstandardparams/>+"&isConfirmation=false&proCtcTermId=" + proCtcTermId + "&componentType=proCtcTerm" + "&crfPageNumbers=" + crfPageNumbers + "&categoryName=" + categoryName,
	        onComplete:function(transport) {
	            var response = transport.responseText;
	            crfPageNumber = 0;
	            $$('div[id^=\"form-pages_\"]').each(function(crfPageItemToRemove) {
	                crfPageItemToRemove.remove();
	            });
                if (crfPageNumber == '') {
                    addCrfPageDiv(transport);
                } else {
                    addCrfPageItemDiv(response, crfPageNumber)
                    hideQuestionsFromForm();
                    postProcessFormChanges();
                    updateConditions();
                }
	            if(categoryName === 'EQ5D-3L' || categoryName === 'EQ5D-5L'){
	                updateEq5dSettings(categoryName);
	            }
	        },
	        method:'get'
	    })
	}

    function addCtcCategory(ctcCategoryId, categoryName) {
        var crfPageNumbers = jQuery("#crfPageNumbers").val();
        var request = new Ajax.Request("<c:url value="/pages/form/addCrfComponent"/>", {
            parameters:<tags:ajaxstandardparams/>+"&isConfirmation=true&ctcCategoryId=" + ctcCategoryId + "&categoryName=" + categoryName + "&componentType=ctcCategory" + "&crfPageNumbers=" + crfPageNumbers,
            onComplete:function(transport) {
                var response = transport.responseText;
                if(response.indexOf("isConfirm") != -1){
                    showConfirmationWindow(transport);   
                } else {
                   addResponseToQuestionsSection(transport, ctcCategoryId);
                   if(categoryName === 'EQ5D-3L' || categoryName === 'EQ5D-5L'){
                       updateEq5dSettings(categoryName);
                   }
                   postProcessFormChanges();
                }
            },
            method:'get'
        })
    }

    function deleteExistingAndAddItemsForCategory(componentType, ctcCategoryId, categoryName, crfPageNumbers){
        closeWindow();
        var crfPageNumbers = jQuery("#crfPageNumbers").val();
        var request = new Ajax.Request("<c:url value="/pages/form/addCrfComponent"/>", {
            parameters:<tags:ajaxstandardparams/>+"&isConfirmation=false&ctcCategoryId=" + ctcCategoryId + "&categoryName=" + categoryName + "&componentType=ctcCategory" + "&crfPageNumbers=" + crfPageNumbers,
            onComplete:function(transport) {
                addResponseToQuestionsSection(transport, ctcCategoryId);
                if(categoryName === 'EQ5D-3L' || categoryName === 'EQ5D-5L'){
                    updateEq5dSettings(categoryName);
                }
                postProcessFormChanges();
            },
            method:'get'
        })
    }
    
    function addResponseToQuestionsSection(transport, ctcCategoryId){
        $$('div[id^=\"form-pages_\"]').each(function(crfPageItemToRemove) {
            crfPageItemToRemove.remove();
        });
        addCrfPageDiv(transport);
        $$('div.crfPageItemsToAdd').each(function(crfPageItemToAdd) {
             var crfPageNumber = crfPageItemToAdd.id.substr(22, crfPageItemToAdd.id.length);
             var crfPageItemHtml = $(crfPageItemToAdd.id).innerHTML;
             addCrfPageItemDiv(crfPageItemHtml, crfPageNumber);
             crfPageItemToAdd.remove();
        });
        hideProCtcTermsForCtcCategory(ctcCategoryId);
    }
    
    function updateEq5dSettings(categoryName){
        //updateFormTitle(categoryName);
        updateRecallPeriod(categoryName);
    }
    
    function updateFormTitle(categoryName){
        jQuery('#crf\\.title').val(categoryName);
    }
    
    function updateRecallPeriod(categoryName){
        if(categoryName === 'PRO-CTCAE'){
            jQuery('#recallPeriod').attr('selectedIndex', '0');
            jQuery('#recallPeriod').trigger('change');
            jQuery('#recallPeriod').removeAttr('disabled');
            jQuery('#recallPeriodOtherSpecifyInput').removeAttr('disabled');
        } else {
            jQuery('#recallPeriod').attr('selectedIndex', '3');
            jQuery('#recallPeriod').trigger('change');
            jQuery('#recallPeriodOtherSpecifyInput').val('Please select the statement that best describes your health today');
            jQuery('#recallPeriod').attr('disabled', 'disabled');
            jQuery('#recallPeriodOtherSpecifyInput').attr('disabled', 'disabled');
        }
    }
    
    function hideProCtcTermsForCtcCategory(ctcCategoryId) {
        $$('a.ctcCategory_' + ctcCategoryId).each(function (proCtcTerm) {
            proCtcTerm.hide();
        })
    }

    function showHideCtcTerm(action, text) {
        $('displayPrefsMenu').innerHTML = '<span class="ui-icon ui-icon-triangle-1-s"></span>' + text;
        jQuery('.displayPrefsCheck').css('visibility', 'hidden');
        removeClassFromHyperlink();
        showHideCtcTermA('right', 'hide');
        showHideCtcTermA('middle', 'hide');
        showHideCtcTermA('left', 'hide');
        showHideCtcTermA('rightpro', 'hide');
        showHideCtcTermA('only', 'hide');
        if (action == 'append') {
            showHideCtcTermA('middle', 'show');
            showHideCtcTermA('right', 'show');
            jQuery('.participantFirst-check').css('visibility', 'visible');
        }
        if (action == 'prepend') {
            showHideCtcTermA('left', 'show');
            showHideCtcTermA('rightpro', 'show');
            jQuery('.ctcaeFirst-check').css('visibility', 'visible');
        }
        if (action == 'noctcterm') {
            showHideCtcTermA('middle', 'show');
            jQuery('.participantOnly-check').css('visibility', 'visible');
        }
        if (action == 'onlyctcterm') {
            showHideCtcTermA('only', 'show');
            jQuery('.ctcaeOnly-check').css('visibility', 'visible');
        }
    }
    function removeClassFromHyperlink() {
        $('a_append').removeClassName('nolink_hyperlink');
        $('a_prepend').removeClassName('nolink_hyperlink');
        $('a_noctcterm').removeClassName('nolink_hyperlink');
        $('a_onlyctcterm').removeClassName('nolink_hyperlink');
    }

    function showHideCtcTermA(side, action) {
        $$('span.ctcterm' + side).each(function (ctcTerm) {
            if (action == 'hide') {
                ctcTerm.hide();
            } else {
                ctcTerm.show();
            }
        })
    }

    function hide(objid) {
        $(objid).style.display = 'none';
    }
    function setVisible(objid) {
        var obj = $(objid);
        obj.style.display = 'inline';
        return false;
    }

    function selectRecallPeriod(value) {
        if (value == 'other') {
            $('recallPeriodOtherSpecifyInput').show();
            jQuery('#recallPeriodOtherSpecifyInput').attr("class", "validate-NOTEMPTY");
            $('recallPeriodOtherSpecifyInput').value = '';
        } else {
            jQuery('#recallPeriodOtherSpecifyInput').removeClass("validate-NOTEMPTY");
            $('recallPeriodOtherSpecifyInput').hide();
            $('recallPeriodOtherSpecifyInput').value = value;
        }

    }
    function selectOtherSpecifyRecallPeriod() {
        $('crf.recallPeriodOtherSpecifyInput').enable();
        $('crf.recallperiodOther-radio').value = $('crf.recallPeriodOtherSpecifyInput').value;

    }

    function isSpclCharForTitle(fieldName) {
        var iChars = "!@#$^&*+=[]\\\';/{}|\"<>?";
        var fieldValue = $(fieldName).value;
        jQuery('#' + fieldName + '.error').hide();
        $(fieldName + '.error').hide();
        for (var i = 0; i < fieldValue.length; i++) {
            if (iChars.indexOf(fieldValue.charAt(i)) != -1) {
                jQuery('#' + fieldName + '.error').show();
                $(fieldName + '.error').show();
                //$(fieldName).value = "";
                return true;
            }
        }
        return false;
    }

</script>
<style type="text/css">

    .makeDraggable {
        cursor: pointer;
        position: relative;
    }

    #form-tabs {
        margin-top: 4px;
    }

    #firstlevelnav_1 {
        left: 0;
        top: 3px;
        display: block;
        font-size: 0;
        text-indent: -9999px;
        padding-top: 40px;
        width: 145px;
        background-image: url(../../images/blue/formbuilder_4thlvl_btns.png);
        overflow: hidden;
        background-repeat: no-repeat;
    }

    #firstlevelnav_3 {
        left: 145px;
        top: 3px;
        display: block;
        font-size: 0;
        text-indent: -9999px;
        padding-top: 40px;
        width: 138px;
        background-image: url(../../images/blue/formbuilder_4thlvl_btns.png);
        overflow: hidden;
        background-position: -145px 0;
        background-repeat: no-repeat;
    }

    #firstlevelnav_2 {
        left: 283px;
        top: 3px;
        display: block;
        font-size: 0;
        text-indent: -9999px;
        padding-top: 40px;
        width: 160px;
        background-image: url(../../images/blue/formbuilder_4thlvl_btns.png);
        overflow: hidden;
        background-position: -283px 0;
        background-repeat: no-repeat;
    }

    .leftBox {
        padding: 10px;
        background-color: #e7eaf3;
        margin-right: 3px;
        margin-top: 44px;
    }

    #firstlevelnav_1.selected_4thlvl {
        background-position: 0px -41px;
    }

    #firstlevelnav_3.selected_4thlvl {
        background-position: -145px -41px;
    }

    #firstlevelnav_2.selected_4thlvl {
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

    ul.tree span.a.children a.addallbtn {
        background-image: none;
        padding: 0;
    }

    .nolink_hyperlink {
        font-weight: bold;
        text-decoration: none;
        cursor: default;
        font-style: italic;
    }

    /* The hint to Hide and Show */
    .hint {
        z-index: 3; /* To handle the overlapping issue*/
        display: none;
        position: absolute;
        width: 300px;
        white-space: normal;
        margin-top: -4px;
        border: 1px solid #c93;
        padding: 10px 12px;
        opacity: .95;
        background: #ffc url(../images/pointer.gif) no-repeat -10px 5px;
    }

    td.help-values {
        border: 1px solid #eaeaea;
        background-color: #fff;
        padding-left: 6px;
        vertical-align: top;
        text-align: left;
    }

    table.widget {
        border: 1px solid #eaeaea;
        border-collapse: collapse;
    }

    #currentPreference {
        font-style: italic;
        font-size: 12px;
    }

    .fg-menu a span {
        float: left;
        margin-right: 5px;
    }

    * {
        zoom: 1;
    }

    ul.tree .formbuilderboxContent li {
        background: none;
    }

    .division h3 {
        border-bottom: 1px solid #CCCCCC;
        color: #2E3257;
        font-size: 1em;
        padding: 4px 8px 1px;
    }
</style>
<!--[if lte IE 8]>
<style>
    #main {
        top: 115px;
    }

    #questionBank, .questionProperties, #formSettings, .leftBox {
        margin-top: -1px;
    }

    div.row div#hackThisForIE {
        margin-left: 12px;
    }
</style>
<![endif]-->
</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
<jsp:attribute name="singleFields">

    <chrome:box>
	    <table>
	        <tr>
	            <td style="text-align:right;font-weight:bold;margin-left:10em;"><tags:message code='form.label.study'/>:</td>
	            <td style="padding-left:10px;">${command.crf.study.displayName}</td>
	        </tr>
	        <tr>
	            <td style="text-align:right;font-weight:bold;">Instructions:</td>
	            <td style="padding-left:10px;"><tags:message code='form.label.title.instruction'/></td>
	        </tr>
	    </table>
	</chrome:box> 
	    
	<chrome:box title="Basic Details">
	 	<table>
	        <tr>
	            <td style="text-align:right;font-weight:bold;margin-left:10em;"><c:if test="${command.crf.crfVersion eq 1.0}"> <tags:requiredIndicator/> </c:if>
	            	<spring:message code="form.label.title"/>:</td>
	            <td>
		            <c:choose>
	                    <c:when test="${command.crf.crfVersion eq 1.0}">
	                        <input type="text" name="crf.title" id="crf.title" value="${command.crf.title}"
	                               onblur="isSpclCharForTitle('crf.title');" style="font-size:1.5em;"
	                               size="60"/>
	                    </c:when>
	                    <c:otherwise>
	                            <input type="text" name="crf.title" id="crf.title" value="${command.crf.title}"
	                                   onblur="isSpclCharForTitle('crf.title');" style="font-size:1.5em;" size="60"/>
	                    </c:otherwise>
	                </c:choose>
	                <ul id="crf.title.error" style="display:none;left-padding:8em;" class="errors">
	                    <li><spring:message code='special.character.message' text='special.character.message'/></li>
	                </ul>
	            </td>
	        </tr>
            <tr><td  style="text-align:right;font-weight:bold;margin-left:10em;">Item bank:</td>
               <td>
               <select id="itemBank" onchange="javascript:selectItemBank(this.value)">
                   <c:forEach items="${itemBank}" var="item">
                       <c:choose>
                           <c:when test="${item eq command.selectedItemBank}">
                               <option value="${item}" selected>${item}</option>
                           </c:when>
                           <c:otherwise>
                               <option value="${item}">${item}</option>
                           </c:otherwise>
                       </c:choose>
                   </c:forEach>
               </select>    
               </td>        
            </tr>
	        <tr>
				<c:set var="isOther" value="true"/>
				<c:set var="style" value="margin:3px;"/>
				<c:forEach items="${recallPeriods}" var="recallPeriod">
				    <c:if test="${recallPeriod.desc eq command.crf.recallPeriod}">
				        <c:set var="isOther" value="false"/>
				        <c:set var="style" value="margin:3px;display:none"/>
				    </c:if>
				</c:forEach>
				<td style="text-align:right;font-weight:bold;margin-left:10em;">
					<tags:message code="recall.period"/>:
				</td>
				<td>
					<select id="recallPeriod" onchange="javascript:selectRecallPeriod(this.value)">
			        <c:forEach items="${recallPeriods}" var="recallPeriod">
			            <c:choose>
			                <c:when test="${(recallPeriod.desc eq command.crf.recallPeriod) or (recallPeriod.code eq 'other' and isOther eq 'true')}">
			                    <option value="${recallPeriod.code}" selected>${recallPeriod.desc}</option>
			                </c:when>
			                <c:otherwise>
			                    <option value="${recallPeriod.code}">${recallPeriod.desc}</option>
			                </c:otherwise>
			            </c:choose>
			        </c:forEach>
			    	</select>
			    	<input type="text" name="crf.recallPeriod" id="recallPeriodOtherSpecifyInput" value="${command.crf.recallPeriod}" size="60"
			           	style="${style}" class="validate-NOTEMPTY" title="Recall period"/>
				</td>
	        </tr>
	    </table>
        
    </chrome:box>
    
<chrome:box title="Questions">

    <div id="displayOptionsMenu" class="hidden">
        <ul>
            <li>
                <a href="#" onclick="showHideCtcTerm('noctcterm','Showing participant term only')"
                   id="a_noctcterm"><span class="ui-icon ui-icon-check displayPrefsCheck participantOnly-check"
                                          style="visibility:hidden;"></span>Participant term only</a>
            </li>
            <li>
                <a href="#" onclick="showHideCtcTerm('onlyctcterm','Showing CTCAE v4 / MedDRA term only')"
                   id="a_onlyctcterm"><span
                        class="ui-icon ui-icon-check displayPrefsCheck ctcaeOnly-check"
                        style="visibility:hidden;"></span>CTCAE v4 / MedDRA term only</a>
            </li>
            <li>
                <a href="#" onclick="showHideCtcTerm('append','Showing both (participant term first)')"
                   id="a_append"><span class="ui-icon ui-icon-check displayPrefsCheck participantFirst-check"
                                       style="visibility:hidden;"></span>Both (participant term first)</a>
            </li>
            <li>
                <a href="#" onclick="showHideCtcTerm('prepend','Showing both (CTCAE v4/MedDRA term first)')"
                   id="a_prepend"><span
                        class="ui-icon ui-icon-check displayPrefsCheck ctcaeFirst-check"></span>Both (CTCAE v4 / MedDRA term first)</a>
            </li>
        </ul>
    </div>

    <table id="formbuilderTable" border="0">
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

                </ul>


                <div id="questionBank" class="leftBox">
                    <tags:instructions code="form.label.question_bank.instructions"/>
                    <table>
                        <tr>
                            <td>
                                <div>
			                        <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
			                           id="displayPrefsMenu"><span class="ui-icon ui-icon-triangle-1-s"></span>Display 
			                            Preferences</a>
			                    </div>
                            </td>
                        </tr>
                    </table>

                    <c:if test="${advance}">
                        <a id="newPageBtn" href="javascript:addCrfPage()"><img
                                src="<tags:imageUrl name="blue/new_page_button.png" />"
                                alt="New Page"/></a></c:if>
                    <ul class="tree">
                        <c:set var="add" value="${advance}"/>
                        <c:forEach items="${ctcCategoryMap}" var="ctcCategory">

                            <li id="${ctcCategory.key.name}">${ctcCategory.key.name}<a
                                    href="javascript:addCtcCategory('${ctcCategory.key.id}', '${ctcCategory.key.name}')"
                                    id="ctcCategory_${ctcCategory.key.id}" class="addallbtn">
                                <img src="/proctcae/images/blue/select_question_btn.png"
                                     alt="Add" onclick=""/></a>
                                <ul>
                                    <c:forEach items="${ctcCategory.value}" var="proCtcTerm">
                                        <li class="closed">
                                            <span class="ctctermleft"> ${proCtcTerm.ctcTerm.ctcTermVocab.termEnglish} - </span>
                                                <span class="ctctermmiddle"
                                                      style="display:none">${proCtcTerm.term}</span>
                                                <span class="ctctermright"
                                                      style="display:none"> - [${proCtcTerm.ctcTerm.ctcTermVocab.termEnglish}]</span>
                                                <span class="ctctermonly"
                                                      style="display:none">${proCtcTerm.ctcTerm.ctcTermVocab.termEnglish}</span>
                                            <span class="ctctermrightpro">[${proCtcTerm.term}]</span>

                                            <c:if test="${!fn:startsWith(ctcCategory.key.name, 'EQ5D')}">
	                                            <a href="javascript:addProctcTerm('${proCtcTerm.id}', '${ctcCategory.key.name}')"
	                                               id="proCtcTerm_${proCtcTerm.id}"
	                                               class="addallbtn ctcCategory_${ctcCategory.key.id}">
	                                                <img src="/proctcae/images/blue/select_question_btn.png"
	                                                     alt="Add" onclick=""/></a>
                                            </c:if>


                                            <ul>

                                                <c:forEach items="${proCtcTerm.proCtcQuestions}"
                                                           var="proCtcQuestion">

                                                    <li id="question_${proCtcQuestion.id}"
                                                        name="question_forterm_${proCtcTerm.id}">
                                                        <tags:formbuilderBox>
                                                            <tags:formbuilderBoxControls add="${add}"
                                                                                         proCtcQuestionId="${proCtcQuestion.id}"
                                                                                         proCtcTermId="${proCtcTerm.id}"/>
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
                </div>

                <div id="questionProperties">
                    <div id="questionProperties0" style="display:none;"></div>
                </div>

            </td>
            <td id="right">
                <table style="border-collapse:collapse; width:100%;height:100%">
                    <tr style="height:5px;" height="5">
                        <td id="formbuilderTable-TL"></td>
                        <td id="formbuilderTable-T">
                            <div style="height:31px;visibility:hidden;">spacer</div>
                        </td>
                        <td id="formbuilderTable-TR"></td>
                    </tr>
                    <tr style="">
                        <td id="formbuilderTable-L"></td>
                        <td id="formbuilderTable-M">
                            <div style="position:relative; top:-20px; left:-20px;">
                        <span class="formbuildersubHeader">There <span id="plural1">are</span>
                            <span id="totalQuestionDivision">
                                <c:choose>
                                    <c:when test="${totalQuestions}">
                                        ${totalQuestions}
                                    </c:when>
                                    <c:otherwise>
                                        0<!--[if IE]>&nbsp;<![endif]-->
                                    </c:otherwise>
                                </c:choose>
                            </span>
                            question<span id="plural2">s</span> in this form.</span>


                                <form:hidden path="questionsIds" id="questionsIds"/>
                                <form:hidden path="questionIdToRemove" id="questionIdToRemove"/>

                                <form:hidden path="crfPageNumbers" id="crfPageNumbers"/>
                                <form:hidden path="crfPageNumberToRemove" id="crfPageNumberToRemove"/>

                                <input type="hidden" id="totalQuestions" value="${totalQuestions}">
                                
                                <div id="formBuildersDiv">
	                                <c:forEach items="${command.crf.crfPagesSortedByPageNumber}"
	                                           var="selectedCrfPage"
	                                           varStatus="status">
	                                    <tags:oneCrfPage crfPage="${selectedCrfPage}"
	                                                     crfPageNumber="${status.index}"
	                                                     advance="${command.crf.advance}"
	                                                     isEq5d="${command.crf.eq5d}">
	                                    </tags:oneCrfPage>
	
	                                </c:forEach>

                                	<div id="hiddenCrfPageDiv"></div>
                                 </div>
                            </div>
                        </td>
                        <td id="formbuilderTable-R"></td>
                    </tr>
                    <tr>
                        <td id="formbuilderTable-BL"></td>
                        <td id="formbuilderTable-B"></td>
                        <td id="formbuilderTable-BR"></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</chrome:box>
<script>
jQuery(document).ready(
    function(){
        jQuery(".tree li").addClass("closed");
        jQuery("")
    }        
);
</script>
</jsp:attribute>
</tags:tabForm>
</body>
