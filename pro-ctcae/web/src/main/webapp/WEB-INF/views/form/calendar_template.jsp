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

    /*.selected_day:hover {*/
    /*background-color: #cccccc;*/
    /*cursor: pointer;*/
    /*}*/

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
</style>
<script type="text/javascript">
var totalCalendars = 0;
var repeatvalues = new Array();
function changeinput(obj, index, value) {
    if (obj.value == 'Indefinitely') {
        $('div_date_' + index).hide();
        $('div_number_' + index).hide();
        $('crf.crfCalendars[' + index + '].repeatUntilAmount').value = '';
    }
    if (obj.value == 'Date') {
        $('div_number_' + index).hide();
        $('div_date_' + index).show();
        $('crfCalendar_date_' + index + '.repeatUntilAmount').value = value;
        $('crf.crfCalendars[' + index + '].repeatUntilAmount').value = value;
    }
    if (obj.value == 'Number') {
        $('div_date_' + index).hide();
        $('div_number_' + index).show();
        $('crfCalendar_number_' + index + '.repeatUntilAmount').value = value;
        $('crf.crfCalendars[' + index + '].repeatUntilAmount').value = value;
    }
}


function none() {
}
Event.observe(window, "load", function () {
    for (var i = 0; i < totalCalendars; i++) {
        var item = document.getElementsByName('crf.crfCalendars[' + i + '].repeatUntilUnit')[0];
        changeinput(item, i, repeatvalues[i]);
    }
})

function addCycle() {
    var request = new Ajax.Request("<c:url value="/pages/form/addFormScheduleCycle"/>", {
        onComplete:addCycleDiv,
        parameters:"subview=subview",
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
    var days_unit = $('crf.crfCycleDefinitions[' + index + '].cycleLengthUnit').value;
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
        for (i = 0; i < children.length; i++) {
            $(children[i]).remove();
        }
    }
}

function emptyTable(index) {
    var tbody = $('cycle_table_' + index).getElementsByTagName("TBODY")[0];
    var children = tbody.childElements();
    for (i = 0; i < children.length; i++) {
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
                dayOnClick(obj, cycleDefinitionIndex, cycleIndex, currentday);
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
            multiselect.appendChild(allOption);
        }

        for (var i = 0; i < repeat; i++) {
            if (i > cycleIndex) {
                var option = new Element('OPTION', {});
                option.text = 'Cycle ' + (i + 1);
                option.value = i;
                option.onclick = function() {
                    applyDaysToCycle($('selecteddays_' + cycleDefinitionIndex + '_' + cycleIndex).value, cycleDefinitionIndex, this.value);
                };
                multiselect.appendChild(option);
                followingCycles = followingCycles + ',' + i;
            }
        }

        if (followingCycles != '') {
            allOption.onclick = function() {
                applyDaysToCycle($('selecteddays_' + cycleDefinitionIndex + '_' + cycleIndex).value, cycleDefinitionIndex, followingCycles);
            };
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
    var arr = new Array();
    arr = objinput.value.split(",");
    arr.sort(sortfunction);
}

function sortfunction(val1, val2) {
    return(parseInt(val1) - parseInt(val2));
}

function deleteCycleDefinition(cycleIndex) {
    var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
        parameters:"confirmationType=deleteCrfCycle&subview=subview&crfCycleIndex="
                + cycleIndex,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 530, 150);
        } ,
        method:'get'
    });
}

function deleteCycleConfirm(crfCycleIndex) {
    closeWindow();
    $('crfCycleIndexToRemove').value = crfCycleIndex;
    submitScheduleTemplateTabPage();

}
function submitScheduleTemplateTabPage() {
    $('_target').name = "_target" + 2;
    $("_finish").name = "_nofinish";
    $('command').submit();
}

function changePlannedRep(cycleDefinitionIndex, value) {
    if (value == '-1') {
        $('cycle_repeat_' + cycleDefinitionIndex).value = -1;
        $('cycle_repeat_' + cycleDefinitionIndex).hide();
    } else {
        $('cycle_repeat_' + cycleDefinitionIndex).value = 0;
        $('cycle_repeat_' + cycleDefinitionIndex).show();
    }

    showCyclesForDefinition(cycleDefinitionIndex, false);
}


