<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="report" tagdir="/WEB-INF/tags/reports" %>
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
<tags:javascriptLink name="mouse"/>
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

function studyLevelReportResults() {
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
    showIndicator();
    var request = new Ajax.Request("<c:url value="/pages/reports/symptomOverTimeReportResults"/>", {
        parameters:"crfId=" + $('formSelect').options[$('formSelect').selectedIndex].value +
                   "&symptom=" + $('symptomSelect').options[$('symptomSelect').selectedIndex].value +
                   "&attribute=" + $('attributeSelect').options[$('attributeSelect').selectedIndex].value +
                   "&gender=all" +
                   "&studySiteId=" + $('studySite').value +
                   "&visitRange=" + visitRange +
                   "&startDate=" + stDate +
                   "&endDate=" + endDate +
                   "&subview=subview",
        onComplete:function(transport) {
            showResults(transport);
            hideIndicator();
        },
        method:'get'
    })
}
function showResults(transport) {
    $('symptomSummaryReportOuterDiv').show();
    $('symptomSummaryReportInnerDiv').innerHTML = transport.responseText;
}

function showDetails(params) {
    showIndicator();
    var request = new Ajax.Request("<c:url value="/pages/reports/showDetailsOverTime"/>", {
        parameters:params,
        onComplete:function(transport) {
            $('symptomSummaryReportInnerDiv').innerHTML = transport.responseText;
            hideIndicator();
        },
        method:'get'
    })
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
function showResponses(id) {
    var request = new Ajax.Request("<c:url value="/pages/participant/showCompletedCrf"/>", {
        parameters:"id=" + id + "&subview=subview",
        onComplete:function(transport) {
            showConfirmationWindow(transport, 700, 500);
        },
        method:'get'
    })
}
function highlightrow(index) {
    $('details_row_' + index).className = 'highlight';
    $('img_' + index).style.visibility = 'visible';
}

function removehighlight(index)
{
    var rEle = $('details_row_' + index);
    rEle.className = '';
    $('img_' + index).style.visibility = 'hidden';
    if ($("dropnoteDiv")) {
        Element.hide($("dropnoteDiv"));
        $("dropnoteDiv").onmouseover = function() {
            rEle.className = "highlight";
            $('img_' + index).style.visibility = "visible";
            Element.show($("dropnoteDiv"));
        };
        $("dropnoteDiv").onmouseout = function() {
            rEle.className = "";
            $('img_' + index).style.visibility = "hidden";
            Element.hide($("dropnoteDiv"));
        };
    }
}

function showPopUpMenu(index, pid, sid, x, y) {

    var html = '<a href="../participant/create?id=' + pid + '" class="link">View participant</a><br/><a href="javascript:showResponses(' + sid + ');" class="link">View all responses</a>';
    Element.show($("dropnoteDiv"));
    $("dropnoteDiv").style.left = (findPosX($("img_" + index)) + x) + 'px';
    $("dropnoteDiv").style.top = (findPosY($("img_" + index)) + y) + 'px';
    $("dropnoteinnerDiv").innerHTML = html;
}

function findPosX(obj) {
    var X = 0;
    if (document.getElementById || document.all) {
        while (obj.offsetParent) {
            X += obj.offsetLeft;
            obj = obj.offsetParent;
        }
    } else {
        if (document.layers) {
            X += obj.x;
        }
    }
    return X;
}
function findPosY(obj) {
    var Y = 0;
    if (document.getElementById || document.all) {
        while (obj.offsetParent) {
            Y += obj.offsetTop;
            obj = obj.offsetParent;
        }
    } else {
        if (document.layers) {
            Y += obj.y;
        }
    }
    return Y;
}
</script>
</head>
<body>
<report:thirdlevelmenu selected="symptomovertime"/>
<chrome:box title="participant.label.search_criteria">
    <div align="left" style="margin-left: 50px">
        <tags:renderAutocompleter propertyName="study"
                                  displayName="Study"
                                  required="true"
                                  size="60"
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
                                      size="60"
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
                                            onclick="studyLevelReportResults()" size="big"
                                            icon="search"/>
                <tags:indicator id="indicator"/>
            </div>
        </div>

    </div>
</chrome:box>
<div id="symptomSummaryReportOuterDiv" style="display:none;">
    <div>
        <div id="symptomSummaryReportInnerDiv"/>
    </div>
</div>
</body>
</html>