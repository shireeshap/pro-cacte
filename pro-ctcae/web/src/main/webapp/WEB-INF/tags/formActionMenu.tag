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
        if (status == 'Released') {
        <proctcae:urlAuthorize url="/pages/form/versionForm">
            html += '<li><a href="#" onclick="javascript:versionForm(' + cid + ')">Create new version</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/participant/schedulecrf"/>?crfId=' + cid + '\'">Schedule form</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/editForm">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/form/editForm"/>?crfId=' + cid + '\'">Edit rules</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/form/viewForm">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/form/viewForm"/>?crfId=' + cid + '\'">View form</a></li>';
        </proctcae:urlAuthorize>
        }
    <proctcae:urlAuthorize url="/pages/participant/copyForm">
        html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/form/copyForm"/>?crfId=' + cid + '\'">Copy form</a></li>';
    </proctcae:urlAuthorize>
        if (status == 'Draft') {
        <proctcae:urlAuthorize url="/pages/form/releaseForm">
            html += '<li><a href="#" onclick="javascript:releaseForm(' + cid + ')">Release form</a></li>';
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

</script>