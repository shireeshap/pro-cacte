<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<link rel="stylesheet" type="text/css"
      href="<c:url value="/css/extremecomponents.css"/>">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <tags:javascriptLink name="extremecomponents"/>
    <tags:dwrJavascriptLink objects="participant"/>
    <style type="text/css">
        .label {
            width: 12em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
        }

        #studyDetails td.label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            width: 12em;
            padding: 1px;
        }
    </style>

    <script>
        function showPopUpMenuParticipant(pid) {
            var html = '<div id="search-engines"><ul>';
        <proctcae:urlAuthorize url="/pages/participant/edit">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/participant/edit"/>?id=' + pid + '\'">Edit/View participant</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/participant/schedulecrf"/>?pId=' + pid + '\'">Manage schedule</a></li>';
        </proctcae:urlAuthorize>

            html += '</ul></div>';
            jQuery('#participantActions' + pid).menu({
                content: html,
                maxHeight: 180,
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


        function showTableLocal(table) {
            $('indicator').className = 'indicator';
            $('tableDiv').insert(table);
        }

        function buildTable(form) {

             $('tableDiv').innerHTML='';
            var firstName = $F('firstName')
            var lastName = $F('lastName')
            var identifier = $F('identifier')
            var study = $F('study')
            if (firstName == '' && lastName == '' && identifier == '' && study == '') {
                $('error').innerHTML = "<font color='#FF0000'>Provide at least one value in the search field</font>";

            } else {
                $('error').innerHTML = ""
                $('bigSearch').show()
                $('indicator').className = '';
                var parameterMap = getParameterMap(form);
                participant.searchParticipant(parameterMap, firstName, lastName, identifier, study, showTableLocal);
            }
        }

        function navigate(e) {
            var mye;
            if (e) {
                mye = e;
            } else {
                mye = event;
            }
            if (mye.keyCode == 13)  //enter pressed
                doSend();
        }
        document.onkeypress = navigate;
        function doSend() {
            buildTable('assembler');
        }
        Event.observe(window, "load", function() {
            $('firstName').value = '%';
            buildTable('assembler');
            $('firstName').value = '';

            var sac = new studyAutoCompleter('study');
            acCreateStudy(sac);

        <c:if test="${study ne null}">
            initializeAutoCompleter('study',
                    '${study.displayName}', '${study.id}')
        </c:if>
            initSearchField();

        })

        function acCreateStudy(mode) {
            new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices",
                    mode.populator, {
                valueSelector: mode.valueSelector,
                afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                    acPostSelect(mode, selectedChoice);
                },
                indicator: mode.basename + "-indicator"
            })

        }

    </script>
</head>
<body>
<chrome:box title="participant.label.search_criteria" autopad="true">
    <p><tags:instructions code="participant.search.top"/></p>


    <div class="row">
        <div class="label"><spring:message code='participant.label.first_name' text=''/></div>
        <div class="value"><input type="text" id="firstName" name="firstName" maxlength="30"/></div>
    </div>
    <div class="row">
        <div class="label"><spring:message code='participant.label.last_name' text=''/></div>
        <div class="value"><input type="text" id="lastName" name="lastName" maxlength="30"/></div>
    </div>
    <div class="row">
        <div class="label"><spring:message code='participant.label.participant_identifier' text=''/></div>
        <div class="value"><input type="text" id="identifier" name="identifier" maxlength="30"/></div>
    </div>

    <tags:renderAutocompleter propertyName="study"
                              displayName="Study"
                              required="false"
                              size="100"
                              noForm="true"/>
    <div id="error"></div>
    <div class="row">
        <div class="label"></div>
        <div class="value">
            <tags:button color="blue" icon="search" type="button" onclick="buildTable('assembler');" value='Search'/>
            <tags:indicator id="indicator"/>
        </div>
    </div>
</chrome:box>

<div id="bigSearch" style="display:none;">
    <div class="endpanes"/>
    <chrome:box title="Results">
        <p><tags:instructions code="study.search.results"/></p>
        <form:form id="assembler">
            <chrome:division id="single-fields">
                <div id="tableDiv">
                    <c:out value="${assembler}" escapeXml="false"/>
                </div>
            </chrome:division>
        </form:form>
    </chrome:box>
</div>


</body>
</html>