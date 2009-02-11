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
        border-bottom: 1px solid #cccccc;
        border-right: 1px solid #cccccc;
        border-left: 1px solid #cccccc;
        background-color: #E7EAF3;
        width: 90%;
    }

    tr.top-header {
        background-color: #cccccc;
        height: 30px;
    }

    table.widget {                                                a
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
        font-size:small;
        font-weight:bold;
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
        height: 20px;
        /*color: #666666;*/
        color: #414141;
        font-size:small;
        font-weight:bold;
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
        background-image: url( /ctcae/images/combined_v5.gif );
        background-position: -148px -17px;
        padding-left: 2px;
        padding-right: 0;
        vertical-align: middle;
    }

    img.removebutton {
        background-image: url( /ctcae/images/combined_v5.gif );
        background-position: -0px -50px;
        padding-left: 2px;
        padding-right: 0;
        vertical-align: middle;
    }

    img.navForward {
        background-image: url( /ctcae/images/combined_v5.gif );
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

</style>
<script type="text/javascript">


function applyCalendar(index, direction) {
    document.body.style.cursor = 'wait';

    var duea = document.getElementsByName('dueDateAmount_' + index)[0].value;
    var dueu = document.getElementsByName('dueDateUnit_' + index)[0].value;
    var reppu = document.getElementsByName('repetitionPeriodUnit_' + index)[0].value;
    var reppa = document.getElementsByName('repetitionPeriodAmount_' + index)[0].value;
    var repuu = document.getElementsByName('repeatUntilUnit_' + index)[0].value;
    var repuv = document.getElementsByName('repeatUntilValue_' + index)[0].value;
    var sdate = document.getElementsByName('startDate_' + index)[0].value;

    getCalendar(index, "duea=" + duea + "&dueu=" + dueu + "&reppu=" + reppu + "&reppa=" + reppa + "&repuu=" + repuu + "&repuv=" + repuv + "&sdate=" + sdate + "&dir=" + direction);
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

function changeinput(obj, index) {
    if (obj.value == 'Indefinitely') {
        $('div_repeatUntilValue_' + index).innerHTML = '';
    }
    if (obj.value == 'Date') {
        $('div_repeatUntilValue_' + index).innerHTML = '<input size="10" name="repeatUntilValue_' + index + '" type="text" ><a href="#" id="${title}-calbutton"> <img src="/ctcae/images/chrome/b-calendar.gif" alt="Calendar" width="17" height="16" border="0" align="absmiddle"/>';
    }
    if (obj.value == 'Number') {
        $('div_repeatUntilValue_' + index).innerHTML = '<input size="3" value="5" name="repeatUntilValue_' + index + '" type="text">';
    }
}

function changerepeat(obj, index, delall) {
    if (obj.value == 'Yes') {
        $('repeatprops_' + index).show();
        $('calendar_' + index).show();
        $('duedate_' + index).hide();
        $('status_' + index).hide();
    }
    if (obj.value == 'No') {
        $('repeatprops_' + index).hide();
        $('calendar_' + index).hide();
        $('duedate_' + index).show();
        $('status_' + index).show();
        if (delall) {
            addRemoveSchedule(index, '', 'delall')
        }
    }
}

Event.observe(window, "load", function () {
    var items = document.getElementsByName("repeatdropdown");
    for (var i = 0; i < items.length; i++) {
        changerepeat(items[i], items[i].id, false);
    }
})


</script>

</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" formName="myForm">
    <jsp:attribute name="singleFields">
        
        <input type="hidden" name="_finish" value="true"/>

            <c:forEach items="${command.participantSchedules}" var="participantSchedule" varStatus="status">
                <c:set var="participantCrf" value="${participantSchedule.studyParticipantCrf}"/>
                <chrome:division title="${participantCrf.crf.title} (${participantCrf.crf.crfVersion})" message="false">

                    <div align="left" style="margin-left: 50px">
                        <table class="top-widget" cellspacing="0">
                            <tr class="top-header">
                                <th><tags:requiredIndicator/><spring:message code="schedulecrf.label.start_date"/></th>
                                <th>Repeat</th>
                                <th><tags:requiredIndicator/><spring:message code="schedulecrf.label.due_date"/></th>
                                <th><spring:message code="schedulecrf.label.status"/></th>
                            </tr>
                            <tr>
                                <td class="border-td">
                                    <input value="<tags:formatDate value='${participantSchedule.calendar.startDate}'/>"
                                           title="start date" type="text"
                                           name="startDate_${status.index}"><a href="#"
                                                                               id="${title}-calbutton">
                                    <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17"
                                         height="16" border="0"
                                         align="absmiddle"/>
                                </a>

                                </td>
                                <td class="border-td">
                                    <select class="" onchange="changerepeat(this, '${status.index}',true)"
                                            name="repeatdropdown" id="${status.index}">
                                        <c:choose>
                                            <c:when test="${participantSchedule.repeat eq 'true'}">
                                                <option value="No">No</option>
                                                <option value="Yes" selected="selected">Yes</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="No" selected="selected">No</option>
                                                <option value="Yes">Yes</option>
                                            </c:otherwise>

                                        </c:choose>
                                    </select>
                                </td>
                                <td class="border-td">
                                    <div id="duedate_${status.index}">
                                        <input value="<tags:formatDate
                                                value='${participantSchedule.studyParticipantCrf.studyParticipantCrfSchedules[0].dueDate}'/>"
                                               title="due date" type="text"
                                               name="dueDate_${status.index}"><a href="#"
                                                                                 id="${title}-calbutton">
                                        <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17"
                                             height="16" border="0"
                                             align="absmiddle"/>
                                    </a>
                                    </div>
                                </td>
                                <td class="border-td">
                                    <div id="status_${status.index}">
                                        <c:if test="${participantSchedule.repeat eq 'false'}">
                                            ${participantSchedule.studyParticipantCrf.studyParticipantCrfSchedules[0].status}
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                            <tr id="repeatprops_${status.index}">
                                <td colspan="2">
                                    <div class="row">
                                        <div class="label">
                                            Repeat every
                                        </div>
                                        <div class="value">
                                            <input type="text" size="2"
                                                   name="repetitionPeriodAmount_${status.index}"
                                                   value="2"/>
                                            <select name="repetitionPeriodUnit_${status.index}">
                                                <option value="Days">Days</option>
                                                <option value="Weeks">Weeks</option>
                                                <option value="Months">Months</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <br/><br/>
                                    </div>
                                </td>
                                <td colspan="2">
                                    <div class="row">
                                        <div class="label">
                                            Form is due after
                                        </div>
                                        <div class="value">
                                            <input type="text" size="2"
                                                   name="dueDateAmount_${status.index}"
                                                   value="24"/>
                                            <select name="dueDateUnit_${status.index}">
                                                <option value="Hours">Hours</option>
                                                <option value="Days">Days</option>
                                                <option value="Weeks">Weeks</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="label">
                                            Repeat until
                                        </div>
                                        <div class="value">
                                            <table>
                                                <tr>
                                                    <td>
                                                        <select name="repeatUntilUnit_${status.index}"
                                                                onchange="changeinput(this,'${status.index}');">
                                                            <option value="Date">Date</option>
                                                            <option value="Number">Number of repititions</option>
                                                            <option value="Indefinitely">Indefinitely</option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <div id="div_repeatUntilValue_${status.index}"><input
                                                                size="10"
                                                                name="repeatUntilValue_${status.index}"
                                                                type="text"
                                                                value='<tags:formatDate value="${today}"/>'><a
                                                                href="#" id="${title}-calbutton"> <img
                                                                src="/ctcae/images/chrome/b-calendar.gif"
                                                                alt="Calendar"
                                                                width="17" height="16" border="0"
                                                                align="absmiddle"/>
                                                        </a>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <input type="button" value="Apply"
                                                               onclick="applyCalendar('${status.index}','');"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4">
                                    <br/>

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
                                <td colspan="4">
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