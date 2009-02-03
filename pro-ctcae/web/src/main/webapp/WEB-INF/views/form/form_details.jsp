<%-- This is the standard decorator for all caAERS pages --%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="crf" tagdir="/WEB-INF/tags/form" %>
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

    <c:if test="${not empty command.crf.crfPagesSortedByPageNumber}">
        sortQuestions();
        updateQuestionsId();
        addRemoveConditionalTriggeringDisplayToQuestion();
        updateCrfPageNumberAndShowHideUpDownLink();
        reOrderQuestionNumber()
        showHideQuestionUpDownLink();

        hideQuestionsFromForm();
        hideProCtcTermFromForm();

    <c:forEach items="${command.crf.crfPagesSortedByPageNumber}" var="crfPage" varStatus="status">
        var crfPageNumber = '${status.index}';
        crfPageItemEditor(crfPageNumber);
    </c:forEach>
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

    function displayReviewLink() {

        //    if ($('totalQuestions').value != '0') {
        //        $('reviewLink').show();
        //        //$('reviewAllLink').show();
        //
        //    } else {
        //        $('reviewLink').hide();
        //        //$('reviewAllLink').hide();
        //
        //    }
    }

    function updateOrderId() {
        //	var i = 0;
        //	$$("div.sortableSpan").each(function (item) {
        //		item.id = i + 1;
        //
        //	})


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
        //        $$("div.selectedProCtcTerm").each(function (item) {
        //
        //            var proCtcTermId = getProCtcTermId(item.id);
        //
        //
        //        })


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
        showHideQuestionUpDownLink();
        updateTotalNumberOfQuestionsInEachPage();
        addRemoveConditionalTriggeringDisplayToQuestion();

    }
</script>
<script type="text/javascript">
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
                    addCrfPageItemDiv(response, crfPageNumber);
                    updateSelectedCrfItems(questionId)
                    addCrfItemPropertiesHtml(questionId);
                    postProcessFormChanges();
                    updateConditions();
                }

            },
            method:'get'
        })
        hideQuestionFromForm(questionId);
        hideProCtcTermLinkFromForm(proCtcTermId);
        $('totalQuestions').value = displayOrder;
        $('totalQuestionDivision').innerHTML = displayOrder;


    }


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


</script>
<script type="text/javascript">
    function addConditionalQuestion(questionId, selectedValidValues) {


        var request = new Ajax.Request("<c:url value="/pages/form/addConditionalQuestion"/>", {
            parameters:"questionId=" + questionId + "&subview=subview&selectedValidValues=" + selectedValidValues,
            onComplete:function(transport) {
                var response = transport.responseText;

                new Insertion.Before("conditions_" + questionId, response)
                addRemoveConditionalTriggeringDisplayToQuestion();
            },
            method:'get'
        })


    }

    function deleteConditions(questionId, proCtcValidValueId) {


        var request = new Ajax.Request("<c:url value="/pages/form/removeConditions"/>", {
            parameters:"questionId=" + questionId + "&subview=subview&proCtcValidValueId=" + proCtcValidValueId,
            onComplete:function(transport) {
                var inputName = 'conditionalQuestion_' + questionId + '_' + proCtcValidValueId;
                $(inputName + '-row').remove();
                addRemoveConditionalTriggeringDisplayToQuestion();
            },
            method:'get'
        })


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

            showQuestionSettingsTab()
            showCrfItemProperties(questionId);
        }
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

    function addCrfPageItemDiv(response, crfPageNumber) {
        var children = $('sortablePage_' + crfPageNumber).childElements();
        new Insertion.After(children[children.length - 1].id, response);
        sortQuestions()
        updateQuestionsId();
        updateCrfPageNumberAndShowHideUpDownLink();

    }
    function addProctcTerm(proCtcTermId) {
        var crfPageNumber = ''

    <c:if test="${!command.crf.advance}">

        getCrfPageNumbersForProCtcTerm(proCtcTermId).each(function(item) {
            crfPageNumber = item;

        })

    </c:if>
        addProCtcTermForCrfPage(proCtcTermId, crfPageNumber);


    }
    function addProCtcTermForCrfPage(proCtcTermId, crfPageNumber) {
        var request = new Ajax.Request("<c:url value="/pages/form/addCrfComponent"/>", {
            parameters:"proCtcTermId=" + proCtcTermId + "&subview=subview&crfPageNumber=" + crfPageNumber + "&componentType=proCtcTerm",
            onComplete:function(transport) {


                var response = transport.responseText;
                if (crfPageNumber == '') {
                    addCrfPageDiv(transport);


                } else {
                    addCrfPageItemDiv(response, crfPageNumber)
                    hideQuestionsFromForm();
                    postProcessFormChanges();
                    updateConditions();

                }

            },
            method:'get'
        })
        hideProCtcTermLinkFromForm(proCtcTermId);
    }
