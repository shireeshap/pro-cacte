<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<head>
<tags:formBuilder/>
<tags:formActionMenu/>
<tags:stylesheetLink name="tabbedflow"/>
<tags:includeScriptaculous/>
<tags:stylesheetLink name="table_menu"/>
<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>
<tags:dwrJavascriptLink objects="study"/>

<tags:includePrototypeWindow/>
<tags:dwrJavascriptLink objects="crf"/>

<style type="text/css">
    .even {
        background-color: #ffffff;
    }

    a.fg-button {
        float: left;
    }

    * {
        zoom: 1;
    }

    td.header-top {
        background-color: #CCCCCC;
        font-weight: bold;
        text-align: left;
    }

    td.data {
        text-align: left;
    }
</style>
<!--[if IE]>
<style>
    div.row div.value {
        margin-left: 7px;
    }
</style>
<![endif]-->
<script type="text/javascript">

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
                {key:"title", label:"Title", sortable:true,resizeable:false, width:225},
                {key:"version", label:"Version", sortable:true, resizeable:false, width:70},
                {key:"effectiveStartDate", label:"Effective date", formatter:"date", sortable:true, resizeable:false, width:100},
                {key:"studyShortTitle", label:"Study", sortable:true, resizeable:false, width:225},
                {key:"status", label:"Status", sortable:true, resizeable:false, width:80},
                {key:"actions", label:"Actions", sortable:false, resizeable:false, width:100}
            ];

            var myDataSource = new YAHOO.util.DataSource("/proctcae/pages/form/fetchCrf?");
            myDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
            myDataSource.responseSchema = {
                resultsList: "shippedRecordSet.searchCrfDTOs",
                fields: ["title","version","effectiveStartDate", "site", "studyShortTitle", "status", "actions"],
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

</head>
<body>
<chrome:box title="Manage" autopad="true">
	<form method="POST" action="manageForm">
	    <input name="useReqParam" value="true" type="hidden"/>
	    <input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}"/>
	
	    <tags:instructions code="crf.search.top"/>
	    <br/>
	    	<div>
		    	<table width="100%">
		    		<tr>
		    			<td width="75%" align="center">
				            <input type="text" id="searchString" name="searchString" style="width: 360px;"
				                   maxlength="50" size="50" onblur="isSpclChar('searchString');" value="${searchString}" class="form-control"/>
				            <tags:button color="blue" icon="search" type="button" value='Search' onclick="submitForm();" id="search"/>
				            <tags:indicator id="indicator"/>
		    			</td>
		    			<td width="25%" align="right">
		    				<proctcae:urlAuthorize url="/pages/form/basicForm">
		    					<tags:button color="blue" markupWithTag="a" icon="createForm" type="button" value='Create Form' href="/proctcae/pages/form/basicForm" />
		    				</proctcae:urlAuthorize>
		    			</td>
		    		</tr>
		    		<tr>
				        <td width="75%" align="center">
	                        <ul id="searchString.error" style="display:none;left-padding:8em;" class="errors">
					                    <li><spring:message code='special.character.message' text='special.character.message'/></li>
					        </ul>
				        </td>
			        	<td width="25%" align="right"></td>
		        	</tr>
		    	</table>
  	        	<br/><br/>
	        </div>
	</form>

    <div id="noForm">
        <proctcae:urlAuthorize url="/pages/form/basicForm">
            <table width="100%">
                <tr>
                    <td align="right">
                        <tags:button color="blue" markupWithTag="a" id="hiddenForms" value="View hidden forms" size="small" href="hiddenForms"/>
                    </td>
                </tr>
            </table>
        </proctcae:urlAuthorize>
    </div>


    <div class="yui-skin-sam">
        <table width="100%">
            <tr>
                <td width="72%">
                    <div id="pag"></div>
                </td>
                <td width="28%" align="right">
                    Show/Hide Column:
                        <select id="columnOptionsForCaseTable" name="columnOptionsForCaseTable" multiple="multiple" title="Show/Hide Columns">
                            <option value="title" selected="selected">Title</option>
                            <option value="version" selected="selected">Version</option>
                            <option value="effectiveStartDate" selected="selected">Effective date</option>
                            <option value="studyShortTitle" selected="selected">Study</option>
                            <option value="status" selected="selected">Status</option>
                        </select>
                </td>
            </tr>
        </table>

        <div id="basic">
        </div>
    </div>
</chrome:box>

</body>