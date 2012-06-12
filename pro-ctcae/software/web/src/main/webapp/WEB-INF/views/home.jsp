<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<html>
<head>
<jsp:useBean id="today" class="java.util.Date"/>
<c:set var="fDate" value='<tags:formatDate value="${today}"/>'/>
<tags:includeScriptaculous/>
<tags:includePrototypeWindow/>
<tags:stylesheetLink name="cycledefinitions"/>
<tags:javascriptLink name="cycledefinitions"/>
<%--<tags:javascriptLink name="yui"/>--%>
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
        margin-left: 7px;
    }

    a.fg-button {
        display: block;
        position: static;
        width: 65px;
        padding-right: 0;
        padding-left: 0;
    }

    .fg-button-icon-right .ui-icon {
        position: static;
        float: right;
        padding: 0;
        margin: 0;
        left: auto;
        right;
        auto;
    }
</style>
<![endif]-->
<script type="text/javascript">

function showMessage(id) {
    var request = new Ajax.Request("<c:url value="/pages/home/notificationdetails"/>", {
        parameters:<tags:ajaxstandardparams/>+"&id=" + id ,
        onComplete:function(transport) {
//            $('tr_' + id).removeClassName('bold');
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
                updateAlertsTable();
            },
            method:'get'
        })
    }
}

function removeOverdueSchedule(id) {
    if (id != '') {
        var request = new Ajax.Request("<c:url value="/public/removeOverdue"/>", {
            parameters:<tags:ajaxstandardparams/>+"&sid=" + id,
            onComplete:function(transport) {
                updateOverdueTable();
            },
            method:'get'
        })
    }
}

function updateOverdueTable() {
    sortState = myOverdueFormsDataTable.getState().sortedBy;
    var sort = sortState ? sortState.key : "id";
    var dir = sortState ? sortState.dir : "yui-dt-desc";
    myOverdueFormsDataTable.sortColumn(myOverdueFormsDataTable.getColumn(sort), dir);
}

function updateAlertsTable() {
    sortState = myAlertsDataTable.getState().sortedBy;
    var sort = sortState ? sortState.key : "id";
    var dir = sortState ? sortState.dir : "yui-dt-desc";
    myAlertsDataTable.sortColumn(myAlertsDataTable.getColumn(sort), dir);
}
;


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

