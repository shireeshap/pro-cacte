<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<html>
<head>
    <tags:stylesheetLink
            name="table_menu"/><tags:includeScriptaculous/><tags:includePrototypeWindow/><tags:formBuilder/><tags:formActionMenu/>
    <style type="text/css">

        .quicklink {
            border-bottom: 1px solid #cccccc;
            padding-left: 15px;
        }

        .bold {
            font-weight: bold;
            color: black;
        }

        .bold a {
            font-weight: bold;
        }

        table.outer td {
            width: 50%;
            vertical-align: top;
        }

        .delete {
            font-weight: bold;
            color: red;
            text-decoration: none;
        }

        .even {
            background-color: #ffffff;
        }

        * {
            zoom: 1;
        }

        /*#alertsdiv {*/
        /*width: 455px;*/
        /*overflow-y: auto;*/
        /*max-height: 400px;*/
        /*}*/

        table.widget {
            border: none;
        }

        .panel {
            width: 470px;
            float: left;
            margin: 4px;
            display: inline;
        }

        a.quickLink, a.quickLink:visited {
            color: #518EC2;
            font-weight: bold;
            font-size: 14px;
            text-decoration: none;
        }

        img.quickLink {
            /*
                    padding-right: 20px;
                    padding-left: 20px;
            */
        }

        div.quickLinkRow {
            display: block;
            clear: both;
        }

        div.quickLinkRow div.quickLinkPicture {
            float: left;
            width: 40px;
            text-align: right;
        }

        div.quickLinkRow div.quickLinkLabel {
            margin-left: 50px;
            text-align: left;
            vertical-align: middle;
        }

        td.quickLinkBGon {
            background-image: url("../images/blue/icons/quickLinkBGon.png")
        }

        td.quickLinkBGoff {
            background-image: url("../images/blue/icons/quickLinkBGoff.png")
        }

        tr.taskTitleRow th {
            color: #518EC2;
            font-weight: bold;
        }

        td.header-top1 {
            font-weight: bold;
        }

        tr.taskTitleRow td, tr.taskTitleRow th {
            border-bottom: 1px #ccc solid;
        }

        a.linkHere, a.linkHere:hover {
            color: blue;
            text-decoration: underline;
        }


    </style>
    <!--[if IE]>
        <style>
        div.row div.value {
        margin-left:7px;
        }
        a.fg-button {
        display:block;
        position:static;
        width:65px;
        padding-right:0;
        padding-left:0;
        }
        .fg-button-icon-right .ui-icon {
        position:static;
        float:right;
        padding:0;
        margin:0;
        left:auto;
        right;auto;
        }
        </style>
    <![endif]-->
    <script type="text/javascript">

        function showMessage(id) {
            var request = new Ajax.Request("<c:url value="/pages/home/notificationdetails"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id ,
                onComplete:function(transport) {
                    $('tr_' + id).removeClassName('bold');
                    showConfirmationWindow(transport, 700, 500);
                },
                method:'get'
            })
        }
        function completedForm(id) {
            var request = new Ajax.Request("<c:url value="/pages/participant/showCompletedCrf"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id ,
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 700, 500);
                },
                method:'get'
            })
        }

        function deleteMsg(id, uuid) {
            if (uuid != '') {
                var request = new Ajax.Request("<c:url value="/public/removealert"/>", {
                    parameters:<tags:ajaxstandardparams/>+"&uuid=" + uuid,
                    onComplete:function(transport) {
                        $('tr_' + id).remove();
                    },
                    method:'get'
                })
            }
        }

        jQuery("td.quickLinkBGon").mouseover(function() {
            jQuery(this).removeClass('quickLinkBGon');
            jQuery(this).addClass('quickLinkBGoff');
        });

        jQuery("td.quickLinkBGon").mouseout(function() {
            jQuery(this).removeClass('quickLinkBGoff');
            jQuery(this).addClass('quickLinkBGon');
        });

        function moreRows() {
        <c:set var="showMore" value="false"/>
            refreshPage();
        }

    </script>
</head>
<body>
<c:forEach items="${notifications}" var="usernotification">
    <c:if test="${!usernotification.markDelete}">
        <c:set var="numberofalerts" scope="page" value="${numberofalerts + 1}"/>
    </c:if>
