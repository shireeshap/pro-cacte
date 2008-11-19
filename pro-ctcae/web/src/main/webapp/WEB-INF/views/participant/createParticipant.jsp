<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<link rel="stylesheet" type="text/css"
      href="<c:url value="/css/extremecomponents.css"/>">
<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:javascriptLink name="extremecomponents"/>
    <tags:dwrJavascriptLink objects="study"/>

    <script>

        function buildTable(form) {

            var type = 'site';
            $('bigSearch').show();
            var parameterMap = getParameterMap(form);
            var text = parameterMap['siteId'];
            if (text != '') {
                study.searchStudiesForSelection(parameterMap, type, text, showTable);
            }
        }

        Event.observe(window, 'load', function() {
            buildTable('assembler');
            Event.observe('siteId', 'change', function() {
                buildTable('assembler');
            })
        });

    </script>
</head>
<body>

<form:form method="post" commandName="participantCommand" id="assembler">
    <chrome:box title="participant.participant_details" autopad="true">
        <tags:hasErrorsMessage hideErrorDetails="false"        />

        <p><tags:instructions code="participant.participant_details.top"/></p>

        <chrome:division title="participant.label.site">
            <tags:renderSelect propertyName="siteId" displayName="participant.label.site"
                               required="true" options="${studysites}"/>
        </chrome:division>

        <chrome:division title="participant.label.demographic_information">

            <table border="0" style="width:100%">
                <tr>
                    <td>
                        <tags:renderText propertyName="participant.firstName" displayName="participant.label.first_name"
                                         required="true"/>
                        <tags:renderText propertyName="participant.lastName" displayName="participant.label.last_name"
                                         required="true"/>
                        <tags:renderText propertyName="participant.middleName" displayName="participant.label.middle_name"/>
                        <tags:renderText propertyName="participant.assignedIdentifier" displayName="participant.label.participant_identifier"
                                         required="true"/>
                    </td>
                    <td>
                        <tags:renderDate propertyName="participant.birthDate" displayName="participant.label.date_of_birth"
                                         required="true"/>
                        <tags:renderSelect propertyName="participant.gender" displayName="participant.label.gender"
                                           required="true" options="${genders}"/>
                        <tags:renderSelect propertyName="participant.ethnicity" displayName="participant.label.ethnicity"
                                           required="true" options="${ethnicities}"/>
                        <tags:renderSelect propertyName="participant.race" displayName="participant.label.race"
                                           required="true" options="${races}"/>
                    </td>
                </tr>
            </table>
        </chrome:division>
        <chrome:division title="participant.label.studies">
            <tags:indicator id="indicator"/>
        </chrome:division>

        <div id="bigSearch" style="display:none;">
            <div class="endpanes"/>
            <chrome:division id="single-fields">
                <div id="tableDiv">
                    <c:out value="${assembler}" escapeXml="false"/>
                </div>
            </chrome:division>
        </div>
        </div>
    </chrome:box>
    <tags:tabControls willSave="true"/>
</form:form>
</body>
</html>