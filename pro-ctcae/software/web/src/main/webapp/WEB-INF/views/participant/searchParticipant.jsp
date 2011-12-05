<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>
    <tags:dwrJavascriptLink objects="organization"/>
    <tags:dwrJavascriptLink objects="study"/>

    <style type="text/css">
        body {
            margin: 0;
            padding: 0;
        }

        .label {
            width: 12em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
        }

        .tableHeader {
            background-color: #2B4186;
            background-image: url(/proctcae/images/blue/eXtableheader_bg.png);
            background-position: center top;
            background-repeat: repeat-x;
            color: white;
            font-size: 13px;
            font-weight: bold;
            margin: 0;
            padding: 4px 3px;
            text-align: left;
            white-space: nowrap;
        }

        .sortable {
            color: white; /*text-decoration: underline;*/
            cursor: pointer;
        }

        .odd {
            background-color: #cccccc;
        }

        .even {
            background-color: white;
        }


    </style>
</head>

<script>

    function showPopUpMenuParticipant(pid, odc) {
        var html = '<div id="search-engines"><ul>';
        if (odc == true || odc == 'true') {
        <proctcae:urlAuthorize url="/pages/participant/view">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/participant/edit"/>?id=' + pid + '\'">View participant</a></li>';
        </proctcae:urlAuthorize>
        } else {
            var edit = false;
        <proctcae:urlAuthorize url="/pages/participant/trueedit">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/participant/edit"/>?id=' + pid + '\'">Edit participant</a></li>';
            edit = true;
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/participant/view">
            if (!edit) {
                html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/participant/edit"/>?id=' + pid + '\'">View participant</a></li>';
            }
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/participant/schedulecrf">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/participant/schedulecrf"/>?pId=' + pid + '\'">Manage schedule</a></li>';
        </proctcae:urlAuthorize>
        }

        html += '</ul></div>';
        jQuery('#participantActions' + pid).menu({
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

    function submitForm() {
        document.forms[0].submit();
    }

    YAHOO.util.Event.addListener(window, "load", function() {
        YAHOO.example.Basic = function() {
            var myColumnDefs = [
                {key:"studyParticipantIdentifier", label:"Identifier",sortable:true, resizeable:false, width:100},
                {key:"lastName", label:"Last name", sortable:true, resizeable:false, width:100},
                {key:"firstName", label:"First name", sortable:true, resizeable:false, width:100},
                {key:"organizationName", label:"Site", sortable:true, resizeable:false, width:180},
                {key:"studyShortTitle", label:"Study", sortable:true, resizeable:false, width:220},
                {key:"actions", label:"Actions", sortable:false, resizeable:false, width:100}
            ];


            var myDataSource = new YAHOO.util.DataSource("/proctcae/pages/participant/fetchParticipant?");
            myDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
            myDataSource.responseSchema = {
                resultsList: "shippedRecordSet.searchParticipantDTOs",
                fields: ["studyParticipantIdentifier", "lastName","firstName", "organizationName", "studyShortTitle", "actions"],
                metaFields: {
                    totalRecords: "shippedRecordSet.totalRecords",
                    startIndex: "shippedRecordSet.startIndex"
                }
            };

                var generateRequest = function(oState, oSelf) {
	        // Get states or use defaults
	        oState = oState || { pagination: null, sortedBy: null };
	        var sort = (oState.sortedBy) ? oState.sortedBy.key : "firstName";
	        var dir = (oState.sortedBy && oState.sortedBy.dir === YAHOO.widget.DataTable.CLASS_DESC) ? "desc" : "asc";
	        var startIndex = (oState.pagination) ? oState.pagination.recordOffset : 0;
	        var results = (oState.pagination) ? oState.pagination.rowsPerPage : 25;

	        // Build custom request
	        return  "sort=" + sort +
                    "&dir=" + dir +
	                "&startIndex=" + startIndex +
	                "&results=" + (startIndex + results)
	    };

             var myConfigs = {
	        generateRequest: generateRequest,
	        initialRequest: generateRequest(), // Initial request for first page of data
	        dynamicData: true, // Enables dynamic server-driven data
	        sortedBy : {key:"firstName", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
	        paginator: new YAHOO.widget.Paginator({ rowsPerPage:25 }) // Enables pagination
	    };


            var myDataTable = new YAHOO.widget.DataTable("basic", myColumnDefs, myDataSource, myConfigs);

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


</script>
<body>
<chrome:box title="participant.label.search_criteria" autopad="true">
    <form method="POST" action="search#searchResults">
        <input name="useReqParam" value="true" type="hidden"/>
        <input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}"/>

        <tags:instructions code="participant.search.top"/>
        <div class="row">
            <div class="label"><spring:message code='participant.label.search_string' text=''/></div>
            <div class="value IEdivValueHack">
                <input type="text" id="searchString" name="searchString"
                       maxlength="30" size="30"
                       value="${searchString}"/>
            </div>
        </div>
        <div class="row">
            <div class="label"></div>
            <div class="value">
                <tags:button color="blue" icon="search" type="button" value='Search' onclick="submitForm();"/>
                <tags:indicator id="indicator"/>
            </div>
        </div>
    </form>
</chrome:box>

<chrome:box title="Results">
    <div class="yui-skin-sam">
        <%--<p>Total records (between 0 and 1000): <input type="text" id="total" value="100"> <input type="button"--%>
                                                                                                  <%--id="update"--%>
                                                                                                  <%--value="Update"></p>--%>

        <div id="basic"></div>
    </div>
</chrome:box>


</body>
</html>