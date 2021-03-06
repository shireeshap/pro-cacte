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
function addRemoveSchedule(index, date, action, participantId) {
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
        getCalendar(index, "dir=refresh", participantId);
    } else {
        //jQuery('#ajaxLoadingImgDiv').show();
        var startTime = new Date().getTime();
        var request = new Ajax.Request("<c:url value="/pages/participant/addCrfSchedule"/>", {
            onComplete:function(transport) {
                //jQuery('#ajaxLoadingImgDiv').hide();
                var actionLabel = getReadableActionString(action);
                try{
    	        	var endTime = new Date().getTime();
    	        	var timeEllapsed = endTime - startTime;
    	        	var label = "Participant_" + ${command.participant.id};
    		       	
    	        	if(isPROD()) {
	    	        	sendTimingHitToGA(actionLabel, label, timeEllapsed);
    	        	}

    	        	if(isTier_SB_DEV()) {
	    		       	sendTimingHitToGA(actionLabel, label, timeEllapsed, 'gaTrackerSBDev');
    		       	}

    		       	if(isTier_SB_QA()) {
	    		       	sendTimingHitToGA(actionLabel, label, timeEllapsed, 'gaTrackerSBQA');
    		       	}
    		       	
                	if(isTier_NCI_DEV()) {
                    	sendTimingHitToGA(actionLabel, label, timeEllapsed, 'gaTrackerNCIDEV');
                	}
                	
                	if(isTier_NCI_STAGE()) {
                    	sendTimingHitToGA(actionLabel, label, timeEllapsed, 'gaTrackerNCISTAGE');
                	}
                	
                	if(isTier_SB_Demo()) {
                    	sendTimingHitToGA(actionLabel, label, timeEllapsed, 'gaTrackerSBDEMO');
                	}
        	    } catch(ex) {	
        		    	console.log('Google analytics: Exception in timing event on participant calendar.');               		
        	    	}
        	    	
                if (transport.responseText == "getCalendar") {
                    getCalendar(index, "dir=refresh", participantId);
                } else {
                    showConfirmationWindow(transport, 650, 210);
                }
            },
            parameters:<tags:ajaxstandardparams/> + "&id=" + participantId 
            										+"&index=" + index + 
            										"&date=" + date + 
            										"&action=" + action + 
            										"&fids=" + fids,
            method:'get'
        })
    }
}

function sendTimingHitToGA(action, label, timeEllapsed, trackerName) {
	try {
	 	if(trackerName != undefined) {
	 		pageTracker(trackerName+'.send', {
	   		  'hitType': 'timing',
	   		  'timingCategory': "ParticipantCalendar",
	   		  'timingVar': action,
	   		  'timingValue': timeEllapsed,
	   		  'timingLabel': label
	   		});
	 	} else {
	 		pageTracker('send', {
	     		  'hitType': 'timing',
	       		  'timingCategory': "ParticipantCalendar",
	       		  'timingVar': action,
	       		  'timingValue': timeEllapsed,
	       		  'timingLabel': label
	       		}); 
	 	}
	} catch(err) {
		console.log('Google analytics: Exception in timing event on participant calendar.');  
	}
 }

function getReadableActionString(action) {
	if(action == 'add,del') {
		return 'moveSchedule';
		
	} else if(action == 'del') {
		return 'deleteSchedule';
		
	} else if(action == 'moveallfuture') {
		return 'moveAllFollowingSchedules';
		
	} else if(action == 'moveall') {
		return 'moveAllSchedules';
		
	} else if(action == 'delallfuture') {
		return 'deleteAllFollowingSchedules';
		
	} else if(action == 'delall') {
		return 'deleteAllSchedules';
		
	} else {
		return 'addSchedule';
	}
}

function closeAndRefresh(participantId){
	closeWindow();
	
    <c:if test="(typeof(participantId) == 'undefined' || participantId == null) && ${param.id != null}">
		participantId = ${param.id};
	</c:if>
	
	getCalendar(index, "dir=refresh", participantId);
}

