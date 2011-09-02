var hasError = false;
var displaySymptom = true;
var displayDate = true;
var studySiteMandatory = false;
var displayParticipants = false;
var selectedCrf = '';

function displayForms(crfid) {
    selectedCrf = crfid;
    var id = $('study').value
    crf.getReducedCrfs(id, updateFormDropDown)
}

function populate(ele, values, itext, ivalue, selectedValue) {
    var option = new Element('OPTION', {});
    option.text = 'Please select';
    option.label = 'Please select';
    option.value = '';
    ele.appendChild(option);
    for (var i = 0; i < values.length; i++) {
        var value = values[i];
        var option = new Element('OPTION', {});
        if (typeof(itext) != 'undefined') {
            option.text = value[itext];
            option.label = value[itext];
        } else {
            option.text = value;
            option.label = value;
        }
        if (typeof(ivalue) != 'undefined') {
            option.value = value[ivalue];
        } else {
            option.value = value;
        }

        if (option.value == selectedValue) {
            option.selected = true;
        }
        ele.appendChild(option);
    }
}

function updateFormDropDown(crfs) {
    var dd = getSelect('form');
    populate(dd, crfs, 'title', 'id', selectedCrf);
    $('formDropDownDiv').show();
    $('search').show();
    if (displaySymptom) {
        dd.onchange = function() {
            displaySymptoms(this);
        }
    }
    if (displayDate) {
        $('dateMenuDiv').show();
    }
}

function displaySymptoms(obj) {
    if (obj.value == '') {
        $('symptomDropDownDiv').hide();
    } else {
        crf.getSymptomsForCrf(obj[obj.selectedIndex].value, updateSymptomDropDown);
    }
}

function updateSymptomDropDown(symptoms) {
    var dd = getSelect('symptom');
    populate(dd, symptoms, 'term', 'id');
    $('symptomDropDown').appendChild(dd);
    $('symptomDropDownDiv').show();
}

function displaySites() {
	var aResults = getOrganizations('%');
	postOrganizationFetch(aResults);
}

function getOrganizations(sQuery) {
    var callbackProxy = function(results) {
        aResults = results;
    };
    var callMetaData = {callback:callbackProxy, async:false};
    organization.matchOrganizationByStudyId(unescape(sQuery), $('study').value, callMetaData);
    return aResults;
}

function postOrganizationFetch(values) {
    $('studySiteAutoCompleterDiv').hide();
    $('studySiteDiv').hide();
    var siteNum = 2;
    if (values != '') {
        var siteNum = values.length;
    }
    
    if (siteNum > 1) {
        $('studySiteAutoCompleterDiv').show();
    } else {
        $('studySite').value = values[0].id;
        $('studySiteName').innerHTML = values[0].displayName;
        $('studySiteDiv').show();
    }
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
    arr[0] = 'formSelect';
    arr[1] = 'symptomSelect';
    arr[2] = 'startDate';
    arr[3] = 'endDate';
    if (studySiteMandatory) {
        arr[5] = 'studySite';
    }
    if (displayParticipants) {
        arr[6] = 'participant';
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
    if ((id == 'startDate' || id == 'endDate' ) && $('dateRange')) {
        if ($('dateRange').style.display == 'none') {
            return
        }
    }
    var obj = $(id);

    if (obj) {
        if (obj.value == '') {
            hasError = true;
            showError(obj);
        } else {
            removeError(obj);
        }
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

function getSelect(id) {
    var dropDown;
    if ($(id + 'Select') == null) {
        dropDown = new Element('SELECT', {'id':id + 'Select','title':id})
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

function fnDisplayParticipants() {
    if (displayParticipants) {
        $('participantAutoCompleterDiv').show();
    }
}
function showResponses(id) {
    var request = new Ajax.Request("../participant/showCompletedCrf", {
        parameters:getStandardParamForAjax() + "&id=" + id  ,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 700, 500);
        },
        method:'get'
    })
}