//function loadMyCalendar() {
    <%--var request = new Ajax.Request("<c:url value="/pages/participant/userCalendar"/>", {--%>
//        onComplete:function(transport) {
//            new Insertion.After('calendar_inner', transport.responseText);
//        },
//        method:'get'
//    })
//}

function getCalendar(dir) {
    $('ajaxLoadingImgDiv').show();
    var request = new Ajax.Request("<c:url value="/pages/user/displayUserCalendar"/>", {
        onComplete:function(transport) {
            showCalendar(transport);
            $('ajaxLoadingImgDiv').hide();
        },
        parameters:<tags:ajaxstandardparams/>+"&dir=" + dir,
        method:'get'
    })
}

function showCalendar(transport) {
    var items = $('calendar_outer').childElements();
    var len = items.length;
    for (var i = 0; i < len; i++) {
        if (items[i].id != 'calendar_inner') {
            items[i].remove();
        }
    }
    new Insertion.After('calendar_inner', transport.responseText);
}

function showDetailsWindow(day) {
    var request = new Ajax.Request("<c:url value="/pages/user/dayScheduleDetails"/>", {
        onComplete:function(transport) {
            showConfirmationWindow(transport, 850, 350);
        },
        parameters:<tags:ajaxstandardparams/> +"&day=" + day,
        method:'get'
    })
}

function showPopUpMenuAlert(day) {
    var html = '<div id="search-engines"><ul>';
    html += '<li><a href="#" onclick="javascript:showDetailsWindow(' + day + ');">Show details</a></li>';
    html += '</ul></div>';
    jQuery('#scheduleActions' + day).menu({
        content: html,
        maxHeight: 350,
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

function showPo(scheduleid, pid) {
    var html = '<div id="search-engines"><ul>';
<proctcae:urlAuthorize url="/pages/participant/enterResponses">
    html += '<li id="nav"><a href="#" >Print form </a><ul><li><a href="#" onclick="location.href=\'participant/printSchedule?lang=en&id=' + scheduleid + '\'">English</a></li><li><a href="#" onclick="location.href=\'participant/printSchedule?lang=es&id=' + scheduleid + '\'">Spanish</a></li></ul></li>';
    html += '<li><a href="#" onclick="location.href=\'participant/enterResponses?id=' + scheduleid + '&lang=es\'">Enter responses</a></li>';
</proctcae:urlAuthorize>
       html += '<li><a href="#" onclick="location.href=\'participant/edit?id=' + pid + '&tab=3\'">Manage schedule</a></li>';
    html += '</ul></div>';
    jQuery('#scheduleAct' + scheduleid).menu({
        content: html,
        maxHeight: 100,
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

var myDataTable;
YAHOO.util.Event.addListener(window, "load", function() {

    YAHOO.example.Basic = function() {
        var myColumnDefs = [
            {key:"title", label:"Title", sortable:true,resizeable:false, width:238},
            {key:"studyShortTitle", label:"Study", sortable:false, resizeable:false, width:245},
            {key:"status", label:"Status", sortable:false, resizeable:false, width:80},
            {key:"actions", label:"Actions", sortable:false, resizeable:false, width:100}
        ];

        var myDataSource = new YAHOO.util.DataSource("/proctcae/pages/form/fetchCrf?");
        myDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
        myDataSource.responseSchema = {
            resultsList: "shippedRecordSet.searchCrfDTOs",
            fields: ["title", "studyShortTitle", "status", "actions"],
            metaFields: {
                totalRecords: "shippedRecordSet.totalRecords",
                startIndex: "shippedRecordSet.startIndex"
            }
        };

        // Customize request sent to server to be able to set total # of records
        var generateRequest = function(oState, oSelf) {
            // Get states or use defaults
            oState = oState || { pagination: null, sortedBy: null };
            var sort = (oState.sortedBy) ? oState.sortedBy.key : "title";
            var dir = (oState.sortedBy && oState.sortedBy.dir === YAHOO.widget.DataTable.CLASS_DESC) ? "desc" : "asc";
            var startIndex = (oState.pagination) ? oState.pagination.recordOffset : 0;
            var results = (oState.pagination) ? oState.pagination.rowsPerPage : 25;
            // Build custom request
            return  "sort=" + sort +
                    "&dir=" + dir +
                    "&startIndex=" + startIndex +
                    "&results=" + (startIndex + results)
        };

        // DataTable configuration
        var myConfigs = {
            generateRequest: generateRequest,
            initialRequest: generateRequest(), // Initial request for first page of data
            dynamicData: true, // Enables dynamic server-driven data
            sortedBy : {key:"title", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
            paginator: new YAHOO.widget.Paginator({
                rowsPerPage:10,
                template: YAHOO.widget.Paginator.TEMPLATE_ROWS_PER_PAGE,
                rowsPerPageOptions: [5,10,25],
                containers  : 'pag'
            }), // Enables pagination
            draggableColumns:true
        };

        myDataTable = new YAHOO.widget.DataTable("basic", myColumnDefs, myDataSource, myConfigs);
        myDataTable.subscribe("rowClickEvent", myDataTable.onEventSelectRow);
        myDataTable.subscribe("rowMouseoverEvent", myDataTable.onEventHighlightRow);
        myDataTable.subscribe("rowMouseoutEvent", myDataTable.onEventUnhighlightRow);
        // Update totalRecords on the fly with values from server
        myDataTable.doBeforeLoadData = function(oRequest, oResponse, oPayload) {
            oPayload.totalRecords = oResponse.meta.totalRecords;
            oPayload.pagination.recordOffset = oResponse.meta.startIndex;
            return oPayload;
        };

        return {
            oDS: myDataSource,
            oDT: myDataTable
        };
    }();
});

var myAlertsDataTable;
YAHOO.util.Event.addListener(window, "load", function() {
    getCalendar('refresh');
    YAHOO.example.Basic = function() {
        var myColumnDefs = [
            {key:"participantName", label:"Participant",sortable:false, resizeable:false, width:212},
            {key:"studyTitle", label:"Study", sortable:true,resizeable:false, width:250},
            {key:"date", label:"Date", formatter:"date", sortable:true, resizeable:false, width:100},
            {key:"actions", label:"Actions", sortable:false, resizeable:false, width:100}
        ];

        var myAlertsDataSource = new YAHOO.util.DataSource("/proctcae/pages/spcSchedule/fetchAlerts?");
        myAlertsDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
        myAlertsDataSource.responseSchema = {
            resultsList: "shippedRecordSet.searchNotificationDTOs",
            fields: ["participantName", "studyTitle", "date", "actions"],
            metaFields: {
                totalRecords: "shippedRecordSet.totalRecords",
                startIndex: "shippedRecordSet.startIndex"
            }
        };

        // Customize request sent to server to be able to set total # of records
        var generateRequest = function(oState, oSelf) {
            // Get states or use defaults
            oState = oState || { pagination: null, sortedBy: null };
            var sort = (oState.sortedBy) ? oState.sortedBy.key : "date";
            var dir = (oState.sortedBy && oState.sortedBy.dir === YAHOO.widget.DataTable.CLASS_DESC) ? "desc" : "asc";
            var startIndex = (oState.pagination) ? oState.pagination.recordOffset : 0;
            var results = (oState.pagination) ? oState.pagination.rowsPerPage : 25;
            // Build custom request
            return  "sort=" + sort +
                    "&dir=" + dir +
                    "&startIndex=" + startIndex +
                    "&results=" + (startIndex + results)
        };

        // DataTable configuration
        var myConfigs = {
            generateRequest: generateRequest,
            initialRequest: generateRequest(), // Initial request for first page of data
            dynamicData: true, // Enables dynamic server-driven data
            sortedBy : {key:"date", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
            paginator: new YAHOO.widget.Paginator({
                rowsPerPage:5,
                template: YAHOO.widget.Paginator.TEMPLATE_ROWS_PER_PAGE,
                rowsPerPageOptions: [5,10,25],
                containers  : 'pagAlerts'
            }), // Enables pagination
            draggableColumns:true
        };

        myAlertsDataTable = new YAHOO.widget.DataTable("basicAlerts", myColumnDefs, myAlertsDataSource, myConfigs);
        myAlertsDataTable.subscribe("rowClickEvent", myAlertsDataTable.onEventSelectRow);
        myAlertsDataTable.subscribe("rowMouseoverEvent", myAlertsDataTable.onEventHighlightRow);
        myAlertsDataTable.subscribe("rowMouseoutEvent", myAlertsDataTable.onEventUnhighlightRow);
        // Update totalRecords on the fly with values from server
        myAlertsDataTable.doBeforeLoadData = function(oRequest, oResponse, oPayload) {
            oPayload.totalRecords = oResponse.meta.totalRecords;
            oPayload.pagination.recordOffset = oResponse.meta.startIndex;
            return oPayload;
        };

        return {
            oDS: myAlertsDataSource,
            oDT: myAlertsDataTable
        };
    }();
});


var myStudyDataTable;
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.Basic = function() {
        var myColumnDefs = [
//	            {key:"assignedIdentifier", label:"Study identifier",sortable:true, resizeable:false, width:140},
            {key:"shortTitle", label:"Short title", sortable:true,resizeable:false, width:605},
//	            {key:"fundingSponsorDisplayName", label:"Funding sponsor", sortable:false, resizeable:false, width:235},
//	            {key:"coordinatingCenterDisplayName", label:"Coordinating center", sortable:false, resizeable:false, width:235},
            {key:"actions", label:"Actions", sortable:false, resizeable:false, width:100}
        ];

        var myStudyDataSource = new YAHOO.util.DataSource("/proctcae/pages/study/fetchStudy?");
        myStudyDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
        myStudyDataSource.responseSchema = {
            resultsList: "shippedRecordSet.searchStudyDTO",
            fields: ["shortTitle", "actions"],
            metaFields: {
                totalRecords: "shippedRecordSet.totalRecords",
                startIndex: "shippedRecordSet.startIndex"
            }
        };

        // Customize request sent to server to be able to set total # of records
        var generateRequest = function(oState, oSelf) {
            // Get states or use defaults
            oState = oState || { pagination: null, sortedBy: null };
            var sort = (oState.sortedBy) ? oState.sortedBy.key : "shortTitle";
            var dir = (oState.sortedBy && oState.sortedBy.dir === YAHOO.widget.DataTable.CLASS_DESC) ? "desc" : "asc";
            var startIndex = (oState.pagination) ? oState.pagination.recordOffset : 0;
            var results = (oState.pagination) ? oState.pagination.rowsPerPage : 25;
            // Build custom request
            return  "sort=" + sort +
                    "&dir=" + dir +
                    "&startIndex=" + startIndex +
                    "&results=" + (startIndex + results)
        };

        // DataTable configuration
        var myConfigs = {
            generateRequest: generateRequest,
            initialRequest: generateRequest(), // Initial request for first page of data
            dynamicData: true, // Enables dynamic server-driven data
            sortedBy : {key:"shortTitle", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
            paginator: new YAHOO.widget.Paginator({
                rowsPerPage:10,
                template: YAHOO.widget.Paginator.TEMPLATE_ROWS_PER_PAGE,
                rowsPerPageOptions: [5,10,25],
                containers  : 'pagStudy'
            }), // Enables pagination
            draggableColumns:true
        };

        myStudyDataTable = new YAHOO.widget.DataTable("basicStudy", myColumnDefs, myStudyDataSource, myConfigs);
        myStudyDataTable.subscribe("rowClickEvent", myStudyDataTable.onEventSelectRow);
        myStudyDataTable.subscribe("rowMouseoverEvent", myStudyDataTable.onEventHighlightRow);
        myStudyDataTable.subscribe("rowMouseoutEvent", myStudyDataTable.onEventUnhighlightRow);
        // Update totalRecords on the fly with values from server
        myStudyDataTable.doBeforeLoadData = function(oRequest, oResponse, oPayload) {
            oPayload.totalRecords = oResponse.meta.totalRecords;
            oPayload.pagination.recordOffset = oResponse.meta.startIndex;
            return oPayload;
        };

        return {
            oDS: myStudyDataSource,
            oDT: myStudyDataTable
        };
    }();
});

var myAvailableFormsDataTable;
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.Basic = function() {
        var myColumnDefs = [
            {key:"participantName", label:"Participant",sortable:false, resizeable:false, width:100},
            {key:"studyTitle", label:"Study", sortable:true,resizeable:false, width:117},
            {key:"formTitle", label:"Form title", sortable:false, resizeable:false, width:110},
            {key:"status", label:"Status", sortable:false, resizeable:false, width:95},
            {key:"dueDate", label:"Due date", formatter:"date", sortable:false, resizeable:false, width:100},
            {key:"actions", label:"Actions", sortable:false, resizeable:false, width:100}
        ];

        var myAvailableFormsDataSource = new YAHOO.util.DataSource("/proctcae/pages/spcSchedule/fetchAvailableForms?");
        myAvailableFormsDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
        myAvailableFormsDataSource.responseSchema = {
            resultsList: "shippedRecordSet.searchScheduleDTOs",
            fields: ["participantName", "studyTitle", "formTitle", "status", "dueDate", "actions"],
            metaFields: {
                totalRecords: "shippedRecordSet.totalRecords",
                startIndex: "shippedRecordSet.startIndex"
            }
        };

        // Customize request sent to server to be able to set total # of records
        var generateRequest = function(oState, oSelf) {
            // Get states or use defaults
            oState = oState || { pagination: null, sortedBy: null };
            var sort = (oState.sortedBy) ? oState.sortedBy.key : "dueDate";
            var dir = (oState.sortedBy && oState.sortedBy.dir === YAHOO.widget.DataTable.CLASS_DESC) ? "desc" : "asc";
            var startIndex = (oState.pagination) ? oState.pagination.recordOffset : 0;
            var results = (oState.pagination) ? oState.pagination.rowsPerPage : 25;
            // Build custom request
            return  "sort=" + sort +
                    "&dir=" + dir +
                    "&startIndex=" + startIndex +
                    "&results=" + (startIndex + results)
        };

        // DataTable configuration
        var myConfigs = {
            generateRequest: generateRequest,
            initialRequest: generateRequest(), // Initial request for first page of data
            dynamicData: true, // Enables dynamic server-driven data
            sortedBy : {key:"dueDate", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
            paginator: new YAHOO.widget.Paginator({
                rowsPerPage:5,
                template: YAHOO.widget.Paginator.TEMPLATE_ROWS_PER_PAGE,
                rowsPerPageOptions: [5,10,25],
                containers  : 'pagAvailableForms'
            }), // Enables pagination
            draggableColumns:true
        };

        myAvailableFormsDataTable = new YAHOO.widget.DataTable("basicAvailableForms", myColumnDefs, myAvailableFormsDataSource, myConfigs);
        myAvailableFormsDataTable.subscribe("rowClickEvent", myAvailableFormsDataTable.onEventSelectRow);
        myAvailableFormsDataTable.subscribe("rowMouseoverEvent", myAvailableFormsDataTable.onEventHighlightRow);
        myAvailableFormsDataTable.subscribe("rowMouseoutEvent", myAvailableFormsDataTable.onEventUnhighlightRow);
        // Update totalRecords on the fly with values from server
        myAvailableFormsDataTable.doBeforeLoadData = function(oRequest, oResponse, oPayload) {
            oPayload.totalRecords = oResponse.meta.totalRecords;
            oPayload.pagination.recordOffset = oResponse.meta.startIndex;
            return oPayload;
        };

        return {
            oDS: myAvailableFormsDataSource,
            oDT: myAvailableFormsDataTable
        };
    }();
});


var myOverdueFormsDataTable;
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.Basic = function() {
        var myColumnDefs = [
            {key:"participantName", label:"Participant",sortable:false, resizeable:false, width:100},
            {key:"studyTitle", label:"Study", sortable:true,resizeable:false, width:117},
            {key:"formTitle", label:"Form title", sortable:false, resizeable:false, width:110},
            {key:"status", label:"Status", sortable:false, resizeable:false, width:95},
            {key:"dueDate", label:"Due date", formatter:"date", sortable:false, resizeable:false, width:100},
            {key:"actions", label:"Actions", sortable:false, resizeable:false, width:100}
        ];

        var myOverdueFormsDataSource = new YAHOO.util.DataSource("/proctcae/pages/spcSchedule/fetchOverdueForms?");
        myOverdueFormsDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
        myOverdueFormsDataSource.responseSchema = {
            resultsList: "shippedRecordSet.searchScheduleDTOs",
            fields: ["participantName", "studyTitle", "formTitle", "status", "dueDate", "actions"],
            metaFields: {
                totalRecords: "shippedRecordSet.totalRecords",
                startIndex: "shippedRecordSet.startIndex"
            }
        };

        // Customize request sent to server to be able to set total # of records
        var generateRequest = function(oState, oSelf) {
            // Get states or use defaults
            oState = oState || { pagination: null, sortedBy: null };
            var sort = (oState.sortedBy) ? oState.sortedBy.key : "dueDate";
            var dir = (oState.sortedBy && oState.sortedBy.dir === YAHOO.widget.DataTable.CLASS_DESC) ? "desc" : "asc";
            var startIndex = (oState.pagination) ? oState.pagination.recordOffset : 0;
            var results = (oState.pagination) ? oState.pagination.rowsPerPage : 25;
            // Build custom request
            return  "sort=" + sort +
                    "&dir=" + dir +
                    "&startIndex=" + startIndex +
                    "&results=" + (startIndex + results)
        };

        // DataTable configuration
        var myConfigs = {
            generateRequest: generateRequest,
            initialRequest: generateRequest(), // Initial request for first page of data
            dynamicData: true, // Enables dynamic server-driven data
            sortedBy : {key:"dueDate", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
            paginator: new YAHOO.widget.Paginator({
                rowsPerPage:5,
                template: YAHOO.widget.Paginator.TEMPLATE_ROWS_PER_PAGE,
                rowsPerPageOptions: [5,10,25],
                containers  : 'pagOverdueForms'
            }), // Enables pagination
            draggableColumns:true
        };

        myOverdueFormsDataTable = new YAHOO.widget.DataTable("basicOverdueForms", myColumnDefs, myOverdueFormsDataSource, myConfigs);
        myOverdueFormsDataTable.subscribe("rowClickEvent", myOverdueFormsDataTable.onEventSelectRow);
        myOverdueFormsDataTable.subscribe("rowMouseoverEvent", myOverdueFormsDataTable.onEventHighlightRow);
        myOverdueFormsDataTable.subscribe("rowMouseoutEvent", myOverdueFormsDataTable.onEventUnhighlightRow);
        // Update totalRecords on the fly with values from server
        myOverdueFormsDataTable.doBeforeLoadData = function(oRequest, oResponse, oPayload) {
            oPayload.totalRecords = oResponse.meta.totalRecords;
            oPayload.pagination.recordOffset = oResponse.meta.startIndex;
            return oPayload;
        };

        return {
            oDS: myOverdueFormsDataSource,
            oDT: myOverdueFormsDataTable
        };
    }();
});

var myUpcomingFormsDataTable;
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.Basic = function() {
        var myColumnDefs = [
            {key:"participantName", label:"Participant",sortable:false, resizeable:false, width:150},
            {key:"studyTitle", label:"Study", sortable:true,resizeable:false, width:150},
            {key:"formTitle", label:"Form title", sortable:false, resizeable:false, width:150},
            {key:"status", label:"Status", sortable:false, resizeable:false, width:95},
            {key:"dueDate", label:"Due date", formatter:"date", sortable:false, resizeable:false, width:100}
        ];

        var myUpcomingFormsDataSource = new YAHOO.util.DataSource("/proctcae/pages/spcSchedule/fetchUpcomingForms?");
        myUpcomingFormsDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
        myUpcomingFormsDataSource.responseSchema = {
            resultsList: "shippedRecordSet.searchScheduleDTOs",
            fields: ["participantName", "studyTitle", "formTitle", "status", "dueDate", "actions"],
            metaFields: {
                totalRecords: "shippedRecordSet.totalRecords",
                startIndex: "shippedRecordSet.startIndex"
            }
        };

        // Customize request sent to server to be able to set total # of records
        var generateRequest = function(oState, oSelf) {
            // Get states or use defaults
            oState = oState || { pagination: null, sortedBy: null };
            var sort = (oState.sortedBy) ? oState.sortedBy.key : "dueDate";
            var dir = (oState.sortedBy && oState.sortedBy.dir === YAHOO.widget.DataTable.CLASS_DESC) ? "desc" : "asc";
            var startIndex = (oState.pagination) ? oState.pagination.recordOffset : 0;
            var results = (oState.pagination) ? oState.pagination.rowsPerPage : 25;
            // Build custom request
            return  "sort=" + sort +
                    "&dir=" + dir +
                    "&startIndex=" + startIndex +
                    "&results=" + (startIndex + results)
        };

        // DataTable configuration
        var myConfigs = {
            generateRequest: generateRequest,
            initialRequest: generateRequest(), // Initial request for first page of data
            dynamicData: true, // Enables dynamic server-driven data
            sortedBy : {key:"dueDate", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
            paginator: new YAHOO.widget.Paginator({
                rowsPerPage:5,
                template: YAHOO.widget.Paginator.TEMPLATE_ROWS_PER_PAGE,
                rowsPerPageOptions: [5,10,25],
                containers  : 'pagUpcomingForms'
            }), // Enables pagination
            draggableColumns:true
        };

        myUpcomingFormsDataTable = new YAHOO.widget.DataTable("basicUpcomingForms", myColumnDefs, myUpcomingFormsDataSource, myConfigs);
        myUpcomingFormsDataTable.subscribe("rowClickEvent", myUpcomingFormsDataTable.onEventSelectRow);
        myUpcomingFormsDataTable.subscribe("rowMouseoverEvent", myUpcomingFormsDataTable.onEventHighlightRow);
        myUpcomingFormsDataTable.subscribe("rowMouseoutEvent", myUpcomingFormsDataTable.onEventUnhighlightRow);
        // Update totalRecords on the fly with values from server
        myUpcomingFormsDataTable.doBeforeLoadData = function(oRequest, oResponse, oPayload) {
            oPayload.totalRecords = oResponse.meta.totalRecords;
            oPayload.pagination.recordOffset = oResponse.meta.startIndex;
            return oPayload;
        };

        return {
            oDS: myUpcomingFormsDataSource,
            oDT: myUpcomingFormsDataTable
        };
    }();
});


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
                <div class="yui-skin-sam">
                    <table width="100%">
                        <tr>
                            <td width="68%">
                                <div id="pagAlerts"></div>
                            </td>
                        </tr>
                    </table>
                    <div id="basicAlerts">
                    </div>
                </div>
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
                                    <div class="quickLinkLabel"><a
                                            href="<c:url value='/pages/participant/schedulecrf' />"
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
                                    <div class="quickLinkLabel"><a
                                            href="<c:url value='/pages/reports/participantReport' />"
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
        <c:if test="${studyLevelRole || siteLevelRole}">
            <chrome:box title="My Calendar" collapsable="true" id="mycalendar" collapsed="false">
                <table border="0" width="95%" align="center">
                    <tr>
                        <td>
                            <div id="calendar_outer">
                    <div id="calendar_inner"></div>
                    <tags:userCalendar userCalendarCommand="${userCalendarCommand}"/>
                </div>
                        </td>
                    </tr>
                </table>


            </chrome:box>
        </c:if>
    </td>
</tr>
<tr>
    <td>
        <c:if test="${studyLevelRole}">
            <chrome:box title="My Forms" collapsable="true" id="myforms" collapsed="false">
                <div class="yui-skin-sam">
                    <table width="100%">
                        <tr>
                            <td width="68%">
                                <div id="pag"></div>
                            </td>
                        </tr>
                    </table>
                    <div id="basic">
                    </div>
                </div>
            </chrome:box>
        </c:if>
    </td>
</tr>
<tr>
    <td>
        <c:if test="${studyLevelRole}">
            <chrome:box title="My Studies" collapsable="true" id="mystudies" collapsed="false">
                <div class="yui-skin-sam">
                    <table width="100%">
                        <tr>
                            <td width="68%">
                                <div id="pagStudy"></div>
                            </td>
                        </tr>
                    </table>
                    <div id="basicStudy">
                    </div>
                </div>
            </chrome:box>
        </c:if>
    </td>
</tr>
<tr>
    <td>
        <c:if test="${siteLevelRole}">
            <chrome:box title="Available Forms" collapsable="true" id="myavailableforms" collapsed="false">
                <div class="yui-skin-sam">
                    <table width="100%">
                        <tr>
                            <td width="68%">
                                <div id="pagAvailableForms"></div>
                            </td>
                        </tr>
                    </table>
                    <div id="basicAvailableForms">
                    </div>
                </div>
            </chrome:box>
        </c:if>
    </td>
</tr>
<tr>
    <td>
        <c:if test="${siteLevelRole}">
            <chrome:box title="Overdue forms" collapsable="true" id="overdueforms" collapsed="false">
                <div class="yui-skin-sam">
                    <table width="100%">
                        <tr>
                            <td width="68%">
                                <div id="pagOverdueForms"></div>
                            </td>
                        </tr>
                    </table>
                    <div id="basicOverdueForms">
                    </div>
                </div>
            </chrome:box>
        </c:if>
    </td>
</tr>
<tr>
    <td>
        <c:if test="${siteLevelRole}">
            <chrome:box title="Upcoming forms" collapsable="true" id="upcomingforms" collapsed="false">
                <div class="yui-skin-sam">
                    <table width="100%">
                        <tr>
                            <td width="68%">
                                <div id="pagUpcomingForms"></div>
                            </td>
                        </tr>
                    </table>
                    <div id="basicUpcomingForms">
                    </div>
                </div>
            </chrome:box>
        </c:if>
    </td>
</tr>

</table>

</body>
</html>
