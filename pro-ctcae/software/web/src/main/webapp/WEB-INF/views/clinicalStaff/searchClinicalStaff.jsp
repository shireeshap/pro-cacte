<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <tags:includePrototypeWindow/>
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

        .Inactive {
            color: red;
            font-weight: bold;
            text-decoration: underline;
        }

    </style>
</head>

<script>

    function showPopUpMenuClinicalStaff(cid, status, odc) {
        var html = '<div id="search-engines"><ul>';
        <proctcae:urlAuthorize url="/pages/admin/viewClinicalStaff">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/admin/viewClinicalStaff"/>?clinicalStaffId=' + cid + '\'">View staff</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/admin/createClinicalStaff"/>?clinicalStaffId=' + cid + '\'">Edit staff</a></li>';
            if (status == 'Active') {
                html += '<li><a href="#" onclick="javascript:effectiveStaff(' + cid + ',\'' + status + '\')">Deactivate</a></li>';
            } else {
                html += '<li><a href="#" onclick="javascript:effectiveStaff(' + cid + ',\'' + status + '\')">Activate</a></li>';
            }
        </proctcae:urlAuthorize>

        html += '</ul></div>';
        jQuery('#clinicalStaffActions' + cid).menu({
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

    function effectiveStaff(cId, status) {
        var request = new Ajax.Request("<c:url value="/pages/admin/clinicalStaff/effectiveStaff"/>", {
            parameters:<tags:ajaxstandardparams/>+"&cId=" + cId + "&status=" + status,
            onComplete:function(transport) {
                showConfirmationWindow(transport, 500, 200);
                AE.registerCalendarPopups();
            },
            method:'get'
        })
    }

    function submitForm() {
        document.forms[0].submit();
    }

    var myDataTable;
    YAHOO.util.Event.addListener(window, "load", function() {
    	var keyPress = {
    			13: "input:text, input:password",
    			end: null
    	};
    	
    	jQuery(document).bind("keydown", function(e){
    		var selector = keyPress[e.which];
    		
    		if(selector !== undefined && jQuery(e.target).is(selector)){
    			if(isSpclChar('searchString')) {
	    			e.preventDefault();
    			}
    		}
    		return true;
    	});
    	
        YAHOO.example.Basic = function() {
            var myColumnDefs = [
                {key:"lastName", label:"Last name", sortable:true,resizeable:false, width:100},
                {key:"firstName", label:"First name", sortable:true, resizeable:false, width:100},
                {key:"nciIdentifier", label:"NCI Identifier", sortable:true, resizeable:false, width:70},
                {key:"site", label:"Site", sortable:false, resizeable:false, width:180},
                {key:"study", label:"Study", sortable:false, resizeable:false, width:185},
                {key:"status", label:"Status", sortable:true, resizeable:false, width:80},
                {key:"actions", label:"Actions", sortable:false, resizeable:false, width:80}
            ];

            var myDataSource = new YAHOO.util.DataSource("/proctcae/pages/clinicalStaff/fetchClinicalStaff?");
            myDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
            myDataSource.responseSchema = {
                resultsList: "shippedRecordSet.searchClinicalStaffDTOs",
                fields: ["lastName","firstName","nciIdentifier", "site", "study", "status", "actions"],
                metaFields: {
                    totalRecords: "shippedRecordSet.totalRecords",
                    startIndex: "shippedRecordSet.startIndex"
                }
            };

            // Customize request sent to server to be able to set total # of records
            var generateRequest = function(oState, oSelf) {
                // Get states or use defaults
                oState = oState || { pagination: null, sortedBy: null };
                var sort = (oState.sortedBy) ? oState.sortedBy.key : "lastName";
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
                initialRequest: generateRequest() + "&first=yes", // Initial request for first page of data
                dynamicData: true, // Enables dynamic server-driven data
                sortedBy : {key:"lastName", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
                paginator: new YAHOO.widget.Paginator({
                    rowsPerPage:25,
                    template: YAHOO.widget.Paginator.TEMPLATE_ROWS_PER_PAGE,
                    rowsPerPageOptions: [10,25,50,100],
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


    function showHideColumnsForYUITable(columnKey) {
        var column = myDataTable.getColumn(columnKey);
        if (column.hidden) {
            // Shows a Column
            myDataTable.showColumn(columnKey);
        }
        else {
            // Hides a Column
            myDataTable.hideColumn(columnKey);
        }
        myDataTable.refreshView();
    }

    jQuery(function() {
        jQuery("#columnOptionsForCaseTable").multiSelect({
            header: "Choose an Option!",
            selectAll: false,
            noneSelected: 'Show columns',
            oneOrMoreSelected: '% visible'
        }, function(event) {
            showHideColumnsForYUITable(event.val())
        }
                );
    });
</script>

<body>
<chrome:box title="clinicalStaff.box.searchCriteria">
    <form method="POST" action="searchClinicalStaff">
        <input name="useReqParam" value="true" type="hidden"/>
        <input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}"/>

        <tags:instructions code="clinicalStaff.search.top"/>
        <table width="100%">
            <tr>
                <td  width="75%" align="center">
                    <input type="text" id="searchString" name="searchString" style="width: 360px;"
                           maxlength="50" size="50" onblur="isSpclChar('searchString');"
                           value="${searchString}" class="form-control"/>
                    <tags:button color="blue" icon="search" type="button" value='Search'
                                 onclick="submitForm();"/>
                    <tags:indicator id="indicator"/>
                    <ul id="searchString.error" style="display:none;" class="errors">
                        <li><spring:message code='special.character.message' text='special.character.message'/></li>
                    </ul>
                </td>
                <td align="right">
	                <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
	                    <tags:button color="blue" markupWithTag="a" id="newStaff" icon="add"
	                                 value="New Staff" href="/proctcae/pages/admin/createClinicalStaff"/>
	                </proctcae:urlAuthorize>
                </td>
            </tr>
        </table>
        <br /><br />
    </form>

    <div class="yui-skin-sam">
        <table width="100%">
            <tr>
                <td width="72%">
                    <div id="pag"></div>
                </td>
                <td width="28%" align="right">
                    <div> Show/Hide Column:
                        <select id="columnOptionsForCaseTable" name="columnOptionsForCaseTable" multiple="multiple"
                                title="Show/Hide Columns">
                            <option value="lastName" selected="selected">Last name</option>
                            <option value="firstName" selected="selected">First name</option>
                            <option value="nciIdentifier" selected="selected">NCI identifier</option>
                            <option value="site" selected="selected">Site</option>
                            <option value="study" selected="selected">Study</option>
                            <option value="status" selected="selected">Status</option>
                        </select>
                    </div>
                </td>
            </tr>
        </table>

        <div id="basic">
        </div>
    </div>
</chrome:box>

</body>
</html>