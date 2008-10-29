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
    
    <title>Patient Details</title>
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
    <chrome:box title="Patient Details" autopad="true">
    <p><tags:instructions code="participant.participant_overview.top"/></p>
    	<chrome:division title="Site">
			<b>${participantCommand.siteName}</b>
		</chrome:division>
        <chrome:division title="Demographic Information">
        <input type="hidden" name="participant.id" value="${participantCommand.participant.id}"/>
   
        <table border="0">
        <tr>
	        <td>
		        <tags:renderText propertyName="participant.firstName" displayName="First Name"
		                         required="true" size="50" />
		        <tags:renderText propertyName="participant.lastName" displayName="Last Name"
		                         required="true" size="50"/>
		        <tags:renderText propertyName="participant.middleName" displayName="Middle Name"
		                         size="50"/>
		        <tags:renderText propertyName="participant.assignedIdentifier" displayName="Patient Identifier"
		                         required="true" size="50"/>
	        </td>
	        <td>
		        <tags:renderDate propertyName="participant.birthDate" displayName="Date of birth"
		                         required="true" />
		        <tags:renderSelect propertyName="participant.gender" displayName="Gender" 
		                         required="true" options="${genders}" />
		        <tags:renderSelect propertyName="participant.ethnicity" displayName="Ethnicity"
		                         required="true" options="${ethnicities}" />
		        <tags:renderSelect propertyName="participant.race" displayName="Race"
		                         required="true" options="${races}"/>
	        </td>
        </tr>
        </table>
        </chrome:division>
		<c:if test="${not empty participantCommand.participant.studyParticipantAssignments}">
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
        </c:if>
        <div class="row">
          	<div class="submit">
              	<input type="submit" id="submitButton" value="Save"/>
          	</div>
      	</div>
</chrome:box>
</form:form>
</body>
</html>
