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
	YAHOO.util.Event.addListener(window, "load", function() {
	    YAHOO.example.Basic = function() {
	        var myColumnDefs = [ 
	            {key:"studyAssignedIdentifier", label:"Study identifier",sortable:true, resizeable:false, width:140}, 
	            {key:"shortTitle", label:"Short title", sortable:true,resizeable:false, width:140}, 
	            {key:"fundingSponsorDisplayName", label:"Funding sponsor", sortable:true, resizeable:false, width:235}, 
	            {key:"coordinatingCenterDisplayName", label:"Coordinating center", sortable:true, resizeable:false, width:235}, 
	            {key:"actions", label:"Actions", sortable:false, resizeable:false, width:80} 
	        ];

	        var myDataSource = new YAHOO.util.DataSource("https://localhost:8443/proctcae/pages/study/fetchStudy");
	        myDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
	        myDataSource.responseSchema = {
       		    resultsList: "shippedRecordSet.searchStudyDTO", 
	            fields: ["studyAssignedIdentifier", "shortTitle","fundingSponsorDisplayName", "coordinatingCenterDisplayName", "actions"]
	        };

	        var myDataTable = new YAHOO.widget.DataTable("basic", myColumnDefs, myDataSource, {caption:""});
	                
	        return {
	            oDS: myDataSource,
	            oDT: myDataTable
	        };
	    }();
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
                    <select id="searchType" name="searchType">
                        <c:forEach items="${searchCriteria}" var="item">
                            <option value="${item.code}"
                                    <c:if test="${searchType eq item.code}">selected</c:if>>${item.desc}</option>
                        </c:forEach>
                    </select>
                    <input type="text" id="searchText" name="searchText" size="25" value="${searchText}">

                    <div id="error"></div>
                </div>
                <input type="hidden" id="site" name="site"/>

                <div class="row">
                    <div class="label"><tags:message code='study.label.study_site'/></div>
                    <div class="value">
                        <tags:yuiAutocompleter inputName="siteInput" value="${site.displayName}" required="false"
                                               hiddenInputName="site"/>
                    </div>
                </div>
            </div>
            <div style="padding-left:140px">
                <tags:button color="blue" icon="search" type="button" value='Search' onclick="filterAndRefreshYuiTableForUser();"/>
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
<div id="basic">
</div>
</div>
</chrome:box>
</body>
</html>