</c:forEach><c:set var="dl" value="1200"/>
<%--<div class="panel">--%>
<table width="100%" border="0">
<tr>
<td width="80%" valign="top">

    <c:if test="${studyLevelRole || siteLevelRole || nurseLevelRole}">
        <chrome:box title="Alerts" collapsable="true" id="alerts" collapsed="false">
            <c:choose>
                <c:when test="${empty numberofalerts}">
                    <div style="margin-left:15px;">
                        You have no alerts.
                    </div>
                </c:when>
                <c:otherwise>
                    <div id="alertsdiv">
                        <table class="widget" cellpadding="3px;" width="100%">
                            <tr>
                                <td class="header-top1">
                                    Date
                                </td>
                                <td class="header-top1">
                                    Participant
                                </td>
                                <td class="header-top1">
                                    Study
                                </td>
                                <td class="header-top1">
                                </td>
                            </tr>
                            <c:forEach items="${notifications}" var="usernotification">
                                <c:if test="${!usernotification.markDelete}">
                                    <tr id="tr_${usernotification.id}"
                                            <c:if test="${usernotification.new}">
                                                class="bold"
                                            </c:if>>
                                        <td>
                                            <a class="link"
                                               href="javascript:completedForm('${usernotification.studyParticipantCrfSchedule.id}');"><tags:formatDate
                                                    value="${usernotification.notification.date}"/></a>
                                        </td>
                                        <td style="text-align:left">
                                            <proctcae:urlAuthorize url="/pages/reports/participantReport">
                                            <a href="reports/participantReport?sid=${usernotification.studyParticipantCrfSchedule.id}"
                                               class="link">
                                                </proctcae:urlAuthorize>
                                                    ${usernotification.participant.displayName}
                                                <proctcae:urlAuthorize url="/pages/reports/participantReport">
                                            </a>
                                            </proctcae:urlAuthorize>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${fn:length(usernotification.study.shortTitle) > dl}">
                                                    <div title="${usernotification.study.shortTitle}">
                                                            ${fn:substring(usernotification.study.shortTitle,0,dl)}...
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    ${usernotification.study.shortTitle}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                                               id="alertActions${usernotification.id}"><span
                                                    class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
                                            <script>
                                                showPopUpMenuAlerts('${usernotification.id}', '${usernotification.studyParticipantCrfSchedule.id}', '${usernotification.uuid}', '${usernotification.participant.id}');
                                            </script>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </chrome:box>
    </c:if>

</td>

