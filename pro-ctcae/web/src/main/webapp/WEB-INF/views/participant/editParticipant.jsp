<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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

</head>
<body>

<form:form method="post" commandName="participantCommand">
    <chrome:box title="Patient Details" autopad="true">
    <p><tags:instructions code="participant.participant_overview.top"/></p>
    
        <chrome:division title="Demographic Information">
        
        <table border="0">
        <tr>
	        <td>
		        <tags:renderText propertyName="participant.firstName" displayName="First Name"
		                         required="true" size="50" />
		        <tags:renderText propertyName="participant.lastName" displayName="Last Name"
		                         required="true" size="50"/>
		       	<tags:renderText propertyName="participant.maidenName" displayName="Maiden Name"
		                         size="50"/>
		        <tags:renderText propertyName="participant.middleName" displayName="Middle Name"
		                         size="50"/>
		        <tags:renderText propertyName="participant.assignedIdentifier" displayName="Assigned Identifier"
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
            <chrome:division title="Assigned Study">
                <table class="tablecontent">
                    <tr>
                        <th scope="col">Study Identifier</th>
                        <th scope="col">Study Short Title</th>
                        <th scope="col">Site</th>
                    </tr>
                    <c:forEach items="${participantCommand.participant.studyParticipantAssignments}" var="assignment">
                        <tr class="results">
 							<td>${assignment.studySite.study.assignedIdentifier}</td>
                            <td>${assignment.studySite.study.shortTitle}</td>
                            <td>${assignment.studySite.organization.name}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </chrome:division>
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
