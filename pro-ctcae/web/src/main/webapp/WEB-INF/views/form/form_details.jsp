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

    <c:if test="${not empty command.crf.crfPagesSortedByPageNumber}">
        updateQuestionsId();
        addRemoveConditionalTriggeringDisplayToQuestion();
        updateCrfPageNumberAndShowHideUpDownLink();
        reOrderQuestionNumber()

        hideQuestionsFromForm();
        hideProCtcTermFromForm();

    <c:forEach items="${command.crf.crfPagesSortedByPageNumber}" var="crfPage" varStatus="status">
        var crfPageNumber = '${status.index}';
        crfPageItemEditor(crfPageNumber);
    </c:forEach>

    <c:forEach items="${selectedProCtcTerms}" var="selectedProCtcTerms" varStatus="status">
        hideProCtcTermLinkFromForm('${selectedProCtcTerms}')
    </c:forEach>
    </c:if>
    })

    Event.observe(window, "load", function () {
        var formNameInPlaceEdit = new Ajax.InPlaceEditor('crfTitle', '/proctcae/pages/form/setName', {
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
        updateQuestionsId();
        updateCrfPageNumberAndShowHideUpDownLink();

    }
    function addProctcTerm(proCtcTermId) {
        var crfPageNumber = ''
        var obj = document.getElementsByName("question_forterm_" + proCtcTermId);
        for (var i = 0; i < obj.length; i++) {
            obj[i].hide();
        }
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
        var yPosition = parseInt($('sortable_' + selectedQuestionId).cumulativeOffset()[1]) - parseInt($('questionProperties_' + selectedQuestionId).cumulativeOffset()[1]) - 90;
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
        crfPageItemEditor(crfPageNumber);
        hideQuestionsFromForm();

        postProcessFormChanges();
        updateConditions();


    }
    function crfPageItemEditor(crfPageNumber) {
        var description = 'crf.crfPagesSortedByPageNumber[' + crfPageNumber + '].description';
        var descriptionProperty = description + '-property';

        var formNameInPlaceEdit = new Ajax.InPlaceEditor(descriptionProperty, '/proctcae/pages/form/setName', {
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
</script>


<script type="text/javascript">

    function addCtcCategory(ctcCategoryId) {

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
        $('currentPreference').innerHTML = text;
        hide('preferencevalues');
        removeClassFromHyperlink();
        showHideCtcTermA('right', 'hide');
        showHideCtcTermA('middle', 'hide');
        showHideCtcTermA('left', 'hide');
        showHideCtcTermA('rightpro', 'hide');
        showHideCtcTermA('only', 'hide');
        if (action == 'append') {
            showHideCtcTermA('middle', 'show');
            showHideCtcTermA('right', 'show');
        }
        if (action == 'prepend') {
            showHideCtcTermA('left', 'show');
            showHideCtcTermA('rightpro', 'show');
        }
        if (action == 'noctcterm') {
            showHideCtcTermA('middle', 'show');
        }
        if (action == 'onlyctcterm') {
            showHideCtcTermA('only', 'show');
        }
        $('a_' + action).addClassName('nolink_hyperlink');
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
        top: 0px;
    }

    #firstlevelnav_1 {
        position: absolute;
        left: 0;
        top: 3px;
        display: block;
        height: 0px;
        padding-top: 41px;
        width: 145px;
        background-image: url(../../images/blue/formbuilder_4thlvl_btns.png);
        overflow: hidden;
    }

    #firstlevelnav_2 {
        position: absolute;
        left: 145px;
        top: 3px;
        display: block;
        height: 0px;
        padding-top: 41px;
        width: 138px;
        background-image: url(../../images/blue/formbuilder_4thlvl_btns.png);
        overflow: hidden;
        background-position: -145px 0;
    }

    #firstlevelnav_3 {
        position: absolute;
        left: 283px;
        top: 3px;
        display: block;
        height: 0px;
        padding-top: 41px;
        width: 160px;
        background-image: url(../../images/blue/formbuilder_4thlvl_btns.png);
        overflow: hidden;
        background-position: -283px 0;
    }

    .leftBox {
        padding: 10px;
        background-color: #e7eaf3;
        margin-right: 3px;
        margin-top: 44px;
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

</style>

</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
<jsp:attribute name="singleFields">

    <div class="instructions">
        <div class="summarylabel"><tags:message code='form.label.study'/></div>
        <div class="summaryvalue">${command.crf.study.displayName}</div>
    </div>

    <table width="0%" border="0">
        <tr>
            <td>
                <tags:button value="Preferences" color="blue" size="small"
                             onclick="javascript:setVisible('preferencevalues')"
                             markupWithTag="a"/>
            </td>
            <td>
                <span id="currentPreference">Showing both [ with CTCAE term first ]</span>
            </td>
        </tr>
    </table>

<span id="preferencevalues" class="hint" style="display: none;">
<table class="widget" cellspacing="0" width="100%" align="center">
    <tr>
        <td align="right">
            <a href="javascript:hide('preferencevalues');">X</a>
        </td>
    </tr>
    <tr>
        <td class="help-values">
            <a href="javascript:showHideCtcTerm('noctcterm','Showing participant term only')" id="a_noctcterm">
                Show only participant term
            </a>
        </td>
    <tr>
        <td class="help-values">
            <a href="javascript:showHideCtcTerm('onlyctcterm','Showing CTCAE term only')" id="a_onlyctcterm">
                Show only CTCAE term
            </a>
        </td>
    </tr>

    <tr>
        <td class="help-values">
            <a href="javascript:showHideCtcTerm('append','Showing both [with participant term first]')" id="a_append">
                Show both [ with participant term first ]
            </a>
        </td>
    </tr>
    <tr>
        <td class="help-values">
            <a href="javascript:showHideCtcTerm('prepend','Showing both [with CTCAE term first]')" id="a_prepend"
               class="nolink_hyperlink">Show both [ with CTCAE term first ]
            </a>
        </td>
    </tr>
</table>
</span>
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
                <%--<a id="shrinkFormUrl" href="javascript:shrinkForm()"><img--%>
                <%--src="<tags:imageUrl name="blue/minimize-right.png" />" style="float:left"--%>
                <%--alt="Minimize"/></a>--%>

                <%--<a id="reviewAllLink" href="javascript:reviewCompleteForm()">Review</a>--%>
                <%--<a id="reviewLink" href="javascript:playForm()">Play</a>--%>
            <table style="border-collapse:collapse; height:800px;width:100%;">
                <tr>
                    <td id="formbuilderTable-TL"></td>
                    <td id="formbuilderTable-T"></td>
                    <td id="setformbuilderTable-TR"></td>
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