<td width="20%" valign="top" rowspan="6">
    <%--<chrome:box title="Quick Links">--%>
    <div style="padding-left:2px; padding-right:2px;">
        <table width="100%" cellpadding="10" cellspacing="0" border="0">
            <tr>
                <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
                    <td id="a1" class="quickLinkBGon"
                        style="border-bottom: 1px #cccccc solid; border-right: 1px #cccccc solid;" width="50%">
                        <div class="quickLinkRow">
                            <div class="quickLinkPicture"><img
                                    src="<c:url value="/images/blue/icons/searchClinicalStaffController_icon.png"/>"
                                    align="middle"
                                    class="quickLink"></div>
                            <div class="quickLinkLabel"><a href="<c:url value='/pages/admin/createClinicalStaff' />"
                                                           class="quickLink">Create New Staff Profile</a></div>
                        </div>
                    </td>
                </proctcae:urlAuthorize>
            </tr>

            <tr>
                <proctcae:urlAuthorize url="/pages/form/basicForm">
                    <td id="a2" class="quickLinkBGon"
                        style="border-bottom: 1px #cccccc solid; border-right: 1px #cccccc solid;" width="50%">
                        <div class="quickLinkRow">
                            <div class="quickLinkPicture"><img
                                    src="<c:url value="/images/blue/icons/basicFormController_icon.png"/>"
                                    align="middle"
                                    class="quickLink"></div>
                            <div class="quickLinkLabel"><a href="<c:url value='/pages/form/basicForm' />"
                                                           class="quickLink">Create new form</a></div>
                        </div>
                    </td>
                </proctcae:urlAuthorize>
            </tr>
            <c:if test="${nurseLevelRole}">
                <tr>
                    <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
                        <td id="a3" class="quickLinkBGon"
                            style="border-bottom: 1px #cccccc solid; border-right: 1px #cccccc solid;" width="50%">
                            <div class="quickLinkRow">
                                <div class="quickLinkPicture"><img
                                        src="<c:url value="/images/blue/icons/scheduleCrfController_icon.png"/>"
                                        align="middle"
                                        class="quickLink"></div>
                                <div class="quickLinkLabel"><a href="<c:url value='/pages/participant/schedulecrf' />"
                                                               class="quickLink">Manage schedule</a></div>
                            </div>
                        </td>
                    </proctcae:urlAuthorize>
                </tr>
                <tr>
                    <proctcae:urlAuthorize url="/pages/reports/participantReport">
                        <td id="a4" class="quickLinkBGon"
                            style="border-bottom: 1px #cccccc solid; border-right: 1px #cccccc solid;" width="50%">
                            <div class="quickLinkRow">
                                <div class="quickLinkPicture"><img
                                        src="<c:url value="/images/blue/icons/routineReportController_icon.png"/>"
                                        align="middle"
                                        class="quickLink"></div>
                                <div class="quickLinkLabel"><a href="<c:url value='/pages/reports/participantReport' />"
                                                               class="quickLink">View reports</a></div>
                            </div>
                        </td>
                    </proctcae:urlAuthorize>
                </tr>
            </c:if>
            <c:if test="${siteLevelRole}">
                <tr>
                    <proctcae:urlAuthorize url="/pages/participant/create">
                        <td id="a5" class="quickLinkBGon"
                            style="border-bottom: 1px #cccccc solid; border-right: 1px #cccccc solid;" width="50%">
                            <div class="quickLinkRow">
                                <div class="quickLinkPicture"><img
                                        src="<c:url value="/images/blue/icons/participantController_icon.png"/>"
                                        align="middle"
                                        class="quickLink"></div>
                                <div class="quickLinkLabel"><a href="<c:url value='/pages/participant/create' />"
                                                               class="quickLink">Add new participant</a></div>
                            </div>
                        </td>
                    </proctcae:urlAuthorize>
                </tr>
                <tr>
                    <proctcae:urlAuthorize url="/pages/study/searchStudy">
                        <td id="a6" class="quickLinkBGon"
                            style="border-bottom: 1px #cccccc solid; border-right: 1px #cccccc solid;" width="50%">
                            <div class="quickLinkRow">
                                <div class="quickLinkPicture"><img
                                        src="<c:url value="/images/blue/icons/searchStudyController_icon.png"/>"
                                        align="middle"
                                        class="quickLink"></div>
                                <div class="quickLinkLabel"><a href="<c:url value='/pages/study/searchStudy' />"
                                                               class="quickLink">My studies</a></div>
                            </div>
                        </td>
                    </proctcae:urlAuthorize>
                </tr>
            </c:if>
            <c:if test="${studyLevelRole || odc}">
                <tr>
                    <proctcae:urlAuthorize url="/pages/form/manageForm">
                        <td id="a7" class="quickLinkBGon"
                            style="border-bottom: 1px #cccccc solid; border-right: 1px #cccccc solid;" width="50%">
                            <div class="quickLinkRow">
                                <div class="quickLinkPicture"><img
                                        src="<c:url value="/images/blue/icons/manageFormController_icon.png"/>"
                                        align="middle"
                                        class="quickLink"></div>
                                <div class="quickLinkLabel"><a href="<c:url value='/pages/form/manageForm' />"
                                                               class="quickLink">Manage forms</a></div>
                            </div>
                        </td>
                    </proctcae:urlAuthorize>
                </tr>
                <tr>
                    <proctcae:urlAuthorize url="/pages/reports/report">
                        <td id="a8" class="quickLinkBGon"
                            style="border-bottom: 1px #cccccc solid; border-right: 1px #cccccc solid;" width="50%">
                            <div class="quickLinkRow">
                                <div class="quickLinkPicture"><img
                                        src="<c:url value="/images/blue/icons/reportSearchCriteriaController_icon.png"/>"
                                        align="middle"
                                        class="quickLink"></div>
                                <div class="quickLinkLabel"><a href="<c:url value='/pages/reports/report' />"
                                                               class="quickLink">Generate study report</a></div>
                            </div>
                        </td>
                    </proctcae:urlAuthorize>
                </tr>

                <tr>
                    <proctcae:urlAuthorize url="/pages/study/searchStudy">
                        <td id="a8" class="quickLinkBGon"
                            style="border-bottom: 1px #cccccc solid; border-right: 1px #cccccc solid;" width="50%">
                            <div class="quickLinkRow">
                                <div class="quickLinkPicture"><img
                                        src="<c:url value="/images/blue/icons/searchStudyController_icon.png"/>"
                                        align="middle"
                                        class="quickLink"></div>
                                <div class="quickLinkLabel"><a href="<c:url value='/pages/study/searchStudy' />"
                                                               class="quickLink">Search for existing study</a></div>
                            </div>
                        </td>
                    </proctcae:urlAuthorize>
                </tr>
            </c:if>
        </table>
    </div>

