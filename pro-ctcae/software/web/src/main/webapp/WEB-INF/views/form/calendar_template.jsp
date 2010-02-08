<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<html>
<head>

<tags:stylesheetLink name="tabbedflow"/>
<tags:includeScriptaculous/>
<tags:includePrototypeWindow/>
<style type="text/css">

    table.top-widget {
        text-align: left;
    }

    td {
        text-align: left;
    }

    img {
        border: 0 none;
    }

    .unselected_day {
        background-color: #FFFFFF;
        height: 40px;
        width: 40px;
        text-align: center;
        color: #000000;
        font-weight: bold;
        font-size: 13px;
        cursor: pointer;
        border-bottom: #0000cc solid 1px;
        border-right: #0000cc solid 1px;
    }

    .selected_day {
        background-color: #0051fc;
        height: 40px;
        width: 40px;
        text-align: center;
        color: #FFFFFF;
        font-weight: bold;
        font-size: 13px;
        cursor: pointer;
        border-bottom: #0000cc solid 1px;
        border-right: #0000cc solid 1px;
    }

    .unselected_day:hover {
        background-color: #cccccc;
        cursor: pointer;
    }

    .top-border {
        border-top: #0000cc solid 1px;
    }

    .left-border {
        border-left: #0000cc solid 1px;
    }

    .empty {
        height: 15px;
    }

    .first-column {
        text-align: left;
        vertical-align: top;
        font-weight: bold;
        width: 60px;
    }

    .empty-column {
        height: 3px;
    }

    div.row {
        margin-bottom: 2px;
        margin-top: 2px;
        padding-left: 0;
        padding-right: 0;
    }

    div.row div.label {
        margin-left: 0.1em;
        width: 7em;
    }

    div.row div.value {
        margin-left: 8em;
    }
</style>
<script type="text/javascript">
var repeatvalues = new Array();
function changeinput(obj, index, value) {
    if (obj.value == 'Indefinitely') {
        $('div_date_' + index).hide();
        $('div_number_' + index).hide();
        $('selectedFormArmSchedule.crfCalendars[' + index + '].repeatUntilValue').value = '';
    }
    if (obj.value == 'Date') {
        $('div_number_' + index).hide();
        $('div_date_' + index).show();
        $('crfCalendar_date_' + index + '.repeatUntilValue').value = value;
        $('selectedFormArmSchedule.crfCalendars[' + index + '].repeatUntilValue').value = value;
    }
    if (obj.value == 'Number') {
        $('div_date_' + index).hide();
        $('div_number_' + index).show();
        $('crfCalendar_number_' + index + '.repeatUntilValue').value = value;
        $('selectedFormArmSchedule.crfCalendars[' + index + '].repeatUntilValue').value = value;
    }
}


function none() {
}
Event.observe(window, "load", function () {
    var item = document.getElementsByName('selectedFormArmSchedule.crfCalendars[0].repeatUntilUnit')[0];
    changeinput(item, 0, '${command.selectedFormArmSchedule.crfCalendars[0].repeatUntilValue}');
})

function addCycle() {
    var request = new Ajax.Request("<c:url value="/pages/form/addFormScheduleCycle"/>", {
        onComplete:addCycleDiv,
        parameters:<tags:ajaxstandardparams/>,
        method:'get'
    })
}

function addCycleDiv(transport) {
    var response = transport.responseText;
    new Insertion.Before("hiddenDiv", response);
}

