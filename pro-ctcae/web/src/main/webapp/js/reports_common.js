var hasError = false;
var displaySymptom = true;
var displayDate = true;
var studySiteMandatory = false;

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


function displayForms() {
    var id = $('study').value
    crf.searchCrf(id, updateFormDropDown)
}

function populate(ele, values, itext, ivalue) {
    var option = new Element('OPTION', {});
    option.text = 'Please select';
    option.value = '';
    ele.appendChild(option);
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
        ele.appendChild(option);
    }
}

function updateFormDropDown(crfs) {
    var dd = getSelect('form');
    populate(dd, crfs, 'title', 'id');
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
        $('attributeDropDownDiv').hide();
    } else {
        crf.getSymptomsForCrf(obj[obj.selectedIndex].value, updateSymptomDropDown);
    }
}

function updateSymptomDropDown(symptoms) {
    var dd = getSelect('symptom');
    dd.onchange = function() {
        displayAttributes(this);
    }
    populate(dd, symptoms, 'term', 'id');
    $('symptomDropDown').appendChild(dd);
    $('symptomDropDownDiv').show();
}

function displayAttributes(obj) {
    if (obj.value == '') {
        $('attributeDropDownDiv').hide();
    } else {
        crf.getAttributesForSymptom(obj[obj.selectedIndex].value, updateAttribueDropDown);
    }
}
function updateAttribueDropDown(attributes) {
    var dd = getSelect('attribute');
    populate(dd, attributes);
    $('attributeDropDown').appendChild(dd);
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
    arr[2] = 'attributeSelect';
    arr[3] = 'startDate';
    arr[4] = 'endDate';
    if (studySiteMandatory) {
        arr[5] = 'studySite';
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
}
function hideIndicator() {
    $('indicator').style.visibility = 'hidden';
}

function getSelect(id) {
    var dropDown ;
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