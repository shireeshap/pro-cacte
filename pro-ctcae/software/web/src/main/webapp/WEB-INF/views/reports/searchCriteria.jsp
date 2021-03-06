<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="report" tagdir="/WEB-INF/tags/reports" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <tags:dwrJavascriptLink objects="crf"/>
    <tags:javascriptLink name="table_menu"/>
    <tags:stylesheetLink name="table_menu"/>
    <tags:includePrototypeWindow/>
    <tags:includeScriptaculous/>
    <tags:stylesheetLink name="reports"/>
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>
    <tags:dwrJavascriptLink objects="study"/>
    <tags:javascriptLink name="reports_common_study"/>
    <style type="text/css">
        table.report {
            width: 90%;
            border: 1px #999999 solid;
        }

        table.report td {
            border-left: 1px #cccccc solid;
            border-top: 1px #eee solid;
            text-align: center;
            padding: 2px 5px;

        }

        table.report td.right {
            text-align: right;
            padding-left: 5px;
        }

        table.report td.bottom {
            border-top: 2px #666 solid;
        }

        table.report td.header {
            background-color: #cccccc;
            font-weight: bold;
        }

        table.report td.left {
            text-align: left;
            padding-right: 5px;
        }

        * {
            zoom: 1;
        }

    </style>
    <script type="text/javascript">
        var showResultsInPopUpFlag = false;
        function reportResults(group, arms) {
            var reportUrl = "${url}";
            if (!performValidations()) {
                return;
            }
            showIndicator();
            showMessage();
            var startTime = new Date().getTime();
            var request = new Ajax.Request(reportUrl, {
                parameters:getQueryString(group, arms),
                onComplete:function(transport) {
                    if (showResultsInPopUpFlag) {
                        showResultsInPopUp(transport);
                    } else {
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
                    	
                        showResults(transport);
                    }
                    hideIndicator();
                    $('waitMessage').hide();
                },
                method:'get'
            })
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
        
        function showMessage(){
        	var reportUrl = "${url}";
        	if(reportUrl.substr(24) == "overallStudyWideFormat"){
           	 $('waitMessage').show();	
           }
        }

        function showText(obj) {
            var val = obj.value;
            if (val == '') {
                $('filterByValue').hide();
                $('filterByValueHelp').hide();
            } else {
                $('filterByValue').show();
                $('filterByValueHelp').show();
            }
        }

    </script>
    
    <script type="text/javascript">
        var managerAutoComp;
        Event.observe(window, 'load', function() {
        	$('waitMessage').hide();
            if (${study eq null}) {
                new YUIAutoCompleter('studyInput', getStudies, handleSelect);
            }
            new YUIAutoCompleter('studySiteInput', getOrganizations, handleSelect);
        });

        function getStudies(sQuery) {
        	showIndicator("studyInput-indicator");
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            var requiredPrivilege = "PRIVILEGE_STUDY_REPORTS";
            study.matchStudy(unescape(sQuery), requiredPrivilege, callMetaData);
            hideIndicator("studyInput-indicator");
            return aResults;
        }

        function handleSelect(stype, args) {
            var ele = args[0];
            var oData = args[2];
            if(oData == null){
            	ele.getInputEl().value="(Begin typing here)";
            	ele.getInputEl().addClassName('pending-search');
            } else {
	            ele.getInputEl().value = oData.displayName;
	            var id = ele.getInputEl().id;
	            var hiddenInputId = id.substring(0, id.indexOf('Input'));
	            $(hiddenInputId).value = oData.id;
	            ele.getInputEl().removeClassName('pending-search');
	            
				if(hiddenInputId == 'study'){
					if (displayForm) {
		                var reportUrl = "${url}";
		                if(reportUrl.substr(24) == 'symptomOverTime'){
		                	displayForms('', reportUrl.substr(24));
		                	displaySymptoms('', $('study').value);
			                displaySites();
		                } else {
		                	displayForms('', reportUrl.substr(24));
			                displaySites();
		                }
		                if (displayFilterBy) {
		                    $('filterByDiv').show();
		                }
		            }
		            $('search').show();
				}
            }
            
             $('studyInput-msg').style.display='none';
        }

       function clearInput(inputId) {
            $(inputId).clear();
            $(inputId + 'Input').clear();
            $(inputId + 'Input').focus();
            $(inputId + 'Input').blur();
        }
                 
       function handleGenerateReport() {
    	   if(jQuery("#studyInput").length > 0){
		   	   if($('studyInput').value == '' || $('studyInput').value == '(Begin typing here)'){
		   	   	   $('studyInput-msg').style.display='block';
		   	   	   return;
		   	   } 
		   	   document.getElementById('studyInput-msg').style.display='none';
    	   }
    	   
	       showResultsInPopUpFlag = false;
	       if ($('symptom') != null) {
	           $('symptom').value = '';
	       }
	       reportResults();
	   }
              
    </script>
</head>
<body>
<report:thirdlevelmenu selected="${param['rt']}"/>
<report:reportSpecificJS selected="${param['rt']}"/>
<tags:instructions code="${param['rt']}Instructions"/>
<c:set var="onlyStudy" value="false"/>
<c:if test="${param['rt'] eq 'enrollmentReport'}">
    <c:set var="onlyStudy" value="true"/>
</c:if>

<chrome:box title="report.label.search_criteria">
    <div align="left" style="margin-left: 50px">
        <c:choose>
            <c:when test="${study ne null}">
                <div class="row">
                    <div class="label"><tags:message code="reports.label.study"/></div>
                    <div class="value">${study.displayName}</div>
                </div>
                <input type="hidden" id="study" name="study" value="${study.id}"/>
            </c:when>
            <c:otherwise>
                <div id="studyCompleter" style="margin-left:11px;">
					<div class="row" style="">
						<div class="label">
							<tags:requiredIndicator/><tags:message code='reports.label.study'/>
						</div>
						<div class="value">
							<input id="study" class="validate-NOTEMPTY" type="hidden" value=""  title="Study" style="display: none;" name="study"/>                         
			         	   	<tags:yuiAutocompleter inputName="studyInput" value="" required="false" hiddenInputName="study"/>
			         	   	<ul id="studyInput-msg" class="errors" style="display:none"><li> Missing  Study </li></ul>
						</div>
					</div> 
                </div>

                <%--</div>--%>
            </c:otherwise>
        </c:choose>
        <c:if test="${not onlyStudy}">
            <c:choose>
                <c:when test="${crfs ne null}">
                 	<c:choose>
	                	<c:when test="${fn:length(crfs) eq 1}">
			                <div class="row">
			                    <div class="label"><tags:message code="reports.label.form"/></div>
			                    <div class="value">${crfs[0].title}</div>
			                    <input type="hidden" name="form" id="form" value="${crfs[0].id}" title="Form"/>
			                </div>
	                	</c:when>
	                	<c:otherwise>
		                	<div class="row">
			                    <div class="label"><tags:message code="reports.label.form"/></div>
			                    <div class="value">
		                        <select onchange="javascript:displaySymptoms(this.value, $('study').value)" name="form" id="form">
		                            <option value="">Please select</option>
		                            <c:forEach items="${crfs}" var="crf">
		                                <option value="${crf.id}">${crf.title}</option>
		                            </c:forEach>
		                        </select>
		                    	</div>
	                		</div>
	                	</c:otherwise>
                	</c:choose>
                </c:when>
                <c:otherwise>
                    <div class="row" id="divFormRow" style="display:none;margin-left:11px;">
                        <div class="label"><tags:message code="reports.label.form"/></div>
                        <div class="value" id="formTitle"></div>
                        <input type="hidden" name="form" id="form" value="" title="Form"/>
                    </div>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${proctcterms ne null && param['rt'] eq 'symptomOverTime'}">
                    <div class="row" style="margin-left:11px;">
                        <div class="label"><tags:requiredIndicator/><tags:message code="reports.label.symptoms"/></div>
                        <div class="value">
                            <select id="proCtcTermsSelect" title="symptom">
                                <option value="">Please select</option>
                                <c:forEach items="${proctcterms}" var="proctcterm">
                                    <option value="${proctcterm.id}">${proctcterm.term}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row" id="divSymptomsRow" style="display:none; display:none; margin-left:11px;">
                        <div class="label"><tags:requiredIndicator/><tags:message code="reports.label.symptoms"/></div>
                        <div class="value" id="proCtcTerms"></div>
                    </div>
                    <c:if test="${study ne null}">
                        <script type="text/javascript">
                        	displaySymptoms('', $('study').value);
                        </script>
                    </c:if>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${studySite ne null}">
                    <div class="row">
                        <div class="label"><tags:message code="reports.label.site"/></div>
                        <div class="value">${studySite.displayName}</div>
                    </div>
                    <input type="hidden" id="studySite" name="studySite" value="${studySite.id}"/>
                </c:when>
                <c:otherwise>
                    <div>
                        <div id="studySiteAutoCompleter" style="display:none;margin-left:11px;">
				            <div class="row">
								<div class="label">
									<tags:message code='reports.label.site'/>
								</div>
								<div class="value">
									<input id="studySite" class="validate-NOTEMPTY" type="hidden" value=""  title="Study site" style="display:none;" name="studySite"/>                         
					         	   	<tags:yuiAutocompleter inputName="studySiteInput" value="" required="false" hiddenInputName="studySite"/>
								</div>
							</div> 
                        </div>
                        <div class="row" id="divStudySiteRow" style="display:none; ;position:relative; left:12px;">
                            <div class="label"><tags:message code="reports.label.site"/></div>
                            <div class="value" id="studySiteDisplayName"></div>
                        </div>
                    </div>
                    <c:if test="${study ne null}">
                        <script type="text/javascript">
                            displaySites();
                        </script>
                    </c:if>
                </c:otherwise>
            </c:choose>

            <c:set var="filterstyle" value="display:none"/>
            <c:if test="${study ne null}">
                <c:set var="filterstyle" value=""/>
            </c:if>
            <div class="row" id="filterByDiv" style="${filterstyle};position:relative; left:12px;">
                <div class="label"><tags:message code="reports.label.FilterBy"/></div>
                <div class="value">
                    <select id="filterBy" title="Filter By" onchange="showText(this);">
                        <option value="">None</option>
                        <option value="cycle">Cycle</option>
                        <option value="week">Week</option>
                        <option value="month">Month</option>
                    </select>
                    <input type="text" name="filterByValue" id="filterByValue" style="display:none" size="8"
                           title="Filter By"/>

                    <div style="font-size:11px;display:none" id="filterByValueHelp"> (You can either specify a range of
                        values using dash, for ex. 2-5, or separate
                        values using comma, for ex. 1,5,8)
                    </div>
                </div>
            </div>
        </c:if>
       <br>
       
		<c:set var="reportType" value="${param.rt}"/>
	   	<input type="hidden" name="reportType" id="reportType" value="${reportType}" />
	   	<input type="hidden" name="reportCategory" id="reportCategory" value="StudyReports" />
		
       <div id="search" class="row" align="left">
            <div style="margin-left:11.5em">
                <tags:button color="blue" value="Generate Report" onclick="handleGenerateReport();" size="big"
                             icon="search"/>
                <tags:indicator id="indicator"/>
                
                <ul class="label" id="waitMessage" style="position:right">
                	<br/> 
                	<li> <tags:message code="reports.fullStudyLevelReport.waitMessage"/>
                	</li>
                </ul>
                
            </div>
            <div id="studydata" align="right" style="display:none;">
            </div>
        </div>
    </div>
</chrome:box>
<div id="reportOuterDiv" style="display:none;">
    <div id="reportInnerDiv"/>
</div>



</body>
</html>