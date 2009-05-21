<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<html>
<head>
<tags:dwrJavascriptLink objects="crf"/>
<tags:includePrototypeWindow/>
<tags:includeScriptaculous/>
<script type="text/javascript">

Event.observe(window, "load", function () {
    var studyAutoCompleter = new studyAutoComplter('study');
    acCreateStudyMonitor(studyAutoCompleter);
    initSearchField();
})

function acCreateStudyMonitor(mode) {
    new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices",
            mode.populator, {
        valueSelector: mode.valueSelector,
        afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
            acPostSelect(mode, selectedChoice);
            displayForms();
            displaySites();
        },
        indicator: mode.basename + "-indicator"
    })
}

function getSelect(id) {
    var dropDown ;
    if ($(id + 'Select') == null) {
        dropDown = new Element('SELECT', {'id':id + 'Select'})
        $(id + 'DropDown').appendChild(dropDown);
    } else {
        dropDown = $(id + 'Select');
        var len = dropDown.length;
        for (i = 0; i < len; i++) {
            $(id + 'Select').remove(0);
        }
    }
    return dropDown;
}

function displayForms() {
    var id = $('study').value
    crf.getReducedCrfs(id, updateFormDropDown)
}

function updateFormDropDown(crfs) {
    var formDropDown = getSelect('form');
    formDropDown.onchange = function() {
        displaySymptoms(this);
    }
    for (var i = 0; i < crfs.length; i++) {
        var crf = crfs[i];
        var option = new Element('OPTION', {});
        option.text = crf.title;
        option.value = crf.id;
        formDropDown.appendChild(option);
    }
    displaySymptoms(formDropDown);
    $('formDropDownDiv').show();
    $('dateMenuDiv').show();
    $('search').show();
}

function displaySymptoms(obj) {
    crf.getSymptomsForCrf(obj[obj.selectedIndex].value, updateSymptomDropDown);
}
function updateSymptomDropDown(symptoms) {
    var symptomDropDown = getSelect('symptom');
    symptomDropDown.onchange = function() {
        displayAttributes(this);
    }
    for (var i = 0; i < symptoms.length; i++) {
        var symptom = symptoms[i];
        var option = new Element('OPTION', {});
        option.text = symptom.term;
        option.value = symptom.id;
        symptomDropDown.appendChild(option);
    }
    displayAttributes(symptomDropDown);
    $('symptomDropDown').appendChild(symptomDropDown);
    $('symptomDropDownDiv').show();
}

function displayAttributes(obj) {
    crf.getAttributesForSymptom(obj[obj.selectedIndex].value, updateAttribueDropDown);
}
function updateAttribueDropDown(atributes) {
    var attributeDropDown = getSelect('attribute');

    for (var i = 0; i < atributes.length; i++) {
        var atribute = atributes[i];
        var option = new Element('OPTION', {});
        option.text = atribute;
        option.value = atribute;
        attributeDropDown.appendChild(option);
    }
    $('attributeDropDown').appendChild(attributeDropDown);
    $('attributeDropDownDiv').show();
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
//
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

function studyLevelReportResults(format, symptomId, selectedTypes) {
    hasError = false;
    //        var forVisits = $('visits').value;

    var visitRangeSelect = $('visitOptions');
    var visitRange = visitRangeSelect.options[visitRangeSelect.selectedIndex].value;

    //            if (visitRange == 'currentPrev' || visitRange == 'currentLast') {
    //                forVisits = "2";
    //            }
    //
    //            if (visitRange == 'lastFour') {
    //                forVisits = "4";
    //            }
    //        if (visitRange == 'all' || visitRange == 'dateRange') {
    //            forVisits = "-1";
    //        }

    var stDate = $('startDate').value;
    var endDate = $('endDate').value;
    if (visitRange == 'dateRange') {
        if (stDate == '') {
            hasError = true;
            showError($('startDate'));
        } else {
            removeError($('startDate'));
        }
        if (endDate == '') {
            hasError = true;
            showError($('endDate'));
        } else {
            removeError($('endDate'));
        }
    }
    if (hasError) {
        return;
    }
    //    showIndicator();
    var url = "<c:url value="/pages/reports/symptomSummaryReportResults"/>" +
              "?crfId=" + $('formSelect').options[$('formSelect').selectedIndex].value +
              "&symptom=" + $('symptomSelect').options[$('symptomSelect').selectedIndex].value +
              "&attribute=" + $('attributeSelect').options[$('attributeSelect').selectedIndex].value +
              "&gender=all" +
              "&studySiteId=" + $('studySite').value +
              "&visitRange=" + visitRange +
              "&startDate=" + stDate +
              "&endDate=" + endDate +
              "&subview=subview";
    $('graph').src = url;

}

function hideHelp() {
    $('attribute-help-content').style.display = 'none';
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

</script>
</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <proctcae:urlAuthorize url="/pages/reports/studyLevelReport">

                <li id="thirdlevelnav-x" class="tab">
                    <div>
                        <a href="studyLevelReport"><tags:message code="reports.tab.studyLevel"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/symptomsummary">

                <li id="thirdlevelnav-x" class="tab selected">
                    <div>
                        <a href="symptomsummary"><tags:message code="reports.tab.symptomsummary"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
        </ul>
    </div>
</div>

<chrome:box title="participant.label.search_criteria">
    <div align="left" style="margin-left: 50px">
        <tags:renderAutocompleter propertyName="study"
                                  displayName="Study"
                                  required="true"
                                  size="50"
                                  noForm="true"/>
        <div id="formDropDownDiv" style="display:none;" class="row">
            <div class="label">Form</div>
            <div class="value" id="formDropDown"></div>
        </div>
        <div id="symptomDropDownDiv" style="display:none;" class="row">
            <div class="label">Symptom</div>
            <div class="value" id="symptomDropDown"></div>
        </div>
        <div id="attributeDropDownDiv" style="display:none;" class="row">
            <div class="label">Attribute</div>
            <div class="value" id="attributeDropDown"></div>
        </div>
        <div id="studySiteAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="studySite"
                                      displayName="Study site"
                                      size="40"
                                      noForm="true"/>
        </div>
        <div id="studySiteDiv" style="display:none" class="row">
            <div class="label">Study site <input id="studySite" name="studySite" type="hidden"></div>
            <div id="studySiteName" class="value"></div>
        </div>
        <div id="dateMenuDiv" style="display:none" class="row">
            <div class="label">Date range</div>
            <div class="value">
                <select id="visitOptions" name="visitOptions"
                        onChange="customVisit(this)">
                    <option value="all">All dates</option>
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
                                 doNotShowFormat="true" required="true"/>
            </div>
            <div class="rightpanel">
                <tags:renderDate noForm="true" displayName="End Date" propertyName="endDate"
                                 doNotShowFormat="true" required="true"/>
            </div>
        </div>

        <div id="search" style="display:none" class="row">
            <div class="value"><tags:button color="blue" value="Search"
                                            onclick="studyLevelReportResults('tabular')" size="big"
                                            icon="search"/>
                <tags:indicator id="indicator"/>
            </div>
        </div>

    </div>
</chrome:box>
<div align="center">
<iframe id="graph" height="500" width="800" frameborder="0" scrolling="auto"></iframe>
    </div>
</body>
</html>