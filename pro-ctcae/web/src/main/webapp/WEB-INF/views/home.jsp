<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<html>
<head>
    <%--<tags:javascriptLink name="table_menu"/>--%>
    <tags:stylesheetLink name="table_menu"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:formBuilder/>
    <tags:formActionMenu/>
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

        a.fg-button {
            float: right;
        }

        * {
            zoom: 1;
        }


    </style>
    <!--[if IE]>
        <style>
            div.row div.value {
                margin-left:7px;
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
</c:forEach>
<c:set var="dl" value="1200"/>
<table cellpadding="0" cellspacing="0" width="100%" class="outer">
<tr>
    <td>
        <chrome:box title="Alerts">
            <c:choose>
                <c:when test="${empty numberofalerts}">
                    <div style="margin-left:15px;">You have no alerts.</div>
                </c:when>
                <c:otherwise>
                    <table class="widget" cellpadding="3px;">
                        <tr>
                            <td class="header-top">
                                Participant
                            </td>
                            <td class="header-top">
                                Study
                            </td>
                            <td class="header-top">
                                Date
                            </td>
                            <td class="header-top">
                                Message
                            </td>
                            <td class="header-top">
                            </td>
                        </tr>
                        <c:forEach items="${notifications}" var="usernotification">
                            <c:if test="${!usernotification.markDelete}">
                                <tr id="tr_${usernotification.id}"
                                    <c:if test="${usernotification.new}">class="bold"</c:if>>
                                    <td class="data">
                                        <proctcae:urlAuthorize url="/pages/reports/participantReport">
                                            <a href="reports/participantReport?sid=${usernotification.studyParticipantCrfSchedule.id}"
                                               class="link">${usernotification.participant.displayName}</a>
                                        </proctcae:urlAuthorize>
                                    </td>
                                    <td class="data">
                                        <c:choose>
                                            <c:when test="${fn:length(usernotification.study.shortTitle) > dl}">
                                                <div title="${usernotification.study.shortTitle}"> ${fn:substring(usernotification.study.shortTitle,0,dl)}...</div>
                                            </c:when>
                                            <c:otherwise>
                                                ${usernotification.study.shortTitle}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="data">
                                        <a class="link"
                                           href="javascript:completedForm('${usernotification.studyParticipantCrfSchedule.id}');"><tags:formatDate
                                                value="${usernotification.notification.date}"/></a>
                                    </td>
                                    <td class="data">
                                        <a class="link"
                                           href="javascript:showMessage('${usernotification.id}');">This is
                                            an
                                            auto..</a>
                                    </td>
                                    <td>
                                        <a href="javascript:deleteMsg('${usernotification.id}','${usernotification.uuid}');"
                                           class="delete">x</a>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>
            <br/>
        </chrome:box>
    </td>
    <td>
        <chrome:box title="Quick Links">
            <proctcae:urlAuthorize url="/pages/participant/create">
                <div class="quicklink">
                    <a class="link" href="participant/create">Enter Participant</a>
                </div>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/study/createStudy">
                <div class="quicklink">
                    <a class="link" href="study/createStudy">Enter Study</a>
                </div>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/study/searchStudy">
                <div class="quicklink">
                    <a class="link" href="study/searchStudy">Search Study</a>
                </div>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/form/basicForm">
                <div class="quicklink">
                    <a class="link" href="form/basicForm">Create Form</a>
                </div>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/form/manageForm">
                <div class="quicklink">
                    <a class="link" href="form/manageForm">Manage Form</a>
                </div>
            </proctcae:urlAuthorize>

            <br/>
        </chrome:box>
    </td>
</tr>
<c:if test="${studyLevelRole}">
    <tr>
        <td>
            <chrome:box title="My Forms">
                <table class="widget" cellpadding="3px;">
                    <tr>
                        <td class="header-top" style="text-align:left">
                            Title
                        </td>
                        <td class="header-top">
                            Status
                        </td>
                        <td class="header-top">
                            Actions
                        </td>
                    </tr>
                    <c:forEach items="${recentCrfs}" var="crf">
                        <tr>
                            <td class="data"  style="text-align:left">
                                <c:choose>
                                    <c:when test="${fn:length(crf.title) > dl}">
                                        <div title="${crf.title}"> ${fn:substring(crf.title,0,dl)}...</div>
                                    </c:when>
                                    <c:otherwise>
                                        ${crf.title}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="data">
                                    ${crf.status}
                            </td>
                            <td class="data" align="right">
                                <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                                   id="crfActions${crf.id}"><span
                                        class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
                                <script>showPopUpMenu('${crf.id}', '${crf.status}');</script>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br/>
            </chrome:box>
        </td>
        <td>
            <chrome:box title="My Studies">
                <table width="100%">
                    <c:forEach items="${studyWithoutForm}" var="study">
                            <tr>
                                <td style="border-bottom: 1px solid #cccccc;padding-left:10px">
                                    <c:choose>
                                        <c:when test="${fn:length(study.shortTitle) > dl}">
                                            <div title="${study.shortTitle}"> ${fn:substring(study.shortTitle,0,dl)}...</div>
                                        </c:when>
                                        <c:otherwise>
                                            ${study.shortTitle}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="border-bottom: 1px solid #cccccc;">
                                    <proctcae:urlAuthorize url="/pages/form/basicForm">

                                        <a class="link" href="form/basicForm?studyId=${study.id}">Create Form</a>
                                    </proctcae:urlAuthorize>
                                </td>
                            </tr>
                    </c:forEach>
                </table>
                <br/>
            </chrome:box>
        </td>
    </tr>
</c:if>
<c:if test="${siteLevelRole}">
    <tr>
        <td><chrome:box title="Overdue forms">
            <c:choose>
                <c:when test="${empty overdue}">
                    <div style="margin-left:15px;">You have no overdue forms.</div>
                </c:when>
                <c:otherwise>
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
                                <td class="data">
                                    <proctcae:urlAuthorize url="/pages/reports/participantReport">
                                        <a href="reports/participantReport?sid=${schedule.id}"
                                           class="link">${schedule.studyParticipantCrf.studyParticipantAssignment.participant.displayName}</a>
                                    </proctcae:urlAuthorize>
                                </td>
                                <td class="data">
                                    <c:choose>
                                        <c:when test="${fn:length(schedule.studyParticipantCrf.crf.study.shortTitle) > dl}">
                                            <div title="${schedule.studyParticipantCrf.crf.study.shortTitle}"> ${fn:substring(schedule.studyParticipantCrf.crf.study.shortTitle,0,dl)}...</div>
                                        </c:when>
                                        <c:otherwise>
                                            ${schedule.studyParticipantCrf.crf.study.shortTitle}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="data">
                                    <c:choose>
                                        <c:when test="${fn:length(schedule.studyParticipantCrf.crf.title) > dl}">
                                            <div title="${schedule.studyParticipantCrf.crf.title}"> ${fn:substring(schedule.studyParticipantCrf.crf.title,0,dl)}...</div>
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
                </c:otherwise>
            </c:choose>
        </chrome:box>
        </td>
        <td>
            <chrome:box title="Upcoming Schedule">
                <c:choose>
                    <c:when test="${empty overdue}">
                        <div style="margin-left:15px;">You have no upcoming schedule.</div>
                    </c:when>
                    <c:otherwise>
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
                                    <td class="data">
                                        <proctcae:urlAuthorize url="/pages/reports/participantReport">
                                            <a href="reports/participantReport?sid=${schedule.id}"
                                               class="link">${schedule.studyParticipantCrf.studyParticipantAssignment.participant.displayName}</a>
                                        </proctcae:urlAuthorize>
                                    </td>
                                    <td class="data">
                                        <c:choose>
                                            <c:when test="${fn:length(schedule.studyParticipantCrf.crf.study.shortTitle) > dl}">
                                                <div title="${schedule.studyParticipantCrf.crf.study.shortTitle}"> ${fn:substring(schedule.studyParticipantCrf.crf.study.shortTitle,0,dl)}...</div>
                                            </c:when>
                                            <c:otherwise>
                                                ${schedule.studyParticipantCrf.crf.study.shortTitle}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="data">
                                        <c:choose>
                                            <c:when test="${fn:length(schedule.studyParticipantCrf.crf.title) > dl}">
                                                <div title="${schedule.studyParticipantCrf.crf.title}"> ${fn:substring(schedule.studyParticipantCrf.crf.title,0,dl)}...</div>
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
                    </c:otherwise>
                </c:choose>
                <br/>
            </chrome:box>
        </td>
    </tr>
</c:if>
</table>
</body>
</html>
