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
<jsp:useBean id="today" class="java.util.Date"/>
<c:set var="fDate" value='<tags:formatDate value="${today}"/>'/>
<tags:stylesheetLink name="tabbedflow"/>
<tags:includeScriptaculous/>
<tags:includePrototypeWindow/>
<script type="text/javascript">
</script>
<style type="text/css">

    table.top-widget {
        border: 1px solid #cccccc;
        background-color: #E7EAF3;
        width: 90%;
    }

    tr.top-header {
        background-color: #cccccc;
        height: 30px;
    }

    table.widget {
        border-left: 9px solid #C3D9FF;
        border-bottom: 6px solid #C3D9FF;
        width: 60%;
        table-layout: fixed;
        background-color: #FFFFFF;
        font-size: x-small;
        font-family: verdana, arial;
    }

    tr.header {
        background-color: #C3D9FF;
        color: #0e065b;
        text-align: center;
        height: 30px;
    }

    td.header {
        border-bottom: 1px solid #77a9ff;
        font-size: small;
        font-weight: bold;
    }

    td.border-td {
        border-bottom: 1px solid #cccccc;
        border-left: 1px solid #cccccc;
    }

    td.data {
        height: 70px;
        text-align: right;
        vertical-align: top;
        border-bottom: 1px solid #C3D9FF;
        border-left: 1px solid #C3D9FF;
    }

    .grey {
        background-color: #eaf1f4;
        height: 20px; /*color: #666666;*/
        color: #414141;
        font-size: small;
        font-weight: bold;
    }

    .blue {
        background-color: #0051fc;
        height: 50px;
        text-align: center;
        color: #FFFFFF;
        cursor: pointer;
    }

    .hoverActive {
        background-color: #990033;
    }

    .aaa {
        background-color: #ff9900;
    }

    .passive {
        height: 50px;
    }

    .passive:hover {
        background-color: #DDDDDD;
        cursor: pointer;
    }

    img.navBack {
        background-image: url( /proctcae/images/combined_v5.gif );
        background-position: -148px -17px;
        padding-left: 2px;
        padding-right: 0;
        vertical-align: middle;
    }

    img.removebutton {
        background-image: url( /proctcae/images/combined_v5.gif );
        background-position: -0px -50px;
        padding-left: 2px;
        padding-right: 0;
        vertical-align: middle;
    }

    img.navForward {
        background-image: url( /proctcae/images/combined_v5.gif );
        background-position: -148px 0;
        padding-left: 2px;
        padding-right: 2px;
        vertical-align: middle;
    }

    img.navbutton {
        cursor: pointer;
    }

    img {
        border: 0 none;
    }

    div#layer1 {
        position: absolute;
        visibility: hidden;
        width: 200px;
        left: 20px;
        top: 300px;
        background-color: #fff;
        border: 1px solid #000;
        padding: 10px;
    }

    #close {
        float: right;
    }

    .unselected_day {
        background-color: #FFFFFF;
        height: 30px;
        width: 30px;
        text-align: center;
        color: #000000;
        font-weight: bold;
        font-size: 10px;
        border-bottom: #0000cc solid 1px;
        border-right: #0000cc solid 1px;
    }

    .selected_day {
        background-color: #0051fc;
        height: 30px;
        width: 30px;
        text-align: center;
        color: #FFFFFF;
        font-weight: bold;
        font-size: 10px;
        border-bottom: #0000cc solid 1px;
        border-right: #0000cc solid 1px;
    }

    .top-border {
        border-top: #0000cc solid 1px;
    }

    .left-border {
        border-left: #0000cc solid 1px;
    }

    .empty {
        height: 5px;
    }

    .first-column-A {
        text-align: left;
        vertical-align: top;
        font-weight: bold;
        width: 170px;
    }

    .first-column-B {
        text-align: left;
        vertical-align: top;
        font-weight: bold;
        width: 60px;
    }

</style>
<script type="text/javascript">


function applyCalendar(index, direction) {
    document.body.style.cursor = 'wait';
    getCalendar(index, "dir=" + direction);
}

function addRemoveSchedule(index, date, action) {
    document.body.style.cursor = 'wait';
    closeWindow();
    if (action == 'cancel') {
        getCalendar(index, "dir=refresh");
    } else {
        var request = new Ajax.Request("<c:url value="/pages/participant/addCrfSchedule"/>", {
            onComplete:function(transport) {
                getCalendar(index, "dir=refresh");
            },
            parameters:"subview=subview&index=" + index + "&date=" + date + "&action=" + action,
            method:'get'
        })
    }
}

function getCalendar(index, parameters) {
    document.body.style.cursor = 'wait';
    var request = new Ajax.Request("<c:url value="/pages/participant/displaycalendar"/>", {
        onComplete:function(transport) {
            var response = transport.responseText;
            $("calendar_" + index).innerHTML = response;
            initializeCalendar(index);
        },
        parameters:"subview=subview&index=" + index + "&" + parameters,
        method:'get'
    })
}