function calculateDays(days_unit, days_amount) {
    var multiplier = 1;
    if (days_unit == 'Weeks') {
        multiplier = 7;
    }
    if (days_unit == 'Months') {
        multiplier = 30;
    }
    var days = days_amount * multiplier;
    return days;
}
function showCyclesForDefinition(index, pageload) {
    var repeat = parseInt($('cycle_repeat_' + index).value);
    if ($('select_repeat_' + index).value == -1) {
        repeat = 1;
    }
    var days_amount = parseInt($('cycle_length_' + index).value);
    var days_unit = $('selectedFormArmSchedule.crfCycleDefinitions[' + index + '].cycleLengthUnit').value;
    var days = calculateDays(days_unit, days_amount);
    if (days > 0 && days < 1000 && repeat > 0) {
        $('div_cycle_selectdays_' + index).show();
        $('div_cycle_table_' + index).show();
        buildTable(index, days, repeat, pageload);
    } else {
        $('div_cycle_table_' + index).hide();
        $('div_cycle_selectdays_' + index).hide();
    }
}

function createHiddenDivs(index) {
    var hidden_div = $('hidden_inputs_div_' + index);

    if (hidden_div == null) {
        hidden_div = new Element('div', {'id': 'hidden_inputs_div_' + index});
        $('hidden_inputs_div').appendChild(hidden_div);
    } else {
        var children = hidden_div.childElements();
        for (var i = 0; i < children.length; i++) {
            $(children[i]).remove();
        }
    }
}

function emptyTable(index) {
    var tbody = $('cycle_table_' + index).getElementsByTagName("TBODY")[0];
    var children = $(tbody).childElements();
    for (var i = 0; i < children.length; i++) {
        $(children[i]).remove();
    }
    return tbody;
}

function addHiddenInput(cycleDefinitionIndex, cycleIndex) {
    var hiddenInput = new Element('input', {'type':'hidden','name': 'selecteddays_' + cycleDefinitionIndex + '_' + cycleIndex,'id': 'selecteddays_' + cycleDefinitionIndex + '_' + cycleIndex});
    $('hidden_inputs_div_' + cycleDefinitionIndex).appendChild(hiddenInput);
}

function addFirstColumn(row, rownumber, cycleNumber) {
    var td = new Element('TD');
    if (rownumber == 0) {
        td.update('Cycle ' + (cycleNumber + 1));
        td.addClassName('first-column')
    }
    row.appendChild(td);
}

function addEmptyRow(tbody) {
    var row = new Element('TR');
    var td = new Element('TD');
    td.addClassName('empty');
    row.appendChild(td);
    tbody.appendChild(row);
}
function addCycleDayColumn(currentday, tbody, row, cycleDefinitionIndex, cycleIndex) {
    var selecteddays = $('selecteddays_' + cycleDefinitionIndex + '_' + cycleIndex).value + ',';
    var td = new Element('TD');
    if (i == 0) {
        td.addClassName('top-border')
    }
    if (j == 0) {
        td.addClassName('left-border')
    }
    var div = new Element('div', {'id': 'div_' + cycleDefinitionIndex + '_' + cycleIndex + '_' + currentday}).update(currentday);
    if (selecteddays.indexOf(',' + currentday + ',') == -1) {
        div.addClassName('unselected_day');
    } else {

        div.addClassName('selected_day');
    }
    div.onclick = function() {
        dayOnClick(this, cycleDefinitionIndex, cycleIndex, this.id.substring(this.id.lastIndexOf('_') + 1));
    }
    td.appendChild(div);
    row.appendChild(td);
    tbody.appendChild(row);
}
function buildTable(index, days, repeat, pageload) {
    if (!pageload) {
        createHiddenDivs(index);
    }
    var tbody = emptyTable(index);
    var daysPerLine = 21;
    var numOfRows = parseInt(days / daysPerLine);
    if (days % daysPerLine > 0) {
        numOfRows = numOfRows + 1;
    }
    for (var k = 0; k < repeat; k++) {
        if (!pageload) {
            addHiddenInput(index, k);
        }
        for (i = 0; i < numOfRows; i++) {
            var row = new Element('TR');
            addFirstColumn(row, i, k);
            for (j = 0; j < daysPerLine; j++) {
                var currentday = i * daysPerLine + j + 1;
                if (currentday <= days) {
                    addCycleDayColumn(currentday, tbody, row, index, k);
                }
            }
        }
        if ($('select_repeat_' + index).value != -1) {
            addMultiSelect(tbody, index, k);
            addEmptyRow(tbody);
        }
    }
}