</script>
<script type="text/javascript">


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
    function reOrderQuestionNumber() {
        var i = 0;
        $$("div.sortableSpan").each(function (item) {
            item.innerHTML = i + 1;
            //			var orderNumberAtCrfItemPropertiesPage = $$("span.sortableSpan")[parseInt(item.id) - 1];
            //			orderNumberAtCrfItemPropertiesPage.innerHTML = i + 1;
            i = i + 1;


        })


    }
    function sortQuestions() {

    <c:if test="${command.crf.advance}">
        var formPages = $$('div.formpages');
        var sortableDivs = [];
        formPages.each(function(item) {
            var index = item.id.substr(11, item.id.length);
            sortableDivs.push('sortablePage_' + index)

        })
        sortableDivs.each(function (item) {
            Sortable.destroy(item)
        })


        sortableDivs.each(function (item) {


            Sortable.create(item, {
                containment:sortableDivs,
                tag    :'div',
                only:['sortable'],
                dropOnEmpty:true,
                scroll:window,
                onUpdate:function (item) {
                    updateQuestionsId();
                    updateTotalNumberOfQuestionsInEachPage();
                    showHideQuestionUpDownLink();
                    updateConditions();
                }
                ,
                onChange:function(item) {
                    reOrderQuestionNumber();

                }

            })
        }
                )

    </c:if>
    }
    function updateConditions() {
        var request = new Ajax.Request("<c:url value="/pages/form/allConditions"/>", {
            parameters:"&subview=subview&questionsIds=" + $('questionsIds').value,
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

</script>
<script type="text/javascript">

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
                        var children = $('sortablePage_' + pageIndex).childElements();
                        var previousItem = children[children.length - 1];
                        previousItemId = previousItem.id;
                        Element.insert(previousItem, {after:$('sortable_' + selectedQuestionId)})

                        selectPage(pageIndex);

                    } else {
                        var previousItem = crfPageItems[i - 1];
                        previousItemId = previousItem.id;
                        Element.insert(previousItem, {before:$('sortable_' + selectedQuestionId)})


                    }

                }
                i++;
            }
        });

        j++;

    });
    if (previousItemId != '') {

        updateQuestionsId();
        updateOrderId();
        postProcessFormChanges();
        updateConditions();
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
        postProcessFormChanges();
        updateConditions();
    }

}

function showHideQuestionUpDownLink() {
<c:if test="${command.crf.advance}">

    var i = 0;
    var j = 0;

    var formPages = $$('div.formpages');

    var totalDummyDiv = formPages.length;
    var crfPageItems = $$('div.sortable');

    crfPageItems.each(function(crfPageItem)
    {
        if (!crfPageItem.id.include('dummySortable_')) {
            var questionId = crfPageItem.id.substr(9, crfPageItem.id.length)
            if (i == 0 & j == 1) {
                $('moveQuestionUpLink_' + questionId).hide();
                $('moveQuestionDownLink_' + questionId).show();

            } else if (i == 0 & j == 0) {
                $('moveQuestionUpLink_' + questionId).hide();
                $('moveQuestionDownLink_' + questionId).show();

            }
            else {
                $('moveQuestionDownLink_' + questionId).show();
                $('moveQuestionUpLink_' + questionId).show();
            }
            if (j == parseInt(crfPageItems.length) - 1) {
                $('moveQuestionDownLink_' + questionId).hide();
                $('moveQuestionUpLink_' + questionId).show();
            }
            i++;
        }
        j++;
    });
</c:if>

}

</script>
<script type="text/javascript">
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
        var allConditions = $$('optgroup.conditions');
        allConditions.each(function(item) {
            item.hide();
        })

        $('questionProperties_' + selectedQuestionId).show();

        questionIdsOfConditionsToDisplay.each(function(item) {
            $$('#condition_' + item + '.conditions').each(function(condition) {
                condition.show();
            });

        })


        addEditingDisplayToQuestion(selectedQuestionId);
        var yPosition = parseInt($('sortable_' + selectedQuestionId).cumulativeOffset()[1]) - parseInt($('questionProperties_' + selectedQuestionId).cumulativeOffset()[1]) - 100;
        new Effect.Move($('questionProperties_' + selectedQuestionId), { y: yPosition, mode: 'relative' });

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


</script>
<script type="text/javascript">
    function deleteCrfPage(selectedCrfPageNumber) {
        var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
            parameters:"confirmationType=deleteCrf&subview=subview&selectedCrfPageNumber=" + selectedCrfPageNumber,
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
            parameters:"confirmationType=deleteQuestion&subview=subview&questionId=" + questionId + "&proCtcTermId=" + proCtcTermId,
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
        $('showForm').value = true;
        $('questionIdsToRemove').value = questionId;
        $('command').submit();

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
        closeWindow();
        $('showForm').value = true;
        $('crfPageNumbersToRemove').value = selectedCrfPageNumber;
        $('command').submit();

    }