</td>
</tr>
<tr>
    <td>
        <c:if test="${studyLevelRole}">
            <chrome:box title="My Forms" collapsable="true" id="myforms" collapsed="false">
                <div id="alertsdiv">
                    <table class="widget" width="100%" border="0">
                        <tr>
                            <td class="header-top1" width="50%" style="text-align:left">
                                Study Title
                            </td>
                            <td class="header-top1" width="30%" style="text-align:left">
                                Form Title
                            </td>
                            <td class="header-top1" width="10%" style="text-align:left">
                                Status
                            </td>
                            <td class="header-top1" width="10%">
                            </td>

                        </tr>
                        <c:forEach items="${recentCrfs}" var="crf">
                            <tr>
                                <td style="text-align:left" width="50%">
                                        ${crf.study.displayName}
                                </td>
                                <td style="text-align:left" width="30%">
                                        ${crf.title}
                                </td>
                                <td class="data" style="text-align:left" width="10%">
                                        ${crf.status}
                                </td>
                                <td>
                                    <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                                       id="crfActions${crf.id}"><span
                                            class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
                                    <script>
                                        showPopUpMenu('${crf.id}', '${crf.status.displayName}');
                                    </script>
                                </td>

                            </tr>
                        </c:forEach>
                    </table>
                    <br/>
                </div>
            </chrome:box>
        </c:if>

    </td>
</tr>
<tr>
    <td>
        <c:if test="${studyLevelRole}">
            <chrome:box title="My Studies" collapsable="true" id="mystudies" collapsed="false">
                <div id="alertsdiv">
                    <table class="widget">
                        <tr>
                            <td class="header-top1" width="90%" style="text-align:left">
                                Short Title
                            </td>
                            <td class="header-top1" width="10%">
                            </td>

                        </tr>
                        <c:forEach items="${studyWithoutForm}" var="study">
                            <tr>
                                <td style="width:90%">
                                        ${study.displayName}
                                </td>
                                <td>
                                    <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                                       id="studyActions${study.id}"><span
                                            class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
                                    <script>
                                        showPopUpMenuStudy('${study.id}');
                                    </script>
                                </td>

                            </tr>
                        </c:forEach>
                    </table>
                    <br/>
                </div>
            </chrome:box>
        </c:if>
    </td>
</tr>
<tr>
    <td>
        <c:if test="${siteLevelRole}">
            <chrome:box title="Overdue forms" collapsable="true" id="overdueforms" collapsed="false">
                <c:choose>
                    <c:when test="${empty overdue}">
                        <div style="margin-left:15px;">
                            You have no overdue forms.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div id="alertsdiv">
                            <table class="widget" cellpadding="5px;">
                                <tr>
                                    <td class="header-top1" width="15%">
                                        Participant
                                    </td>
                                    <td class="header-top1" width="45%">
                                        Study
                                    </td>
                                    <td class="header-top1" width="20%">
                                        Form title
                                    </td>
                                    <td class="header-top1" width="10%">
                                        Start date
                                    </td>
                                    <td class="header-top1" width="10%">
                                        Due date
                                    </td>
                                </tr>

                                <c:forEach items="${overdue}" var="schedule">

                                    <c:set var="count" value="${count + 1}"/>
                                    <%--<c:if test="${showMore ? count<11 : count<100}">--%>
                                    <c:if test="${count <20}">
                                        <tr>
                                            <td style="text-align:left" width="15%">
                                                <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
                                                    <a href="participant/schedulecrf?sid=${schedule.id}"
                                                       class="link">${schedule.studyParticipantCrf.studyParticipantAssignment.participant.displayName}</a>
                                                </proctcae:urlAuthorize>
                                            </td>
                                            <td style="text-align:left" width="45%">
                                                <c:choose>
                                                    <c:when test="${fn:length(schedule.studyParticipantCrf.crf.study.shortTitle) > dl}">
                                                        <div title="${schedule.studyParticipantCrf.crf.study.shortTitle}">
                                                                ${fn:substring(schedule.studyParticipantCrf.crf.study.shortTitle,0,dl)}...
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${schedule.studyParticipantCrf.crf.study.shortTitle}
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="text-align:left" width="20%">
                                                <c:choose>
                                                    <c:when test="${fn:length(schedule.studyParticipantCrf.crf.title) > dl}">
                                                        <div title="${schedule.studyParticipantCrf.crf.title}">
                                                                ${fn:substring(schedule.studyParticipantCrf.crf.title,0,dl)}...
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${schedule.studyParticipantCrf.crf.title}
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="text-align:left" width="10%">
                                                <tags:formatDate value="${schedule.startDate}"/>
                                            </td>
                                            <td style="text-align:left" width="10%">
                                                <tags:formatDate value="${schedule.dueDate}"/>
                                            </td>
                                            <td style="text-align:left" width="10%">
                                            </td>
                                        </tr>

                                    </c:if>
                                </c:forEach>
                                <tr align="right">                                   
                                    <c:if test="${load eq 'all'}">
                                        <td colspan="5">
                                            <A HREF="./home?load=all">show more</A>
                                        </td>
                                    </c:if>
                                    <c:if test="${load eq 'less'}">
                                        <td colspan="5">
                                            <A HREF="./home?load=less">show less</A>
                                        </td>
                                    </c:if>
                                </tr>
                            </table>
                            <br/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </chrome:box>
        </c:if>
    </td>
