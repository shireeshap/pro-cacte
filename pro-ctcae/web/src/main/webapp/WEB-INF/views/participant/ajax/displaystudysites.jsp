<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style type="text/css">
    .tableHeader {
        background-color: #2B4186;
        background-image: url( /ctcae/images/blue/eXtableheader_bg.png );
        background-position: center top;
        background-repeat: repeat-x;
        color: white;
        font-size: 13px;
        font-weight: bold;
        margin: 0;
        padding: 4px 3px;
        text-align: left;
    }

    table.widget {
        width: 100%;
        background-color: #e9e8e8;
        border-top: 1px solid #999999;
        border-right: 1px solid #999999;
    }

    td.data {
        vertical-align: top;
        border-bottom: 1px solid #999999;
        border-left: 1px solid #999999;
    }
</style>
<table cellpadding="0" width="100%">
    <tr>
        <td class="tableHeader">
            Select
        </td>
        <td class="tableHeader">
            Study
        </td>
        <td class="tableHeader">
            Patient study identifier
        </td>


        <c:forEach items="${studyparticipantassignments}" var="studyParticipantAssignment" varStatus="spastatus">
        <c:set var="studysite" value="${studyParticipantAssignment.studySite}"/>

        <td class="tableHeader">
            Treatment End Date
        </td>
    </tr>
    <tr>
        <td>
        </td>
        <td>
            [${studysite.study.assignedIdentifier}] ${studysite.study.shortTitle}
        </td>
        <td>
                ${studyParticipantAssignment.studyParticipantIdentifier}
        </td>
        <td>
            <c:choose>
                <c:when test="${studyParticipantAssignment.offTreatmentDate ne null}">
                    <tags:formatDate value="${studyParticipantAssignment.offTreatmentDate}"/>
                </c:when>
                <c:otherwise>
                    <a href="javascript:participantOffStudy(${studyParticipantAssignment.id})">Set treatment end
                        date...</a>
                </c:otherwise>
            </c:choose>
        </td>

    </tr>
    <tr id="forms_${studysite.id}">
        <td>
            &nbsp;
        </td>
        <td colspan="4">
            <table class="widget" cellspacing="0">
                <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="spacrf"
                           varStatus="spacrfstatus">
                    <tr>
                        <td class="data" width="50%" align="left">
                            &nbsp;&nbsp;<b>Form: </b>${spacrf.crf.title}
                        </td>
                        <td class="data" align="center">
                            <b><spring:message code="participant.label.startdate"/></b><tags:formatDate
                                value="${spacrf.startDate}"/>
                        </td>

                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
    </c:forEach>
    <c:forEach items="${unselectedstudysites}" var="studysite">
        <c:forEach items="${studysite.study.crfs}" var="crf">
            <c:if test="${crf.status eq 'Released' and crf.nextVersionId eq null}">
                <c:set var="hasforms" value="true"/>
            </c:if>
        </c:forEach>
        <tr>
            <td>
                <input type="checkbox" name="studySites" value="${studysite.id}"
                       onchange="javascript:showForms(this, '${studysite.id}')"/>

            </td>
            <td>
                [${studysite.study.assignedIdentifier}] ${studysite.study.shortTitle}
            </td>
            <td>
                <input type="text" name="participantStudyIdentifier_${studysite.id}" value="">
            </td>
        </tr>
        <c:if test="${hasforms eq 'true'}">
            <c:set var="hasforms" value="false"/>
            <tr id="forms_${studysite.id}" style="display:none">
                <td>
                    &nbsp;
                </td>
                <td colspan="4">
                    <table class="widget" cellspacing="0">
                        <c:forEach items="${studysite.study.crfs}" var="crf">
                            <c:if test="${crf.status eq 'Released' and crf.nextVersionId eq null}">
                                <tr>
                                    <td class="data" width="40%" align="left">
                                        &nbsp;&nbsp;<b>Form: </b>${crf.title}
                                    </td>
                                    <td class="data" align="center">
                                        <b><spring:message code="participant.label.startdate"/></b> <tags:renderDate
                                            propertyName="form_date_${crf.id}"
                                            doNotshowLabel="true"
                                            noForm="true"/>
                                    </td>

                                </tr>
                            </c:if>
                        </c:forEach>
                    </table>
                </td>
            </tr>
        </c:if>
    </c:forEach>
</table>