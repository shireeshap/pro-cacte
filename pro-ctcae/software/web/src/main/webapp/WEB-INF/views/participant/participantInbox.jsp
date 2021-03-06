<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="gov.nih.nci.ctcae.core.domain.ProCtcAECalendar" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <style type="text/css">
        div.row div.value {
            white-space: normal;
        }

        .label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            padding: 1px;
            font-size: 20px;
        }

        #inboxTable {
            width: 952px;
            border: 0;
            margin-left: -4px;
            border-collapse: collapse;
            margin-bottom: 3px;
        }

        #inboxTable th {
            background: #a4a6a9 url(../../images/table/inboxtable_th.png) repeat-x top;
            padding: 10px 10px 10px 20px;
            font-size: 16px;
            color: #000;
            text-shadow: 0 1px #fff;
            border-top: 1px solid lightgray;
            text-align: left;
        }

        #inboxTable td {
            padding: 8px 10px 7px 20px;

        }

        #inboxTable tr {
            background: url(../../images/table/inboxtable_tr.png) repeat-x top;
            border-bottom: 1px solid lightgray;
            text-shadow: 0 1px white;
            color: #333;
            font-size: 16px;
        }

        #inboxTable tr:hover {
            background-position: 0 -55px;
        }

        #inboxTitle {
            color: #333;
            height: 75px;
        }

        #inboxTitle .bolder {
            font-weight: bold;
            color: #004a93
        }

        #inboxTitle h1 {
            font-size: 37px;
            color: #004a93;
            margin: 0;
            padding: 0;
        }

        #inboxTitle img {
            float: left;
            margin: 15px 15px 0 10px;
        }

        .current {
            background-position: 0 -62px;

        }


    </style>
    <script type="text/javascript">
        function setCurrent(className) {
            alert(className);
            var x = document.getElementsByClassName('right');
            x.className = 'current';
        }
        function openNewWindow(url) {
             popupWin = window.open(url,
             'open_window',
             'menubar, toolbar, location, directories, status, scrollbars, resizable, dependent, width=640, height=480, left=0, top=0')
         }
        
        
        Event.observe(window, 'load', function() {
        	jQuery('[id^="beginSurvey"]').click(function(event){
	        	trackEvent(event);
        	});

        }); 
        
        function trackEvent(event) {
	       	var hrefLink = jQuery(event.currentTarget).attr("href");
	       	var action = hrefLink.substring(hrefLink.indexOf('id=') + 3, hrefLink.indexOf('&'));
	      	 	 
	    	if(isPROD()) {
		       	sendEventHitToGA(action);
	    	}

	    	if(isTier_SB_DEV()) {
		       	sendEventHitToGA(action, 'gaTrackerSBDev');
	       	}

	       	if(isTier_SB_QA()) {
		       	sendEventHitToGA(action, 'gaTrackerSBQA');
	       	}
	       	
        	if(isTier_NCI_DEV()) {
        		sendEventHitToGA(action, 'gaTrackerNCIDEV');
        	}
        	
        	if(isTier_NCI_STAGE()) {
        		sendEventHitToGA(action, 'gaTrackerNCISTAGE');
        	}
        	
        	if(isTier_SB_Demo()) {
        		sendEventHitToGA(action, 'gaTrackerSBDEMO');
        	}

        }
        
        function sendEventHitToGA(action, trackerName) {
        	try {
	        	if(trackerName != undefined) {
	        		pageTracker(trackerName+'.send', 'event', {
		        	  'eventCategory': 'Survey',
		        	  'eventAction': action,
		        	  'eventLabel': 'Started'
		        	});
	        	} else {
	        		pageTracker('send', 'event', {
		        	  'eventCategory': 'Survey',
		        	  'eventAction': action,
		        	  'eventLabel': 'Started'
		        	});
	        	}
        	} catch(err) {
        		console.log('Google analytics: Exception in tracking Survey Start event');
        	}
        }
    </script>

</head>
<body>
<%-- <c:set var="todaysdate" value="<%= ProCtcAECalendar.getCalendarForDate(new Date()).getTime()%>"/> --%>
<c:set var="todaysdate" value="${today}"/>
<c:set var="missedFormsAvailable" value="false"/>
<%
    Calendar calendar = new java.util.GregorianCalendar();
    calendar.add(Calendar.DAY_OF_MONTH, -15);
    java.util.Date date = calendar.getTime();
    request.setAttribute("missedFormDate", date);
