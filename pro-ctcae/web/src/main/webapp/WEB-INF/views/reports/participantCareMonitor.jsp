<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator"
          prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ctcae"
           uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<html>
<head>
<tags:dwrJavascriptLink objects="crf"/>
<tags:dwrJavascriptLink objects="participant"/>
<tags:includePrototypeWindow/>
<tags:includeScriptaculous/>
<script type="text/javascript">
Event.observe(window, "load", function () {
    var studyAutoCompleter = new studyAutoComplter('study');
    acCreateStudyMonitor(studyAutoCompleter);
    initSearchField();
})

function acCreateStudyMonitor(mode) {
    new Autocompleter.DWR(mode.basename + "-input", mode.basename +
                                                    "-choices",
            mode.populator, {
        valueSelector: mode.valueSelector,
        afterUpdateElement: function(inputElement, selectedElement,
                                     selectedChoice) {
            acPostSelect(mode, selectedChoice);
            displayForms();
            displaySites();
            displayParticipants();
        },
        indicator: mode.basename + "-indicator"
    })

}
function displayForms() {
    var id = $('study').value
    crf.getReducedCrfs(id, updateFormDropDown)
}

function clearDiv(divid) {
    var children = $(divid).childElements();
    for (i = 0; i < children.length; i++) {
        $(children[i]).remove();
    }
}

function updateFormDropDown(crfs) {
    //clearDiv('formDropDown');
    $('displayParticipantCareResults').hide();
    var formDropDown = new Element('SELECT', {'id':'formSelect'})

    for (var i = 0; i < crfs.length; i++) {
        var crf = crfs[i];
        var option = new Element('OPTION', {});
        option.text = crf.title;
        option.value = crf.id;
        formDropDown.appendChild(option);
    }

    $('formDropDown').appendChild(formDropDown);
    $('formDropDownDiv').show();
    $('dateMenuDiv').show();
    $('search').show();
}


function displaySites() {

    organization.matchOrganizationByStudyId('%', $('study').value, function(values) {
        var siteNum = values.length;
        var myStudySiteAutoComplter = new studySiteAutoComplter
                ('studySite', $('study').value);
        acCreate(myStudySiteAutoComplter);
        initSearchField();
        if (siteNum > 1) {
            $('studySiteAutoCompleterDiv').show();
        } else {
            $('studySite').value = values[0].id;
            $('studySiteName').innerHTML = values[0].displayName;
            $('studySiteDiv').show();
        }
    })
}

function displayParticipants() {
    var myParticipantAutoCompleter = new participantAutoCompleter
            ('participant', function(autocompleter, text) {
                participant.matchParticipantByStudySiteId(text,
                        $('studySite').value, $('study').value, function(values) {
                    autocompleter.setChoices(values)
                })
            });
    acCreate(myParticipantAutoCompleter);
    initSearchField();
    $('participantAutoCompleterDiv').show();
}

function customVisit(showVisit) {
    var myindex = showVisit.selectedIndex
    var selValue = showVisit.options[myindex].value
    if (selValue == "custom") {
        $('visitNum').show();
    } else {
        $('visitNum').hide();
    }
    if (selValue == "dateRange") {
        $('dateRange').show();
    } else {
        $('dateRange').hide();
    }

}
var hasError = false;
function showError(element) {
    hasError = true;
    removeError(element);
    new Insertion.Bottom(element.parentNode, " <ul id='" + element.name + "-msg'class='errors'><li>" + 'Missing ' + element.title + "</li></ul>");
}
function removeError(element) {
    msgId = element.name + "-msg"
    $(msgId) != null ? new Element.remove(msgId) : null
}

function showIndicator() {
    $('indicator').style.visibility = 'visible';
}
function hideIndicator() {
    $('indicator').style.visibility = 'hidden';
}
function participantCareResults(format, symptomId, selectedTypes) {
    hasError = false;

    var studyId = $('study').value;
    var forVisits = $('visits').value;

    var crfSelect = $('formSelect');
    var crfId = crfSelect.options[crfSelect.selectedIndex].value;

    var visitRangeSelect = $('visitOptions');
    var visitRange = visitRangeSelect.options
            [visitRangeSelect.selectedIndex].value;

    var studySiteId = $('studySite').value;
    if (studySiteId == '') {
        showError($('studySite'));
    } else {
        removeError($('studySite'));
    }
    var participantId = $('participant').value;
    if (participantId == '') {
        showError($('participant'));
    } else {
        removeError($('participant'));
    }

    if (visitRange == 'currentPrev' || visitRange == 'currentLast') {
        forVisits = "2";
    }

    if (visitRange == 'lastFour') {
        forVisits = "4";
    }
    if (visitRange == 'all' || visitRange == 'dateRange') {
        forVisits = "-1";
    }
    var stDate = $('startDate').value;
    var endDate = $('endDate').value;
    if (hasError) {
        return;
    }
    if (format == 'tabular') {
        showIndicator();
        var request = new Ajax.Request("<c:url value="/pages/reports/participantCareResults"/>", {
            parameters:"studyId=" + studyId + "&crfId=" + crfId +
                       "&studySiteId=" + studySiteId + "&participantId=" + participantId +
                       "&visitRange=" + visitRange + "&forVisits=" + forVisits + "&startDate=" + stDate + "&endDate=" + endDate +
                       "&subview=subview",
            onComplete:function(transport) {
                showResultsTable(transport);
                hideIndicator();
            },
            method:'get'
        })
    }
    if (format == 'graphical') {
        if (typeof(selectedTypes) == 'undefined') {
            selectedTypes = '';
        }
        var url = "<c:url value='/pages/reports/participantCareResultsGraph'/>" + "?symptomId=" + symptomId +
                  "&selectedTypes=" + selectedTypes +
                  "&subview=subview";
        $('graph').src = url;
    }
}