var checkStatus = true;
function initializeCalendar(index) {
    document.body.style.cursor = 'wait';
    var items = document.getElementsByName(index + '_schedule_div');
    for (var i = 0; i < items.length; i++) {
        try {
            Droppables.add(index + '_schedule_' + (i + 1), {hoverclass: 'hoverActive', onDrop: moveItem});
        } catch(err) {
        }
    }

    items = document.getElementsByName(index + '_temp_div');
    var j = items.length;
    while (j > 0) {
        var item = items[0];
        var date = item.id.substring(item.id.indexOf('_', 2) + 1);
        item.addClassName('blue');
        if (item.innerHTML.indexOf('In-progress') != -1) {
            item.style.background = '#ff9900';
        }
        if (item.innerHTML.indexOf('Completed') != -1) {
            item.style.background = '#00cc00';
        }
        if (item.title == 'true') {
//            item.style.background = '#006666';
            item.style.background = 'red';
        }
        new Draggable(item, {revert:true});
        checkStatus = false;
        moveItem(item, $(index + '_schedule_' + date));
        j--;
    }

    items = document.getElementsByName(index + '_temp_div');
    for (var i = 0; i < items.length; i++) {
        var item = items[i];
        var date = item.id.substring(item.id.indexOf('_', 2) + 1);
        $(index + '_schedule_' + date).onclick = function() {
        };
    }
    document.body.style.cursor = 'default';
}


function moveItem(draggable, droparea) {
    try {
        droparea.onclick = function() {
        };
        var index = draggable.id.substring(0, draggable.id.indexOf('_'));
        if (checkStatus) {
            if (draggable.innerHTML.indexOf('Scheduled') == -1) {
                return;
            }
        }
        checkStatus = true;
        droparea.innerHTML = '';
        draggable.parentNode.removeChild(draggable);
        droparea.appendChild(draggable);
        var olddate = draggable.id.substring(draggable.id.indexOf('_', 2) + 1);
        if (olddate != '') {
            var newdate = droparea.id.substring(droparea.id.indexOf('_', 2) + 1);
            droparea.addClassName('blue');
            if (droparea.id != (index + '_schedule_' + olddate)) {
                $(index + '_schedule_' + olddate).removeClassName('blue');
            }
            if (newdate != olddate) {
                showMoveWindow(olddate, newdate, index);
            }
        }
    } catch(err) {
        getCalendar(index, "dir=refresh");
    }
}

function showMoveWindow(olddate, newdate, index) {
    olddate = parseInt(olddate);
    newdate = parseInt(newdate);
    var diff = 0;
    var dir = '';
    if (olddate > newdate) {
        diff = olddate - newdate;
        dir = 'back';
    } else {
        diff = newdate - olddate;
        dir = 'forward';
    }
    var htmlcontent = '<table width="100%"><tr><td align="center"><b>Would you like to move only this event, all events, or this and all future events by ' + diff + ' day(s)?<br></b></td></tr>' +
                      '<tr><td>&nbsp;</td></tr>' +
                      '<tr><td align="center"><input type="button" value="Only this instance" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + newdate + ',' + olddate + '\',\'add,del\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="All events" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + newdate + ',' + olddate + '\',\'moveall\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="All following" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + newdate + ',' + olddate + '\',\'moveallfuture\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + newdate + ',' + olddate + '\',\'cancel\'' +
                      ')"/></td></tr></table>';
    var win = Windows.getFocusedWindow();
    if (win == null) {
        win = new Window({ id: '100' , className: "alphacube", closable : false, minimizable : false, maximizable :
                false, title: "", height:70, width: 600, left: (screen.width / 2) - 300, top: document.viewport.getScrollOffsets()[1] + (250)});
        win.setDestroyOnClose();
        win.setHTMLContent(htmlcontent);
        win.show(true)

    } else {
        win.setHTMLContent(htmlcontent);
        win.refresh();
    }
}

function showDeleteWindow(date, index) {
    date = parseInt(date);
    var htmlcontent = '<table width="100%"><tr><td align="center"><b>Would you like to delete only this event, all events, or this and all future events?<br></b></td></tr>' +
                      '<tr><td>&nbsp;</td></tr>' +
                      '<tr><td align="center"><input type="button" value="Only this instance" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'del\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="All events" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'delall\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="All following" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'delallfuture\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'cancel\'' +
                      ')"/></td></tr></table>';
    var win = Windows.getFocusedWindow();
    if (win == null) {
        win = new Window({ id: '100' , className: "alphacube", closable : false, minimizable : false, maximizable :
                false, title: "", height:70, width: 600, left: (screen.width / 2) - 300, top: document.viewport.getScrollOffsets()[1] + (250)});
        win.setDestroyOnClose();
        win.setHTMLContent(htmlcontent);
        win.show(true)

    } else {
        win.setHTMLContent(htmlcontent);
        win.refresh();
    }
}

