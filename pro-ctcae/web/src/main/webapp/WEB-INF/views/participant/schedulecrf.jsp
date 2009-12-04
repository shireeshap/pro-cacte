<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<html>
<head>
    <jsp:useBean id="today" class="java.util.Date"/>
    <c:set var="fDate" value='<tags:formatDate value="${today}"/>'/>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:stylesheetLink name="cycledefinitions"/>
    <tags:javascriptLink name="cycledefinitions"/>
    <script type="text/javascript"
            src="http://yui.yahooapis.com/combo?2.7.0/build/yahoo-dom-event/yahoo-dom-event.js&2.7.0/build/dragdrop/dragdrop-min.js"></script>
    <script type="text/javascript">
        function addRemoveSchedule(index, date, action) {
            closeWindow();
            if (action == 'cancel') {
                getCalendar(index, "dir=refresh");
            } else {
                var request = new Ajax.Request("<c:url value="/pages/participant/addCrfSchedule"/>", {
                    onComplete:function(transport) {
                        getCalendar(index, "dir=refresh");
                    },
                    parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&action=" + action,
                    method:'get'
                })
            }
        }

        function getCalendar(index, parameters) {
            var request = new Ajax.Request("<c:url value="/pages/participant/displaycalendar"/>", {
                onComplete:function(transport) {
                    showCalendar(index, transport);
                },
                parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&" + parameters,
                method:'get'
            })
        }

        function showCalendar(index, transport) {
            var items = $('calendar_' + index + '_outer').childElements();
            var len = items.length;
            for (var i = 0; i < len; i++) {
                if (items[i].id != 'calendar_' + index + '_inner') {
                    items[i].remove();
                }
            }
            new Insertion.After('calendar_' + index + '_inner', transport.responseText);
        }

        function showPopUpMenuSchedule(date, index, sid) {
            var html = '<div id="search-engines"><ul>';
            html += '<li><a href="#" onclick="javascript:showDeleteWindow(' + date + ', ' + index + ');">Delete form</a></li>';
            html += '<li><a href="#" onclick="javascript:showMoveWindow(' + date + ', ' + date + ', ' + index + ');">Move form to other date</a></li>';
            html += '<li><a href="#" onclick="location.href=\'printSchedule?id=' + sid + '\'">Print form</a></li>';
            html += '<li><a href="#" onclick="location.href=\'enterResponses?id=' + sid + '\'">Enter responses</a></li>';

            html += '</ul></div>';
            jQuery('#scheduleActions' + sid).menu({
                content: html,
                maxHeight: 180,
                positionOpts: {
                    directionV: 'down',
                    posX: 'left',
                    posY: 'bottom',
                    offsetX: 0,
                    offsetY: 0
                },
                showSpeed: 300
            });
        }


    </script>
</head>
<body>
<table>
    <tr>
        <td>
            <div class="label_nomargin"><b>Participant:</b>&nbsp;</div>
        </td>
        <td>
            <div class="value_nomargin">${command.participant.displayName}</div>
        </td>
    </tr>
    <tr>
        <td VALIGN="top">
            <div class="label_nomargin"><b>Instructions</b></div>
        </td>
        <td>
            <div class="value_nomargin"><spring:message code="participant.schedule.crf"/></div>
        </td>
    </tr>
</table>
<%--<div class="row">--%>
<%--<div class="label_nomargin"><spring:message code="schedulecrf.label.participant"/>:&nbsp;</div>--%>
<%--<div class="value_nomargin">${command.participant.displayName}</div>--%>
<%--</div>--%>
<%--<div class="row">--%>


<%--</div>--%>
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
                                    <div id="calendar_${status.index}_outer">
                                        <div id="calendar_${status.index}_inner"></div>
                                        <tags:participantcalendar schedule="${participantSchedule}"
                                                                  index="${status.index}"/>

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