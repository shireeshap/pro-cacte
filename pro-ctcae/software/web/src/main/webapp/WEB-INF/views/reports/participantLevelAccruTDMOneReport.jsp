<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="report" tagdir="/WEB-INF/tags/reports" %>
<html>
<head>
    <tags:stylesheetLink name="reports"/>
    <tags:dwrJavascriptLink objects="crf"/>
    <tags:dwrJavascriptLink objects="participant"/>
    <tags:includePrototypeWindow/>
    <tags:includeScriptaculous/>
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>
    <tags:dwrJavascriptLink objects="study"/>
    <tags:javascriptLink name="reports_common"/>
    <script type="text/javascript">
        displayParticipants = true;
        displaySymptom = false;
        function initializeFields() {
            <c:if test="${study ne null}">
                fnDisplayParticipants();
                setTimeout("participantCareResults();", 2000);
            </c:if>
        }
        
        function dateRangeCheck() {
			var stDateString = $('startDate').value;
			var endDateString = $('endDate').value;
			var stDate;
			var endDate;
			 try {
			   stDate = new Date(stDateString);
			   endDate = new Date(endDateString);
			 } catch(ex) {
				 return true;
			 }
			 if(endDate.getTime() < stDate.getTime()){
				 return true;
			 }
			 return false;
          }
        
        function participantCareResults(format, symptomId, selectedTypes) {
            if (!performValidations()) {
                return;
            }
            
            if(dateRangeCheck()){
            	alert('Please choose an End date greater or equal to the selected Start date');
            	return;
            }
            
            if (typeof(format) == 'undefined') {
                format = 'tabular';
            }
            var studyId = $('study').value;
            var forVisits = $('visits').value;
            var visitRange = $('visitOptions').value;
            var participantId = $('participant').value;
            var stDate = $('startDate').value;
            var endDate = $('endDate').value;
            var startTime = new Date().getTime();
            if (format == 'tabular') {
                showIndicator();
                var request = new Ajax.Request("<c:url value="/pages/reports/participantAccruTDMResults"/>", {
                    parameters:<tags:ajaxstandardparams/>+"&studyId=" + studyId + "&participantId=" + participantId +
                            "&visitRange=" + visitRange + "&forVisits=" + forVisits + "&startDate=" + stDate
                            + "&endDate=" + endDate,
                    onComplete:function(transport) {
                    	// Capture report generation time and send it to google analytics
                    	try{
	                    	var endTime = new Date().getTime();
	                    	var timeEllapsed = endTime - startTime;
	                    	
	                    	// Send timing hit to GA
	                    	if(isPROD()) {
		                    	sendTimingHitToGA(timeEllapsed);
	                    	}

	                    	if(isTier_SB_DEV()) {
		                    	sendTimingHitToGA(timeEllapsed, 'gaTrackerSBDev');
	                    	}

	                    	if(isTier_SB_QA()) {
		                    	sendTimingHitToGA(timeEllapsed, 'gaTrackerSBQA');
	                    	}
	                    	
	                    	if(isTier_NCI_DEV()) {
		                    	sendTimingHitToGA(timeEllapsed, 'gaTrackerNCIDEV');
	                    	}
	                    	
	                    	if(isTier_NCI_STAGE()) {
		                    	sendTimingHitToGA(timeEllapsed, 'gaTrackerNCISTAGE');
	                    	}
	                    	
	                    	if(isTier_SB_Demo()) {
		                    	sendTimingHitToGA(timeEllapsed, 'gaTrackerSBDEMO');
	                    	}
                    	} catch(ex) {
                    		console.log('Google Analytics: Exception in tracking report time.');
                    	}
                        showResultsTable(transport);
                        hideIndicator();
                    },
                    method:'get'
                })
            }
        }
        
        function sendTimingHitToGA(timeEllapsed, trackerName) {
        	try {
	        	if(trackerName != undefined) {
	        		pageTracker(trackerName+'.send', {
		        		  'hitType': 'timing',
		        		  'timingCategory': jQuery("#reportCategory").val(),
		        		  'timingVar': jQuery("#reportType").val(),
		        		  'timingValue': timeEllapsed,
		        		  'timingLabel': jQuery("#reportCategory").val()
		        		});
	        	} else {
	        		pageTracker('send', {
		        		  'hitType': 'timing',
		        		  'timingCategory': jQuery("#reportCategory").val(),
		        		  'timingVar': jQuery("#reportType").val(),
		        		  'timingValue': timeEllapsed,
		        		  'timingLabel': jQuery("#reportCategory").val()
		        		});
	        	}
        	} catch(err) {
        		console.log('Google Analytics: Exception in tracking report time.');
        	}
        }

        function showResultsTable(transport) {
            $('displayParticipantCareResults').show();
            $('displayResultsTable').innerHTML = transport.responseText;
        }

        function getChartView() {
            $('careResultsTable').hide();
            $('careResultsGraph').show();
        }
        function getTableView() {
            $('careResultsTable').show();
            $('careResultsGraph').hide();
        }
        function getChart(symptomId) {
            participantCareResults('graphical', symptomId);
        }
        function updateChart(chkbox, symptomId) {
            var obj = document.getElementsByName('attribute');
            var selectedTypes = '';
            for (var i = 0; i < obj.length; i++) {
                if (obj[i].checked) {
                    selectedTypes = selectedTypes + '_' + obj[i].value;
                }
            }
            if (selectedTypes == '') {
                alert('Please select at least one question type.');
                chkbox.checked = true;
                return;
            }
            participantCareResults('graphical', symptomId, selectedTypes);
        }

        function hideHelp() {
            $('attribute-help-content').style.display = 'none';
        }

    </script>
    <style type="text/css">
        * {
            zoom: 1;
        }
    </style>

    <script type="text/javascript">
        var managerAutoComp;
        Event.observe(window, 'load', function() {
            new YUIAutoCompleter('studyInput', getStudies, handleSelect);
            new YUIAutoCompleter('participantInput', getParticipants, handleSelect);
        });

        function getStudies(sQuery) {
            showIndicator("studyInput-indicator");
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            var requiredPrivilege = "PRIVILEGE_PARTICIPANT_REPORTS";
            study.matchStudy(unescape(sQuery), requiredPrivilege, callMetaData);
            hideIndicator("studyInput-indicator");
            return aResults;
        }

        function handleSelect(stype, args) {
            var ele = args[0];
            var oData = args[2];
            if (oData == null) {
                ele.getInputEl().value = "(Begin typing here)";
                ele.getInputEl().addClassName('pending-search');
            } else {
                ele.getInputEl().value = oData.displayName;
                var id = ele.getInputEl().id;
                var hiddenInputId = id.substring(0, id.indexOf('Input'));
                $(hiddenInputId).value = oData.id;
                ele.getInputEl().removeClassName('pending-search');

                if (hiddenInputId == 'study') {
                    $('search').show();
                    displayParticipants = true;
                    fnDisplayParticipants();
                    $('dateMenuDiv').show();
                    $('dateRange').show();
                    jQuery('#visitOptions option:selected').removeAttr('selected');
                    jQuery('#visitOptions').find('option[value="dateRange"]').attr("selected", true);
                }
            }
        }

        function clearInput(inputId) {
            $(inputId).clear();
            $(inputId + 'Input').clear();
            $(inputId + 'Input').focus();
            $(inputId + 'Input').blur();
        }

        function getParticipants(sQuery) {
            showIndicator("participantInput-indicator");
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = {callback:callbackProxy, async:false};
            participant.matchParticipantByStudySiteId(unescape(sQuery), null, $('study').value, callMetaData);
            hideIndicator("participantInput-indicator");
            return aResults;
        }
    </script>