function beginHoldOnSchedules(index, date, action, pid) {

    if (date == null || date == '') {
        alert('Please enter a date');
        return;
    }
    closeWindow();
    if (action == 'cancel') {
        getCalendar(index, "dir=refresh", pid);
    } else {
        //jQuery('#ajaxLoadingImgDiv').show();
        var request = new Ajax.Request("<c:url value="/pages/participant/addCrfSchedule"/>", {
            onComplete:function(transport) {
                //jQuery('#ajaxLoadingImgDiv').hide();
                if (transport.responseText == "getCalendar") {
                    getCalendar(index, "dir=refresh", pid);
                } else {
                    showConfirmationWindow(transport, 650, 210);
                }
            },
            parameters:<tags:ajaxstandardparams/> + "&index=" + index + 
            										"&date=" + date + 
            										"&action=" + action + 
            										"&id=" + pid,
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
        //jQuery('#ajaxLoadingImgDiv').show();
        var request = new Ajax.Request("<c:url value="/pages/participant/moveFormScheduleValidate"/>", {
            onComplete:function(transport) {
                //jQuery('#ajaxLoadingImgDiv').hide();
                if (transport.responseText == "getCalendar") {
                    addRemoveSchedule(index, date, action, pid);
                } else {
                    showConfirmationWindow(transport, 650, 210);
                }
            },
            parameters:<tags:ajaxstandardparams/> + "&index=" + index + 
            										"&date=" + date + 
           											"&action=" + action + 
           											"&fids=" + fids + 
           											"&id=" + pid,
            method:'get'
        })
    }
}

function showMoveWindow(olddate, newdate, index, sids, participantId) {
    if (typeof(sids) == 'undefined') {
        sids = getScheduleIdsForDay(index, olddate);
    }
    
     <c:if test="(typeof(participantId) == 'undefined' || participantId == null) && ${param.id != null}">
		participantId = ${param.id};
	 </c:if>
	 
    //jQuery('#ajaxLoadingImgDiv').show();
    var request = new Ajax.Request("<c:url value="/pages/participant/moveFormSchedule"/>", {
        parameters:<tags:ajaxstandardparams/> + "&index=" + index + 
        										"&olddate=" + olddate + 
        										"&newdate=" + newdate + 
        										"&sids=" + sids + 
        										"&id=" + participantId,
        onComplete:function(transport) {
        		showConfirmationWindow(transport, 650, 210);
                AE.registerCalendarPopups();
        },
        method:'get'
    })

}

function showDeleteWindow(date, index, sids, participantId) {
    //$('ajaxLoadingImgDiv').show();
    var request = new Ajax.Request("<c:url value="/pages/participant/deleteFormSchedule"/>", {
        onComplete:function(transport) {
            //$('ajaxLoadingImgDiv').hide();
            showConfirmationWindow(transport, 650, 180);
        },
        parameters:<tags:ajaxstandardparams/> + "&index=" + index + 
        										"&date=" + date + 
        										"&sids=" + sids + 
        										"&id=" + participantId,
        method:'get'
    })
}
function showDetailsWindow(date, index, sids, participantId) {
    //jQuery('#ajaxLoadingImgDiv').show();
    var request = new Ajax.Request("<c:url value="/pages/participant/detailsFormSchedule"/>", {
        onComplete:function(transport) {
            //jQuery('#ajaxLoadingImgDiv').hide();
            showConfirmationWindow(transport, 650, 300);
        },
        parameters:<tags:ajaxstandardparams/> + "&index=" + index + 
        										"&date=" + date + 
        										"&sids=" + sids + 
        										"&id=" + participantId,
        method:'get'
    })
}

function showAddWindow(participantId, date, index, sids) {
    //$('ajaxLoadingImgDiv').show();
    var request = new Ajax.Request("<c:url value="/pages/participant/addFormSchedule"/>", {
        onComplete:function(transport) {
            //$('ajaxLoadingImgDiv').hide();
            showConfirmationWindow(transport, 650, 210);
        },
        parameters:<tags:ajaxstandardparams/> + "&id=" + participantId + 
        										"&index=" + index + 
        										"&date=" + date + 
        										"&sids=" + sids,
        method:'get'
    })
}

function showUpdateStartDateWindow(spcrfid) {
   // jQuery('#ajaxLoadingImgDiv').show();
    var request = new Ajax.Request("<c:url value="/pages/participant/updateStudyParticipantCrfStartDate"/>", {
        parameters:<tags:ajaxstandardparams/>+"&spcrfid=" + spcrfid,
        onComplete:function(transport) {
            //jQuery('#ajaxLoadingImgDiv').hide();
            showConfirmationWindow(transport, 650, 280);
            AE.registerCalendarPopups();
        },
        method:'get'
    })

}

function getCalendar(index, parameters, participantId) {
	 <c:if test="(typeof(participantId) == 'undefined' || participantId == null) && ${param.id != null}">
		participantId = ${param.id};
	 </c:if>
	
    var request = new Ajax.Request("<c:url value="/pages/participant/displaycalendar"/>", {
        onComplete:function(transport) {
            //jQuery('#ajaxLoadingImgDiv').hide();
            showCalendar(index, transport);
        },
        parameters:<tags:ajaxstandardparams/>+ "&id=" + participantId + 
        										"&index=" + index + 
        										"&" + parameters,
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

function participantOnHold(participantId, date, index) {
    //jQuery('#ajaxLoadingImgDiv').show();
    var startTime = new Date().getTime();
    var request = new Ajax.Request("<c:url value="/pages/participant/participantOnHold"/>", {
        parameters:<tags:ajaxstandardparams/> + "&flow=schedulecrf" + 
        										"&id=" + participantId + 
        										"&date=" + date + 
        										"&index=" + index,
        onComplete:function(transport) {
            //jQuery('#ajaxLoadingImgDiv').hide();
            try{
	        	var endTime = new Date().getTime();
	        	var timeEllapsed = endTime - startTime;
	        	var label = "Participant_" + ${command.participant.id};
		       	       	
	        	if(isPROD()) {
		        	sendTimingHitToGA('onHold', label, timeEllapsed);
	        	}
	        	
	        	if(isTier_SB_DEV()) {
			       	sendTimingHitToGA('onHold', label, timeEllapsed, 'gaTrackerSBDev');
		      	}
		      	
	        	if(isTier_SB_QA()) {
			       	sendTimingHitToGA('onHold', label, timeEllapsed, 'gaTrackerSBQA');
		      	}
	        	
            	if(isTier_NCI_DEV()) {
                	sendTimingHitToGA('onHold', label, timeEllapsed, 'gaTrackerNCIDEV');
            	}
            	
            	if(isTier_NCI_STAGE()) {
                	sendTimingHitToGA('onHold', label, timeEllapsed, 'gaTrackerNCISTAGE');
            	}
            	
            	if(isTier_SB_Demo()) {
                	sendTimingHitToGA('onHold', label, timeEllapsed, 'gaTrackerSBDEMO');
            	}

   	    	} catch(ex) {	
   	    		console.log('Google analytics: Exception in timing event on participant calendar.');               		               		
   	    	}
    	    	
            showConfirmationWindow(transport, 600, 250);
        },
        method:'get'
    })
}

var _winOffHold;
function participantOffHold(id, date, index) {
    //jQuery('#ajaxLoadingImgDiv').show();
    var request = new Ajax.Request("<c:url value="/pages/participant/participantOffHold"/>", {
        onComplete:function(transport) {
            //jQuery('#ajaxLoadingImgDiv').hide();
            if (transport.responseText == "getCalendar") {
                getCalendar(index, "dir=refresh", id);
            } else {
                showConfirmationWindow(transport, 650, 250);
            }
        },
        parameters:<tags:ajaxstandardparams/> + "&id=" + id + 
        										"&date=" + date + 
        										"&index=" + index,
        method:'get'
    })
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

function showPopUpMenuSchedule(date, currentMonth, currentYear, index, sid, showDeleteOption, hasShowCalendarActionsPrivilege, hasEnterResponsePrivilege, participantId) {
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
        /* commenting off-study check, as dropdown arrow is prevented from showing up in 
        the first place, for dates greater than off-study date */
        //if (${command.selectedStudyParticipantAssignment.status.displayName ne 'OffStudy'}) {
        	if(hasShowCalendarActionsPrivilege){
        		   if (${command.selectedStudyParticipantAssignment.onHoldTreatmentDate eq null}) {
                       html += '<li><a href="#" onclick="javascript:showAddWindow(\'' + participantId + '\', ' + date + ', ' + index + ');">Schedule form</a></li>';
                       html += '<li><a href="#" onclick="javascript:participantOnHold(\'' + participantId + '\', ' + holdDate + ', ' + index + ');">Treatment on hold</a></li>';
                   } else if(isHoldDateAfterCurrentDate(newHoldYear, currentYear, newHoldMonth, currentMonth, newHoldDate, holdDate)){
                   	html += '<li><a href="#" onclick="javascript:showAddWindow(\'' + participantId + '\', ' + date + ', ' + index + ');">Schedule form</a></li>';
                   } else {
                       html += '<li><a href="#" onclick="javascript:participantOffHold(\'' + participantId + '\', ' + holdDate + ', ' + index + ');">Remove hold</a></li>';
                   }
        	}
         
        //}
        html += '</ul></div>';
    } else {
        //TODO:Suneel A needs to clean up commented line after issue resolved
        //menuindex = sid;
        var html = '<div id="search-engines"><ul>';
        html += '<li><a href="#" onclick="javascript:showDetailsWindow(' + date + ', ' + index + ', \'' + sid + '\', \'' + participantId + '\');">Show details</a></li>';
        /* commenting off-study check, as dropdown arrow is prevented from showing up in 
        the first place, for dates greater than off-study date */
        //if (${command.selectedStudyParticipantAssignment.status.displayName ne 'OffStudy'}) {
            if (${command.selectedStudyParticipantAssignment.onHoldTreatmentDate eq null}) {
                if(hasShowCalendarActionsPrivilege){
                	if (${crfsSize>1}) {
                        html += '<li><a href="#" onclick="javascript:showAddWindow(\'' + participantId + '\', ' + date + ', ' + index + ', \'' + sid + '\');">Schedule form</a></li>';
                    }
                    if (showDeleteOption) {
                        html += '<li><a href="#" onclick="javascript:showDeleteWindow(' + date + ', ' + index + ', \'' + sid + '\', \'' + participantId + '\');">Delete form</a></li>';
                        html += '<li><a href="#" onclick="javascript:showMoveWindow(' + date + ', ' + date + ', ' + index + ', \'' + sid + '\', \'' + participantId + '\');">Move form to other date</a></li>';
                    }
                    html += '<li><a href="#" onclick="javascript:participantOnHold(\'' + participantId + '\', ' + holdDate + ', ' + index + ');">Treatment on hold</a></li>';
                }
            	if(hasEnterResponsePrivilege){
            	     var split = sid.split('_');
                     for (var a = 0; a < split.length; a++) {
                         var scheduleid = split[a];
                         if (scheduleid != '') {
                             var formName = forms[index][scheduleid];
                             
                             html += '<li><hr></li>';
                             if (${command.selectedStudyParticipantAssignment.homeWebLanguage ne null && command.selectedStudyParticipantAssignment.homeWebLanguage eq 'SPANISH'}) {
                             
                                 html += '<li id="nav"><a href="#" >Print form (' + formName + ')</a><ul><li><a href="#" onclick="location.href=\'printSchedule?lang=en&id=' + scheduleid + '\'">English</a></li><li><a href="#" onclick="location.href=\'printSchedule?lang=es&id=' + scheduleid + '\'">Spanish</a></li></ul></li>';
                                 if(isSelectedAfterCurrentDate(currentYear, currentMonth, date)){  
                                 	html += '<li><a href="#" onclick="location.href=\'enterResponses?id=' + scheduleid + "&prevTab=" + ${tab.targetNumber} + '&lang=es\'">Enter responses (' + formName + ')</a></li>';
                             	}
                             } else {
                                 html += '<li id="nav"><a href="#" >Print form (' + formName + ')</a><ul><li><a href="#" onclick="location.href=\'printSchedule?lang=en&id=' + scheduleid + '\'">English</a></li><li><a href="#" onclick="location.href=\'printSchedule?lang=es&id=' + scheduleid + '\'">Spanish</a></li></ul></li>';
                                 if(isSelectedAfterCurrentDate(currentYear, currentMonth, date)){
                                	 html += '<li><a href="#" onclick="location.href=\'enterResponses?id=' + scheduleid + "&prevTab=" + ${tab.targetNumber} + '&lang=en\'">Enter responses (' + formName + ')</a></li>';
                                }
                             }

                         }
                     }
            	}
           
            } else {
                //ensure the onHoldDate is greater than the current date before showing the options.
                //if (newHoldYear >= currentYear && newHoldMonth >= currentMonth && newHoldDate > holdDate) {
                if(isHoldDateAfterCurrentDate(newHoldYear, currentYear, newHoldMonth, currentMonth, newHoldDate, holdDate)){
                   	if(hasShowCalendarActionsPrivilege){
                    	if (${crfsSize>1}) {
                            html += '<li><a href="#" onclick="javascript:showAddWindow(' + participantId + ', ' + date + ', ' + index + ', \'' + sid + '\');">Schedule form</a></li>';
                        }
                        if (showDeleteOption) {
                            html += '<li><a href="#" onclick="javascript:showDeleteWindow(' + date + ', ' + index + ', \'' + sid + '\', ' + participantId + ');">Delete form</a></li>';
                            html += '<li><a href="#" onclick="javascript:showMoveWindow(' + date + ', ' + date + ', ' + index + ', \'' + sid + '\', ' + participantId + ');">Move form to other date</a></li>';
                        }
                        html += '<li><a href="#" onclick="javascript:participantOffHold(' + participantId + ', ' + holdDate + ', ' + index + ');">Remove hold</a></li>';
                   	}
                   	if(hasEnterResponsePrivilege){
                        var split = sid.split('_');
                        for (var a = 0; a < split.length; a++) {
                            var scheduleid = split[a];
                            if (scheduleid != '') {
                                var formName = forms[index][scheduleid];
                                
                                	html += '<li><hr></li>';
    	                            html += '<li><a href="#" onclick="location.href=\'printSchedule?id=' + scheduleid + '\'">Print form (' + formName + ')</a></li>';
    	                            if(isSelectedAfterCurrentDate(currentYear, currentMonth, date)){
    	                            	html += '<li><a href="#" onclick="location.href=\'enterResponses?id=' + scheduleid + "&prevTab=" + ${tab.targetNumber} + '\'">Enter responses (' + formName + ')</a></li>';
                                	}
                            }
                        }
                   	}
                }
                
            }
        //}
            
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


participantOffHoldPost = function(index, date, cycle, day, action, participantId) {
    if (date == null || date == '') {
        alert('Please enter a date');
        return;
    }
    closeWindow();
    if (action == 'cancel') {
        getCalendar(index, "dir=refresh", participantId);
    } else {
        //jQuery('#ajaxLoadingImgDiv').show();
	    var startTime = new Date().getTime();
        var request = new Ajax.Request("<c:url value='/pages/participant/addCrfSchedule'/>", {
	        onComplete:function(transport) {
	           // jQuery('#ajaxLoadingImgDiv').hide();
		           try{
			        	var endTime = new Date().getTime();
			        	var timeEllapsed = endTime - startTime;
			        	var label = "Participant_" + ${command.participant.id};
				       	
			        	if(isPROD()) {
				        	sendTimingHitToGA('offHold', label, timeEllapsed);
			        	}

			        	if(isTier_SB_DEV()) {
					       	sendTimingHitToGA('offHold', label, timeEllapsed, 'gaTrackerSBDev');
				     	}

				     	if(isTier_SB_QA()) {
					       	sendTimingHitToGA('offHold', label, timeEllapsed, 'gaTrackerSBQA');
				     	}
				     	
                    	if(isTier_NCI_DEV()) {
	                    	sendTimingHitToGA('offHold', label, timeEllapsed, 'gaTrackerNCIDEV');
                    	}
                    	
                    	if(isTier_NCI_STAGE()) {
	                    	sendTimingHitToGA('offHold', label, timeEllapsed, 'gaTrackerNCISTAGE');
                    	}
                    	
                    	if(isTier_SB_Demo()) {
	                    	sendTimingHitToGA('offHold', label, timeEllapsed, 'gaTrackerSBDEMO');
                    	}

	    	    	} catch(ex) {
	    	    		console.log('Google analytics: Exception in timing event on participant calendar.');               		               		
	    	   	  }
	            if (transport.responseText == "getCalendar") {
	                getCalendar(index, "dir=refresh", participantId);
	            } else {
	                showConfirmationWindow(transport, 650, 210);
	            }
	         },
	         parameters:<tags:ajaxstandardparams/> + "&index=" + index + 
	         										"&offHoldDate=" + date + 
	         										"&cycle=" + cycle  + 
	         										"&day=" + day + 
	         										"&action=" + action + 
	         										"&id=" + participantId,
	         method:'get'
         })
    }
};


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
                                                                  index="0" hasShowCalendarActionsPrivilege="${hasShowCalendarActionsPrivilege}" hasEnterResponsePrivilege="${hasEnterResponsePrivilege}"/>
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