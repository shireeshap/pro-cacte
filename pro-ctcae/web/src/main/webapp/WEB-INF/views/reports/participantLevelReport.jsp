<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator"
          prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ctcae"
           uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<html>
<head>
	<tags:stylesheetLink name="reports"/>
    <tags:dwrJavascriptLink objects="crf"/>
    <tags:dwrJavascriptLink objects="participant"/>
    <tags:includePrototypeWindow/>
    <tags:includeScriptaculous/>
    <tags:javascriptLink name="reports_common"/>
    <script type="text/javascript">
        displayParticipants = true;
        displaySymptom = false;
        studySiteMandatory = true;
        function initializeFields() {
        <c:if test="${study ne null}">
        initializeAutoCompleter('study', '${study.displayName}', ${study.id});
        displayForms(${crf.id});
        displaySites();
        initializeAutoCompleter('studySite', '${studySite.displayName}', ${studySite.id});
        fnDisplayParticipants();
        initializeAutoCompleter('participant', '${participant.displayName}', ${participant.id});
        setTimeout("participantCareResults();", 2000);
        </c:if>
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
                    parameters:"studyId=" + studyId + "&crfId=" + crfId +
                               "&studySiteId=" + studySiteId + "&participantId=" + participantId +
                               "&visitRange=" + visitRange + "&forVisits=" + forVisits + "&startDate=" + stDate + "&endDate=" + endDate +
                               "&subview=subview",
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
                    parameters:"symptomId=" + symptomId + "&selectedTypes=" + selectedTypes +
                               "&subview=subview",
                    onComplete:function(transport) {
                        showConfirmationWindow(transport, 850, 570);
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
                    selectedTypes = selectedTypes + ',' + obj[i].value;
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
</head>
<body>
<tags:instructions code="participantReportInstructions"/>
<chrome:box title="participant.label.search_criteria">
    <div align="left" style="margin-left: 50px">
        <tags:renderAutocompleter propertyName="study"
                                  displayName="Study"
                                  required="true"
                                  size="100"
                                  noForm="true"/>
        <div id="formDropDownDiv" style="display:none;" class="row">
            <div class="label">Form</div>
            <div class="value" id="formDropDown"></div>
        </div>
        <div id="studySiteAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="studySite"
                                      displayName="Study site"
                                      size="60"
                                      noForm="true" required="true"/>
        </div>
        <div id="studySiteDiv" style="display:none" class="row">
            <div class="label">Study site <input id="studySite" name="studySite" type="hidden"></div>
            <div id="studySiteName" class="value"></div>
        </div>
        <div id="participantAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="participant"
                                      displayName="Participant"
                                      size="40"
                                      noForm="true" required="true"/>
        </div>
        <div id="dateMenuDiv" style="display:none" class="row">
            <div class="label">Visits</div>
            <div class="value">
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

        <div id="search" style="display:none" class="row">
            <div class="value"><tags:button color="blue" value="Search"
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