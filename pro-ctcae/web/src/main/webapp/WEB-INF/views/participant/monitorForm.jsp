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
<%@ taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/ctcae/tags" %>

<html>
<head>
<tags:dwrJavascriptLink objects="crf"/>
<tags:dwrJavascriptLink objects="participant"/>
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
            displayParticipants();
        },
        indicator: mode.basename + "-indicator"
    })

}

function displaySites() {
    var myStudySiteAutoComplter = new studySiteAutoComplter('studySite', $('study').value);
    acCreate(myStudySiteAutoComplter);
    initSearchField();
    $('studySiteAutoCompleterDiv').show();

}

function displayParticipants() {
    var myParticipantAutoCompleter = new participantAutoCompleter('participant', function(autocompleter, text) {
        participant.matchParticipantByStudySiteId(text, $('studySite').value, $('study').value, function(values) {
            autocompleter.setChoices(values)
        })
    });
    acCreate(myParticipantAutoCompleter);
    initSearchField();
    $('participantAutoCompleterDiv').show();
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
    $('displayFormStatusDiv').hide();
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
    $('searchForm').show();
    $('statusDiv').show();
}
function customDate(showDate) {
    var myindex = showDate.selectedIndex
    var selValue = showDate.options[myindex].value
    if (selValue == "custom") {
        $('dateRange').show();
    } else {
        $('dateRange').hide();
    }

}
function formStatus(useStartDate) {
    var studyId = $('study').value;
    var stDate = $('startDate').value;
    var endDate = $('endDate').value;

    var crfSelect = $('formSelect');
    var crfId = crfSelect.options[crfSelect.selectedIndex].value;

    var dateRangeSelect = $('dateOptions');
    var dateRange = dateRangeSelect.options[dateRangeSelect.selectedIndex].value;

    var studySiteId = $('studySite').value;
    var participantId = $('participant').value;

    var statusSelect = $('formStatus');
    var status = statusSelect.options[statusSelect.selectedIndex].value;

    var pgStartDateNext = '';
    if (useStartDate == 'next') {
        try {
            pgStartDateNext = $('pgStartDateNext').value;
        } catch(e) {
        }
    }

    var pgStartDatePrev = '';
    if (useStartDate == 'prev') {
        try {
            pgStartDatePrev = $('pgStartDatePrev').value;
        } catch(e) {
        }
    }


    var request = new Ajax.Request("<c:url value="/pages/participant/monitorFormStatus"/>", {
        parameters:"studyId=" + studyId + "&crfId=" + crfId + "&studySiteId=" + studySiteId + "&participantId=" + participantId + "&dateRange=" + dateRange + "&stDate=" + stDate + "&endDate=" + endDate + "&status=" + status + "&pgStartDateNext=" + pgStartDateNext + "&pgStartDatePrev=" + pgStartDatePrev +"&subview=subview",
        onComplete:function(transport) {
            showStatusTable(transport);
        },
        method:'get'
    })
}

function showStatusTable(transport) {
    $('displayFormStatusDiv').show();
    $('displayFormStatus').innerHTML = transport.responseText;
}

</script>
<style type="text/css">
    table.content {
        font-size: 10pt; /*width: 100%;*/
    }
</style>

</head>
<body>
<chrome:box title="participant.label.search_criteria">
<div align="left" style="margin-left: 50px">
<table class="content" cellpadding="0" cellspacing="0">
<tr>
    <td>
        <tags:renderAutocompleter propertyName="study"
                                  displayName="Study"
                                  required="true"
                                  size="60"
                                  noForm="true"/>
    </td>
</tr>
<tr>
    <td colspan="2">
        <div id="formDropDownDiv" style="display:none;" class="label">
            <div class="row">
                <div class="label">Form</div>
                <div class="value" id="formDropDown"></div>
            </div>
        </div>
    </td>
</tr>

<tr>
    <td>
        <div id="statusDiv" style="display:none">
            <div class="row">
                <div class="label">Status</div>
                <div class="value">
                    <select id="formStatus" name="statusOptions">
                        <option value="all">All</option>
                        <option value="INPROGRESS">In-progress</option>
                        <option value="SCHEDULED">Scheduled</option>
                        <option value="COMPLETED">Completed</option>
                        <option value="PASTDUE">Past due</option>
                    </select>
                </div>
            </div>
        </div>
    </td>
</tr>


<tr>
    <td>
        <div id="studySiteAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="studySite"
                                      displayName="Study site"
                                      size="60"
                                      noForm="true"/>
        </div>
    </td>
</tr>
<tr>
    <td>
        <div id="participantAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="participant"
                                      displayName="Participant"
                                      size="60"
                                      noForm="true"/>
        </div>
    </td>
</tr>

<tr>
    <td>
        <div id="dateMenuDiv" style="display:none">
            <div class="row">
                <div class="label">Date range</div>
                <div class="value">
                    <select id="dateOptions" name="dateOptions" onChange="customDate(this)">
                        <option value="thisWeek">This Week</option>
                        <option value="lastWeek">Last Week</option>
                        <option value="thisMonth">This Month</option>
                        <option value="lastMonth">Last Month</option>
                        <option value="custom">Custom</option>
                    </select>
                </div>
            </div>
        </div>
    </td>
</tr>
<tr>
    <td>
        <div id="dateRange" style="display:none">
            <table class="content" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <tags:renderDate noForm="true" displayName="Start Date" propertyName="startDate"
                                         doNotShowFormat="true"/>
                    </td>
                    <td>
                        <tags:renderDate noForm="true" displayName="End Date" propertyName="endDate"
                                         doNotShowFormat="true"/>
                    </td>
                </tr>
            </table>

        </div>
    </td>
</tr>
<tr>
    <td>
        <div id="searchForm" style="display:none">
            <input type="button" value="search" onclick="formStatus(false)" style="margin:10px 0 0 149px;"/>
        </div>
    </td>
</tr>
</table>
</div>
</chrome:box>

<br/><br/>

<div id="displayFormStatusDiv" style="display:none;">
    <chrome:box title="Results">
        <input type="button" value="prev" onclick="formStatus('prev')"/>
        <input type="button" value="next" onClick="formStatus('next')"/>

        <br>

        <div id="displayFormStatus"/>
    </chrome:box>
</div>
</body>
</html>