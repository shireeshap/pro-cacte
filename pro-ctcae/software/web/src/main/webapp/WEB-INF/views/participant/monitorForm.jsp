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
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<html>
<head>
<tags:dwrJavascriptLink objects="crf"/>
<tags:dwrJavascriptLink objects="participant"/>
<tags:includePrototypeWindow/>
<tags:includeScriptaculous/>
<tags:javascriptLink name="table_menu"/>
<script type="text/javascript">

function hideme() {
    Element.hide($("dropnoteDiv"));
}
function showPopUpMenu(index, sid, x, y) {
    var html = '';
    html = '<table cellpadding="0" cellspacing="0"><tr><td align="right"><a href="javascript:hideme();" class="link">x</a></td></tr><tr><td><a href="printSchedule?id=' + sid + '" target="_blank" class="link">Print form</a></td></tr><tr><td><a href="enterResponses?id=' + sid + '" class="link">Enter responses</a></td></tr></table>';
    Element.show($("dropnoteDiv"));
    $("dropnoteDiv").style.left = (findPosX($("img_" + index)) + x) + 'px';
    $("dropnoteDiv").style.top = (findPosY($("img_" + index)) + y) + 'px';
    $("dropnoteinnerDiv").innerHTML = html;
}

function completedForm(id) {
    var request = new Ajax.Request("<c:url value="/pages/participant/showCompletedCrf"/>", {
        parameters:<tags:ajaxstandardparams/>+"&id=" + id ,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 700, 500);
        },
        method:'get'
    })
}

