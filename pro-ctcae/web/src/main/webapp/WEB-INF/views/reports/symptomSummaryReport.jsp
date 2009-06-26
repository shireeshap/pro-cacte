<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="report" tagdir="/WEB-INF/tags/reports" %>

<html>
<head>
    <tags:dwrJavascriptLink objects="crf"/>
    <tags:includePrototypeWindow/>
    <tags:includeScriptaculous/>
    <tags:javascriptLink name="reports_common"/>
    <tags:javascriptLink name="table_menu"/>
    <script type="text/javascript">
        function showResponses(id) {
            var request = new Ajax.Request("<c:url value="/pages/participant/showCompletedCrf"/>", {
                parameters:"id=" + id + "&subview=subview",
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 700, 500);
                },
                method:'get'
            }
                    )
        }

        function showDetails(params) {
            showIndicator();
            var request = new Ajax.Request("<c:url value="/pages/reports/showDetails"/>", {
                parameters:params,
                onComplete:function(transport) {
                    $('reportInnerDiv').innerHTML = transport.responseText;
                    hideIndicator();
                },
                method:'get'
            }
                    )
        }

        function reportResults(attributes) {

            if (!performValidations()) {
                return;
            }
            if(typeof(attributes) == 'undefined'){
                attributes='';
            }
            var visitRangeSelect = $('visitOptions');
            var visitRange = visitRangeSelect.options[visitRangeSelect.selectedIndex].value;
            var stDate = $('startDate').value;
            var endDate = $('endDate').value;
            showIndicator();
            var request = new Ajax.Request("<c:url value="/pages/reports/symptomSummaryReportResults"/>", {
                parameters:"crfId=" + $('formSelect').value +
                           "&symptom=" + $('symptomSelect').value +
                           "&gender=all" +
                           "&studySiteId=" + $('studySite').value +
                           "&visitRange=" + visitRange +
                           "&startDate=" + stDate +
                           "&endDate=" + endDate +
                           "&attributes=" + attributes+
                           "&subview=subview",
                onComplete:function(transport) {
                    showResults(transport);
                    hideIndicator();
                },
                method:'get'
            }
                    )
        }
        function updateChart(chkbox) {
            var obj = document.getElementsByName('attribute');
            var selectedAttributes = '';
            for (var i = 0; i < obj.length; i++) {
                if (obj[i].checked) {
                    selectedAttributes = selectedAttributes + ',' + obj[i].value;
                }
            }
            if (selectedAttributes == '') {
                alert('Please select at least one question type.');
                chkbox.checked = true;
                return;
            }
            reportResults(selectedAttributes);
        }

    </script>
</head>
<body>
<report:thirdlevelmenu selected="symptomsummary"/>
<chrome:box title="participant.label.search_criteria">
    <div align="left" style="margin-left: 50px">
        <tags:renderAutocompleter propertyName="study" displayName="Study" required="true" size="80" noForm="true"/>
        <div id="formDropDownDiv" style="display:none;" class="row">
            <div class="label">Form</div>
            <div class="value" id="formDropDown"></div>
        </div>
        <div id="symptomDropDownDiv" style="display:none;" class="row">
            <div class="label">Symptom</div>
            <div class="value" id="symptomDropDown"></div>
        </div>
        <div id="studySiteAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="studySite" displayName="Study site" size="60" noForm="true"/>
        </div>
        <div id="studySiteDiv" style="display:none" class="row">
            <div class="label">Study site <input id="studySite" name="studySite" type="hidden"></div>
            <div id="studySiteName" class="value"></div>
        </div>
        <div id="dateMenuDiv" style="display:none" class="row">
            <div class="label">Date range</div>
            <div class="value">
                <select id="visitOptions" name="visitOptions"
                        onChange="customVisit(this)">
                    <option value="all">All dates</option>
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
                                 doNotShowFormat="true" required="true"/>
            </div>
            <div class="rightpanel">
                <tags:renderDate noForm="true" displayName="End Date" propertyName="endDate"
                                 doNotShowFormat="true" required="true"/>
            </div>
        </div>

        <div id="search" style="display:none" class="row">
            <div class="value"><tags:button color="blue" value="Search"
                                            onclick="reportResults()" size="big"
                                            icon="search"/>
                <tags:indicator id="indicator"/>
            </div>
        </div>

    </div>
</chrome:box>
<div id="reportOuterDiv" style="display:none;">
    <div>
        <div id="reportInnerDiv"/>
    </div>
</div>
</body>
</html>