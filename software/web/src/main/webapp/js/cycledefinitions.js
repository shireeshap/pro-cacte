function applyCalendar(index, direction) {
    getCalendar(index, "dir=" + direction);
}
var calendarArr = new Array();
var scheduleArr = new Array();
var forms = new Array();


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
function getScheduleIdsForDay(index, day) {
    var mySchedules = scheduleArr[index];
    var myschedule = mySchedules[day];
    var scheduleid = '';
    if (isdefined(myschedule)) {
        if (myschedule.length > 1) {
            for (var a = 0; a < myschedule.length; a++) {
                scheduleid += myschedule[a][3] + '_';
            }
        } else {
            myschedule = myschedule[0];
            scheduleid = myschedule[3];
        }
        return scheduleid;
    }
}
function initializeCalendar(index) {
    initialize();
    var myCalendar = calendarArr[index];
    var mySchedules = scheduleArr[index];
//    var item = $(div_id);
    for (var day = 0; day < myCalendar.length; day++) {
//        var item = $(div_id);
        if (isdefined(myCalendar[day])) {
            var div_id = index + '_schedule_' + day;
            var myschedule = mySchedules[day];
            var showdropdown = false;
            var showDeleteOption = true;
            var isEnableDrag = true;
            var item = $(div_id);
            if (isdefined(myschedule)) {
                var scheduleid = '';
                var item = $(div_id);
                item.addClassName('blue');
                item.removeClassName('passive');
                item.style.cursor = 'default';
                if (myschedule.length >1) {
                    var baseline = false;
                    item.style.background = 'green';
                    var title = '';
                    var allCompleted = true;
                    var allInprogress = true;
                    var allNa = true;
                    var hasPastDue = false;
                    var onHold = false;
                    for (var a = 0; a < myschedule.length; a++) {
                        scheduleid += myschedule[a][3] + '_';
                        title = title + forms[index][myschedule[a][3]];
                        var status = myschedule[a][0];
                        if (status == 'Past-due') {
                            hasPastDue = true;
                        }
                        if (status != 'Completed') {
                            allCompleted = false;
                        }
                        if (status != 'In-progress') {
                            allInprogress = false;
                        }
                        if (status != 'N/A') {
                            allNa = false;
                        }
                        if (status == 'On-hold') {
                            item.style.background = 'yellow';
                            item.style.color = "black";
                            onHold = true;
                        }
                        if (status == 'N/A') {
                            item.style.background = 'lightgrey';
                            item.style.color = "black";
                        }

                        if (a != myschedule.length - 1) {
                            title = title + ', '
                        }
                    }
                    item.innerHTML = '<br/>Multiple forms<br/>';
                    if (allCompleted) {
                        item.style.background = '#00cc00';
                        item.innerHTML = '<br/>Multiple forms<br/>(Completed)';
                        showDeleteOption = false;
                        isEnableDrag = false;
                    }
                    if (allInprogress) {
                        item.style.background = '#ff9900';
                        item.innerHTML = '<br/>Multiple forms<br/>(In-progress)';
                        showDeleteOption = false;
                        isEnableDrag = false;
                    }
                    if (allNa) {
                        item.style.background = 'lightgrey';
                        item.innerHTML = '<br/>Multiple forms<br/>(N/A)';
                        //showDeleteOption = false;
                       // isEnableDrag = false;
                        showdropdown = false;
                    }
                    if (hasPastDue) {
                        item.style.background = 'red';
                        item.innerHTML = '<br/>Multiple forms<br/>(Past-due)';
                    }
                    if (onHold) {
                        item.innerHTML = '<br/>Multiple forms<br/>(On-hold)';
                    }
                    if(!allNa){
                        showdropdown = true;
                    }
                    item.title = title;

                } else {
                    myschedule = myschedule[0];
                    var status = myschedule[0];
                    var baseline = myschedule[1];
                    var holiday = myschedule[2];
                    scheduleid = myschedule[3];
                    item.innerHTML = '<br/>' + status;
                    item.title = forms[index][scheduleid];
                    if (baseline == 'true') {
                        item.innerHTML = item.innerHTML + '<br/>(baseline)';
                    }
                    if (status == 'In-progress') {
                        item.style.background = '#ff9900';
                        showDeleteOption = false;
                    }
                    if (status == 'Completed') {
                        item.style.background = '#00cc00';
                        showDeleteOption = false;
                        isEnableDrag = false;
                    }
                    if (status == 'Past-due') {
                        item.style.background = 'red';
                        showDeleteOption = false;
                    }
                    if (status == 'On-hold') {
                        item.style.background = 'yellow';
                        item.style.color = 'black';
                    }
                    if (status == 'Cancelled') {
                        item.style.background = 'lightgrey';
                    }

                    if (status == 'N/A') {
                        item.style.background = 'lightgrey';
                    }

                    if (status == 'Scheduled' || status == 'Past-due' || status == 'In-progress' || status == 'On-hold' || status=='Completed') {
                        if (holiday == 'true') {
//                            item.style.background = 'blue';
                            item.addClassName('blue');
                        }
                        item.style.cursor = 'pointer';
                        showdropdown = true;
                    }
                }
                if (showdropdown) {
                    var delIcon = '<div style="float:right">' +
                                  '<a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="scheduleActions' + day + '">' +
                                  '<span class="ui-icon ui-icon-triangle-1-s"></span></a>' +
                                  '</div>';
                    item.innerHTML = delIcon + item.innerHTML;
                    showPopUpMenuSchedule(day, index, scheduleid,showDeleteOption);
                    if(isEnableDrag){
                        myCalendar[day] = new YAHOO.example.DDPlayer(div_id, 'date');
                    }

                }
                 myCalendar[day] = new YAHOO.util.DDTarget(div_id, 'date');

            } else {
                myCalendar[day] = new YAHOO.util.DDTarget(div_id, 'date');
                var delIcon = '<div style="float:right">' +
                                  '<a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="scheduleActions' + day + '">' +
                                  '<span class="ui-icon ui-icon-triangle-1-s"></span></a>' +
                                  '</div>';
                    item.innerHTML = delIcon + item.innerHTML;
                
                showPopUpMenuSchedule(day, index, null,true);
//                Event.observe(div_id, "click", function() {
//                    showAddWindow(getDate(this), getIndex(this));
//                })
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
