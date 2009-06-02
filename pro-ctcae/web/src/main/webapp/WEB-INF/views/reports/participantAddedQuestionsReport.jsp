<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator"
          prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="report" tagdir="/WEB-INF/tags/reports" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ctcae"
           uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<html>
<head>
    <tags:dwrJavascriptLink objects="crf"/>
    <tags:includePrototypeWindow/>
    <tags:includeScriptaculous/>
    <tags:javascriptLink name="reports_common"/>
    <script type="text/javascript">
        displaySymptom = false;
        displayDate = false;
        function reportResults() {
            if (!performValidations()) {
                return;
            }
            var studyId = $('study').value;
            var crfSelect = $('formSelect');
            var crfId = crfSelect.options[crfSelect.selectedIndex].value;
            var studySiteId = $('studySite').value;
            showIndicator();
            var request = new Ajax.Request("<c:url value="/pages/reports/participantAddedQuestionsResults"/>", {
                parameters:"studyId=" + studyId + "&crfId=" + crfId +
                           "&studySiteId=" + studySiteId +
                           "&subview=subview",
                onComplete:function(transport) {
                    showResults(transport);
                    hideIndicator();
                },
                method:'get'
            })
        }
    </script>
</head>
<body>
<report:thirdlevelmenu selected="participantAddedQuestions"/>
<chrome:box title="participant.label.search_criteria">
    <div align="left" style="margin-left: 50px">
        <tags:renderAutocompleter propertyName="study"
                                  displayName="Study"
                                  required="true"
                                  size="60"
                                  noForm="true"/>
        <div id="formDropDownDiv" style="display:none;" class="row">
            <div class="label">Form</div>
            <div class="value" id="formDropDown"></div>
        </div>
        <div id="studySiteAutoCompleterDiv" style="display:none">
            <tags:renderAutocompleter propertyName="studySite"
                                      displayName="Study site"
                                      size="60"
                                      noForm="true"/>
        </div>
        <div id="studySiteDiv" style="display:none" class="row">
            <div class="label">Study site <input id="studySite" name="studySite" type="hidden"></div>
            <div id="studySiteName" class="value"></div>
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