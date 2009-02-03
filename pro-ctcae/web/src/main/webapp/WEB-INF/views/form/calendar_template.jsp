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
<script type="text/javascript">
</script>
<style type="text/css">

    table.top-widget {
        border: 1px solid #cccccc;
        table-layout: fixed;
        background-color: #E7EAF3;
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
        color: #0e0094;
        text-align: center;
        height: 30px;
    }

    td.header {
        border-bottom: 1px solid #77a9ff;
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
        color: #666666;
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
    }

    img.navBack {
        background-image: url( /ctcae/images/combined_v5.gif );
        background-position: -148px -17px;
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
</style>
<%--<script type="text/javascript">--%>

<%--function applyCalendar(index, direction) {--%>
<%--document.body.style.cursor = 'wait';--%>

<%--var duea = document.getElementsByName('dueDateAmount_' + index)[0].value;--%>
<%--var dueu = document.getElementsByName('dueDateUnit_' + index)[0].value;--%>
<%--var reppu = document.getElementsByName('repetitionPeriodUnit_' + index)[0].value;--%>
<%--var reppa = document.getElementsByName('repetitionPeriodAmount_' + index)[0].value;--%>
<%--var repuu = document.getElementsByName('repeatUntilUnit_' + index)[0].value;--%>
<%--var repuv = document.getElementsByName('repeatUntilValue_' + index)[0].value;--%>
<%--var sdate = document.getElementsByName('startDate_' + index)[0].value;--%>

<%--getCalendar(index, "duea=" + duea + "&dueu=" + dueu + "&reppu=" + reppu + "&reppa=" + reppa + "&repuu=" + repuu + "&repuv=" + repuv + "&sdate=" + sdate + "&dir=" + direction);--%>
<%--}--%>

<%--function addRemoveSchedule(index, date, action) {--%>

<%--var request = new Ajax.Request("<c:url value="/pages/participant/addCrfSchedule"/>", {--%>
<%--onComplete:function(transport) {--%>
<%--getCalendar(index, "dir=refresh");--%>
<%--},--%>
<%--parameters:"subview=subview&index=" + index + "&date=" + date + "&action=" + action,--%>
<%--method:'get'--%>
<%--})--%>
<%--}--%>

<%--function getCalendar(index, parameters) {--%>
<%--document.body.style.cursor = 'wait';--%>
<%--var request = new Ajax.Request("<c:url value="/pages/form/displaycalendar"/>", {--%>
<%--onComplete:function(transport) {--%>
<%--var response = transport.responseText;--%>
<%--$("calendar_" + index).innerHTML = response;--%>
<%--initializeCalendar(index);--%>
<%--},--%>
<%--parameters:"subview=subview&index=" + index + "&" + parameters,--%>
<%--method:'get'--%>
<%--})--%>
<%--}--%>

<%--var checkStatus = true;--%>
<%--function initializeCalendar(id) {--%>
<%--var items = document.getElementsByName(id + '_schedule_div');--%>
<%--for (var i = 0; i < items.length; i++) {--%>
<%--try {--%>
<%--Droppables.add(id + '_schedule_' + (i + 1), {hoverclass: 'hoverActive', onDrop: moveItem});--%>
<%--} catch(err) {--%>
<%--}--%>
<%--}--%>

<%--items = document.getElementsByName(id + '_temp_div');--%>
<%--var j = items.length;--%>
<%--while (j > 0) {--%>
<%--var item = items[0];--%>
<%--var date = item.id.substring(item.id.indexOf('_', 2) + 1);--%>
<%--item.addClassName('blue');--%>
<%--if (item.innerHTML.indexOf('In-progress') != -1) {--%>
<%--item.style.background = '#ff9900';--%>
<%--}--%>
<%--if (item.innerHTML.indexOf('Completed') != -1) {--%>
<%--item.style.background = '#00cc00';--%>
<%--}--%>
<%--new Draggable(item, {revert:true});--%>
<%--checkStatus = false;--%>
<%--moveItem(item, $(id + '_schedule_' + date));--%>
<%--j--;--%>
<%--}--%>
<%--document.body.style.cursor = 'default';--%>
<%--}--%>


<%--function moveItem(draggable, droparea) {--%>
<%--if (checkStatus) {--%>
<%--if (draggable.innerHTML.indexOf('Scheduled') == -1) {--%>
<%--return;--%>
<%--}--%>
<%--}--%>
<%--checkStatus = true;--%>
<%--droparea.innerHTML = '';--%>
<%--draggable.parentNode.removeChild(draggable);--%>
<%--droparea.appendChild(draggable);--%>
<%--var olddate = draggable.id.substring(draggable.id.indexOf('_', 2) + 1);--%>
<%--if (olddate != '') {--%>
<%--var newdate = droparea.id.substring(droparea.id.indexOf('_', 2) + 1);--%>
<%--var index = draggable.id.substring(0, draggable.id.indexOf('_'));--%>
<%--droparea.addClassName('blue');--%>
<%--if (droparea.id != (index + '_schedule_' + olddate)) {--%>
<%--$(index + '_schedule_' + olddate).removeClassName('blue');--%>
<%--}--%>
<%--if (newdate != olddate) {--%>
<%--addRemoveSchedule(index, newdate + ',' + olddate, 'add,del');--%>
<%--}--%>
<%--}--%>
<%--}--%>

<%--function selectDate(obj, text, index) {--%>
<%--document.body.style.cursor = 'wait';--%>
<%--var myclasses = obj.classNames().toString();--%>
<%--var date = obj.id.substring(obj.id.indexOf('_', 2) + 1);--%>
<%--if (myclasses.indexOf('blue') == -1) {--%>
<%--addRemoveSchedule(index, date, 'add');--%>
<%--} else {--%>
<%--addRemoveSchedule(index, date, 'del');--%>
<%--}--%>
<%--}--%>

<%--</script>--%>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" formName="myForm">
<jsp:attribute name="singleFields">
<input type="hidden" name="_finish" value="true"/>
<jsp:useBean id="today" class="java.util.Date"/>
<div align="left" style="margin-left: 50px">
    <table class="top-widget" cellspacing="0">
        <tr>
            <td colspan="2">
                <div class="row">
                    <div class="label">
                        Repeat every
                    </div>
                    <div class="value">
                        <input type="text" size="2"
                               name="repetitionPeriodAmount"
                               value="2"/>
                        <select name="repetitionPeriodUnit">
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
                               name="dueDateAmount"
                               value="24"/>
                        <select name="dueDateUnit">
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
                        <select name="repeatUntilUnit">
                            <option value="Number">Number of repititions</option>
                            <option value="Indefinitely">Indefinitely</option>
                            <option value="Date">Date</option>
                        </select>
                        <input size="3" value="5"
                               name="repeatUntilValue"
                               type="text">&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="button" value="Apply"
                               onclick="applyCalendar('${status.index}','');"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <div id="calendar">
                    <%--<tags:participantcalendar schedule="${participantSchedule}"--%>
                                              <%--index="${status.index}"/>--%>
                    <%--<script type="text/javascript">--%>
                        <%--initializeCalendar('${status.index}');--%>
                    <%--</script>--%>
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
</jsp:attribute>
</tags:tabForm>
</body>
</html>