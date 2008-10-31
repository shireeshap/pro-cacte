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
    <chrome:box title="Patient Details" autopad="true">

        <p><tags:instructions code="participant.participant_details.top"/></p>

        <chrome:division title="Site">
            <tags:renderSelect propertyName="siteId" displayName="Site"
                               required="true" options="${studysites}"/>
        </chrome:division>

        <chrome:division title="Demographic information">

            <table border="0" style="width:100%">
                <tr>
                    <td>
                        <tags:renderText propertyName="participant.firstName" displayName="First name"
                                         required="true"/>
                        <tags:renderText propertyName="participant.lastName" displayName="Last name"
                                         required="true"/>
                        <tags:renderText propertyName="participant.middleName" displayName="Middle name"/>
                        <tags:renderText propertyName="participant.assignedIdentifier" displayName="Patient identifier"
                                         required="true"/>
                    </td>
                    <td>
                        <tags:renderDate propertyName="participant.birthDate" displayName="Date of birth"
                                         required="true"/>
                        <tags:renderSelect propertyName="participant.gender" displayName="Gender"
                                           required="true" options="${genders}"/>
                        <tags:renderSelect propertyName="participant.ethnicity" displayName="Ethnicity"
                                           required="true" options="${ethnicities}"/>
                        <tags:renderSelect propertyName="participant.race" displayName="Race"
                                           required="true" options="${races}"/>
                    </td>
                </tr>
            </table>
        </chrome:division>
        <chrome:division title="Studies">
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