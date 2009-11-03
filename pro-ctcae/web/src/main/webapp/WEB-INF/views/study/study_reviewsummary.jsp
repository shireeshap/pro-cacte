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
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <style type="text/css">
        div.row div.label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            width: 22em;
            padding: 1px;
        }

        td.clinicalstaff {
            border-right: none;
            vertical-align: top;
            text-align: right;
            width: 40%;
            font-weight: bold;
            padding-right: 1em;
        }

        a {
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

    </style>
    <script type="text/javascript">
        function goTab(tabnumber) {
            $('_target').name = "_target" + tabnumber;
            $('command').submit();
        }
    </script>
</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="false">
    <jsp:attribute name="singleFields">

        <chrome:division title="study.tab.study_details" linkontitle="javascript:goTab('1');"
                         linkurl="/study/editdetails">
            <div class="row">
                <div class="label"><tags:message code='study.label.assigned_identifier'/></div>
                <div class="value">${command.study.assignedIdentifier} </div>
            </div>
            <div class="row">
                <div class="label"><tags:message code='study.label.short_title'/></div>
                <div class="value">${command.study.shortTitle} </div>
            </div>
            <div class="row">
                <div class="label"><tags:message code='study.label.long_title'/></div>
                <div class="value">${command.study.longTitle} </div>
            </div>

            <div class="row">
                <div class="label"><tags:message code='study.label.description'/></div>
                <div class="value">${command.study.description} </div>
            </div>
            <div class="row">
                <div class="label"><tags:message code='study.label.study_sponsor'/></div>
                <div class="value">${command.study.studySponsor.organization.displayName} </div>
            </div>
            <div class="row">
                <div class="label"><tags:message code='study.label.study_coordinating_center'/></div>
                <div class="value">${command.study.dataCoordinatingCenter.organization.displayName} </div>
            </div>
            <div class="row">
                <div class="label"><tags:message code='study.label.study_funding_sponsor'/></div>
                <div class="value">${command.study.fundingSponsor.organization.displayName} </div>
            </div>
            <div class="row">
                <div class="label"><tags:message code='study.label.study_lead_site'/></div>
                <div class="value">${command.study.leadStudySite.organization.displayName} </div>
            </div>
        </chrome:division>
    <chrome:division title="study.section.study_arms" linkontitle="javascript:goTab('1');" linkurl="/study/editdetails">
        <div align="left" style="margin-left: 100px">
            <table width="75%" class="tablecontent">
                <tr id="sa-table-head" class="amendment-table-head">
                    <th width="30%" class="tableHeader"><spring:message code='study.label.arm.name' text=''/></th>
                    <th width="70%" class="tableHeader" style=" background-color: none"><spring:message
                            code='study.label.arm.desc' text=''/></th>
                </tr>
                <c:forEach items="${command.study.arms}" var="arm">
                    <c:if test="${arm.defaultArm eq 'false'}">
                        <tr>
                            <td style="border-right:none;">${arm.title}</td>
                            <td>${arm.description}</td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
    </chrome:division>
    <chrome:division title="study.section.study_sites" linkontitle="javascript:goTab('2');" linkurl="/study/editsites">
        <div align="left" style="margin-left: 100px">
            <table width="75%" class="tablecontent">
                <tr id="ss-table-head" class="amendment-table-head">
                    <th width="95%" class="tableHeader"><spring:message code='study.label.sites' text=''/></th>
                </tr>
                <tr>
                    <td>
                        <c:forEach items="${command.study.studySites}" var="studySite">
                            <c:if test="${studySite ne command.study.leadStudySite}">
                                <div class="row">
                                        ${studySite.organization.displayName}
                                </div>
                            </c:if>
                        </c:forEach>
                    </td>
                </tr>
            </table>
        </div>
    </chrome:division>
    <chrome:division title="study.tab.clinical_staff" linkontitle="javascript:goTab('3');"
                     linkurl="/study/editoverallstaff">
        <div class="row">
            <div class="label"><tags:message code="study.label.clinical.staff.odc"/></div>
            <div class="value">${command.study.overallDataCoordinator.displayName} </div>
        </div>
        <div class="row">
            <div class="label"><tags:message code="study.label.clinical.staff.lead.cra"/></div>
            <div class="value">${command.study.leadCRA.displayName} </div>
        </div>
        <div class="row">
            <div class="label"><tags:message code="study.label.clinical.staff.pi"/></div>
            <div class="value">${command.study.principalInvestigator.displayName} </div>
        </div>
    </chrome:division>
    <c:set var="tabnumber" value="1"/>
    <proctcae:urlAuthorize url="/study/editdetails"><c:set var="tabnumber" value="4"/></proctcae:urlAuthorize>

    <chrome:division title="study.tab.study_site_clinical_staff" linkontitle="javascript:goTab('${tabnumber}');"
                     linkurl="/study/editsitestaff">
        <c:forEach items="${command.study.studySites}" var="studySite">
            <div align="left" style="margin-left: 100px">
                <table width="75%" class="tablecontent">
                    <tr id="ss-table-head" class="amendment-table-head">
                        <th width="95%" class="tableHeader" colspan="2">${studySite.displayName}</th>
                    </tr>
                    <tr>
                        <td class="clinicalstaff">
                            <tags:message code="study.label.clinical.staff.lead.site_pi"/>
                        </td>
                        <td>
                            <c:forEach items="${studySite.sitePIs}" var="sitePI">
                                ${sitePI.displayName}<br/>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td class="clinicalstaff">
                            <tags:message code="study.label.clinical.staff.lead.site_cra"/>
                        </td>
                        <td>
                            <c:forEach items="${studySite.siteCRAs}" var="siteCRA">
                                ${siteCRA.displayName}<br/>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td class="clinicalstaff">
                            <tags:message code="study.label.clinical.staff.lead.treating_physican"/>
                        </td>
                        <td>
                            <c:forEach items="${studySite.treatingPhysicians}" var="treatingPhysician">
                                ${treatingPhysician.displayName}<br/>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td class="clinicalstaff">
                            <tags:message code="study.label.clinical.staff.lead.nurse"/>
                        </td>
                        <td>
                            <c:forEach items="${studySite.researchNurses}" var="researchNurse">
                                ${researchNurse.displayName}<br/>
                            </c:forEach>
                        </td>
                    </tr>
                </table>
            </div>
            <br/>
        </c:forEach>
    </chrome:division>

</jsp:attribute>
</tags:tabForm>

</body>
</html>