function applyDaysToCycle(days, cycleDefinitionIndex, cycleIndexes) {
    var cycles = cycleIndexes.split(',');
    for (var j = 0; j < cycles.length; j++) {
        var cycleIndex = cycles[j];
        if (cycleIndex != '') {
            resetCycle(cycleDefinitionIndex, cycleIndex);
            var temp = new Array();
            temp = days.split(",");
            for (var i = 1; i < temp.length; i++) {
                var currentday = temp[i];
                var obj = $('div_' + cycleDefinitionIndex + '_' + cycleIndex + '_' + currentday);
                try {
                    dayOnClick(obj, cycleDefinitionIndex, cycleIndex, currentday);
                } catch(err) {
                }
            }
        }
    }
}

function resetCycle(cycleDefinitionIndex, cycleIndex) {
    var days_amount = parseInt($('cycle_length_' + cycleDefinitionIndex).value);
    for (var i = 0; i < days_amount; i++) {
        var obj = $('div_' + cycleDefinitionIndex + '_' + cycleIndex + '_' + (i + 1));
        unselectday(obj, cycleDefinitionIndex, cycleIndex, i);
    }
    $('selecteddays_' + cycleDefinitionIndex + '_' + cycleIndex).value = '';
}

function addMultiSelect(tbody, cycleDefinitionIndex, cycleIndex) {
    var repeat = parseInt($('cycle_repeat_' + cycleDefinitionIndex).value);

    if (cycleIndex < repeat - 1) {
        var multiselectsize = repeat - cycleIndex;
        if (multiselectsize > 5) {
            multiselectsize = 5;
        }
        if (multiselectsize == 2) {
            multiselectsize = 1;
        }
        var followingCycles = '';
        var allOption = new Element('OPTION', {});

        var multiselect = new Element('SELECT', {'id':'multiselect_' + cycleDefinitionIndex + '_' + cycleIndex, 'multiple':true, 'size':multiselectsize})

        if (cycleIndex < repeat - 2) {
            allOption.text = 'All';
            allOption.label = 'All';
            allOption.value = 'All';
            multiselect.appendChild(allOption);
        }

        for (var i = 0; i < repeat; i++) {
            if (i > cycleIndex) {
                var option = new Element('OPTION', {});
                option.text = 'Cycle ' + (i + 1);
                option.label = 'Cycle ' + (i + 1);
                option.value = i;
                multiselect.appendChild(option);
                followingCycles = followingCycles + ',' + i;
            }
        }

        multiselect.onclick = function() {
            if (this.value == 'All') {
                applyDaysToCycle($('selecteddays_' + cycleDefinitionIndex + '_' + cycleIndex).value, cycleDefinitionIndex, followingCycles);
            } else {
                applyDaysToCycle($('selecteddays_' + cycleDefinitionIndex + '_' + cycleIndex).value, cycleDefinitionIndex, this.value);
            }
        }

        var row = new Element('TR');
        var td = new Element('TD');
        td.colSpan = 2;
        td.addClassName('empty-column');
        row.appendChild(td);
        tbody.appendChild(row);
        row = new Element('TR');
        td = new Element('TD');
        td.update('Apply to ');
        td.addClassName('first-column');
        row.appendChild(td);
        td = new Element('TD');
        td.colSpan = 2;
        td.appendChild(multiselect);
        row.appendChild(td);
        tbody.appendChild(row);
    }


}

function dayOnClick(obj, cycleDefinitionIndex, cycleIndex, currentday) {
    var list = $('multiselect_' + cycleDefinitionIndex + '_' + cycleIndex);
    try {
        list.selectedIndex = -1;
    } catch(err) {
    }
    if ($(obj).hasClassName('selected_day')) {
        unselectday(obj, cycleDefinitionIndex, cycleIndex, currentday);
    } else {
        selectday(obj, cycleDefinitionIndex, cycleIndex, currentday);
    }
}

