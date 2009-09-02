function applyCalendar(index, direction) {
    document.body.style.cursor = 'wait';
    getCalendar(index, "dir=" + direction);
}


var checkStatus = true;
function initializeCalendar(index) {
    document.body.style.cursor = 'wait';
    var items = document.getElementsByClassName(index + '_schedule_div');
    for (var i = 0; i < items.length; i++) {
        var ele = $(index + '_schedule_' + (i + 1));
        if (ele != null) {
            Droppables.add(index + '_schedule_' + (i + 1), {hoverclass: 'hoverActive', onDrop: moveItem});
        }
    }

    items = document.getElementsByClassName(index + '_temp_div');
    var j = items.length;
    while (j > 0) {
        var item = items[j - 1];
        var date = item.id.substring(item.id.indexOf('_', 2) + 1);
        item.removeClassName(index + '_temp_div');
        item.addClassName('blue');
        if (item.innerHTML.indexOf('In-progress') != -1) {
            item.style.background = '#ff9900';
        }
        if (item.innerHTML.indexOf('Completed') != -1) {
            item.style.background = '#00cc00';
        }
        if (item.title == 'true') {
            item.style.background = 'red';
        }
        new Draggable(item, {revert:true});
        checkStatus = false;
        moveItem(item, $(index + '_schedule_' + date));
        j--;
    }

    items = document.getElementsByClassName(index + '_temp_div');
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
        //        alert(draggable.parentNode);
        draggable.parentNode.removeChild(draggable);
        droparea.appendChild(draggable);
        //        var olddate = draggable.id.substring(draggable.id.indexOf('_', 2) + 1);
        //        if (olddate != '') {
        //            var newdate = droparea.id.substring(droparea.id.indexOf('_', 2) + 1);
        //            droparea.addClassName('blue');
        //            if (droparea.id != (index + '_schedule_' + olddate)) {
        //                $(index + '_schedule_' + olddate).removeClassName('blue');
        //            }
        //            if (newdate != olddate) {
        //                showMoveWindow(olddate, newdate, index);
        //            }
        //        }
    } catch(err) {
        alert(err.description);
        //        getCalendar(index, "dir=refresh");
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
    var children = tbody.childNodes;
    for (var i = 0; i < children.length; i++) {
        if (children[i].nodeType == 1) {
            $(children[i]).remove();
        }
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
