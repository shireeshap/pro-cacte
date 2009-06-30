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
<tags:javascriptLink name="table_menu"/>
<script type="text/javascript">

<%--function printSchedule(id) {--%>
    <%--var request = new Ajax.Request("<c:url value=""/>", {--%>
        <%--parameters:"id=" + id + "&subview=subview",--%>
        <%--onComplete:function(transport) {--%>
            <%--showPdf(transport);--%>
        <%--},--%>
        <%--method:'get'--%>
    <%--})--%>
<%--}--%>

function showPopUpMenu(index, sid, x, y) {
    var html = '';
        html = '<a href="printSchedule?id=' + sid + '" target="_blank" class="link">Print form</a><br/><a href="javascript:enterResponses(' + sid + ');" class="link">Enter responses</a>';
    Element.show($("dropnoteDiv"));
    $("dropnoteDiv").style.left = (findPosX($("img_" + index)) + x) + 'px';
    $("dropnoteDiv").style.top = (findPosY($("img_" + index)) + y) + 'px';
    $("dropnoteinnerDiv").innerHTML = html;
}

function enterResponses(id) {
    var request = new Ajax.Request("<c:url value="/pages/participant/enterResponses"/>", {
        parameters:"id=" + id + "&subview=subview",
        onComplete:function(transport) {
            showConfirmationWindow(transport, 630, 550);
        },
        method:'get'
    })
}

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
    $('displayParticipantScheduleTable').hide();
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

}
var hasError=false;
function showError(element) {
    hasError = true;
    removeError(element);
    new Insertion.Bottom(element.parentNode, " <ul id='" + element.name + "-msg'class='errors'><li>" + 'Missing ' + element.title + "</li></ul>");
}
function removeError(element) {
    msgId = element.name + "-msg"
    $(msgId) != null ? new Element.remove(msgId) : null
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
        displayParticipants();

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
    $('search').show();
}

function participantScheduleTable() {
    hasError=false;
    var studyId = $('study').value;
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
    var crfSelect = $('formSelect');
    var crfId = crfSelect.options[crfSelect.selectedIndex].value;
    if (hasError) {
        return;
    }

    var request = new Ajax.Request("<c:url value="/pages/participant/showParticipantScheduleTable"/>", {
        parameters:"studyId=" + studyId + "&studySiteId=" + studySiteId + "&participantId=" + participantId + "&crfId=" + crfId +
                   "&subview=subview",
        onComplete:function(transport) {
            showResultsTable(transport);
        },
        method:'get'
    })
}

function showResultsTable(transport) {
    $('displayParticipantScheduleTable').show();
    $('displayResultsTable').innerHTML = transport.responseText;
}

</script>
</head>
<body>
<chrome:box title="participant.label.search_criteria">
    <div align="left">
        <tags:renderAutocompleter propertyName="study"
                                  displayName="Study"
                                  required="true"
                                  size="60"
                                  noForm="true"/>
    </div>
    <div id="formDropDownDiv" style="display:none;" class="row">
        <div class="label">Form</div>
        <div class="value" id="formDropDown"></div>
    </div>
    <div id="studySiteAutoCompleterDiv" style="display:none">
        <tags:renderAutocompleter propertyName="studySite"
                                  displayName="Study site"
                                  size="60"
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
    <div id="search" style="display:none" class="row">
        <div class="value"><tags:button color="blue" value="Search"
                                        onclick="participantScheduleTable()" size="big"
                                        icon="search"/>
        </div>
    </div>

</chrome:box>

<div id="displayParticipantScheduleTable" style="display:none;">
    <div>
        <div id="displayResultsTable"/>
    </div>
</div>
 <div id="dropnoteDiv" class="ddnotediv shadowB" style="display:none;left:0;top:0">
    <div id="dropnoteinnerDiv" class="shadowr">
    </div>
</div>

</body>
</html>
