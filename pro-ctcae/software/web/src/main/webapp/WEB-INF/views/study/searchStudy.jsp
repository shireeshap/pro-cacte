<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <tags:formActionMenu/>
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>
    <tags:dwrJavascriptLink objects="organization"/>
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
        
        .yui-skin-sam .yui-dt-liner { white-space: }  
    </style>
</head>
	
<script>
	var myDataTable;
	YAHOO.util.Event.addListener(window, "load", function() {
	    YAHOO.example.Basic = function() {
	        var myColumnDefs = [ 
	            {key:"assignedIdentifier", label:"Study identifier",sortable:true, resizeable:false, width:140}, 
	            {key:"shortTitle", label:"Short title", sortable:true,resizeable:false, width:140}, 
	            {key:"fundingSponsorDisplayName", label:"Funding sponsor", sortable:false, resizeable:false, width:235}, 
	            {key:"coordinatingCenterDisplayName", label:"Coordinating center", sortable:false, resizeable:false, width:235}, 
	            {key:"actions", label:"Actions", sortable:false, resizeable:false, width:80} 
	        ];

	        var myDataSource = new YAHOO.util.DataSource("/proctcae/pages/study/fetchStudy?");
	        myDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
	        myDataSource.responseSchema = {
       		    resultsList: "shippedRecordSet.searchStudyDTO", 
	            fields: ["assignedIdentifier", "shortTitle","fundingSponsorDisplayName", "coordinatingCenterDisplayName", "actions"],
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
                    rowsPerPage:25,
                    template: YAHOO.widget.Paginator.TEMPLATE_ROWS_PER_PAGE,
	                rowsPerPageOptions: [10,25,50,100],
	                containers  : 'pag'
                }), // Enables pagination
                draggableColumns:true
            };
            
	        myDataTable = new YAHOO.widget.DataTable("basic", myColumnDefs, myDataSource, myConfigs);
	        myDataTable.subscribe("rowClickEvent",myDataTable.onEventSelectRow);
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

    jQuery(function(){
       jQuery("#columnOptionsForCaseTable").multiSelect({
           header: "Choose an Option!",
           selectAll: false,
           noneSelected: 'Show columns',
           oneOrMoreSelected: '% visible'
                  },function(event) {
                   showHideColumnsForYUITable(event.val())
               }
		);
    });
</script>

<script>
    function getSites(sQuery) {
        showIndicator("siteInput-indicator");
        var callbackProxy = function(results) {
            aResults = results;
        };
        var callMetaData = { callback:callbackProxy, async:false};
        organization.matchOrganizationForStudySitesWithSecurity(unescape(sQuery), callMetaData);
        hideIndicator("siteInput-indicator");
        return aResults;
    }

    var managerAutoComp;
    Event.observe(window, 'load', function() {
        new YUIAutoCompleter('siteInput', getSites, handleSelect);
        if ('${site.displayName}' != "") {
            $('siteInput').value = "${site.displayName}";
            $('siteInput').removeClassName('pending-search');
        }
    });

    function handleSelect(stype, args) {
        var ele = args[0];
        var oData = args[2];
        if(oData == null){
        	ele.getInputEl().value="(Begin typing here)";
        	ele.getInputEl().addClassName('pending-search');
        } else {
	        ele.getInputEl().value = oData.displayName;
	        var id = ele.getInputEl().id;
	        ele.getInputEl().removeClassName('pending-search');
	        var hiddenInputId = id.substring(0, id.indexOf('Input'));
	        $(hiddenInputId).value = oData.id;
        }
    }

    function clearInput(inputId) {
        $(inputId).clear();
        $(inputId + 'Input').clear();
        $(inputId + 'Input').focus();
        $(inputId + 'Input').blur();
    }

    function sortResults(sort, currentSort) {
        $('sort').value = sort;
        $('sortDir').value = currentSort;
        $('doSort').value = true;
        submitForm();

    }
    function setPage(page) {
        $('page').value = page;
        submitForm();
    }
    function setRowsPerPage(rowsPerPage) {
        $('rowsPerPage').value = rowsPerPage;
        submitForm();
    }

    function submitForm() {
        document.forms[0].submit();
    }

    function navigate(e) {
        var mye;
        if (e) {
            mye = e;
        } else {
            mye = event;
        }
        if (mye.keyCode == 13)  //enter pressed
            doSend();
    }
    document.onkeypress = navigate;
    function doSend() {
        submitForm();
    }
</script>

<body>
<chrome:box title="study.label.search" autopad="true">
    <form method="POST" action="searchStudy#searchResults">
    	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
        <input name="useReqParam" value="true" type="hidden"/>
        <p><tags:instructions code="study.search.top"/></p>

        <div class="content">
            <div class="row" name="inputs">
                <div class="label"> <tags:message code='study.label.search_by'/></div>
                <div class="value">
                    <input type="text" id="searchText" name="searchText" size="25" value="${searchText}">
                    <div id="error"></div>
                </div>
            </div>
            <div style="padding-left:140px">
                <tags:button color="blue" icon="search" type="button" value='Search' onclick="submitForm();"/>
            </div>
        </div>
        <input type="hidden" name="sort" value="${sort}" id="sort"/>
        <input type="hidden" name="page" value="${page}" id="page"/>
        <input type="hidden" name="rowsPerPage" value="${rowsPerPage}" id="rowsPerPage"/>
        <input type="hidden" name="sortDir" value="${sortDir}" id="sortDir"/>
        <input type="hidden" name="doSort" value="false" id="doSort"/>
    </form>
</chrome:box>
<%--<a name="searchResults"/>--%>

<chrome:box title="Results">
	<div class="yui-skin-sam">
		<table width="100%">
            <tr>
                <td width="72%">
                    <div id="pag"></div>
                </td>
                <td width="28%">
                     <div> Show/Hide Column:
                        <select id="columnOptionsForCaseTable" name="columnOptionsForCaseTable" multiple="multiple" title="Show/Hide Columns">
                            <option value="assignedIdentifier" selected="selected">Study identifier</option>
                            <option value="shortTitle" selected="selected">Short title</option>
                            <option value="fundingSponsorDisplayName" selected="selected">Funding sponsor</option>
                            <option value="coordinatingCenterDisplayName" selected="selected">Coordinating center</option>
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