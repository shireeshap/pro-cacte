var hasError = false;
var displaySymptom = false;
var displayDate = true;
var displayParticipants = false;
var selectedCrf = '';
var displayParticipant = false;

function createStudyAutoCompleter() {
    var sac = new studyAutoCompleter('study');
    acCreateStudyMonitor(sac);
    initSearchField();
    try {
        initializeFields();
    } catch(e) {
    }
}

function acCreateStudyMonitor(mode) {
    new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices",
            mode.populator, {
        valueSelector: mode.valueSelector,
        afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
            $('search').hide();
            acPostSelect(mode, selectedChoice);
            displayForms();
            displaySites();
            $('search').show();
            //            fnDisplayParticipants();
        },
        indicator: mode.basename + "-indicator"
    })
}

function displayForms(crfid) {
    $('divSymptomsRow').hide();
    $('divFormRow').hide();
    selectedCrf = crfid;
    var id = $('study').value
    crf.getReducedCrfs(id, updateFormDropDown)
}

function displaySites() {
    $('divStudySiteRow').hide();
    $('studySiteAutoCompleter').hide();
    organization.matchOrganizationByStudyId('%', $('study').value, function(values) {
        var siteNum = values.length;
        if (siteNum == 1) {
            $('studySite').value = values[0].id;
            $('studySiteDisplayName').innerHTML = values[0].displayName;
            $('divStudySiteRow').show();
        } else {
            $('studySiteAutoCompleter').show();
            var ssac = new studySiteAutoComplter('studySite', $('study').value);
            acCreate(ssac);
            initSearchField();
        }
    })
}

function updateFormDropDown(crfs) {
    $('divFormRow').show();
    if (crfs.length == 1) {
        var value = crfs[0];
        $('formTitle').innerHTML = value['title'];
        $('form').value = value['id'];
        displaySymptoms(value['id']);
    } else {
        var select = populate('formTitle', crfs, 'title', 'id', selectedCrf, 'Form');
        if (displaySymptom) {
            select.onchange = function() {
                displaySymptoms(this.value);
            }
        }
    }
}

function displaySymptoms(crfid) {
    $('form').value = crfid;
    if (displaySymptom) {
        crf.getSymptomsForCrf(crfid, updateSymptomDropDown);
    }
}

function updateSymptomDropDown(symptoms) {
    $('divSymptomsRow').show();
    var select = populate('proCtcTerms', symptoms, 'term', 'id', '', 'Symptom');
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
function performValidations() {
    hasError = false;
    var arr = new Array();
    arr[0] = 'form';
    if (displaySymptom) {
        arr[1] = 'proCtcTermsSelect';
    }
    for (var i = 0; i < arr.length; i++) {
        validateField(arr[i]);
    }
    if (hasError) {
        return false;
    }
    return true;
}
function validateField(id) {
    var obj = $(id);
    if (obj) {
        if (obj.value == '') {
            hasError = true;
            showError(obj);
        } else {
            removeError(obj);
        }
    } else {
        hasError = true;
        showError(obj);
    }
}

function showResults(transport) {
    $('reportOuterDiv').show();
    $('reportInnerDiv').innerHTML = transport.responseText;
}


function hideHelp() {
    $('attribute-help-content').style.display = 'none';
}
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
    if (typeof(xMousePos) != 'undefined') {
        $("indicator").style.left = xMousePos + 'px';
        $("indicator").style.top = yMousePos + 'px';
    }
}
function hideIndicator() {
    $('indicator').style.visibility = 'hidden';
}

