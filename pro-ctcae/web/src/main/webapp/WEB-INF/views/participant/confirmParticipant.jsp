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
<chrome:flashMessage flashMessage="participant.flash.save"></chrome:flashMessage>

<chrome:box title="participant.label.confirmation">

    <chrome:division title="participant.label.site">
        <b>${participantCommand.siteName}</b>
    </chrome:division>
    <chrome:division title="participant.label.demographic_information">

        <table border="0" style="width:100%">
            <tr>
                <td>
                    <div class="row">
                        <div class="label"><spring:message code='participant.label.first_name' text=''/></div>
                        <div class="value">${participantCommand.participant.firstName}</div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code='participant.label.last_name' text=''/></div>
                        <div class="value">${participantCommand.participant.lastName}</div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code='participant.label.middle_name' text=''/></div>
                        <div class="value">${participantCommand.participant.middleName}</div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code='participant.label.participant_identifier'
                                                           text=''/></div>
                        <div class="value">${participantCommand.participant.assignedIdentifier}</div>
                    </div>
                </td>
                <td>
                    <div class="row">
                        <div class="label"><spring:message code='participant.label.date_of_birth' text=''/></div>
                        <div class="value"><tags:formatDate value="${participantCommand.participant.birthDate}"/></div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code='participant.label.gender' text=''/></div>
                        <div class="value">${participantCommand.participant.gender}</div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code='participant.label.ethnicity' text=''/></div>
                        <div class="value">${participantCommand.participant.ethnicity}</div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code='participant.label.race' text=''/></div>
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
        <chrome:division title="participant.label.assigned_studies">
            <table class="tablecontent">
                <tr>
                    <th scope="col"><spring:message code='participant.label.study_identifier' text=''/></th>
                    <th scope="col"><spring:message code='participant.label.study_short_title' text=''/></th>
                    <th scope="col"><spring:message code='participant.label.participant_study_identifier' text=''/></th>
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
