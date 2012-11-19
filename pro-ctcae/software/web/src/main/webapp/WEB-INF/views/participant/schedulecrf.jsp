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
function addRemoveSchedule(index, date, action, pid) {
//    alert (1);
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
                    getCalendar(index, "dir=refresh", pid);
                } else {
                    showConfirmationWindow(transport, 650, 210);
                }
            },
            parameters:<tags:ajaxstandardparams/>+"&id=" +pid +"&index=" + index + "&date=" + date + "&action=" + action + "&fids=" + fids,
            method:'get'
        })
    }
}

function beginHoldOnSchedules(index, date, action, pid) {

    if (date == null || date == '') {
        alert('Please enter a date');
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
            parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&action=" + action + "&id=" + pid,
            method:'get'
        })
    }
}

function addRemoveValidationSchedule(index, date, action, pid) {
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
        getCalendar(index, "dir=refresh", pid);
    } else {
        var request = new Ajax.Request("<c:url value="/pages/participant/moveFormScheduleValidate"/>", {
            onComplete:function(transport) {

                if (transport.responseText == "getCalendar") {
                    addRemoveSchedule(index, date, action, pid);
                } else {
                    showConfirmationWindow(transport, 650, 210);
                }


            },
            parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&action=" + action + "&fids=" + fids + "&id=" + pid,
            method:'get'
        })
    }
}

function showMoveWindow(olddate, newdate, index, sids, pid) {
    if (typeof(sids) == 'undefined') {
        sids = getScheduleIdsForDay(index, olddate);
    }
    var request = new Ajax.Request("<c:url value="/pages/participant/moveFormSchedule"/>", {
        parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&olddate=" + olddate + "&newdate=" + newdate + "&sids=" + sids + "&id=" + pid,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 650, 210);
            AE.registerCalendarPopups();
        },
        method:'get'
    })

}
function showDeleteWindow(date, index, sids, pid) {
    var request = new Ajax.Request("<c:url value="/pages/participant/deleteFormSchedule"/>", {
        onComplete:function(transport) {
            showConfirmationWindow(transport, 650, 180);
        },
        parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&sids=" + sids + "&id=" + pid,
        method:'get'
    })
}
function showDetailsWindow(date, index, sids, pid) {
    var request = new Ajax.Request("<c:url value="/pages/participant/detailsFormSchedule"/>", {
        onComplete:function(transport) {
            showConfirmationWindow(transport, 650, 300);
        },
        parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&date=" + date + "&sids=" + sids + "&id=" + pid,
        method:'get'
    })
}

