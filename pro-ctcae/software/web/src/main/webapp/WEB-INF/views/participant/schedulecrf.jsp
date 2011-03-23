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
<tags:javascriptLink name="yui"/>
<script type="text/javascript">
function addRemoveSchedule(index, date, action) {
    var forms = document.getElementsByName("selectedForms")
    var fids = '';
    if (forms.length == 1) {
        fids = forms[0].value + ',';
    } else {
        for (var i = 0; i < forms.length; i++) {
            if (forms[i].checked) {
                fids += forms[i].value + ',';
            }
        }
    }
    if (fids == '' && action != 'cancel') {
        alert('Please select at least one form');
        return;
    }
    closeWindow();
    if (action == 'cancel') {
        getCalendar(index, "dir=refresh");
    } else {
        var request = new Ajax.Request("<c:url value="/pages/participant/addCrfSchedule"/>", {
            onComplete:function(transport) {

                if (transport.responseText == "getCalendar") {
                    getCalendar(index, "dir=refresh");
                } else {
                    showConfirmationWindow(transport, 650, 210);
                }
            },
            parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&action=" + action + "&fids=" + fids,
            method:'get'
        })
    }
}

function addRemoveValidationSchedule(index, date, action) {
    var forms = document.getElementsByName("selectedForms")
    var fids = '';
    if (forms.length == 1) {
        fids = forms[0].value + ',';
    } else {
        for (var i = 0; i < forms.length; i++) {
            if (forms[i].checked) {
                fids += forms[i].value + ',';
            }
        }
    }
    if (fids == '' && action != 'cancel') {
        alert('Please select at least one form');
        return;
    }
    closeWindow();
    if (action == 'cancel') {
        getCalendar(index, "dir=refresh");
    } else {
        var request = new Ajax.Request("<c:url value="/pages/participant/moveFormScheduleValidate"/>", {
            onComplete:function(transport) {

                if (transport.responseText == "getCalendar") {
                    addRemoveSchedule(index, date, action);
                } else {
                    showConfirmationWindow(transport, 650, 210);
                }


            },
            parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&action=" + action + "&fids=" + fids,
            method:'get'
        })
    }
}

function showMoveWindow(olddate, newdate, index, sids) {
    if (typeof(sids) == 'undefined') {
        sids = getScheduleIdsForDay(index, olddate);
    }
    var request = new Ajax.Request("<c:url value="/pages/participant/moveFormSchedule"/>", {
        parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&olddate=" + olddate + "&newdate=" + newdate + "&sids=" + sids,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 650, 210);
            AE.registerCalendarPopups();
        },
        method:'get'
    })

}
function showDeleteWindow(date, index, sids) {
    var request = new Ajax.Request("<c:url value="/pages/participant/deleteFormSchedule"/>", {
        onComplete:function(transport) {
            showConfirmationWindow(transport, 650, 180);
        },
        parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&sids=" + sids,
        method:'get'
    })
}
function showDetailsWindow(date, index, sids) {
    var request = new Ajax.Request("<c:url value="/pages/participant/detailsFormSchedule"/>", {
        onComplete:function(transport) {
            showConfirmationWindow(transport, 650, 300);
        },
        parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&sids=" + sids,
        method:'get'
    })
}

function showAddWindow(date, index, sids) {

    var request = new Ajax.Request("<c:url value="/pages/participant/addFormSchedule"/>", {
        onComplete:function(transport) {
            showConfirmationWindow(transport, 650, 210);
        },
        parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&sids=" + sids,
        method:'get'
    })
}

function showUpdateStartDateWindow(spcrfid) {
    var request = new Ajax.Request("<c:url value="/pages/participant/updateStudyParticipantCrfStartDate"/>", {
        parameters:<tags:ajaxstandardparams/>+"&spcrfid=" + spcrfid,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 650, 280);
            AE.registerCalendarPopups();
        },
        method:'get'
    })

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