function unselectday(obj, cycleDefinitionIndex, cycleIndex, currentday) {
    $(obj).removeClassName('selected_day');
    $(obj).addClassName('unselected_day');
    updateDisplayedDays(cycleDefinitionIndex, cycleIndex, currentday, 'del');
}

function selectday(obj, cycleDefinitionIndex, cycleIndex, currentday) {
    $(obj).removeClassName('unselected_day');
    $(obj).addClassName('selected_day');
    updateDisplayedDays(cycleDefinitionIndex, cycleIndex, currentday, 'add');
}

function updateDisplayedDays(cycleDefinitionIndex, cycleIndex, currentday, action) {
    var objinput = $('selecteddays_' + cycleDefinitionIndex + '_' + cycleIndex);
    if (action == 'del') {
        var temp = new Array();
        temp = objinput.value.split(",");
        temp = temp.without(currentday);
        objinput.value = temp.toString();
    }
    if (action == 'add') {
        objinput.value = objinput.value + ',' + parseInt(currentday);
    }
    objinput.value = unique(objinput.value.split(",")).toString();
    if (objinput.value.charAt(0) != ',') {
        objinput.value = ',' + objinput.value;
    }
    var arr = new Array();
    arr = objinput.value.split(",");
    arr.sort(sortfunction);
}

function sortfunction(val1, val2) {
    if (val1 == '')val1 = 0;
    if (val2 == '')val2 = 0;
    return(parseInt(val1) - parseInt(val2));
}

function deleteCycleDefinition(cycleIndex) {
    var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
        parameters:<tags:ajaxstandardparams/>+"&confirmationType=deleteCrfCycle&crfCycleIndex=" + cycleIndex,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 530, 150);
        } ,
        method:'get'
    });
}

function deleteCycleConfirm(crfCycleIndex) {
    closeWindow();
    $('crfCycleIndexToRemove').value = crfCycleIndex;
    refreshPage();

}

function changePlannedRep(cycleDefinitionIndex, value) {
    if (value == '-1') {
        $('cycle_repeat_' + cycleDefinitionIndex).value = -1;
        $('cycle_repeat_' + cycleDefinitionIndex).hide();
    } else {
        if ($('cycle_repeat_' + cycleDefinitionIndex).value == -1) {
            $('cycle_repeat_' + cycleDefinitionIndex).value = '';
        }
        $('cycle_repeat_' + cycleDefinitionIndex).show();
    }
    showCyclesForDefinition(cycleDefinitionIndex, false);
}

function showSchedule(scheduleType) {
    $('calendarBasedDiv').hide();
    $('cycleBasedDiv').hide();
    $(scheduleType + 'Div').show();
}
function refreshPageLocal() {
    if ($('allArmsCheck').checked) {
        $('allArms').value = 'true';
    } else {
        $('allArms').value = 'false';
    }
    refreshPage();
}
function unique(arrayName)
{
    var newArray = new Array();
    label:for (var i = 0; i < arrayName.length; i++)
    {
        for (var j = 0; j < newArray.length; j++)
        {
            if (newArray[j] == arrayName[i])
                continue label;
        }
        newArray[newArray.length] = arrayName[i];
    }
    return newArray;
}

function baselinCheck(obj) {
    if (obj.checked) {
        $('crf.createBaseline').value = true;
    } else {
        $('crf.createBaseline').value = false;
    }
}