</tr>
<tr>
    <td>
        <c:if test="${siteLevelRole}">
            <chrome:box title="Upcoming Schedule" collapsable="true" id="upcoming" collapsed="false">
                <c:choose>
                    <c:when test="${empty overdue}">
                        <div style="margin-left:15px;">
                            You have no upcoming schedule.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div id="alertsdiv">
                            <table class="widget" cellpadding="5px;">
                                <tr>
                                    <td class="header-top1" width="15%" style="text-align:left">
                                        Participant
                                    </td>
                                    <td class="header-top1" width="45%" style="text-align:left">
                                        Study
                                    </td>
                                    <td class="header-top1" width="20%" style="text-align:left">
                                        Form title
                                    </td>
                                    <td class="header-top1" width="10%" style="text-align:left">
                                        Start date
                                    </td>
                                    <td class="header-top1" width="10%" style="text-align:left">
                                        Due date
                                    </td>
                                </tr>
                                <c:forEach items="${upcoming}" var="schedule">
                                    <tr>
                                        <td style="text-align:left" width="15%">
                                            <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
                                                <a href="participant/schedulecrf?sid=${schedule.id}"
                                                   class="link">${schedule.studyParticipantCrf.studyParticipantAssignment.participant.displayName}</a>
                                            </proctcae:urlAuthorize>
                                        </td>
                                        <td style="text-align:left" width="45%">
                                            <c:choose>
                                                <c:when test="${fn:length(schedule.studyParticipantCrf.crf.study.shortTitle) > dl}">
                                                    <div title="${schedule.studyParticipantCrf.crf.study.shortTitle}">
                                                            ${fn:substring(schedule.studyParticipantCrf.crf.study.shortTitle,0,dl)}...
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    ${schedule.studyParticipantCrf.crf.study.shortTitle}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="text-align:left" width="20%">
                                            <c:choose>
                                                <c:when test="${fn:length(schedule.studyParticipantCrf.crf.title) > dl}">
                                                    <div title="${schedule.studyParticipantCrf.crf.title}">
                                                            ${fn:substring(schedule.studyParticipantCrf.crf.title,0,dl)}...
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    ${schedule.studyParticipantCrf.crf.title}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="text-align:left" width="10%">
                                            <tags:formatDate value="${schedule.startDate}"/>
                                        </td>
                                        <td style="text-align:left" width="10%">
                                            <tags:formatDate value="${schedule.dueDate}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <tr align="right">
                                    <c:if test="${load eq 'all'}">
                                        <td colspan="5">
                                            <A HREF="./home?load=all">show more</A>
                                        </td>
                                    </c:if>
                                    <c:if test="${load eq 'less'}">
                                        <td colspan="5">
                                            <A HREF="./home?load=less">show less</A>
                                        </td>
                                    </c:if>
                                </tr>
                            </table>
                            <br/>
                        </div>
                    </c:otherwise>
                </c:choose>
                <br/>
            </chrome:box>
        </c:if>
    </td>
</tr>
</table>


<%--</div>--%>
<%--<div class="panel">--%>

<%--</div>--%>
</body>
</html>