</script>
<script type="text/javascript">
    function addCrfPageDiv(transport) {
        var response = transport.responseText;
        new Insertion.Before("hiddenCrfPageDiv", response);
        sortQuestions()
        updateQuestionsId()
        //			updateSelectedCrfItems(questionId)
        //			addCrfItemPropertiesHtml(questionId);
        updateCrfPageNumberAndShowHideUpDownLink();


        var formPages = $$('div.formpages');
        var item = formPages[formPages.length - 1];
        var crfPageNumber = item.id.substr(11, item.id.length);
        crfPageItemEditor(crfPageNumber);
        hideQuestionsFromForm();

        postProcessFormChanges();
        updateConditions();


    }
    function crfPageItemEditor(crfPageNumber) {
        var description = 'crf.crfPagesSortedByPageNumber[' + crfPageNumber + '].description';
        var descriptionProperty = description + '-property';

        var formNameInPlaceEdit = new Ajax.InPlaceEditor(descriptionProperty, '/ctcae/pages/form/setName', {
            rows:1,
            cancelControl:false,
            okControl:false,
            size:18,
            submitOnBlur:true,
            onEnterEditMode:function() {
                if ($(descriptionProperty).innerHTML == 'Click here to name') {
                    $(descriptionProperty).innerHTML = ''

                }

            }  ,
            onComplete:function(transport) {
                $(descriptionProperty).innerHTML = transport.responseText;
                $(description).value = transport.responseText;
                if ($(descriptionProperty).value == '') {
                    $(descriptionProperty).innerHTML = 'Click here to name';

                }


            },
            callback:function(form, value) {
                return 'crfTitle=' + encodeURIComponent(value)
            }
        });
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
</script>

<script type="text/javascript">
    function switchToAdvance() {
        $('switchToAdvance').value = true;
        $('command').submit();

    }
</script>

<script type="text/javascript">

    function addCtcCategory(ctcCategoryId) {

        getAllSelectedProCtcTerm();
        var request = new Ajax.Request("<c:url value="/pages/form/addCrfComponent"/>", {
            parameters:"subview=subview&ctcCategoryId=" + ctcCategoryId + "&componentType=ctcCategory",
            onComplete:function(transport) {

                var response = transport.responseText;
                addCrfPageDiv(transport);
                $$('div.crfPageItemsToAdd').each(function(crfPageItemToAdd) {
                    var crfPageNumber = crfPageItemToAdd.id.substr(22, crfPageItemToAdd.id.length);
                    var crfPageItemHtml = $(crfPageItemToAdd.id).innerHTML;
                    addCrfPageItemDiv(crfPageItemHtml, crfPageNumber);
                    crfPageItemToAdd.remove();

                });

                postProcessFormChanges();
            },
            method:'get'
        })

        // hideProCtcTermLinkFromForm(proCtcTermId);
    }

    function getAllSelectedProCtcTerm() {
        var proCtcTermIds = [];

        var formPages = $$('div.formpages');
        formPages.each(function(item) {
            var crfPageNumber = item.id.substr(11, item.id.length);
            //this will break for advance form builder
            $$(' div.selectedCrfPageForProCtcTerm_' + crfPageNumber).each(function(proCtcTerm) {
                var proCtcTermId = proCtcTerm.id.substr(19, proCtcTerm.id.length);
                if (proCtcTermIds.indexOf(proCtcTermId) == -1)
                    proCtcTermIds.push(proCtcTermId);
            })

        });
        return proCtcTermIds;

    }
</script>
<style type="text/css">

    .makeDraggable {
        cursor: move;
        position: relative;
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

    ul.tree span.a.children a.addallbtn {
        background-image: none;
        padding: 0;
    }


</style>

</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
<jsp:attribute name="singleFields">
        <div class="instructions">

            <div class="summarylabel"><tags:message code='form.label.study'/></div>
            <div class="summaryvalue">${command.crf.study.displayName}</div>
        </div>
    <%--<a id="reviewLink" href="javascript:reviewForm()" style="display:none">Preview</a>--%>
    <c:if test="${!command.crf.advance}"> <a id="advanceMode" href="javascript:switchToAdvance()">Switch to Advanced
        Form
        Builder</a>
    </c:if>


    <a id="expandQuestionBankUrl" href="javascript:expandQuestionBank()" style="display:none;"><img
            src="<tags:imageUrl name="blue/maximize-right.png" />" style="float:left" alt="Maximize"/></a>
	<a id="expandFormUrl" href="javascript:expandForm()" style="display:none;" style="float:right;"><img
            src="<tags:imageUrl name="blue/maximize-left.png" />" alt="Maximize"/></a>

            <table id="formbuilderTable">
                <tr>
                    <td id="left">
                        <a id="shrinkQuestionBankUrl" href="javascript:shrinkQuestionBank()"><img
                                src="<tags:imageUrl name="blue/minimize-left.png" />" style="float:right"
                                alt="Minimize"/></a>

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
                            <c:if test="${advance}">
                                <a id="newPageBtn" href="javascript:addCrfPage()"><img
                                        src="<tags:imageUrl name="blue/new_page_button.png" />"
                                        alt="New Page"/></a></c:if>
                            <ul class="tree">
                                <c:set var="add" value="${advance}"/>
                                <c:forEach items="${ctcCategoryMap}" var="ctcCategory">

                                    <li>${ctcCategory.key.name}<a
                                            href="javascript:addCtcCategory(${ctcCategory.key.id})"
                                            id="ctcCategory_${ctcCategory.key.id}" class="addallbtn">
                                        <img src="/ctcae/images/blue/select_question_btn.png"
                                             alt="Add" onclick=""/></a>
                                        <ul>
                                            <c:forEach items="${ctcCategory.value}" var="proCtcTerm">
                                                <li class="closed">${proCtcTerm.term}
                                                    <a href="javascript:addProctcTerm(${proCtcTerm.id})"
                                                       id="proCtcTerm_${proCtcTerm.id}" class="addallbtn">
                                                        <img src="/ctcae/images/blue/select_question_btn.png"
                                                             alt="Add" onclick=""/></a>


                                                    <ul>

                                                        <c:forEach items="${proCtcTerm.proCtcQuestions}"
                                                                   var="proCtcQuestion">

                                                            <li id="question_${proCtcQuestion.id}">
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

                        <div id="formSettings" style="display:none;">
                            <crf:formSettings crf="${command.crf}"></crf:formSettings>
                        </div>
                    </td>
                    <td id="right">
                        <a id="shrinkFormUrl" href="javascript:shrinkForm()"><img
                                src="<tags:imageUrl name="blue/minimize-right.png" />" style="float:left"
                                alt="Minimize"/></a>

                            <%--<a id="reviewAllLink" href="javascript:reviewCompleteForm()">Review</a>--%>
                            <%--<a id="reviewLink" href="javascript:playForm()">Play</a>--%>
                        <table style="border-collapse:collapse; height:800px;width:100%;">
                            <tr>
                                <td id="formbuilderTable-TL"></td>
                                <td id="formbuilderTable-T"></td>
                                <td id="formbuilderTable-TR"></td>
                            </tr>
                            <tr>
                                <td id="formbuilderTable-L"></td>
                                <td id="formbuilderTable-M">
                                    <div style="width:105%; position:relative; top:-20px; left:-20px;">
                                        <c:choose>
                                            <c:when test="${command.crf.crfVersion eq 1.0}">

                                                <span class="formbuilderHeader" id="crfTitle">${command.title}</span>
                                            </c:when>
                                            <c:otherwise><h1>${command.title}</h1></c:otherwise>
                                        </c:choose>


                                        <form:hidden path="crf.title" id="formTitle"/>

                                        <span class="formbuildersubHeader">There <span id="plural1">are</span> <span
                                                id="totalQuestionDivision"><c:choose>
                                            <c:when test="${totalQuestions}">${totalQuestions}</c:when>
                                            <c:otherwise>0</c:otherwise>
                                        </c:choose>
										</span> question<span id="plural2">s</span> in this form.</span>


                                        <input type="hidden" id="switchToAdvance" name="switchToAdvance" value=""/>
                                        <input type="hidden" id="showForm" name="showForm" value=""/>

                                        <form:hidden path="questionsIds" id="questionsIds"/>
                                        <form:hidden path="questionIdsToRemove" id="questionIdsToRemove"/>

                                        <form:hidden path="crfPageNumbers" id="crfPageNumbers"/>
                                        <form:hidden path="crfPageNumbersToRemove" id="crfPageNumbersToRemove"/>
                                        <form:hidden path="numberOfQuestionsInEachPage"
                                                     id="numberOfQuestionsInEachPage"/>
                                        <input type="hidden" id="totalQuestions" value="${totalQuestions}">
                                        <c:forEach items="${command.crf.crfPagesSortedByPageNumber}"
                                                   var="selectedCrfPage"
                                                   varStatus="status">
                                            <tags:oneCrfPage crfPage="${selectedCrfPage}"
                                                             crfPageNumber="${status.index}"
                                                             advance="${command.crf.advance}">
                                            </tags:oneCrfPage>

                                        </c:forEach>

                                        <div id="hiddenCrfPageDiv"></div>
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

</jsp:attribute>
</tags:tabForm>
</body>