function showAddWindow(date, index) {
    date = parseInt(date);
    var htmlcontent = '<table width="100%"><tr><td align="center"><b>Would you like to add a new survey date?<br></b></td></tr>' +
                      '<tr><td>&nbsp;</td></tr>' +
                      '<tr><td align="center"><input type="button" value="Yes" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'add\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'cancel\'' +
                      ')"/></td></tr></table>';
    var win = Windows.getFocusedWindow();
    if (win == null) {
        win = new Window({ id: '100' , className: "alphacube", closable : false, minimizable : false, maximizable :
                false, title: "", height:70, width: 260, left: (screen.width / 2) - 130, top: document.viewport.getScrollOffsets()[1] + (250)});
        win.setDestroyOnClose();
        win.setHTMLContent(htmlcontent);
        win.show(true)

    } else {
        win.setHTMLContent(htmlcontent);
        win.refresh();
    }
}


function selectDate(obj, text, index) {
    document.body.style.cursor = 'wait';
    addRemoveSchedule(index, date, 'add');
}

Event.observe(window, "load", function () {
    var items = document.getElementsByName("repeatdropdown");
    for (var i = 0; i < items.length; i++) {
        changerepeat(items[i], items[i].id, false);
    }
})
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
var isIndefinite = false;
function showCyclesForDefinition(cycleDefinitionIndex, cycleLength, cycleLengthUnit, repeat) {
    var days_amount = cycleLength;
    var days_unit = cycleLengthUnit;
    var days = calculateDays(days_unit, days_amount);
    var repeat = repeat;
    if (repeat == -1) {
        repeat = 1;
        isIndefinite = true;
    }
    if (days > 0 && days < 1000 && repeat > 0) {
        $('div_cycle_table_' + cycleDefinitionIndex).show();
        buildTable(cycleDefinitionIndex, days, repeat);
        isIndefinite = false;
    } else {
        $('div_cycle_table_' + cycleDefinitionIndex).hide();
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
function addFirstColumn(row, rownumber, cycleNumber) {
    var td = new Element('TD');
    if (rownumber == 0) {
        if (isIndefinite) {
            td.update('Cycle applied indefinitely');
            td.addClassName('first-column-A')
        } else {
            td.update('Cycle ' + (cycleNumber + 1));
            td.addClassName('first-column-B')
        }
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
    td.appendChild(div);
    row.appendChild(td);
    tbody.appendChild(row);
}
function buildTable(index, days, repeat) {
    var tbody = emptyTable(index);
    var daysPerLine = 21;
    var numOfRows = parseInt(days / daysPerLine);
    if (days % daysPerLine > 0) {
        numOfRows = numOfRows + 1;
    }
    for (var k = 0; k < repeat; k++) {
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
        addEmptyRow(tbody);
    }
}
</script>

</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" formName="myForm">
    <jsp:attribute name="singleFields">
        
        <input type="hidden" name="_finish" value="true"/>

            <c:forEach items="${command.participantSchedules}" var="participantSchedule" varStatus="status">
                <c:set var="participantCrf" value="${participantSchedule.studyParticipantCrf}"/>
                <chrome:division title="${participantCrf.crf.title} (${participantCrf.crf.crfVersion})" message="false">
                    <div align="left">
                        <b>Start date: <tags:formatDate value="${participantCrf.startDate}"/> </b>
                        <table class="top-widget" cellspacing="0" align="center">
                            <c:forEach items="${participantCrf.crfCycleDefinitions}" var="crfCycleDefinition"
                                       varStatus="statuscycledefinition">
                                <tr>
                                    <td>
                                        <tags:formScheduleCycleDefinition
                                                cycleDefinitionIndex="${statuscycledefinition.index}"
                                                crfCycleDefinition="${crfCycleDefinition}"
                                                readonly="true"
                                                crfIndex="${status.index}"/>
                                        <script type="text/javascript">
                                            showCyclesForDefinition('${status.index}_${statuscycledefinition.index}', ${crfCycleDefinition.cycleLength}, '${crfCycleDefinition.cycleLengthUnit}', '${crfCycleDefinition.repeatTimes}');
                                        </script>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td>
                                    <br/>
                                    <chrome:division title=" "/>
                                    <div id="calendar_${status.index}">
                                        <tags:participantcalendar schedule="${participantSchedule}"
                                                                  index="${status.index}"/>
                                        <script type="text/javascript">
                                            initializeCalendar('${status.index}');
                                        </script>

                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <br/><br/>
                                </td>
                            </tr>
                        </table>
                    </div>

                </chrome:division>
            </c:forEach>
</jsp:attribute>
</tags:tabForm>
</body>
</html>