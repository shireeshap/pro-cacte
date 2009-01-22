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
            border-bottom: 1px solid #cccccc;
            border-right: 1px solid #cccccc;
            border-left: 1px solid #cccccc;
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

        .passive {
            height: 50px;
        }

    </style>
    <script type="text/javascript">

        function applyCalendar(index, scheduleid, direction) {
            var duea = document.getElementsByName('dueDateAmount_' + index)[0].value;
            var dueu = document.getElementsByName('dueDateUnit_' + index)[0].value;
            var reppu = document.getElementsByName('repetitionPeriodUnit_' + index)[0].value;
            var reppa = document.getElementsByName('repetitionPeriodAmount_' + index)[0].value;
            var repuu = document.getElementsByName('repeatUntilUnit_' + index)[0].value;
            var repuv = document.getElementsByName('repeatUntilValue_' + index)[0].value;
            var sdate = document.getElementsByName('startDate_' + index)[0].value;

            var request = new Ajax.Request("<c:url value="/pages/participant/displaycalendar"/>", {
                onComplete:function(transport) {
                    var response = transport.responseText;
                    $("calendar_" + index).innerHTML = response;
                    initializeCalendar(scheduleid);
                },
                parameters:"subview=subview&index=" + index + "&duea=" + duea + "&dueu=" + dueu + "&reppu=" + reppu + "&reppa=" + reppa + "&repuu=" + repuu + "&repuv=" + repuv + "&sdate=" + sdate + "&dir=" + direction,
                method:'get'
            })

        }

        function initializeCalendar(id) {
            var items = document.getElementsByName(id + '_schedule_div');
            for (var i = 0; i < items.length; i++) {
                try {
                    Droppables.add(id + '_schedule_' + (i + 1), {hoverclass: 'hoverActive', onDrop: moveItem});
                } catch(err) {
                }
            }

            items = document.getElementsByName(id + '_temp_div');
            var j = items.length;
            while (j > 0) {
                var item = items[0];
                var date = item.id.substring(item.id.indexOf('_', 2) + 1);
                item.addClassName('blue');
                new Draggable(item, {revert:true});
                moveItem(item, $(id + '_schedule_' + date));
                j--;
            }
        }

        function moveItem(draggable, droparea) {
            droparea.innerHTML = '';
            draggable.parentNode.removeChild(draggable);
            droparea.appendChild(draggable);
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
                    <jsp:useBean id="today" class="java.util.Date"/>
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
                                    <input value="01/21/2009" title="start date" type="text"
                                           name="startDate_${status.index}"><a href="#"
                                                                               id="${title}-calbutton">
                                    <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17"
                                         height="16" border="0"
                                         align="absmiddle"/>
                                </a>
                                    <i>(mm/dd/yyyy)</i>
                                </td>
                                <td class="border-td">
                                    <select class="" id="dd">
                                        <option value="0">No</option>
                                        <option value="1" selected="selected">Yes</option>
                                    </select>
                                </td>
                                <td class="border-td">
                                </td>
                                <td class="border-td">
                                </td>
                            </tr>

                            <tr>
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
                                            <select name="repeatUntilUnit_${status.index}">
                                                <option value="Number">Number of repititions</option>
                                                <option value="Indefinitely">Indefinitely</option>
                                                <option value="Date">Date</option>
                                            </select>
                                            <input size="3" value="5"
                                                   name="repeatUntilValue_${status.index}"
                                                   type="text">&nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="button" value="Apply"
                                                   onclick="applyCalendar('${status.index}','${participantSchedule.studyParticipantCrf.id}','');"/>

                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4">
                                    <a href="javascript:applyCalendar('${status.index}','${participantSchedule.studyParticipantCrf.id}','prev');">
                                        &lt;</a>&nbsp;&nbsp;<a
                                        href="javascript:applyCalendar('${status.index}','${participantSchedule.studyParticipantCrf.id}','next');">
                                    &gt;</a>
                                    <div id="calendar_${status.index}">
                                        <tags:participantcalendar schedule="${participantSchedule}"/>
                                        <script type="text/javascript">
                                            initializeCalendar('${participantSchedule.studyParticipantCrf.id}');
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