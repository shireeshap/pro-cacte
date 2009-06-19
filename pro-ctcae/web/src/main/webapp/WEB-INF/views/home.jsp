<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<html>
<head>
    <tags:javascriptLink name="table_menu"/>
    <tags:stylesheetLink name="table_menu"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <style type="text/css">
        .quicklink {
            border-bottom: 1px solid #cccccc;
            padding-left: 15px;
        }
    </style>

    <script type="text/javascript">
        function showMessage(id) {
            var request = new Ajax.Request("<c:url value="/pages/home/notificationdetails"/>", {
                parameters:"id=" + id + "&subview=subview",
                onComplete:function(transport) {
                    $('new_' + id).innerHTML='&nbsp';
                    showConfirmationWindow(transport, 700, 500);
                },
                method:'get'
            })
        }
    </script>
</head>
<body>
<c:set var="dl" value="12"/>
<div style="float:left;width:50%;">
    <chrome:box title="Alerts">
        <table class="widget">
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
            </tr>
            <c:forEach items="${notifications}" var="usernotification">
                <tr>
                    <td class="data">
                        <div id="new_${usernotification.id}"
                             style="float:left;margin-right:2px;margin-left:2px;color:#ff3300;font-weight:bold;"><c:if
                                test="${usernotification.new}">*</c:if><c:if
                                test="${!usernotification.new}">&nbsp;</c:if></div>
                        <div style="float:left">${usernotification.participant.displayName}</div>
                    </td>
                    <td class="data">
                        <c:choose>
                            <c:when test="${fn:length(usernotification.study.shortTitle) > dl}">
                                ${fn:substring(usernotification.study.shortTitle,0,dl)}...
                            </c:when>
                            <c:otherwise>
                                ${usernotification.study.shortTitle}
                            </c:otherwise>
                        </c:choose>
                        </fn>
                    </td>
                    <td class="data">
                        <tags:formatDate value="${usernotification.notification.date}"/>
                    </td>
                    <td class="data">
                        <a class="link" href="javascript:showMessage('${usernotification.id}');">This is an auto..</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <br/>
    </chrome:box>
</div>
<div style="float:right;width:50%">
    <chrome:box title="Quick Links">
        <proctcae:urlAuthorize url="/pages/participant/create">
            <div class="quicklink">
                <a class="link" href="/pages/participant/create">Enter Participant</a>
            </div>
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/study/createStudy">
            <div class="quicklink">
                <a class="link" href="/pages/study/createStudy">Enter Study</a>
            </div>
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/study/searchStudy">
            <div class="quicklink">
                <a class="link" href="/pages/study/searchStudy">Search Study</a>
            </div>
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/basicForm">
            <div class="quicklink">
                <a class="link" href="/pages/form/basicForm">Create Form</a>
            </div>
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/manageForm">
            <div class="quicklink">
                <a class="link" href="/pages/form/manageForm">Manage Form</a>
            </div>
        </proctcae:urlAuthorize>

        <br/>
    </chrome:box>
</div>
</body>
</html>
