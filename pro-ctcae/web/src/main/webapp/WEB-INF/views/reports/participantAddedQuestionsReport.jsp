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
    <tags:dwrJavascriptLink objects="participant"/>
    <tags:includePrototypeWindow/>
    <tags:includeScriptaculous/>
    <script type="text/javascript">
        Event.observe(window, "load", function () {
            var studyAutoCompleter = new studyAutoComplter('study');
            acCreateStudyMonitor(studyAutoCompleter);
            initSearchField();
        })

        function acCreateStudyMonitor(mode) {
            new Autocompleter.DWR(mode.basename + "-input", mode.basename +
                                                            "-choices",
                    mode.populator, {
                valueSelector: mode.valueSelector,
                afterUpdateElement: function(inputElement, selectedElement,
                                             selectedChoice) {
                    acPostSelect(mode, selectedChoice);
                    displayForms();
                    displaySites();
                },
                indicator: mode.basename + "-indicator"
            })

        }
        function displayForms() {
            var id = $('study').value
            crf.getReducedCrfs(id, updateFormDropDown)
        }

        function clearDiv(divid) {
            var children = $(divid).childElements();
            for (i = 0; i < children.length; i++) {
                $(children[i]).remove();
            }
        }

        function updateFormDropDown(crfs) {
            //            clearDiv('formDropDown');
            var formDropDown = new Element('SELECT', {'id':'formSelect'})

            for (var i = 0; i < crfs.length; i++) {
                var crf = crfs[i];
                var option = new Element('OPTION', {});
                option.text = crf.title;
                option.value = crf.id;
                formDropDown.appendChild(option);
            }

            $('formDropDown').appendChild(formDropDown);
            $('formDropDownDiv').show();
            $('search').show();
        }


        function displaySites() {

            organization.matchOrganizationByStudyId('%', $('study').value, function(values) {
                var siteNum = values.length;
                var myStudySiteAutoComplter = new studySiteAutoComplter
                        ('studySite', $('study').value);
                acCreate(myStudySiteAutoComplter);
                initSearchField();
                if (siteNum > 1) {
                    $('studySiteAutoCompleterDiv').show();
                } else {
                    $('studySite').value = values[0].id;
                    $('studySiteName').innerHTML = values[0].displayName;
                    $('studySiteDiv').show();
                }
            })
        }


        function studyLevelReportResults() {
            var studyId = $('study').value;
            var crfSelect = $('formSelect');
            var crfId = crfSelect.options[crfSelect.selectedIndex].value;
            var studySiteId = $('studySite').value;

            if (hasError) {
                return;
            }
                showIndicator();
                var request = new Ajax.Request("<c:url value="/pages/reports/participantAddedQuestionsResults"/>", {
                    parameters:"studyId=" + studyId + "&crfId=" + crfId +
                               "&studySiteId=" + studySiteId +
                               "&subview=subview",
                    onComplete:function(transport) {
                        showResultsTable(transport);
                        hideIndicator();
                    },
                    method:'get'
                })
        }

        function showResultsTable(transport) {
            $('displayParticipantCareResults').show();
            $('displayResultsTable').innerHTML = transport.responseText;
        }

        var hasError = false;
        function showError(element) {
            hasError = true;
            removeError(element);
            new Insertion.Bottom(element.parentNode, " <ul id='" + element.name + "-msg'class='errors'><li>" + 'Missing ' + element.title + "</li></ul>");
        }
        function removeError(element) {
            msgId = element.name + "-msg"
            $(msgId) != null ? new Element.remove(msgId) : null
        }

        function showIndicator() {
            $('indicator').style.visibility = 'visible';
        }
        function hideIndicator() {
            $('indicator').style.visibility = 'hidden';
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
                                            onclick="studyLevelReportResults()" size="big"
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