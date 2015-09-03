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

    function showPopUpMenuSystemAlert(systemAlertId, alertStatus) {
        var html = '<div id="search-engines"><ul>';
        <proctcae:urlAuthorize url="/pages/admin/createSystemAlert">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/admin/createSystemAlert"/>?systemAlertId=' + systemAlertId + '\'">Edit Alert</a></li>';
        </proctcae:urlAuthorize>
        <proctcae:urlAuthorize url="/pages/admin/createSystemAlert">
        	html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/admin/alert/copySystemAlert"/>?systemAlertId=' + systemAlertId + '\'">Duplicate</a></li>';
    	</proctcae:urlAuthorize>
        if(alertStatus == 'Active') {
            html += '<li><a href="#" onclick="javascript:toggleSystemAlertStatus(' + systemAlertId + ', \'' + alertStatus + '\')">Deactivate</a></li>';
        } else {
        	html += '<li><a href="#" onclick="javascript:toggleSystemAlertStatus(' + systemAlertId + ', \'' + alertStatus + '\')">Activate</a></li>';
        }
        html += '</ul></div>';
        jQuery('#alertActions' + systemAlertId).menu({
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

    function toggleSystemAlertStatus(systemAlertId, alertStatus) {
        var request = new Ajax.Request("<c:url value="/pages/admin/alert/toggleSystemAlertStatus"/>", {
            parameters:<tags:ajaxstandardparams/>+"&systemAlertId=" + systemAlertId + "&alertStatus=" + alertStatus,
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
                {key:"startDate", label:"Start Date", sortable:true, resizeable:false, width:80},
                {key:"endDate", label:"End date", sortable:true,resizeable:false, width:80},
                {key:"alertStatus", label:"Status", sortable:true, resizeable:false, width:180},
                {key:"alertMessage", label:"Alert message", sortable:true, resizeable:false, width:400},
                {key:"actions", label:"Actions", sortable:false, resizeable:false, width:100}
            ];

            var myDataSource = new YAHOO.util.DataSource("/proctcae/pages/alert/fetchSystemAlertBanner?");
            myDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
            myDataSource.responseSchema = {
                resultsList: "shippedRecordSet.searchAlertDTOs",
                fields: ["startDate","endDate","study", "alertStatus", "alertMessage", "actions"],
                metaFields: {
                    totalRecords: "shippedRecordSet.totalRecords",
                    startIndex: "shippedRecordSet.startIndex"
                }
            };

            // Customize request sent to server to be able to set total # of records
            var generateRequest = function(oState, oSelf) {
                // Get states or use defaults
                oState = oState || { pagination: null, sortedBy: null };
                var sort = (oState.sortedBy) ? oState.sortedBy.key : "startDate";
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
                sortedBy : {key:"startDate", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
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
        });
    });
</script>

<body>
<chrome:box title="alert.box.searchCriteria">
    <form method="POST" action="searchAlert">
        <input name="useReqParam" value="true" type="hidden"/>
        <input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}"/>

        <tags:instructions code="alert.search.top"/>
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
	                    <tags:button color="blue" markupWithTag="a" id="newAlert" icon="add"
	                                 value="New Alert" href="/proctcae/pages/admin/createSystemAlert"/>
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
                            <option value="startDate" selected="selected">Start date</option>
                            <option value="endDate" selected="selected">End date</option>
                            <option value="alertStatus" selected="selected">Status</option>
                            <option value="alertMessage" selected="selected">Alert message</option>
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