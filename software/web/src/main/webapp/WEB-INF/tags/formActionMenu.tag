<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    function deleteForm(crfId) {
        var request = new Ajax.Request("<c:url value="/pages/form/deleteForm"/>", {
            parameters:<tags:ajaxstandardparams/>+"&crfId=" + crfId ,
            onComplete:function(transport) {
                showConfirmationWindow(transport);
            },
            method:'get'
        })
    }

    function hideForm(crfId) {
        var request = new Ajax.Request("<c:url value="/pages/form/hideForm"/>", {
            parameters:<tags:ajaxstandardparams/>+"&crfId=" + crfId ,
            onComplete:function(transport) {
                showConfirmationWindow(transport);
            },
            method:'get'
        })
    }

    function versionForm(crfId) {
        var request = new Ajax.Request("<c:url value="/pages/form/versionForm"/>", {
            parameters:<tags:ajaxstandardparams/>+"&crfId=" + crfId ,
            onComplete:function(transport) {
                showConfirmationWindow(transport);
            },
            method:'get'
        })
    }
    function showPopUpMenu(cid, status) {
        var html = '<div id="search-engines"><ul>';
        if (status == 'Final') {
        <proctcae:urlAuthorize url="/pages/form/versionForm">
            html += '<li><a href="#" onclick="javascript:versionForm(' + cid + ')">Create new version</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/editForm">
            var showruleslink = true;
        <proctcae:urlAuthorize url="/pages/form/editSchedules">
            showruleslink = false;
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/form/editForm"/>?crfId=' + cid + '\'">Edit schedules/notifications</a></li>';
        </proctcae:urlAuthorize>
            if (showruleslink) {
                html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/form/editForm"/>?crfId=' + cid + '\'">Edit notifications</a></li>';
            }
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/viewForm">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/form/viewForm"/>?crfId=' + cid + '\'">View form</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/versionForm">
            html += '<li><a href="#" onclick="javascript:hideForm(' + cid + ')">Hide form</a></li>';
        </proctcae:urlAuthorize>
        }
    <proctcae:urlAuthorize url="/pages/form/copyForm">
        html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/form/copyForm"/>?crfId=' + cid + '\'">Copy form</a></li>';
    </proctcae:urlAuthorize>
        if (status == 'Draft') {
        <proctcae:urlAuthorize url="/pages/form/releaseForm">
            html += '<li><a href="#" onclick="javascript:releaseForm(' + cid + ')">Finalize form</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/deleteForm">
            html += '<li><a href="#" onclick="javascript:deleteForm(' + cid + ')">Delete form</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/editForm">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/form/editForm"/>?crfId=' + cid + '\'">Edit form</a></li>';
        </proctcae:urlAuthorize>

        }

        html += '</ul></div>';
        jQuery('#crfActions' + cid).menu({
            content: html,
            maxHeight: 180,
            positionOpts: {
                directionV: 'down',
                posX: 'left',
                posY: 'bottom',
                offsetX: 0,
                offsetY: 0
            },
            showSpeed: 300
        });
    }
    function showPopUpMenuStudy(sid, isODC) {
        if (typeof(isODC) == 'undefined') {
            isODC = false;
        }
        var html = '<div id="search-engines"><ul>';
        <c:set var="tabVal" value="1"/>
        <proctcae:urlAuthorize url="/study/editoverallstaff">
            <c:set var="tabVal" value="2"/>
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/study/editsites">
            <c:set var="tabVal" value="2"/>
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/study/createStudy">
            <c:set var="tabVal" value="2"/>
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/createAdmin">
	        <c:set var="tabVal" value="3"/>
	    </proctcae:urlAuthorize>
        
        if (isODC == 'true' || isODC == true) {
            <proctcae:urlAuthorize url="/pages/study/editStudy">
                html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/study/editStudy"/>?studyId=' + sid + '\'">View Study</a></li>';
            </proctcae:urlAuthorize>
        } else {
             <c:set var="nextTabVal" value="1"/>
            <proctcae:urlAuthorize url="/pages/study/editStudy">
                html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/study/editStudy"/>?studyId=' + sid + '\'">View/Edit Study</a></li>';
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/form/basicForm">
                html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/form/basicForm"/>?studyId=' + sid + '\'">Create Form</a></li>';
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/study/editoverallstaff">
                html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/study/editStudy"/>?studyId=' + sid + '&tab=${tabVal}\'">Manage Study Staff</a></li>';
                <c:set var="nextTabVal" value="${tabVal + 1}"/>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/study/editsitestaff">
                <c:if test="${nextTabVal eq 1}">
                    <proctcae:urlAuthorize url="/study/editsites">
                        <c:set var="nextTabVal" value="2"/>
                    </proctcae:urlAuthorize>
                </c:if>
                html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/study/editStudy"/>?studyId=' + sid + '&tab=${nextTabVal}\'">Manage Study Site Staff</a></li>';
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/report">
                html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/reports/report?rt=overallStudy"/>&studyId=' + sid + '\'">Generate Study Report</a></li>';
            </proctcae:urlAuthorize>
        }

        html += '</ul></div>';
        jQuery('#studyActions' + sid).menu({
            content: html,
            maxHeight: 180,
            positionOpts: {
                directionV: 'down',
                posX: 'left',
                posY: 'bottom',
                offsetX: 0,
                offsetY: 0
            },
            showSpeed: 300
        });
    }

    function showPopUpMenuAlerts(uid, spcrfid, uuid, pid) {
        var html = '<div id="search-engines"><ul>';
        html += '<li><a href="#" onclick="javascript:showMessage(' + uid + ')">Alert message</a></li>';
        html += '<li><a href="#" onclick="javascript:completedForm(' + spcrfid + ')">Show all responses</a></li>';
    <proctcae:urlAuthorize url="/pages/reports/participantReport">
        html += '<li><a href="#" onclick="location.href=\'<c:url value="reports/participantReport"/>?sid=' + spcrfid + '\'">Show report</a></li>';
    </proctcae:urlAuthorize>
    <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
        html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/participant/schedulecrf"/>?pId=' + pid + '\'">Manage schedule</a></li>';
    </proctcae:urlAuthorize>
        html += '<li><a href="#" onclick="javascript:deleteMsg(' + uid + ',\'' + uuid + '\')">Clear</a></li>';

        html += '</ul></div>';
        jQuery('#alertActions' + uid).menu({
            content: html,
            maxHeight: 180,
            positionOpts: {
                directionV: 'down',
                posX: 'left',
                posY: 'bottom',
                offsetX: 0,
                offsetY: 0
            },
            showSpeed: 300
        });
    }

</script>