</script>
</head>
<body>
<c:set var="selectedFormArmSchedule" value="${command.selectedFormArmSchedule}"/>
<table>
    <tr>
        <td style="text-align:right;font-weight:bold;"><tags:message code='form.label.study'/></td>
        <td style="padding-left:10px;">${command.crf.study.displayName}</td>
    </tr>
    <tr>
        <td style="text-align:right;font-weight:bold;"><tags:message code='form.tab.form'/></td>
        <td style="padding-left:10px;">${command.crf.title}</td>
    </tr>
    <c:if test="${not (fn:length(command.crf.formArmSchedules) eq 1 && command.crf.formArmSchedules[0].arm.defaultArm eq 'true')}">
        <tr>
            <td style="text-align:right;font-weight:bold;vertical-align:top"><tags:message code='form.tab.arms'/></td>
            <td style="padding-left:10px;">
                <table>
                    <tr>
                        <td><b>Title</b></td>
                        <td></td>
                        <td><b>Description</b></td>
                    </tr>
                    <c:forEach items="${command.crf.formArmSchedules}" var="formArmSchedule">
                        <tr>
                            <td>${formArmSchedule.arm.title}</td>
                            <td>&nbsp;&nbsp;&nbsp;</td>
                            <td>${formArmSchedule.arm.description}</td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
        <tr>
            <td style="text-align:left;font-weight:bold;vertical-align:top" colspan="2">
                <c:if test="${command.crf.createBaseline}">
                    <c:set var="checked" value="checked='true'"/>
                </c:if>
                <input type="checkbox" name="baselineCheck" ${checked} onclick="javascript:baselinCheck(this);"
                       checked/>
                <input type="hidden" name="crf.createBaseline" value="${command.crf.createBaseline}"
                       id="crf.createBaseline"/>
                <tags:message code='form.tab.baseline'/>
            </td>
        </tr>
    </c:if>
    </tr>
</table>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="true" notDisplayInBox="true">
<jsp:attribute name="singleFields">
<form:hidden path="crfCycleDefinitionIndexToRemove" id="crfCycleIndexToRemove"/>
<chrome:box title="form.tab.calendar_template">
    <c:if test="${fn:length(command.crf.formArmSchedules) eq 1 && command.crf.formArmSchedules[0].arm.defaultArm eq 'true' }">
        <c:set var="styleHidden" value="style='display:none'"/>
    </c:if>

    <div class="row" ${styleHidden}>
        <div class="label"><spring:message code="form.calendar.arm"></spring:message></div>
        <div class="value">
            <select id="newSelectedFormArmSchedule" name="newSelectedFormArmSchedule" onchange="refreshPageLocal();">
                <c:forEach items="${command.crf.formArmSchedules}" var="formArmSchedule">
                    <c:choose>
                        <c:when test="${command.selectedFormArmSchedule.id eq formArmSchedule.id}">
                            <option value="${formArmSchedule.id}"
                                    selected="selected">${formArmSchedule.arm.title}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${formArmSchedule.id}">${formArmSchedule.arm.title}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
            <input type="checkbox" id="allArmsCheck" name="allArmsCheck"
                   <c:if test="${command.allArms}">checked</c:if>/>Apply this arm's schedule to all the arms
            <input type="hidden" name="allArms" id="allArms" value="${command.allArms}"/>
        </div>
    </div>
    <div class="row">
        <div class="label"><spring:message code="form.calendar.scheduletype"></spring:message></div>
        <div class="value">
            <input type="radio" name="scheduleType" value="calendarBased" onclick="showSchedule(this.value)"
                   style="margin:3px"
                   <c:if test="${selectedFormArmSchedule.crfCalendars[0].valid}">checked</c:if>/>
            <spring:message code="form.calendar.genericschedule"/>
            <input type="radio" name="scheduleType" value="cycleBased" onclick="showSchedule(this.value)"
                   style="margin:3px"
                   <c:if test="${fn:length(selectedFormArmSchedule.crfCycleDefinitions)gt 0}">checked</c:if>/>
            <spring:message code="form.calendar.cyclebasedschedule"/>
        </div>
    </div>