function showAddWindow(id, date, index, sids) {

    var request = new Ajax.Request("<c:url value="/pages/participant/addFormSchedule"/>", {
        onComplete:function(transport) {
            showConfirmationWindow(transport, 650, 210);
        },
        parameters:<tags:ajaxstandardparams/>+"&id=" + id + "&index=" + index + "&date=" + date + "&sids=" + sids,
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

function getCalendar(index, parameters, pid) {
    var request = new Ajax.Request("<c:url value="/pages/participant/displaycalendar"/>", {
        onComplete:function(transport) {
            showCalendar(index, transport);
        },
        parameters:<tags:ajaxstandardparams/>+"&id=" + pid +"&index=" + index + "&" + parameters,
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
<%--var url = "<c:url value="/pages/participant/participantOffHold"/>" + "?flow=schedulecrf&sid=" + id + "&date=" + date + "&index=" + index + "&subview=x";--%>
//    _winOffHold = showModalWindow(url, 600, 350);
    var request = new Ajax.Request("<c:url value="/pages/participant/participantOffHold"/>", {
        onComplete:function(transport) {
//            alert(22);
            if (transport.responseText == "getCalendar") {
                getCalendar(index, "dir=refresh");
            } else {
                showConfirmationWindow(transport, 650, 250);
            }
        },
        parameters:<tags:ajaxstandardparams/>+"&sid=" + id + "&date=" + date + "&index=" + index,
        method:'get'
    })
}
function participantOffHoldPost(index, date, cycle, day, action) {
    if (date == null || date == '') {
        alert('Please enter a date');
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
            parameters:<tags:ajaxstandardparams/> +"&index=" + index + "&offHoldDate=" + date + "&cycle=" + cycle  + "&day=" + day + "&action=" + action,
            method:'get'
        })
    }
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

function showPopUpMenuSchedule(date, currentMonth, currentYear, index, sid, showDeleteOption) {
    var html = '';
    var menuindex = date;
    var holdDate = date;
    var newHoldDate = -1;
    var newHoldMonth = -1;
    var newHoldYear = -1;
    <c:if test="${command.selectedStudyParticipantAssignment.onHoldTreatmentDate ne null}">
	    newHoldDate = ${command.selectedStudyParticipantAssignment.onHoldTreatmentDate.date};
	    newHoldMonth = ${command.selectedStudyParticipantAssignment.onHoldTreatmentDate.month} + 1;
	    newHoldYear = ${command.selectedStudyParticipantAssignment.onHoldTreatmentDate.year} + 1900;
	</c:if>

    if (sid == null) {
        html = '<div id="search-engines"><ul>';
        if (${command.selectedStudyParticipantAssignment.status.displayName ne 'OffStudy'}) {
            if (${command.selectedStudyParticipantAssignment.onHoldTreatmentDate eq null}) {
                html += '<li><a href="#" onclick="javascript:showAddWindow(' + ${command.selectedStudyParticipantAssignment.id} + ', ' + date + ', ' + index + ');">Schedule form</a></li>';
                html += '<li><a href="#" onclick="javascript:participantOnHold(' + ${command.selectedStudyParticipantAssignment.id} + ', ' + holdDate + ', ' + index + ');">Treatment on hold</a></li>';
            } else if(isHoldDateAfterCurrentDate(newHoldYear, currentYear, newHoldMonth, currentMonth, newHoldDate, holdDate)){
            	html += '<li><a href="#" onclick="javascript:showAddWindow(' + ${command.selectedStudyParticipantAssignment.id} + ', ' + date + ', ' + index + ');">Schedule form</a></li>';
            } else {
                html += '<li><a href="#" onclick="javascript:participantOffHold(' + ${command.selectedStudyParticipantAssignment.id} + ', ' + holdDate + ', ' + index + ');">Remove hold</a></li>';
            }
        }
        html += '</ul></div>';
    } else {
        //TODO:Suneel A needs to clean up commented line after issue resolved
        //menuindex = sid;
        var html = '<div id="search-engines"><ul>';
        html += '<li><a href="#" onclick="javascript:showDetailsWindow(' + date + ', ' + index + ', \'' + sid + '\', ' + ${command.selectedStudyParticipantAssignment.id} + ');">Show details</a></li>';
        if (${command.selectedStudyParticipantAssignment.status.displayName ne 'OffStudy'}) {
            if (${command.selectedStudyParticipantAssignment.onHoldTreatmentDate eq null}) {
                if (${crfsSize>1}) {
                    html += '<li><a href="#" onclick="javascript:showAddWindow(' + ${command.selectedStudyParticipantAssignment.id} + ', ' + date + ', ' + index + ', \'' + sid + '\');">Schedule form</a></li>';
                }
                if (showDeleteOption) {
                    html += '<li><a href="#" onclick="javascript:showDeleteWindow(' + date + ', ' + index + ', \'' + sid + '\');">Delete form</a></li>';
                    html += '<li><a href="#" onclick="javascript:showMoveWindow(' + date + ', ' + date + ', ' + index + ', \'' + sid + '\');">Move form to other date</a></li>';
                }
                html += '<li><a href="#" onclick="javascript:participantOnHold(' + ${command.selectedStudyParticipantAssignment.id} + ', ' + holdDate + ', ' + index + ');">Treatment on hold</a></li>';
                var split = sid.split('_');
                for (var a = 0; a < split.length; a++) {
                    var scheduleid = split[a];
                    if (scheduleid != '') {
                        var formName = forms[index][scheduleid];
                        
                    <proctcae:urlAuthorize url="/pages/participant/enterResponses">
                        html += '<li><hr></li>';
                    </proctcae:urlAuthorize>
                        if (${command.selectedStudyParticipantAssignment.homeWebLanguage ne null && command.selectedStudyParticipantAssignment.homeWebLanguage eq 'SPANISH'}) {
                        <proctcae:urlAuthorize url="/pages/participant/enterResponses">
                        
                            html += '<li id="nav"><a href="#" >Print form (' + formName + ')</a><ul><li><a href="#" onclick="location.href=\'printSchedule?lang=en&id=' + scheduleid + '\'">English</a></li><li><a href="#" onclick="location.href=\'printSchedule?lang=es&id=' + scheduleid + '\'">Spanish</a></li></ul></li>';
                            if(isSelectedAfterCurrentDate(currentYear, currentMonth, date)){  
                            	html += '<li><a href="#" onclick="location.href=\'enterResponses?id=' + scheduleid + '&lang=es\'">Enter responses (' + formName + ')</a></li>';
                        }
                        </proctcae:urlAuthorize>
                        } else {
                        <proctcae:urlAuthorize url="/pages/participant/enterResponses">
                            html += '<li id="nav"><a href="#" >Print form (' + formName + ')</a><ul><li><a href="#" onclick="location.href=\'printSchedule?lang=en&id=' + scheduleid + '\'">English</a></li><li><a href="#" onclick="location.href=\'printSchedule?lang=es&id=' + scheduleid + '\'">Spanish</a></li></ul></li>';
                            if(isSelectedAfterCurrentDate(currentYear, currentMonth, date)){
                           	 html += '<li><a href="#" onclick="location.href=\'enterResponses?id=' + scheduleid + '&lang=en\'">Enter responses (' + formName + ')</a></li>';
                           }
                        </proctcae:urlAuthorize>
                        }

                    }
                }
            } else {
                //ensure the onHoldDate is greater than the current date before showing the options.
                //if (newHoldYear >= currentYear && newHoldMonth >= currentMonth && newHoldDate > holdDate) {
                if(isHoldDateAfterCurrentDate(newHoldYear, currentYear, newHoldMonth, currentMonth, newHoldDate, holdDate)){
                    if (${crfsSize>1}) {
                        html += '<li><a href="#" onclick="javascript:showAddWindow(' + ${command.selectedStudyParticipantAssignment.id} + ', ' + date + ', ' + index + ', \'' + sid + '\');">Schedule form</a></li>';
                    }
                    if (showDeleteOption) {
                        html += '<li><a href="#" onclick="javascript:showDeleteWindow(' + date + ', ' + index + ', \'' + sid + '\', ' + ${command.selectedStudyParticipantAssignment.id} + ');">Delete form</a></li>';
                        html += '<li><a href="#" onclick="javascript:showMoveWindow(' + date + ', ' + date + ', ' + index + ', \'' + sid + '\', ' + ${command.selectedStudyParticipantAssignment.id} + ');">Move form to other date</a></li>';
                    }
                    var split = sid.split('_');
                    for (var a = 0; a < split.length; a++) {
                        var scheduleid = split[a];
                        if (scheduleid != '') {
                            var formName = forms[index][scheduleid];
                            
                            	html += '<li><hr></li>';
	                            html += '<li><a href="#" onclick="location.href=\'printSchedule?id=' + scheduleid + '\'">Print form (' + formName + ')</a></li>';
	                            if(isSelectedAfterCurrentDate(currentYear, currentMonth, date)){
	                            	html += '<li><a href="#" onclick="location.href=\'enterResponses?id=' + scheduleid + '\'">Enter responses (' + formName + ')</a></li>';
                            	}
                        }
                    }
                }
                html += '<li><a href="#" onclick="javascript:participantOffHold(' + ${command.selectedStudyParticipantAssignment.id} + ', ' + holdDate + ', ' + index + ');">Remove hold</a></li>';
            }
        }
        html += '</ul></div>';
    }
    jQuery('#scheduleActions' + menuindex).menu({
        content: html,
        maxHeight: 350,
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


function isHoldDateAfterCurrentDate(newHoldYear, currentYear, newHoldMonth, currentMonth, newHoldDate, currentDate){
	if(newHoldYear > currentYear){
		return true;
	}
	if(newHoldYear == currentYear && newHoldMonth > currentMonth){
		return true;
	}
	if(newHoldYear == currentYear && newHoldMonth == currentMonth && newHoldDate > currentDate){
		return true;
	}	
	return false;
}


/*
 * Entering responses should not be an available action option for form schedules that are not yet available.  
 */
function isSelectedAfterCurrentDate(selectedYear, selectedMonth, selectedDay){
	
	var currentTime= new Date();
	var month= currentTime.getMonth() + 1;
	var day= currentTime.getDate();
	var year= currentTime.getFullYear();
	
	
	if(selectedYear > year)
		return false;
	
	if(selectedYear == year && selectedMonth > month)
		return false;
	
	if(selectedYear == year && selectedMonth == month && selectedDay > day)
		return false;
	
	return true;
}

</script>
<style type="text/css">


</style>
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
<tags:tabForm tab="${tab}" flow="${flow}" willSave="true" formName="myForm">
    <jsp:attribute name="singleFields">
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
                                                                  studyParticipantAssignment="${command.selectedStudyParticipantAssignment}"
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


            </c:forEach>
</jsp:attribute>
</tags:tabForm>
</body>
</html>