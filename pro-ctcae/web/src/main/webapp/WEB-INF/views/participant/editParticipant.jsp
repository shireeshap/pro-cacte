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
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:javascriptLink name="extremecomponents"/>
    <tags:dwrJavascriptLink objects="study"/> 
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

        function buildTable(form) {
    		var type = 'site';
            $('bigSearch').show();
            var parameterMap = getParameterMap(form);
            var text = '${participantCommand.siteId}';
            study.searchStudiesForSelection(parameterMap, type, text, showTable);
        }

        Event.observe(window, 'load', function() {
        	buildTable('assembler');
        });
        
    </script>
</head>
<body>

<form:form method="post" commandName="participantCommand" id="assembler">
    <chrome:box title="participant.participant_details" autopad="true">
    <p><tags:instructions code="participant.participant_overview.top"/></p>
    	<chrome:division title="participant.label.site">
			<b>${participantCommand.siteName}</b>
		</chrome:division>
        <chrome:division title="participant.label.demographic_information">
        <input type="hidden" name="participant.id" value="${participantCommand.participant.id}"/>
   
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
		                         required="true" />
		        <tags:renderSelect propertyName="participant.gender" displayName="participant.label.gender"
		                         required="true" options="${genders}" />
		        <tags:renderSelect propertyName="participant.ethnicity" displayName="participant.label.ethnicity"
		                         required="true" options="${ethnicities}" />
		        <tags:renderSelect propertyName="participant.race" displayName="participant.label.race"
		                         required="true" options="${races}"/>
	        </td>
        </tr>
        </table>
        </chrome:division>
		<c:if test="${not empty participantCommand.participant.studyParticipantAssignments}">
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
        </c:if>
</chrome:box>
	<tags:tabControls willSave="true"/>
</form:form>
</body>
</html>