function showResultsTable(transport) {
    $('displayParticipantCareResults').show();
    $('displayResultsTable').innerHTML = transport.responseText;
}

function getChartView() {
    $('careResultsTable').hide();
    $('careResultsGraph').show();
}
function getTableView() {
    $('careResultsTable').show();
    $('careResultsGraph').hide();
}
function getChart(symptomId) {
    var obj = document.getElementsByName('div_questiontype');
    for (var i = 0; i < obj.length; i++) {
        obj[i].hide();
    }
    var obj1 = document.getElementsByName('questiontype_' + symptomId);
    if (obj1.length > 1) {
        $('div_questiontype_' + symptomId).show();
    }
    participantCareResults('graphical', symptomId);
}
function updateChart(chkbox, symptomId) {
    var obj = document.getElementsByName('questiontype_' + symptomId);
    var selectedTypes = '';
    for (var i = 0; i < obj.length; i++) {
        if (obj[i].checked) {
            selectedTypes = selectedTypes + ',' + obj[i].value;
        }
    }
    if (selectedTypes == '') {
        alert('Please select at least one question type.');
        chkbox.checked = true;
        return;
    }
    participantCareResults('graphical', symptomId, selectedTypes);
}

function hideHelp() {
    $('attribute-help-content').style.display = 'none';
}

</script>
</head>
<body>
<chrome:box title="participant.label.search_criteria">
    <div align="left" style="margin-left: 50px">
        <tags:renderAutocompleter propertyName="study"
                                  displayName="Study"
                                  required="true"
                                  size="70"
                                  noForm="true"/>
        <div id="formDropDownDiv" style="display:none;" class="row">
            <div class="label">Form</div>
            <div class="value" id="formDropDown"></div>
        </div>
        <div id="studySiteAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="studySite"
                                      displayName="Study site"
                                      size="70"
                                      noForm="true" required="true"/>
        </div>
        <div id="studySiteDiv" style="display:none" class="row">
            <div class="label">Study site <input id="studySite" name="studySite" type="hidden"></div>
            <div id="studySiteName" class="value"></div>
        </div>
        <div id="participantAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="participant"
                                      displayName="Participant"
                                      size="40"
                                      noForm="true" required="true"/>
        </div>
        <div id="dateMenuDiv" style="display:none" class="row">
            <div class="label">Visits</div>
            <div class="value">
                <select id="visitOptions" name="visitOptions"
                        onChange="customVisit(this)">
                    <option value="all">All</option>
                    <option value="currentPrev">Current & Previous</option>
                    <option value="lastFour">Last four</option>
                    <option value="currentLast">Current & First</option>
                    <option value="custom">Custom</option>
                    <option value="dateRange">Date range</option>
                </select>
            </div>

        </div>
        <div id="visitNum" class="row" style="display:none">
            <div class="label"> Most recent</div>
            <div class="value"><input type="text" id="visits" class="validate-NUMERIC" style="width:25px;"/>
                <b>visits</b>
            </div>
        </div>
        <div id="dateRange" style="display:none">
            <div class="leftpanel">
                <tags:renderDate noForm="true" displayName="Start Date" propertyName="startDate"
                                 doNotShowFormat="true"/>
            </div>
            <div class="rightpanel">
                <tags:renderDate noForm="true" displayName="End Date" propertyName="endDate"
                                 doNotShowFormat="true"/>
            </div>
        </div>

        <div id="search" style="display:none" class="row">
            <div class="value"><tags:button color="blue" value="Search"
                                            onclick="participantCareResults('tabular')" size="big"
                                            icon="search"/>
                <tags:indicator id="indicator"/>
            </div>
        </div>

    </div>
</chrome:box>
<div id="displayParticipantCareResults" style="display:none;">
    <div>
        <div id="displayResultsTable"/>
    </div>
</div>
</body>
</html>