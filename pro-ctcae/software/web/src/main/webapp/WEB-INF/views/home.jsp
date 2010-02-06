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

        #alertsdiv {
            width: 455px;
            overflow-y: auto;
            max-height: 400px;
        }

        table.widget {
            border: none;
        }

        .panel {
            width: 470px;
            float: left;
            margin: 4px;
            display: inline;
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

    </script>
</head>
<body>
<c:forEach items="${notifications}" var="usernotification">
    <c:if test="${!usernotification.markDelete}">
        <c:set var="numberofalerts" scope="page" value="${numberofalerts + 1}"/>
    </c:if>
</c:forEach><c:set var="dl" value="1200"/>
<div class="panel">
    <c:if test="${studyLevelRole || siteLevelRole || nurseLevelRole}">
    <chrome:box title="Alerts">
        <c:choose>
            <c:when test="${empty numberofalerts}">
                <div style="margin-left:15px;">
                    You have no alerts.
                </div>
            </c:when>
            <c:otherwise>
                <div id="alertsdiv">
                    <table class="widget" cellpadding="3px;">
                        <tr>
                            <td class="header-top">
                                Date
                            </td>
                            <td class="header-top">
                                Participant
                            </td>
                            <td class="header-top">
                                Study
                            </td>
                            <td class="header-top">
                            </td>
                        </tr>
                        <c:forEach items="${notifications}" var="usernotification">
                            <c:if test="${!usernotification.markDelete}">
                                <tr id="tr_${usernotification.id}"
                                        <c:if test="${usernotification.new}">
                                            class="bold"
                                        </c:if>>
                                    <td class="data">
                                        <a class="link"
                                           href="javascript:completedForm('${usernotification.studyParticipantCrfSchedule.id}');"><tags:formatDate
                                                value="${usernotification.notification.date}"/></a>
                                    </td>
                                    <td class="data" style="text-align:left">
                                        <proctcae:urlAuthorize url="/pages/reports/participantReport">
                                        <a href="reports/participantReport?sid=${usernotification.studyParticipantCrfSchedule.id}"
                                           class="link">
                                            </proctcae:urlAuthorize>
                                                ${usernotification.participant.displayName}
                                            <proctcae:urlAuthorize url="/pages/reports/participantReport">
                                        </a>
                                        </proctcae:urlAuthorize>
                                    </td>
                                    <td class="data">
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
    <c:if test="${studyLevelRole}">
        <chrome:box title="My Forms">
            <div id="alertsdiv">
                <table class="widget">
                    <tr>
                        <td class="header-top" style="text-align:left">
                            Study Title
                        </td>
                        <td class="header-top" style="text-align:left">
                            Title
                        </td>
                        <td class="header-top">
                            Status
                        </td>
                        <td class="header-top">
                        </td>
                        <td class="header-top">
                            &nbsp;
                        </td>
                    </tr>
                    <c:forEach items="${recentCrfs}" var="crf">
                        <tr>
                            <td>
                                    ${crf.study.displayName}
                            </td>
                            <td>
                                    ${crf.title}
                            </td>
                            <td class="data">
                                    ${crf.status}
                            </td>
                            <td>
                                <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                                   id="crfActions${crf.id}"><span
                                        class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
                                <script>
                                    showPopUpMenu('${crf.id}', '${crf.status}');
                                </script>
                            </td>
                            <td>
                                &nbsp;
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br/>
            </div>
        </chrome:box>
    </c:if>
    <c:if test="${siteLevelRole}">
        <chrome:box title="Overdue forms">
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
                                <td class="header-top">
                                    Participant
                                </td>
                                <td class="header-top">
                                    Study
                                </td>
                                <td class="header-top">
                                    Form
                                </td>
                                <td class="header-top">
                                    Start date
                                </td>
                                <td class="header-top">
                                    Due date
                                </td>
                            </tr>
                            <c:forEach items="${overdue}" var="schedule">
                                <tr>
                                    <td class="data" style="text-align:left">
                                        <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
                                            <a href="participant/schedulecrf?sid=${schedule.id}"
                                               class="link">${schedule.studyParticipantCrf.studyParticipantAssignment.participant.displayName}</a>
                                        </proctcae:urlAuthorize>
                                    </td>
                                    <td class="data">
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
                                    <td class="data">
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
                                    <td class="data">
                                        <tags:formatDate value="${schedule.startDate}"/>
                                    </td>
                                    <td class="data">
                                        <tags:formatDate value="${schedule.dueDate}"/>
                                    </td>
                                    <td class="data">
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <br/>
                    </div>
                </c:otherwise>
            </c:choose>
        </chrome:box>
    </c:if>
</div>
<div class="panel">
    <chrome:box title="Quick Links">
        <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
            <div class="quicklink">
                <a class="link" href="admin/createClinicalStaff"><img
                        src="<chrome:imageUrl name="../blue/icons/16px/searchClinicalStaffController.png"/>" alt=""/>
                    Add new staff profile </a>
            </div>
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/basicForm">
            <div class="quicklink">
                <a class="link" href="form/basicForm"><img
                        src="<chrome:imageUrl name="../blue/icons/16px/basicFormController.png"/>" alt=""/> Create new
                    form</a>
            </div>
        </proctcae:urlAuthorize>
        <c:if test="${nurseLevelRole}">
            <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
                <div class="quicklink">
                    <a class="link" href="/proctcae/pages/participant/schedulecrf"><img
                            src="<chrome:imageUrl name="../blue/icons/16px/basicFormController.png"/>" alt=""/> Manage
                        schedule</a>
                </div>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/participantReport">
                <div class="quicklink">
                    <a class="link" href="/proctcae/pages/reports/participantReport"><img
                            src="<chrome:imageUrl name="../blue/icons/16px/basicFormController.png"/>" alt=""/> View
                        reports</a>
                </div>
            </proctcae:urlAuthorize>
        </c:if>
        <c:if test="${siteLevelRole}">
            <proctcae:urlAuthorize url="/pages/participant/create">
                <div class="quicklink">
                    <a class="link" href="participant/create"><img
                            src="<chrome:imageUrl name="../blue/icons/16px/participantController.png"/>" alt=""/> Add
                        new participant</a>
                </div>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/study/searchStudy">
                <div class="quicklink">
                    <a class="link" href="study/searchStudy"><img
                            src="<chrome:imageUrl name="../blue/icons/16px/createStudyController.png"/>" alt=""/> My
                        studies</a>
                </div>
            </proctcae:urlAuthorize>
        </c:if>
        <c:if test="${studyLevelRole || odc}">
            <proctcae:urlAuthorize url="/pages/form/manageForm">
                <div class="quicklink">
                    <a class="link" href="form/manageForm"><img
                            src="<chrome:imageUrl name="../blue/icons/16px/captureAdverseEventController.png"/>"
                            alt=""/> Manage forms</a>
                </div>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/report">
                <div class="quicklink">
                    <a class="link" href="reports/report"><img
                            src="<chrome:imageUrl name="../blue/icons/16px/reportSearchCriteriaController.png"/>"
                            alt=""/> Generate study report</a>
                </div>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/study/searchStudy">
                <div class="quicklink">
                    <a class="link" href="study/searchStudy"><img
                            src="<chrome:imageUrl name="../blue/icons/16px/searchStudyController.png"/>" alt=""/> Search
                        for existing study</a>
                </div>
            </proctcae:urlAuthorize>
        </c:if>
        <br/>
    </chrome:box>
    <c:if test="${studyLevelRole}">
        <chrome:box title="My Studies">
            <div id="alertsdiv">
                <table class="widget">
                    <tr>
                        <td class="header-top" style="text-align:left">
                            Short Title
                        </td>
                        <td class="header-top">
                        </td>
                        <td class="header-top">
                            &nbsp;
                        </td>
                    </tr>
                    <c:forEach items="${studyWithoutForm}" var="study">
                        <tr>
                            <td style="width:70%">
                                    ${study.displayName}
                            </td>
                            <td>
                                <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                                   id="studyActions${study.id}"><span class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
                                <script>
                                    showPopUpMenuStudy('${study.id}');
                                </script>
                            </td>
                            <td>
                                &nbsp;
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br/>
            </div>
        </chrome:box>
    </c:if>
    <c:if test="${siteLevelRole}">
        <chrome:box title="Upcoming Schedule">
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
                                <td class="header-top">
                                    Participant
                                </td>
                                <td class="header-top">
                                    Study
                                </td>
                                <td class="header-top">
                                    Form
                                </td>
                                <td class="header-top">
                                    Start date
                                </td>
                                <td class="header-top">
                                    Due date
                                </td>
                            </tr>
                            <c:forEach items="${upcoming}" var="schedule">
                                <tr>
                                    <td class="data" style="text-align:left">
                                        <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
                                            <a href="participant/schedulecrf?sid=${schedule.id}"
                                               class="link">${schedule.studyParticipantCrf.studyParticipantAssignment.participant.displayName}</a>
                                        </proctcae:urlAuthorize>
                                    </td>
                                    <td class="data">
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
                                    <td class="data">
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
                                    <td class="data">
                                        <tags:formatDate value="${schedule.startDate}"/>
                                    </td>
                                    <td class="data">
                                        <tags:formatDate value="${schedule.dueDate}"/>
                                    </td>
                                    <td class="data">
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <br/>
                    </div>
                </c:otherwise>
            </c:choose>
            <br/>
        </chrome:box>
    </c:if>
</div>
</body>
</html>