Event.observe(window, "load", function () {
    var sac = new studyAutoCompleter('study');
    acCreateStudyMonitor(sac);
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

    organization.matchOrganizationByStudyId('%', $('study').value, function(values) {
    })
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
    var div = $(divid);
    var formDropDown = $('formSelect');
    if (formDropDown != null) {
        div.removeChild(formDropDown);
    }
}

function updateFormDropDown(crfs) {
    clearDiv('formDropDown');
    $('displayFormStatusDiv').hide();
    var formDropDown = new Element('SELECT', {'id':'formSelect', title:'Form'})

    if (crfs.length > 1) {
        var option = new Element('OPTION', {'label':'Please select','value':''});
        option.text = 'Please select';
        formDropDown.appendChild(option);
    }
    for (var i = 0; i < crfs.length; i++) {
        var crf = crfs[i];
        var option = new Element('OPTION', {});
        option.text = crf.title;
        option.label = crf.title;
        option.value = crf.id;
        formDropDown.appendChild(option);
    }

    $('formDropDown').appendChild(formDropDown);
    $('formDropDownDiv').show();
    $('dateMenuDiv').show();
    $('searchForm').show();
    $('statusDiv').show();
    //    $('tableViewDiv').show();
}
function showError(element) {
    removeError(element);
    new Insertion.Bottom(element.parentNode, " <ul id='" + element.name + "-msg'class='errors'><li>" + 'Missing ' + element.title + "</li></ul>");
}
function removeError(element) {
    msgId = element.name + "-msg"
    $(msgId) != null ? new Element.remove(msgId) : null
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
function formStatus(period) {
    $('indicator').show();
    var studyId = $('study').value;

    var dateRangeSelect = $('dateOptions');
    var dateRange = dateRangeSelect.options[dateRangeSelect.selectedIndex].value;

    var stDate = $('startDate').value;
    var endDate = $('endDate').value;
    var crfSelect = $('formSelect');
    var crfId = '';
    if (crfSelect.options.length > 0) {
        crfId = crfSelect.options[crfSelect.selectedIndex].value;
    }
    var error = false;
    if (dateRange == 'custom') {
        if (stDate == '') {
            error = true;
            showError($('startDate'));
        } else {
            removeError($('startDate'));
        }
        if (endDate == '') {
            error = true;
            showError($('endDate'));
        } else {
            removeError($('endDate'));
        }
    }
    if (crfId == '') {
        error = true;
        showError($('formSelect'));
    } else {
        removeError($('formSelect'));
    }
    if (error) {
        return;
    }


    var studySiteId = $('studySite').value;
    var participantId = $('participant').value;

    var statusSelect = $('formStatus');
    var status = statusSelect.options[statusSelect.selectedIndex].value;

    var pgStartDateNext = '';
    var pgStartDatePrev = '';
    var direction = '';
    var view = period;
    if (period == 'next') {
        pgStartDateNext = $('pgStartDateNext').value;
        direction = period;
        view = $('periodButton').value;
    }
    if (period == 'prev') {
        pgStartDatePrev = $('pgStartDatePrev').value;
        direction = period;
        view = $('periodButton').value;
    }
    if (period == 'weekly') {
        pgStartDatePrev = $('pgStartDatePrev').value;
        view = period;
        $('monthlyButton').show();
        $('weeklyButton').hide();
    }
    if (period == 'monthly') {
        pgStartDatePrev = $('pgStartDatePrev').value;
        view = period;
        $('monthlyButton').hide();
        $('weeklyButton').show();
    }
    if (period == 'initial') {
        $('monthlyButton').show();
        $('weeklyButton').hide();
    }

    showIndicator();
    var request = new Ajax.Request("<c:url value="/pages/participant/monitorFormStatus"/>", {
        parameters:<tags:ajaxstandardparams/>+"&studyId=" + studyId + "&crfId=" + crfId + "&studySiteId=" + studySiteId + "&participantId=" + participantId + "&dateRange=" + dateRange + "&stDate=" + stDate + "&endDate=" + endDate + "&status=" + status + "&pgStartDateNext=" + pgStartDateNext + "&pgStartDatePrev=" + pgStartDatePrev + "&direction=" + direction + "&view=" + view ,
        onComplete:function(transport) {
            showStatusTable(transport);
            hideIndicator();
        },
        method:'get'
    })
}

function showStatusTable(transport) {
    $('displayFormStatusDiv').show();
    $('displayFormStatus').innerHTML = transport.responseText;
    $('indicator').hide();
}

function showIndicator() {
    $('indicator').style.visibility = 'visible';
}
function hideIndicator() {
    $('indicator').style.visibility = 'hidden';
}

</script>
<tags:stylesheetLink name="table_menu"/>
<style type="text/css">
    table.content {
        font-size: 10pt; /*width: 100%;*/
    }

    table.widget {
        border-left: 1px solid #C3D9FF;
        border-bottom: 1px solid #C3D9FF;
        width: 100%;
        font-size: small;
    }

    td.data {
        border-bottom: 1px solid #77a9ff;
        border-right: 1px solid #77a9ff;
        font-size: small;
        white-space: nowrap;
        text-align: center;
    }

    td.OffStudy {
        background-color: #666666;
    }

    td.data-left {
        border-bottom: 1px solid #77a9ff;
        border-left: 1px solid #77a9ff;
        border-right: 1px solid #77a9ff;
        font-weight: bold;
        white-space: nowrap;
        background-color: #cccccc;
        text-align: left;
        padding-left: 5px;
    }

    td.header-top {
        border-top: 1px solid #77a9ff;
        border-bottom: 1px solid #77a9ff;
        border-right: 1px solid #77a9ff;
        font-weight: bold;
        text-align: center;
        background-color: #cccccc;
    }

    #formStatusTable {
    /*text-align: center;*/
        overflow-x: scroll;
    }

    a.nolink {
        text-decoration: none;
        cursor: default;
    }

    * {
        zoom: 0;
    }
</style>
</head>
<body>
<tags:instructions code="participant.monitor.instructions"/>
<chrome:box title="participant.label.search_criteria">
    <div align="left" style="margin-left: 50px">
        <tags:renderAutocompleter propertyName="study"
                                  displayName="Study"
                                  required="true"
                                  size="100"
                                  noForm="true"/>
        <div id="formDropDownDiv" class="row" style="display:none">
            <div class="label"><tags:requiredIndicator/>Form&nbsp;&nbsp;</div>
            <div class="value IEdivValueHack" id="formDropDown"></div>
        </div>

        <div id="statusDiv" class="row" style="display:none">
            <div class="label">Status&nbsp;&nbsp;</div>
            <div class="value IEdivValueHack">
                <select id="formStatus" name="statusOptions">
                    <option value="all">All</option>
                    <option value="IN-PROGRESS">In-progress</option>
                    <option value="SCHEDULED">Scheduled</option>
                    <option value="COMPLETED">Completed</option>
                    <option value="PASTDUE">Past due</option>
                    <option value="CANCELLED">Cancelled</option>
                </select>
            </div>
        </div>
        <div id="studySiteAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="studySite"
                                      displayName="Study site"
                                      size="100"
                                      noForm="true"/>
        </div>
        <div id="participantAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="participant"
                                      displayName="Participant"
                                      size="70"
                                      noForm="true"/>
        </div>
        <div id="dateMenuDiv" style="display:none" class="row">
            <div class="label">Date range&nbsp;&nbsp;</div>
            <div class="value IEdivValueHack">
                <select id="dateOptions" name="dateOptions" onChange="customDate(this)">
                    <option value="thisWeek">This Week</option>
                    <option value="lastWeek">Last Week</option>
                    <option value="thisMonth">This Month</option>
                    <option value="lastMonth">Last Month</option>
                    <option value="custom">Custom</option>
                </select>
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
        <div id="searchForm" style="display:none" class="row">
            <div class="label"></div>
            <div class="value"><tags:button color="blue" value="Search" onclick="formStatus('initial')" size="big"
                                            icon="search"/> <tags:indicator id="indicator"/></div>
        </div>
    </div>
</chrome:box>


<div id="displayFormStatusDiv" style="display:none;">
    <chrome:box title="Results">
        <div>
            <div style="height: 25px">
                <div style="float:left"><tags:button type="button" value="Previous" icon="back" color="blue"
                                                     size="small" onclick="formStatus('prev')"/></div>
                <div id="monthlyButton" style="display:none; float:left;"><tags:button type="button"
                                                                                       value="Switch to monthly view"
                                                                                       icon="monthly"
                                                                                       color="blue"
                                                                                       size="small"
                                                                                       onclick="formStatus('monthly')"/></div>
                <div id="weeklyButton" style="display:none; float:left;"><tags:button type="button"
                                                                                      value="Switch to weekly view"
                                                                                      icon="weekly"
                                                                                      color="blue"
                                                                                      size="small"
                                                                                      onclick="formStatus('weekly')"/></div>
                <div style="float:left;"><tags:indicator id="indicator"/></div>
                <div style="float:right"><tags:button type="button" value="Next" icon="next" color="blue"
                                                      size="small" onclick="formStatus('next')"/></div>
            </div>
            <table>
                <tr>
                    <td>
                        Scheduled = <img src="../../images/blue/Scheduled.png"/> &nbsp;&nbsp;
                    </td>
                    <td>
                        In-progress = <img src="../../images/blue/In-progress.png"/> &nbsp;&nbsp;
                    </td>
                    <td>
                        Completed = <img src="../../images/blue/Completed.png"/> &nbsp;&nbsp;
                    </td>
                    <td>
                        Past-due = <img src="../../images/blue/Past-due.png"/> &nbsp;&nbsp;
                    </td>
                    <td>
                        Cancelled = <img src="../../images/blue/Cancelled.png"/>
                    </td>
                </tr>
            </table>


            <div id="displayFormStatus"/>
        </div>
    </chrome:box>
</div>
<div id="dropnoteDiv" class="ddnotediv shadowB" style="display:none;left:0;top:0">
    <div id="dropnoteinnerDiv" class="shadowr">
    </div>
</div>

</body>
</html>