function fnDisplayParticipants() {
    if (displayParticipants) {
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
}
function showResponses(id) {
    var request = new Ajax.Request("../participant/showCompletedCrf", {
        parameters:"id=" + id + "&subview=subview",
        onComplete:function(transport) {
            showConfirmationWindow(transport, 700, 500);
        },
        method:'get'
    })
}

function populate(divid, values, itext, ivalue, selectedValue, title) {
    var ele = $(divid);
    if (ele.firstChild != null) {
        ele.removeChild(ele.firstChild);
    }
    //    var select = new Element('SELECT', {'id':divid + 'Select'});
    var select = new Element('SELECT', {'id':divid + 'Select','title':title});
    addPleaseSelect(select);

    for (var i = 0; i < values.length; i++) {
        var value = values[i];
        var option = new Element('OPTION', {});
        if (typeof(itext) != 'undefined') {
            option.text = value[itext];
        } else {
            option.text = value;
        }
        if (typeof(ivalue) != 'undefined') {
            option.value = value[ivalue];
        } else {
            option.value = value;
        }
        if (option.value == selectedValue) {
            option.selected = true;
        }
        select.appendChild(option);
    }
    ele.empty();
    ele.appendChild(select);
    return select;
}
function addPleaseSelect(select) {
    var option = new Element('OPTION', {});
    option.text = 'Please select';
    option.value = '';
    select.appendChild(option);
}
function updateChart(chkbox, popup) {
    var obj = document.getElementsByName('attribute');
    var selectedAttributes = '';
    for (var i = 0; i < obj.length; i++) {
        if (obj[i].checked) {
            selectedAttributes = selectedAttributes + ',' + obj[i].value;
        }
    }
    if (selectedAttributes == '') {
        alert('Please select at least one question type.');
        chkbox.checked = true;
        return;
    }
    reportResults(selectedAttributes);
}

function getQueryString(attributes, igroup) {
    if (typeof(attributes) == 'undefined') {
        attributes = '';
    }
    var queryString = 'subview=subview';
    queryString += "&study=" + $('study').value;
    queryString += "&crf=" + $('form').value;
    if (displaySymptom) {
        queryString += "&symptom=" + $('proCtcTermsSelect').value;
    } else {
        if ($('symptom') != null) {
            queryString += "&symptom=" + $('symptom').value;
        }
    }
    queryString += "&studySite=" + $('studySite').value;
    queryString += "&attributes=" + attributes;
    var group = 'cycle';
    if (typeof(igroup) == 'undefined' || igroup == '') {
        if ($('groupby') != null) {
            group = $('groupby').value.toLowerCase();
        }
    } else {
        group = igroup;
    }
    queryString += "&group=" + group;
    return queryString;
}
function showItems(Id, grade, att, period) {
    var request = new Ajax.Request("/proctcae/pages/reports/getParticipantItems", {
        parameters:getQueryString() + '&pid=' + Id + '&grade=' + grade + '&att=' + att + '&period=' + period,
        onComplete:function(transport) {
            var response = transport.responseText;
            new Insertion.After('items_row_' + Id, response);
            $('pShowImage_' + Id).hide();
            $('pHideImage_' + Id).show();
        },
        method:'get'
    }
            )
}
function hideItems(Id) {
    $('pShowImage_' + Id).show();
    $('pHideImage_' + Id).hide();
    var items = document.getElementsByName('childTableRow_' + Id);
    var len = items.length;
    for (var i = 0; i < len; i++) {
        items[0].remove();
    }
}

function showDetails(params) {
    showIndicator();
    var request = new Ajax.Request("reportDetails", {
        parameters:params,
        onComplete:function(transport) {
            if (showResultsInPopUpFlag) {
                showResultsInPopUp(transport);
            } else {
                $('reportInnerDiv').innerHTML = transport.responseText;
            }
            hideIndicator();
        },
        method:'get'
    }
            )
}

function showResultsInPopUp(transport) {
    var win = showConfirmationWindow(transport, 850, 570);
}
function showChartInPopup(symptomId) {
    $('symptom').value = symptomId;
    showResultsInPopUpFlag = true;
    reportResults();
}
function resetPopUpFlagAndCallResults() {
    showResultsInPopUpFlag = false;
    if ($('symptom') != null) {
        $('symptom').value = '';
    }
    reportResults();
}

