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
<chrome:box title="Confirmation">

    <p><tags:instructions code="participant.participant_overview.top"/></p>
    <chrome:division title="Site">
			<b>${participantCommand.siteName}</b>
		</chrome:division>
    <chrome:division title="Demographic Information">
        
        <table border="0">
        <tr>
	        <td>
		        <div class="row">
                	<div class="label">First Name</div>
                	<div class="value">${participantCommand.participant.firstName}</div>
            	</div>
		        <div class="row">
                	<div class="label">Last Name</div>
                	<div class="value">${participantCommand.participant.lastName}</div>
            	</div>
		        <div class="row">
                	<div class="label">Middle Name</div>
                	<div class="value">${participantCommand.participant.middleName}</div>
            	</div>
		        <div class="row">
                	<div class="label">Patient Identifier</div>
                	<div class="value">${participantCommand.participant.assignedIdentifier}</div>
            	</div>
	        </td>
	        <td>
		       	<div class="row">
                	<div class="label">Date of Birth</div>
                	<div class="value">${participantCommand.participant.birthDate}</div>
            	</div>
		       	<div class="row">
                	<div class="label">Gender</div>
                	<div class="value">${participantCommand.participant.gender}</div>
            	</div>
		       	<div class="row">
                	<div class="label">Ethnicity</div>
                	<div class="value">${participantCommand.participant.ethnicity}</div>
            	</div>
		       	<div class="row">
                	<div class="label">Race</div>
                	<div class="value">${participantCommand.participant.race}</div>
            	</div>
		       	<div class="row">
                	<div class="label">&nbsp;</div>
                	<div class="value">&nbsp;</div>
            	</div>
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
                        <th scope="col">Patient Study Identifier</th>
                    </tr>
                    <c:forEach items="${participantCommand.participant.studyParticipantAssignments}" var="assignment">
                        <tr class="results">
 							<td>${assignment.studySite.study.assignedIdentifier}</td>
                            <td>${assignment.studySite.study.shortTitle}</td>
                            <td>${assignment.studyParticipantIdentifier}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </chrome:division>
        </c:if>





</chrome:box>

</body>
</html>