</script>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="true" notDisplayInBox="true">
<jsp:attribute name="singleFields">
    <input type="hidden" name="_finish" value="true" id="_finish">
<form:hidden path="crfCycleDefinitionIndexToRemove" id="crfCycleIndexToRemove"/>
     <chrome:box title="Generic schedule" autopad="true">
         <c:forEach items="${command.crf.crfCalendars}" var="crfCalendar" varStatus="status">
             <script>
                 repeatvalues[totalCalendars] = '${crfCalendar.repeatUntilAmount}';
                 totalCalendars = totalCalendars + 1;
             </script>
             <table class="top-widget" width="100%">
                 <tr id="repeatprops">
                     <td>
                         <b>Repeat every</b>
                         <input type="text" size="2"
                                name="crf.crfCalendars[${status.index}].repeatEveryAmount"
                                value="${crfCalendar.repeatEveryAmount}" class="validate-NUMERIC"/>
                         <form:select path="crf.crfCalendars[${status.index}].repeatEveryUnit"
                                      items="${repetitionunits}"
                                      itemLabel="desc" itemValue="code"/>

                     </td>
                     <td>
                         <b>Form is due after</b>
                         <input type="text" size="2"
                                name="crf.crfCalendars[${status.index}].dueDateAmount"
                                value="${crfCalendar.dueDateAmount}" class="validate-NUMERIC"/>
                         <form:select path="crf.crfCalendars[${status.index}].dueDateUnit" items="${duedateunits}"
                                      itemLabel="desc" itemValue="code"/>
                     </td>
                     <td>
                         <table>
                             <tr>
                                 <td>
                                     <b>Repeat until</b>
                                     <form:select path="crf.crfCalendars[${status.index}].repeatUntilUnit"
                                                  items="${repeatuntilunits}"
                                                  itemLabel="desc" itemValue="code"
                                                  onchange="changeinput(this,'${status.index}','');"/>

                                     </select>
                                 </td>
                                 <td>
                                     <div id="div_date_${status.index}" style="display:none;">
                                         <tags:renderDate
                                                 propertyName="crfCalendar_date_${status.index}.repeatUntilAmount"
                                                 doNotShowFormat="true" noForm="true" doNotshowLabel="true"/>
                                     </div>
                                     <div id="div_number_${status.index}" style="display:none;">
                                         <input size="3" id="crfCalendar_number_${status.index}.repeatUntilAmount"
                                                name="crfCalendar_number_${status.index}.repeatUntilAmount"
                                                type="text">
                                     </div>
                                     <input type="hidden" id="crf.crfCalendars[${status.index}].repeatUntilAmount"
                                            name="crf.crfCalendars[${status.index}].repeatUntilAmount"
                                            value="${crfCalendar.repeatUntilAmount}"/>
                                 </td>
                             </tr>
                         </table>
                     </td>
                 </tr>
             </table>
         </c:forEach>
     </chrome:box>
</jsp:attribute>
    <jsp:attribute name="repeatingFields">
        <chrome:box title="Cycle based schedule" autopad="true">
            <table class="top-widget" width="100%">
                <tr>
                    <td>
                        <c:forEach items="${command.crf.crfCycleDefinitions}" var="crfCycleDefinition"
                                   varStatus="status">
                            <tags:formScheduleCycleDefinition cycleDefinitionIndex="${status.index}"
                                                              crfCycleDefinition="${crfCycleDefinition}"
                                                              repeatOptions="${cycleplannedrepetitions}"/>
                        </c:forEach>
                        <div id="hiddenDiv"></div>
                        <div class="local-buttons">
                            <tags:button color="blue" markupWithTag="a" onClick="javascript:addCycle()"
                                         value="form.schedule.add_cycle"/>
                        </div>
                    </td>
                </tr>
            </table>
        </chrome:box>
        <div id="hidden_inputs_div">
            <c:forEach items="${command.crf.crfCycleDefinitions}" var="crfCycleDefinition" varStatus="status">
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