</head>
<body>
<report:participantReportThirdlevelMenu selected="accruTDMOne"/>
<tags:instructions code="reports.ctcaeGrades.instructions"/>
<chrome:box title="report.label.search_criteria">
    <div align="left" style="margin-left: 50px">

        <div class="row" style="">
            <div class="label">
                <tags:requiredIndicator/><tags:message code='reports.label.study'/>
            </div>
            <div class="value">
                <input id="study" class="validate-NOTEMPTY" type="hidden" value="" title="Study" style="display: none;"
                       name="study"/>
                <tags:yuiAutocompleter inputName="studyInput" value="" required="false" hiddenInputName="study"/>
            </div>
        </div>

        <div id="participantAutoCompleterDiv" style="display:none">
            <div class="row">
                <div class="label">
                    <tags:requiredIndicator/><tags:message code='reports.label.participant'/>
                </div>
                <div class="value">
                    <input id="participant" class="validate-NOTEMPTY" type="hidden" value="" title="Participant"
                           style="display: none;" name="participant"/>
                    <tags:yuiAutocompleter inputName="participantInput" value="" required="false"
                                           hiddenInputName="participant"/>
                </div>
            </div>
        </div>

        <div id="dateMenuDiv" style="display:none" class="row">
            <div class="label"><tags:message code='reports.label.visits'/></div>
            <div class="value IEdivValueHack">
                <select id="visitOptions" name="visitOptions" onChange="customVisit(this)">
                	<option value="dateRange">Date range</option>
                    <option value="all">All</option>
                </select>
            </div>

        </div>
        <div id="visitNum" class="row" style="display:none">
            <div class="label"> Most recent</div>
            <div class="value"><input type="text" id="visits" class="validate-NUMERIC" style="width:25px;"/>
                <b>visits</b>
            </div>
        </div>
        <div id="dateRange" style="display:none">
            <div class="leftpanel">
                <tags:renderDate noForm="true" displayName="Reporting Period Start" propertyName="startDate"
                                 doNotShowFormat="true"/>
            </div>
            <div class="rightpanel" align="left">
                <tags:renderDate noForm="true" displayName="Reporting Period End" propertyName="endDate"
                                 doNotShowFormat="true" />
            </div>
        </div>

        <br/>

 		<c:set var="reportType" value="${param.rt}"/>
		<input type="hidden" name="reportType" id="reportType" value="${reportType}" />
		<input type="hidden" name="reportCategory" id="reportCategory" value="ParticipantReports" />

        <div id="search" style="display:none" class="row">
            <div style="margin-left:9em"><tags:button color="blue" value="Generate Report"
                                                      onclick="participantCareResults('tabular')" size="big"
                                                      icon="search"/>
                <tags:indicator id="indicator"/>
            </div>
        </div>

    </div>
</chrome:box>
<div id="displayParticipantCareResults" style="display:none;">
    <div>
        <div id="displayResultsTable"/>
    </div>
</div>
</body>
</html>