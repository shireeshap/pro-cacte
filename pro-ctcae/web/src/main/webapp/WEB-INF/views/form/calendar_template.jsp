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
        height: 50px;
        width: 50px;
        text-align: center;
        color: #000000;
        font-weight: bold;
        font-size: 15px;
        cursor: pointer;
        border-bottom: #0000cc solid 1px;
        border-right: #0000cc solid 1px;
    }

    .selected_day {
        background-color: #0051fc;
        height: 50px;
        width: 50px;
        text-align: center;
        color: #FFFFFF;
        font-weight: bold;
        font-size: 15px;
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

    function showCycle(index) {
        $('selecteddays[' + index + ']').value = '';
        var days_amount = parseInt($('cycle_length_' + index).value);
        var days_unit = $('crf.crfCycles[' + index + '].cycleLengthUnit').value;
        var multiplier = 1;
        if (days_unit == 'Weeks') {
            multiplier = 7;
        }
        if (days_unit == 'Months') {
            multiplier = 30;
        }
        var days = days_amount * multiplier;
        if (days > 0 && days < 1000) {
            $('div_cycle_selectdays_' + index).show();
            $('div_cycle_table_' + index).show();
            $('div_cycle_repeat_' + index).show();
            buildTable(index, days, $('selecteddays[' + index + ']').value + ',');
        } else {
            $('div_cycle_table_' + index).hide();
            $('div_cycle_selectdays_' + index).hide();
            $('div_cycle_repeat_' + index).hide();
        }
    }


    function buildTable(index, days, selecteddays) {
        var tbody = $('cycle_table_' + index).getElementsByTagName("TBODY")[0];
        var children = tbody.childElements();
        for (i = 0; i < children.length; i++) {
            $(children[i]).remove();
        }
        var daysPerLine = 14;
        var numOfRows = parseInt(days / daysPerLine);
        if (days % daysPerLine > 0) {
            numOfRows = numOfRows + 1;
        }
        for (i = 0; i < numOfRows; i++) {
            var row = new Element('TR');
            for (j = 0; j < daysPerLine; j++) {
                var currentday = i * daysPerLine + j + 1;
                if (currentday <= days) {
                    var td = new Element('TD');
                    if (i == 0) {
                        td.addClassName('top-border')
                    }
                    if (j == 0) {
                        td.addClassName('left-border')
                    }
                    var div = new Element('div', {'id': 'div_' + index + '_' + currentday}).update(currentday);
                    if (selecteddays.indexOf(',' + currentday + ',') == -1) {
                        div.addClassName('unselected_day');
                    } else {
                        div.addClassName('selected_day');
                    }
                    div.onclick = function() {
                        dayOnClick(this, index, this.id.substring(this.id.indexOf('_', 4) + 1));
                    }
                    td.appendChild(div);
                    row.appendChild(td);
                }
            }
            tbody.appendChild(row);
        }
    }

    function dayOnClick(obj, index, currentday) {
        if ($(obj).hasClassName('selected_day')) {
            unselectday(obj, index, currentday);
        } else {
            selectday(obj, index, currentday);
        }
    }

    function unselectday(obj, index, currentday) {
        $(obj).removeClassName('selected_day');
        $(obj).addClassName('unselected_day');
        updateDisplayedDays(index, currentday, 'del');
    }

    function selectday(obj, index, currentday) {
        $(obj).removeClassName('unselected_day');
        $(obj).addClassName('selected_day');
        updateDisplayedDays(index, currentday, 'add');
    }

    function updateDisplayedDays(index, currentday, action) {
        var objinput = $('selecteddays[' + index + ']');
        var objdiv = $('div_selecteddays_' + index);
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
        //        objdiv.update(arr.toString().substr(1));
    }

    function sortfunction(val1, val2) {
        return(parseInt(val1) - parseInt(val2));
    }

    function deleteCycle(cycleIndex) {
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


</script>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="true" notDisplayInBox="true">
<jsp:attribute name="singleFields">
<input type="hidden" name="_finish" value="true" id="_finish"/>
<form:hidden path="crfCycleIndexToRemove" id="crfCycleIndexToRemove"/>
     <chrome:box title="Generic schedule" autopad="true">
         <c:forEach items="${command.crf.crfCalendars}" var="crfCalendar" varStatus="status">
             <script>
                 repeatvalues[totalCalendars] = '${crfCalendar.repeatUntilAmount}';
                 totalCalendars = totalCalendars + 1;
             </script>
             <%--<chrome:division title="Generic schedule"/>--%>
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
                        <c:forEach items="${command.crf.crfCycles}" var="crfCycle" varStatus="status">
                            <tags:formScheduleCycle cycleIndex="${status.index}" crfCycle="${crfCycle}" />
                            <c:if test="${crfCycle.cycleDays ne ''}">
                                <script type="text/javascript">
                                    showCycle(${status.index});
                                </script>
                            </c:if>
                        </c:forEach>
                        <div id="hiddenDiv"></div>
                        <div class="local-buttons">
                            <tags:button type="anchor" onClick="javascript:addCycle()"
                                         value="form.schedule.add_cycle"/>
                        </div>
                    </td>
                </tr>
            </table>
        </chrome:box>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>