</chrome:box>
<div id="calendarBasedDiv"
     <c:if test="${not selectedFormArmSchedule.crfCalendars[0].valid}">style="display:none"</c:if>>
    <chrome:box title="form.calendar.genericschedule">
        <table class="top-widget" width="100%">
            <tr id="repeatprops">
                <td width="1%">
                    <tags:renderText propertyName="selectedFormArmSchedule.crfCalendars[0].repeatEveryValue"
                                     displayName="form.calendar.repeatEvery" size="2"/>
                </td>
                <td width="1%">
                    <tags:renderSelect propertyName="selectedFormArmSchedule.crfCalendars[0].repeatEveryUnit"
                                       options="${repetitionunits}" doNotshowLabel="true"/>
                </td>
                <td width="1%">
                    <tags:renderText propertyName="selectedFormArmSchedule.crfCalendars[0].dueDateValue"
                                     displayName="form.calendar.dueAfter" size="2"/>
                </td>
                <td width="1%">
                    <tags:renderSelect propertyName="selectedFormArmSchedule.crfCalendars[0].dueDateUnit"
                                       options="${duedateunits}" doNotshowLabel="true"/>
                </td>
                <td width="1%">
                    <tags:renderSelect
                            propertyName="selectedFormArmSchedule.crfCalendars[0].repeatUntilUnit"
                            options="${repeatuntilunits}"
                            onchange="changeinput(this,'0','');"
                            displayName="form.calendar.repeatUntil"/>
                </td>
                <td align="left">
                    <div id="div_date_0" style="display:none;">
                        <tags:renderDate
                                propertyName="crfCalendar_date_0.repeatUntilValue"
                                doNotShowFormat="true" noForm="true" doNotshowLabel="true"/>
                    </div>
                    <div id="div_number_0" style="display:none;">
                        <input size="3" id="crfCalendar_number_0.repeatUntilValue"
                               name="crfCalendar_number_0.repeatUntilValue"
                               type="text">
                    </div>
                    <input type="hidden"
                           id="selectedFormArmSchedule.crfCalendars[0].repeatUntilValue"
                           name="selectedFormArmSchedule.crfCalendars[0].repeatUntilValue"
                           value="${crfCalendar.repeatUntilValue}"/>
                </td>

            </tr>
        </table>
        <br/>
    </chrome:box>
</div>
<div id="cycleBasedDiv"
     <c:if test="${fn:length(selectedFormArmSchedule.crfCycleDefinitions) eq 0}">style="display:none;"</c:if>>
    <chrome:box title="form.calendar.cyclebasedschedule">
        <table class="top-widget" width="100%">
            <tr>
                <td>
                    <c:forEach items="${selectedFormArmSchedule.crfCycleDefinitions}" var="crfCycleDefinition"
                               varStatus="status">
                        <tags:formScheduleCycleDefinition cycleDefinitionIndex="${status.index}"
                                                          crfCycleDefinition="${crfCycleDefinition}"
                                                          repeatOptions="${cycleplannedrepetitions}"/>
                    </c:forEach>
                    <div id="hiddenDiv"></div>
                    <chrome:division title=" "/>
                    <div style="width:4.5em;">
                        <tags:button color="blue" size="small" markupWithTag="a" onclick="javascript:addCycle()"
                                     value="form.schedule.add_cycle" icon="add"/>
                    </div>
                </td>
            </tr>
        </table>
        <br/>
    </chrome:box>
</div>
<div id="hidden_inputs_div">
    <c:forEach items="${selectedFormArmSchedule.crfCycleDefinitions}" var="crfCycleDefinition"
               varStatus="status">
        <c:if test="${not empty crfCycleDefinition}">
            <div id="hidden_inputs_div_${status.index}">
                <c:forEach items="${crfCycleDefinition.crfCycles}" var="crfCycle" varStatus="cyclestatus">
                    <input type="hidden" name='selecteddays_${status.index}_${cyclestatus.index}'
                           id='selecteddays_${status.index}_${cyclestatus.index}'
                           value="${crfCycle.cycleDays}"/>
                </c:forEach>
            </div>
            <script type="text/javascript">
                showCyclesForDefinition(${status.index}, true);
            </script>
        </c:if>
    </c:forEach>
</div>
</jsp:attribute>
</tags:tabForm>
</body>
</html>