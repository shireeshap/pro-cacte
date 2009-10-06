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
<tags:includeScriptaculous/>
<tags:includePrototypeWindow/>
<script type="text/javascript">

    Event.observe(window, "load", function () {

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
            width:200,
            positionOpts:{directionV: 'down',posX: 'left',posY: 'bottom',offsetX: 0,offsetY: 0},
            showSpeed: 300
        });
    })


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
    hideFormSettings();
    $("firstlevelnav_1").addClassName('selected_4thlvl');
    $("firstlevelnav_3").removeClassName('selected_4thlvl');
    $("firstlevelnav_2").removeClassName('selected_4thlvl');

}
function showQuestionSettings() {
    showQuestionSettingsTab();

    if ($$('div.sortable').size() != 0) {
        var firstQuestion = $$('div.sortable')[1].id;
        var questionId = firstQuestion.substr(9, firstQuestion.length);
        showCrfItemProperties(questionId);
    }
}
function showQuestionSettingsTab() {
    hideQuestionBank();
    hideCrfItemProperties();
    hideFormSettings();
    $("firstlevelnav_2").addClassName('selected_4thlvl');
    $("firstlevelnav_3").removeClassName('selected_4thlvl');
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

function addCrfPageItemDiv(response, crfPageNumber) {
    var children = $('sortablePage_' + crfPageNumber).childElements();
    new Insertion.After(children[children.length - 1].id, response);
    updateQuestionsId();
    updateCrfPageNumberAndShowHideUpDownLink();

}
function addProctcTerm(proCtcTermId) {
    var crfPageNumber = ''
    var obj = document.getElementsByName("question_forterm_" + proCtcTermId);
    for (var i = 0; i < obj.length; i++) {
        obj[i].hide();
    }
    getCrfPageNumbersForProCtcTerm(proCtcTermId).each(function(item) {
        crfPageNumber = item;
    })

    addProCtcTermForCrfPage(proCtcTermId, crfPageNumber);
}
function addProCtcTermForCrfPage(proCtcTermId, crfPageNumber) {
    var request = new Ajax.Request("<c:url value="/pages/form/addCrfComponent"/>", {
        parameters:<tags:ajaxstandardparams/>+"&proCtcTermId=" + proCtcTermId + "&crfPageNumber=" + crfPageNumber + "&componentType=proCtcTerm",
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
    var allConditions = document.getElementsByClassName("conditions");
    for (var i = 0; i < allConditions.length; i++) {
        $(allConditions[i]).hide();
    }

    $('questionProperties_' + selectedQuestionId).show();

    questionIdsOfConditionsToDisplay.each(function(item) {
        var conditions = document.getElementsByClassName("condition_" + item);
        for (var i = 0; i < conditions.length; i++) {
            $(conditions [i]).show();
        }
    });


    addEditingDisplayToQuestion(selectedQuestionId);
    //    var yPosition = parseInt($('sortable_' + selectedQuestionId).cumulativeOffset()[1]) - parseInt($('questionProperties_' + selectedQuestionId).cumulativeOffset()[1]) - 71;
    //    new Effect.Move($('questionProperties_' + selectedQuestionId), { y: yPosition, mode: 'relative' });

}
function deleteCrfPage(selectedCrfPageNumber, crfPageDescription) {
    var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
        parameters:<tags:ajaxstandardparams/>+"&confirmationType=deleteCrf&selectedCrfPageNumber=" + selectedCrfPageNumber + "&description=" + crfPageDescription,
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
        $('crfPageNumberToRemove').value = selectedCrfPageNumber;
        refreshPage();

    }

</script>
<script type="text/javascript">
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
</script>


<script type="text/javascript">

    function addCtcCategory(ctcCategoryId) {

        var request = new Ajax.Request("<c:url value="/pages/form/addCrfComponent"/>", {
            parameters:<tags:ajaxstandardparams/>+"&ctcCategoryId=" + ctcCategoryId + "&componentType=ctcCategory",
            onComplete:function(transport) {

                var response = transport.responseText;
                addCrfPageDiv(transport);
                $$('div.crfPageItemsToAdd').each(function(crfPageItemToAdd) {
                    var crfPageNumber = crfPageItemToAdd.id.substr(22, crfPageItemToAdd.id.length);
                    var crfPageItemHtml = $(crfPageItemToAdd.id).innerHTML;
                    addCrfPageItemDiv(crfPageItemHtml, crfPageNumber);
                    crfPageItemToAdd.remove();

                });

                hideProCtcTermsForCtcCategory(ctcCategoryId);
                postProcessFormChanges();
            },
            method:'get'
        })

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

    #firstlevelnav_2 {
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

    #firstlevelnav_3 {
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
</style>
</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
<jsp:attribute name="singleFields">
    
    <div style="float:left"/>
    <div class="instructions">
        <div class="summarylabel"><spring:message code="form.label.study"/></div>
        <div class="summaryvalue">${command.crf.study.displayName}</div>
    </div>
    <div class="instructions">
        <div class="summarylabel"><tags:requiredIndicator/>&nbsp;<spring:message code="form.label.title"/></div>
        <div style="margin-left:3em"><input type="text" name="crf.title" value="${command.crf.title}" size="80"/></div>
    </div>
    <div/>
    <div style="float:right;">
        <tags:button type="submit" icon="Save & Continue" color="green" id="flow-next" value="Save & Continue"/>
    </div>

    <tags:instructions code="form.label.instructions"/>
    <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="displayPrefsMenu"
       style="margin-left:11px"><span class="ui-icon ui-icon-triangle-1-s"></span>Display Preferences</a>

    <div id="displayOptionsMenu" class="hidden">
        <ul>
            <li>
                <a href="#" onclick="showHideCtcTerm('noctcterm','Showing participant term only')"
                   id="a_noctcterm"><span class="ui-icon ui-icon-check displayPrefsCheck participantOnly-check"
                                          style="visibility:hidden;"></span>Participant term only</a>
            </li>
            <li>
                <a href="#" onclick="showHideCtcTerm('onlyctcterm','Showing CTCAE term only')"
                   id="a_onlyctcterm"><span
                        class="ui-icon ui-icon-check displayPrefsCheck ctcaeOnly-check"
                        style="visibility:hidden;"></span>CTCAE term only</a>
            </li>
            <li>
                <a href="#" onclick="showHideCtcTerm('append','Showing both (participant term first)')"
                   id="a_append"><span class="ui-icon ui-icon-check displayPrefsCheck participantFirst-check"
                                       style="visibility:hidden;"></span>Both (participant term first)</a>
            </li>
            <li>
                <a href="#" onclick="showHideCtcTerm('prepend','Showing both (CTCAE term first)')"
                   id="a_prepend"><span
                        class="ui-icon ui-icon-check displayPrefsCheck ctcaeFirst-check"></span>Both (CTCAE term
                    first)</a>
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

                    <li class="">
                        <a id="firstlevelnav_3" href="javascript:showFormSettings()">
                            <tags:message code='form.form_settings'/> </a>
                    </li>
                </ul>


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
                                <img src="/proctcae/images/blue/select_question_btn.png"
                                     alt="Add" onclick=""/></a>
                                <ul>
                                    <c:forEach items="${ctcCategory.value}" var="proCtcTerm">
                                        <li class="closed">
                                            <span class="ctctermleft"> ${proCtcTerm.ctcTerm.term} - </span>
                                                <span class="ctctermmiddle"
                                                      style="display:none">${proCtcTerm.term}</span>
                                                <span class="ctctermright"
                                                      style="display:none"> - [${proCtcTerm.ctcTerm.term}]</span>
                                                <span class="ctctermonly"
                                                      style="display:none">${proCtcTerm.ctcTerm.term}</span>
                                            <span class="ctctermrightpro">[${proCtcTerm.term}]</span>

                                            <a href="javascript:addProctcTerm(${proCtcTerm.id})"
                                               id="proCtcTerm_${proCtcTerm.id}"
                                               class="addallbtn ctcCategory_${ctcCategory.key.id}">
                                                <img src="/proctcae/images/blue/select_question_btn.png"
                                                     alt="Add" onclick=""/></a>


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

                <div id="formSettings" class="leftBox" style="display:none;">
                    <tags:formSettings crf="${command.crf}"></tags:formSettings>
                </div>
            </td>
            <td id="right">
                <table style="border-collapse:collapse; width:100%;">
                    <tr style="height:5px;" height="5">
                        <td id="formbuilderTable-TL"></td>
                        <td id="formbuilderTable-T">
                            <div style="height:31px;visibility:hidden;">spacer</div>
                        </td>
                        <td id="formbuilderTable-TR"></td>
                    </tr>
                    <tr style="height:750px;">
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