%>
<%--this loop is the same code as below that renders the forms, but it just gets the number of forms to display under the 'Inbox' text--%>
<c:set var="numberofCrfs" scope="page" value="0"/>
<c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
    <c:set var="spaStatus" value="${studyParticipantAssignment.status}"/>
    <c:if test="${studyParticipantAssignment.onHoldTreatmentDate le todaysdate}">
	    <c:set var="spaStatusDate" value="true"/>
    </c:if>
    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
        <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}" var="studyParticipantCrfSchedule">
            <c:set scope="page" var="remainingDays"
                   value="${(studyParticipantCrfSchedule.dueDate.time - todaysdate.time) / (1000 * 60 * 60 * 24)}"/>
            <c:if test="${(studyParticipantCrfSchedule.status.displayName eq 'In-progress' || 
						   studyParticipantCrfSchedule.status.displayName eq 'Scheduled') && 
						   (studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false' && 
            			   studyParticipantCrfSchedule.startDate <= todaysdate && remainingDays ge 0)}">
                <c:set var="numberofCrfs" scope="page" value="${numberofCrfs + 1}"/>
            </c:if>
        </c:forEach>
    </c:forEach>
</c:forEach>

<c:set var="futureNumberofCrfs" scope="page" value="0"/>
<c:forEach items="${command.studyParticipantAssignments}" var="studyParticipantAssignment">
    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
        <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}" var="studyParticipantCrfSchedule">
            <c:set scope="page" var="remainingDays"
                   value="${(studyParticipantCrfSchedule.dueDate.time - todaysdate.time) / (1000 * 60 * 60 * 24)}"/>
            <c:if test="${(studyParticipantCrfSchedule.status.displayName eq 'In-progress' || studyParticipantCrfSchedule.status.displayName eq 'Scheduled') &&
            			  (studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false' && studyParticipantCrfSchedule.startDate > todaysdate && remainingDays ge 0)}">
                <c:set var="futureNumberofCrfs" scope="page" value="${futureNumberofCrfs + 1}"/>
                <c:if test="${futureNumberofCrfs == 1}">
                    <c:set var="futureSurveyAvailableDate" scope="page"
                           value="${studyParticipantCrfSchedule.startDate}"/>
                </c:if>
                <c:if test="${studyParticipantCrfSchedule.startDate < futureSurveyAvailableDate}">
                    <c:set var="futureSurveyAvailableDate" scope="page"
                           value="${studyParticipantCrfSchedule.startDate}"/>
                </c:if>
            </c:if>
        </c:forEach>
    </c:forEach>
</c:forEach>

<div id="inboxTitle" style="height:39px;">
    <c:set var="currentEn" value="current"/>
    <c:set var="currentEs" value=""/>
    <c:if test="${pageContext.response.locale == 'es' or param.lang eq 'es'}">
       <c:set var="currentEs" value="current"/>
       <c:set var="currentEn" value=""/>
    </c:if>

    <div class="language-toggle1" style="float:right;height:0px;">
        <a class="left ${currentEn}" href="?lang=en">English</a>
        <a class="right ${currentEs}" href="?lang=es">Español</a>
    </div>

    <img src="<tags:imageUrl name="blue/mailbox.jpg" />" alt="mailbox"/>

    <h1><tags:message code="participant.box.inbox"/>(${numberofCrfs})</h1>
</div>
<table><tr><td width="778px">
    <span style="font-size:13pt; margin-left:10px;">
    <c:choose>
        <c:when test="${numberofCrfs gt 0}">
            <c:if test="${numberofCrfs != 1}"><tags:message
                    code="participant.youHave"/>&nbsp;<span class="bolder">[${numberofCrfs}]</span><tags:message
                    code="participant.messageEndingPlural"/></c:if>
            <c:if test="${numberofCrfs == 1}"><tags:message
                    code="participant.youHave"/>&nbsp;<span class="bolder">[${numberofCrfs}]</span><tags:message
                    code="participant.messageEndingSingular"/></c:if>
        </c:when>
        <c:otherwise><tags:message code="participant.noformsmessage"/>
        </c:otherwise>
    </c:choose>
    </span>
    </td><td>

             <a onclick="openNewWindow('${videoUrl}');" style="cursor:pointer;">
                <table><tr><td>
                        <img style="margin:0px;" src="<chrome:imageUrl name="../video_camera_icon.png"/>" alt="" />
                    </td><td valign="middle" style="color:#006CD7">
                        <spring:message code="help.video"/>
                </td></tr></table>

            </a>
</td></tr></table>


<tags:instructions code="participant.instruction"/><br/>
    <%--<div style="text-align:right;font-weight:bold;"><a href="../participant/responseReport">View old responses</a></div>--%>
<spring:message code="label.scheduledForms" var="labelScheduledForms"/>
<c:choose>
    <c:when test="${(spaStatus eq 'ONHOLD' && spaStatusDate eq 'true') || spaStatus eq 'OFFSTUDY'}">
        <tags:message code="participant.onHoldMessage"/> ${spaStatus}.
    </c:when>
    <c:otherwise>
		<chrome:box title="${labelScheduledForms}">
		    <c:if test="${numberofCrfs eq 0}">
		        <div style="margin-left:50px;">
		            <c:if test="${futureNumberofCrfs eq 0}">
		                <tags:message code="participant.noSurveyAvailable"/>
		            </c:if>
		            <c:if test="${futureNumberofCrfs gt 0}">
		                <tags:message code="participant.noSurveyAvailable"/> <tags:message code="participant.nextSurveyAvailable"/>
		                <tags:formatDate value="${futureSurveyAvailableDate}"/>.
		            </c:if>
		        </div>
		    </c:if>
		    <c:if test="${numberofCrfs gt 0}">
		        <table id="inboxTable">
		            <tr>
		                <th>
		                    <tags:message code="participant.label.title"/>
		                </th>
		                <th>
		                    <tags:message code="participant.label.status"/>
		                </th>
		                    <%--  <th>
		                       <tags:message code="participant.label.scheduleDate"/>
		                   </th> --%>
		                <th>
		                    <tags:message code="participant.label.dueDate"/>
		                </th>
		                <th>
		
		                </th>
		            </tr>
		            <c:forEach items="${command.sortedStudyParticipantCrfSchedules}" var="studyParticipantCrfSchedule">
		               <%-- <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="studyParticipantCrf">
		                    <c:forEach items="${studyParticipantCrf.studyParticipantCrfSchedules}"
		                               var="studyParticipantCrfSchedule">--%>
		                        <c:set scope="page" var="remainingDays"
		                               value="${(studyParticipantCrfSchedule.dueDate.time - todaysdate.time) / (1000 * 60 * 60 * 24)}"/>
		                        <%--<c:if test="${studyParticipantCrfSchedule.status.displayName eq 'In-progress' || (studyParticipantCrfSchedule.status.displayName eq 'Scheduled' && studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false' && studyParticipantCrfSchedule.startDate <= todaysdate && remainingDays ge 0)}">--%>
		                        <c:if test="${(studyParticipantCrfSchedule.studyParticipantCrf.crf.hidden eq 'false' && 
		                        			   studyParticipantCrfSchedule.startDate <= todaysdate && remainingDays ge 0)}">
		                            <tr>
		                                <td>
		                                        ${studyParticipantCrfSchedule.studyParticipantCrf.crf.title}
		                                        <%--<c:if test="${studyParticipantCrfSchedule.baseline}">(Baseline)</c:if> --%>
		                                </td>
		                                <td>
		                                    <tags:message code="crfStatus_${studyParticipantCrfSchedule.status.code}"/>
		                                </td>
		                                    <%--  <td>
		                                       <tags:formatDate value="${studyParticipantCrfSchedule.startDate}"/>
		                                   </td> --%>
		                                <td>
		
		                                    <c:if test="${(studyParticipantCrfSchedule.dueDate.time eq todaysdate.time)}">
		                                        <tags:message code="participant.today"/>
		                                    </c:if>
		                                    <c:if test="${(studyParticipantCrfSchedule.dueDate.time gt todaysdate.time)}">
		                                        <c:if test="${remainingDays eq 1}">
		                                            <tags:message code="participant.tomorrow"/>
		                                        </c:if>
		                                        <c:if test="${remainingDays gt 1}">
		                                            <tags:message code="participant.in"/> <fmt:formatNumber type="number"
		                                                                                                    maxFractionDigits="0"
		                                                                                                    value="${remainingDays}"/>
		                                            <tags:message code="participant.days"/>
		                                        </c:if>
		                                    </c:if>
		                                    
		                                    <c:set var="expiredFlag" value="true"/>
		                                    <c:if test="${(studyParticipantCrfSchedule.dueDate.time lt todaysdate.time)}">
		                                        <c:set var="expiredFlag" value="false"/>
		                                        <c:if test="${remainingDays eq -1}">
		                                            <tags:message code="participant.expired"/> <fmt:formatNumber type="number"
		                                                                                                         maxFractionDigits="0"
		                                                                                                         value="${(todaysdate.time - studyParticipantCrfSchedule.dueDate.time) / (1000 * 60 * 60 * 24)}"/>
		                                            <tags:message code="participant.days.ago"/>
		                                        </c:if>
		                                        <c:if test="${remainingDays lt -1}">
		                                            <tags:message code="participant.expired"/> <fmt:formatNumber type="number"
		                                                                                                         maxFractionDigits="0"
		                                                                                                         value="${(todaysdate.time - studyParticipantCrfSchedule.dueDate.time) / (1000 * 60 * 60 * 24)}"/>
		                                            <tags:message code="participant.days.ago"/>
		                                        </c:if>
		
		                                    </c:if>
		
		                                </td>
		                                <td>
		                                    <c:if test="${expiredFlag}">
		                                        <a href="../../pages/form/submit?id=${studyParticipantCrfSchedule.id}&isBegin=true"
		                                           class="btn small-green" id="beginSurvey" onclick="anchorClik()"><span id="${studyParticipantCrfSchedule.id}"><tags:message code="label.start"/></span></a>
		                                    </c:if>
		                                </td>
		                            </tr>
		                        </c:if>
		                    <%--</c:forEach>
		                </c:forEach>--%>
		            </c:forEach>
		
		        </table>
		    </c:if>
		</chrome:box>
</c:otherwise>
</c:choose>

</body>
</html>
