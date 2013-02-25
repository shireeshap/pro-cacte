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
    <tags:javascriptLink name="common_help"/>
    
    <script type="text/javascript">
        displayParticipants = true;
        displaySymptom = false;
        studySiteMandatory = true;
        window.onload = function() {
            initializeFields();
        }
        function initializeFields() {
	        <c:if test="${showReports}">
	            setTimeout("participantCareResultsFromHome();", 1000);
	        </c:if>
			
	        if(isIE()){
	        	jQuery('#formDropDown').css("position","relative");
	        	jQuery('#formDropDown').css("left","12px");
	        	jQuery('#visitOptions').css("position","relative");
	        	jQuery('#visitOptions').css("left","12px");
			}
	    }

        function participantCareResultsFromHome() {
             var request = new Ajax.Request("<c:url value="/pages/reports/participantCareResults"/>", {
                parameters:<tags:ajaxstandardparams/>+"&studyId=" + ${study.id} + "&crfId=" + ${crf.id} +
                        "&studySiteId=" + ${studySite.id} + "&participantId=" + ${participant.id} +
                        "&visitRange=" + "all" + "&forVisits=" + null + "&startDate=" + null + "&endDate=" + null,
                onComplete:function(transport) {
                    showResultsTable(transport);
                },
                method:'get'
            })
        }
        function participantCareResults(format, symptomId, selectedTypes) {
            if (!performValidations()) {
                return;
            }
            if (typeof(format) == 'undefined') {
                format = 'tabular';
            }
            var studyId = $('study').value;
            var forVisits = $('visits').value;
            var crfId = $('formSelect').value;
            var visitRange = $('visitOptions').value;
            var studySiteId = $('studySite').value;
            var participantId = $('participant').value;
            var stDate = $('startDate').value;
            var endDate = $('endDate').value;
            if (format == 'tabular') {
                showIndicator();
                var request = new Ajax.Request("<c:url value="/pages/reports/participantCareResults"/>", {
                    parameters:<tags:ajaxstandardparams/>+"&studyId=" + studyId + "&crfId=" + crfId +
                            "&studySiteId=" + studySiteId + "&participantId=" + participantId +
                            "&visitRange=" + visitRange + "&forVisits=" + forVisits + "&startDate=" + stDate + "&endDate=" + endDate,
                    onComplete:function(transport) {
                        showResultsTable(transport);
                        hideIndicator();
                    },
                    method:'get'
                })
            }
            if (format == 'graphical') {
                if (typeof(selectedTypes) == 'undefined') {
                    selectedTypes = '';
                }
                showIndicator();
                var request = new Ajax.Request("<c:url value="/pages/reports/participantCareResultsGraph"/>", {
                    parameters:<tags:ajaxstandardparams/>+"&symptomId=" + symptomId + "&selectedTypes=" + selectedTypes ,
                    onComplete:function(transport) {
                        showConfirmationWindow(transport, 800, 500);
                        hideIndicator();
                    },
                    method:'get'
                })
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
            new YUIAutoCompleter('studySiteInput', getOrganizations, handleSelect);
        });

        function getStudies(sQuery) {
            showIndicator("studyInput-indicator");
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            study.matchStudy(unescape(sQuery), callMetaData);
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
                    displayForms();
                    displaySites();
                    fnDisplayParticipants();
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
            participant.matchParticipantByStudySiteId(unescape(sQuery), $('studySite').value, $('study').value, callMetaData);
            hideIndicator("participantInput-indicator");
            return aResults;
        }
    </script>
</head>
<body>
<report:participantReportThirdlevelMenu selected="overallParticipant"/>
<tags:instructions code="participantReportInstructions"/>
<chrome:box title="report.label.search_criteria">
    <div align="left" style="margin-left: 50px">

        <div class="row" style="">
            <div class="label">
                <tags:requiredIndicator/><tags:message code='reports.label.study'/>
            </div>
            <div class="value">
                <input id="study" class="validate-NOTEMPTY" type="hidden" value="${study.id}" title="Study"
                       style="display: none;" name="study"/>
                <tags:yuiAutocompleter inputName="studyInput" value="" required="false" hiddenInputName="study"/>
            </div>
        </div>

        <div id="formDropDownDiv" style="display:none;" class="row">
            <div class="label"><tags:requiredIndicator/><tags:message code='reports.label.form'/></div>
            <div class="value IEdivValueHack" id="formDropDown"></div>
        </div>

        <div id="studySiteAutoCompleterDiv" style="display:none">
            <div class="row">
                <div class="label">
                    <tags:requiredIndicator/><tags:message code='reports.label.site'/>
                </div>
                <div class="value">
                    <input id="studySite" class="validate-NOTEMPTY" type="hidden" value="" title="Study site"
                           style="display:none;" name="studySite"/>
                    <tags:yuiAutocompleter inputName="studySiteInput" value="" required="false"
                                           hiddenInputName="studySite"/>
                </div>
            </div>
        </div>

        <div id="studySiteDiv" style="display:none">
            <input id="studySite" name="studySite" type="hidden">

            <div class="row">
                <div class="label"><tags:requiredIndicator/><tags:message code='reports.label.site'/></div>
                <div id="studySiteName" class="value"></div>
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
                <select id="visitOptions" name="visitOptions"
                        onChange="customVisit(this)">
                    <option value="all">All</option>
                    <option value="currentPrev">Current & Previous</option>
                    <option value="lastFour">Last four</option>
                    <option value="currentLast">Current & First</option>
                    <option value="custom">Custom</option>
                    <option value="dateRange">Date range</option>
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
                <tags:renderDate noForm="true" displayName="Start Date" propertyName="startDate"
                                 doNotShowFormat="true"/>
            </div>
            <div class="rightpanel">
                <tags:renderDate noForm="true" displayName="End Date" propertyName="endDate"
                                 doNotShowFormat="true"/>
            </div>
        </div>

        <br/>

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