function applyCalendar(index, direction) {
    getCalendar(index, "dir=" + direction);
}
var calendarArr = new Array();
var scheduleArr = new Array();


var checkStatus = true;
function isdefined(el) {
    return !(typeof(el) == 'undefined');
}
function getDate(item) {
    return item.id.substring(item.id.indexOf('_', 2) + 1);
}
function getIndex(item) {
    return item.id.substring(0, item.id.indexOf('_'));
}

function initializeCalendar(index) {
    initialize();
    var myCalendar = calendarArr[index];
    var mySchedules = scheduleArr[index];
    for (var i = 0; i < myCalendar.length; i++) {
        if (isdefined(myCalendar[i])) {
            var div_id = index + '_schedule_' + i;
            var myschedule = mySchedules[i];
            if (isdefined(myschedule)) {
                var tilda = myschedule.indexOf('~');
                var status = myschedule.substring(0, tilda);
                var remaining = myschedule.substring(tilda + 1);
                tilda = remaining.indexOf('~');
                var baseline = remaining.substring(0, tilda);
                var holiday = remaining.substring(tilda + 1);

                var item = $(div_id);
                item.addClassName('blue');
                item.removeClassName('passive');
                item.innerHTML = '<br/>' + status;
                item.style.cursor = 'default';
                if (baseline == 'true') {
                    item.innerHTML = item.innerHTML + '<br/>(baseline)';
                }
                if (status == 'In-progress') {
                    item.style.background = '#ff9900';
                }
                if (status == 'Completed') {
                    item.style.background = '#00cc00';
                }
                if (status == 'Past-due') {
                    item.style.background = 'red';
                }
                if (status == 'Scheduled' || status == 'Past-due') {
                    if (holiday == 'true') {
                        item.style.background = '#666666';
                    }
                    item.style.cursor = 'pointer';
                    if (baseline != 'true') {
                        var delIcon = '<div style="float:right"><img height="13" width="12" src="/proctcae/images/blank.gif" class="removebutton" ' +
                                      'onclick="showDeleteWindow(' + i + ', ' + index + ');"/></div>';
                        item.innerHTML = delIcon + item.innerHTML;
                    }
                    myCalendar[i] = new YAHOO.example.DDPlayer(div_id, 'date');
                }
            } else {
                myCalendar[i] = new YAHOO.util.DDTarget(div_id, 'date');
                Event.observe(div_id, "click", function() {
                    showAddWindow(getDate(this), getIndex(this));
                })
            }
        }
    }
}

function initialize() {
    YAHOO.example.DDPlayer = function(id, sGroup, config) {
        YAHOO.example.DDPlayer.superclass.constructor.apply(this, arguments);
        this.initPlayer(id, sGroup, config);
    };
    YAHOO.extend(YAHOO.example.DDPlayer, YAHOO.util.DDProxy, {
        TYPE: "DDPlayer",
        initPlayer: function(id, sGroup, config) {
            if (!id) {
                return;
            }

            var el = this.getDragEl()
            YAHOO.util.Dom.setStyle(el, "borderColor", "transparent");
            YAHOO.util.Dom.setStyle(el, "opacity", 0.76);
            this.isTarget = false;
            this.originalStyles = [];
            this.type = YAHOO.example.DDPlayer.TYPE;
            this.slot = null;
            this.startPos = YAHOO.util.Dom.getXY(this.getEl());
        },

        startDrag: function(x, y) {
            var Dom = YAHOO.util.Dom;
            var dragEl = this.getDragEl();
            var clickEl = this.getEl();
            dragEl.innerHTML = clickEl.innerHTML;
            dragEl.className = clickEl.className;
            Dom.setStyle(dragEl, "color", Dom.getStyle(clickEl, "color"));
            Dom.setStyle(dragEl, "backgroundColor", Dom.getStyle(clickEl, "backgroundColor"));
            Dom.setStyle(clickEl, "opacity", 0.5);
        },
        endDrag: function(e) {
            YAHOO.util.Dom.setStyle(this.getEl(), "opacity", 1);
        },

        onDragDrop: function(e, id) {
            var oDD;

            if ("string" == typeof id) {
                oDD = YAHOO.util.DDM.getDDById(id);
            } else {
                oDD = YAHOO.util.DDM.getBestMatch(id);
            }
            var el = this.getEl();
            if (oDD.player) {
                YAHOO.util.Dom.setXY(el, this.startPos);
            } else {
                var olddate = getDate(this);
                var newdate = getDate(oDD);
                var index = getIndex(this);
                YAHOO.util.DDM.moveToEl(el, oDD.getEl());
                showMoveWindow(olddate, newdate, index);
            }
        },
        onDragOver: function(e, id) {
        },
        onDrag: function(e, id) {
        }
    });
}

function showWindow(htmlcontent) {
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
    var htmlcontent = '<table width="100%"><tr><td align="center"><b>Would you like to move only this event, all events, or this and all following events by ' + diff + ' day(s)?<br></b></td></tr>' +
                      '<tr><td>&nbsp;</td></tr>' +
                      '<tr><td align="center"><input type="button" value="Only this instance" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + newdate + ',' + olddate + '\',\'add,del\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="All events" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + newdate + ',' + olddate + '\',\'moveall\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="All following" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + newdate + ',' + olddate + '\',\'moveallfuture\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + newdate + ',' + olddate + '\',\'cancel\'' +
                      ')"/></td></tr></table>';

    showWindow(htmlcontent);
}

function showDeleteWindow(date, index) {
    date = parseInt(date);
    var htmlcontent = '<table width="100%"><tr><td align="center"><b>Would you like to delete only this event, all events, or this and all following events?<br></b></td></tr>' +
                      '<tr><td>&nbsp;</td></tr>' +
                      '<tr><td align="center"><input type="button" value="Only this instance" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'del\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="All events" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'delall\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="All following" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'delallfuture\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'cancel\'' +
                      ')"/></td></tr></table>';
    showWindow(htmlcontent);
}

function showAddWindow(date, index) {
    date = parseInt(date);
    var htmlcontent = '<table width="100%"><tr><td align="center"><b>Would you like to add a new survey date?<br></b></td></tr>' +
                      '<tr><td>&nbsp;</td></tr>' +
                      '<tr><td align="center"><input type="button" value="Yes" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'add\'' +
                      ')"/>&nbsp;&nbsp;&nbsp;<input type="button" value="Cancel" onclick="parent.addRemoveSchedule(\'' + index + '\',\'' + date + '\',\'cancel\'' +
                      ')"/></td></tr></table>';
    showWindow(htmlcontent);
}


function selectDate(obj, text, index) {
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