function participantOnHold(id, date, index) {
    var request = new Ajax.Request("<c:url value="/pages/participant/participantOnHold"/>", {
        parameters:<tags:ajaxstandardparams/>+"&flow=schedulecrf&id=" + id + "&date=" + date + "&index=" + index,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 600, 250);
        },
        method:'get'
    })
}

var _winOffHold;
function participantOffHold(id, date, index) {
    var url = "<c:url value="/pages/participant/participantOffHold"/>" + "?flow=schedulecrf&id=" + id + "&date=" + date + "&index=" + index + "&subview=x";
    _winOffHold = showModalWindow(url, 600, 350);
<%--var request = new Ajax.Request("<c:url value="/pages/participant/participantOffHold"/>", {--%>
<%--parameters:<tags:ajaxstandardparams/>+"&id=" + id + "&date=" + date + "&index=" + index,--%>
    //            onComplete:function(transport) {
    //                showConfirmationWindow(transport, 600, 350);
    //            },
    //            method:'get'
    //        })
}

function showHideCycleDay(value) {
    if (value) {
        jQuery('#cycle_day').show();
    } else {
        jQuery('#cycle_day').hide();
    }
}

function validateAndSubmit(date, form) {
    if (date == '') {
        alert('Please provide a valid date');
        return;
    }
    form.submit();
}

function showPopUpMenuSchedule(date, index, sid, showDeleteOption) {
    var html = '';
    var menuindex = date;
    var holdDate = date;
    if (sid == null) {
        html = '<div id="search-engines"><ul>';

        if (${command.studyParticipantAssignment.onHoldTreatmentDate eq null}) {
            html += '<li><a href="#" onclick="javascript:showAddWindow(' + date + ', ' + index + ');">Schedule form</a></li>';
            html += '<li><a href="#" onclick="javascript:participantOnHold(' + ${command.studyParticipantAssignment.id} + ', ' + holdDate + ', ' + index + ');">Treatment on hold</a></li>';
        } else {
            html += '<li><a href="#" onclick="javascript:participantOffHold(' + ${command.studyParticipantAssignment.id} + ', ' + holdDate + ', ' + index + ');">Remove hold</a></li>';
        }

        html += '</ul></div>';
    } else {
        //TODO:Suneel A needs to clean up commented line after issue resolved
        //menuindex = sid;
        var html = '<div id="search-engines"><ul>';
        html += '<li><a href="#" onclick="javascript:showDetailsWindow(' + date + ', ' + index + ', \'' + sid + '\');">Show details</a></li>';
        if (${command.studyParticipantAssignment.onHoldTreatmentDate eq null}) {
            if (${crfsSize>1}) {
                html += '<li><a href="#" onclick="javascript:showAddWindow(' + date + ', ' + index + ', \'' + sid + '\');">Schedule form</a></li>';
            }
            if (showDeleteOption) {
                html += '<li><a href="#" onclick="javascript:showDeleteWindow(' + date + ', ' + index + ', \'' + sid + '\');">Delete form</a></li>';
                html += '<li><a href="#" onclick="javascript:showMoveWindow(' + date + ', ' + date + ', ' + index + ', \'' + sid + '\');">Move form to other date</a></li>';
            }
            html += '<li><a href="#" onclick="javascript:participantOnHold(' + ${command.studyParticipantAssignment.id} + ', ' + holdDate + ', ' + index + ');">Treatment on hold</a></li>';
            var split = sid.split('_');
            for (var a = 0; a < split.length; a++) {
                var scheduleid = split[a];
                if (scheduleid != '') {
                    var formName = forms[index][scheduleid];
                    html += '<li><hr></li>';
                    html += '<li><a href="#" onclick="location.href=\'printSchedule?id=' + scheduleid + '\'">Print form (' + formName + ')</a></li>';
                    html += '<li><a href="#" onclick="location.href=\'enterResponses?id=' + scheduleid + '\'">Enter responses (' + formName + ')</a></li>';
                }
            }
        } else {
            var newHoldDate = -1;
            <c:if test="${command.studyParticipantAssignment.onHoldTreatmentDate ne null}">
               newHoldDate = ${command.studyParticipantAssignment.onHoldTreatmentDate.date};
            </c:if>
                if (newHoldDate > holdDate) {
                    if (${crfsSize>1}) {
                        html += '<li><a href="#" onclick="javascript:showAddWindow(' + date + ', ' + index + ', \'' + sid + '\');">Schedule form</a></li>';
                    }
                    if (showDeleteOption) {
                        html += '<li><a href="#" onclick="javascript:showDeleteWindow(' + date + ', ' + index + ', \'' + sid + '\');">Delete form</a></li>';
                        html += '<li><a href="#" onclick="javascript:showMoveWindow(' + date + ', ' + date + ', ' + index + ', \'' + sid + '\');">Move form to other date</a></li>';
                    }
                    var split = sid.split('_');
                    for (var a = 0; a < split.length; a++) {
                        var scheduleid = split[a];
                        if (scheduleid != '') {
                            var formName = forms[index][scheduleid];
                            html += '<li><hr></li>';
                            html += '<li><a href="#" onclick="location.href=\'printSchedule?id=' + scheduleid + '\'">Print form (' + formName + ')</a></li>';
                            html += '<li><a href="#" onclick="location.href=\'enterResponses?id=' + scheduleid + '\'">Enter responses (' + formName + ')</a></li>';
                        }
                    }
                }
                html += '<li><a href="#" onclick="javascript:participantOffHold(' + ${command.studyParticipantAssignment.id} + ', ' + holdDate + ', ' + index + ');">Remove hold</a></li>';
        }

        html += '</ul></div>';
    }
    jQuery('#scheduleActions' + menuindex).menu({
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
            <div class="label_nomargin"><b>Participant:</b></div>
        </td>
        <td>
            <div class="value_nomargin">${command.participant.displayName}</div>
        </td>
    </tr>
    <tr>
        <td VALIGN="top">
            <div class="label_nomargin"><b>Instructions:</b></div>
        </td>
        <td>
            <div class="value_nomargin"><spring:message code="participant.schedule.crf"/></div>
        </td>
    </tr>

</table>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" formName="myForm">
    <jsp:attribute name="singleFields">
        <input type="hidden" name="_finish" value="true"/>
            <c:forEach items="${command.participantSchedules}" var="participantSchedule" varStatus="status">
                <chrome:division title="" message="false">
                    <div align="left">
                        <table class="top-widget" cellspacing="0" align="center">
                            <tr>
                                <td>
                                    <chrome:division title=" "/>
                                    <div id="calendar_${status.index}_outer">
                                        <div id="calendar_${status.index}_inner"></div>
                                        <tags:participantcalendar schedule="${participantSchedule}"
                                                                  studyParticipantAssignment="${command.studyParticipantAssignment}"
                                                                  index="0"/>
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
                <c:forEach items="${participantSchedule.studyParticipantCrfs}" var="participantCrf"
                           varStatus="crfIndex">
                    <table class="top-widget" cellspacing="0" align="center">
                        <tr>
                            <td>
                                <b>${participantCrf.crf.title} (${participantCrf.crf.crfVersion})</b>
                                <b>Start date: <tags:formatDate value="${participantCrf.startDate}"/> </b>&nbsp;
                            </td>
                        </tr>
                        <c:forEach items="${participantCrf.crfCycleDefinitions}" var="crfCycleDefinition"
                                   varStatus="statuscycledefinition">
                            <tr>
                                <td>
                                    <tags:formScheduleCycleDefinition
                                            cycleDefinitionIndex="${statuscycledefinition.index}"
                                            crfCycleDefinition="${crfCycleDefinition}"
                                            readonly="true"
                                            crfIndex="${status.index}_${crfIndex.index}"/>
                                    <script type="text/javascript">
                                        showCyclesForDefinition('${status.index}_${crfIndex.index}_${statuscycledefinition.index}', ${crfCycleDefinition.cycleLength}, '${crfCycleDefinition.cycleLengthUnit}', '${crfCycleDefinition.repeatTimes}');
                                    </script>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    <br/>
                </c:forEach>

            </c:forEach>
</jsp:attribute>
</tags:tabForm>
</body>
</html>