<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
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

    function showPopUpMenuClinicalStaff(cid, status, odc) {
        var html = '<div id="search-engines"><ul>';
        if (odc == true || odc == 'true') {
        <proctcae:urlAuthorize url="/pages/admin/viewClinicalStaff">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/admin/viewClinicalStaff"/>?clinicalStaffId=' + cid + '\'">View staff</a></li>';
        </proctcae:urlAuthorize>
        } else {
        <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
            html += '<li><a href="#" onclick="location.href=\'<c:url value="/pages/admin/createClinicalStaff"/>?clinicalStaffId=' + cid + '\'">Edit staff</a></li>';
        </proctcae:urlAuthorize>
            if (status == 'Active') {
                html += '<li><a href="#" onclick="javascript:effectiveStaff(' + cid + ',\'' + status + '\')">Deactivate</a></li>';
            } else {
                html += '<li><a href="#" onclick="javascript:effectiveStaff(' + cid + ',\'' + status + '\')">Activate</a></li>';
            }
        }
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
    function effectiveStaff(cId, status) {
        var request = new Ajax.Request("<c:url value="/pages/admin/clinicalStaff/effectiveStaff"/>", {
            parameters:<tags:ajaxstandardparams/>+"&cId=" + cId + "&status=" + status,
            onComplete:function(transport) {
                showConfirmationWindow(transport, 650, 280);
                AE.registerCalendarPopups();
            },
            method:'get'
        })
    }
</script>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
                <li id="thirdlevelnav-x" class="tab ">
                    <div>
                        <a href="<c:url value="createClinicalStaff"/>"><tags:message
                                code="clinicalStaff.tab.createStaff"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/admin/searchClinicalStaff">
                <li id="thirdlevelnav-x" class="tab selected">
                    <div>
                        <a href="<c:url value="searchClinicalStaff"/>"><tags:message
                                code="clinicalStaff.tab.searchStaff"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>

        </ul>
    </div>
</div>
<chrome:box title="clinicalStaff.box.searchCriteria" autopad="true">
    <form method="POST" action="searchClinicalStaff#searchResults">
        <input name="useReqParam" value="true" type="hidden"/>

        <p><tags:instructions code="clinicalStaff.search.top"/></p>

        <div class="row">
            <div class="label"><spring:message code='participant.label.first_name' text=''/></div>
            <div class="value"><input type="text" id="firstName" name="firstName" maxlength="30" value="${firstName}"/>
            </div>
        </div>
        <div class="row">
            <div class="label"><spring:message code='participant.label.last_name' text=''/></div>
            <div class="value"><input type="text" id="lastName" name="lastName" maxlength="30" value="${lastName}"/>
            </div>
        </div>
        <div class="row">
            <div class="label"><spring:message code='participant.label.participant_identifier' text=''/></div>
            <div class="value"><input type="text" id="identifier" name="identifier" maxlength="30"
                                      value="${identifier}"/>
            </div>
        </div>

        <div id="error"></div>
        <div class="row">
            <div class="label"></div>
            <div class="value">
                <tags:button color="blue" icon="search" type="button" value='Search' onclick="submitForm();"/>
                <tags:indicator id="indicator"/>
            </div>
        </div>
        <input type="hidden" name="sort" value="${sort}" id="sort"/>
        <input type="hidden" name="page" value="${page}" id="page"/>
        <input type="hidden" name="rowsPerPage" value="${rowsPerPage}" id="rowsPerPage"/>
        <input type="hidden" name="sortDir" value="${sortDir}" id="sortDir"/>
        <input type="hidden" name="doSort" value="false" id="doSort"/>
    </form>
</chrome:box>
<chrome:box title="Results">
    <a name="searchResults"/>
    <table width="100%">
        <tr>
            <c:choose>
                <c:when test="${totalRecords eq 0}">
                    <td colspan="6" style="font-weight:bold;">No results found
                        <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
                            <tags:button value="Create New Staff Profile" color="blue" markupWithTag="a"
                                         href="createClinicalStaff" size="small"/>
                        </proctcae:urlAuthorize>
                    </td>
                </c:when>
                <c:otherwise>
                    <td style="white-space:nowrap;font-size:10px;">
                            ${totalRecords} results found, displaying ${begin}to ${end}<br/>
                        <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
                            <tags:button value="Create New Staff Profile" color="blue" markupWithTag="a"
                                         href="createClinicalStaff" size="small"/>
                        </proctcae:urlAuthorize>
                    </td>
                    <td colspan="4" style="text-align:center;font-size:12px;"> Page:
                        <c:if test="${page > 1}"><a
                                href="javascript:setPage(${page-1})"><img
                                src="../../images/table/prevPage.gif" height="12" width="15"></a>&nbsp;&nbsp;</c:if>
                        <c:forEach var="pageNumber" begin="1" end="${numberOfPages}" step="1">
                            <c:choose>
                                <c:when test="${pageNumber eq page}">
                                    <b>${pageNumber}</b>&nbsp;
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:setPage(${pageNumber})">${pageNumber}</a>&nbsp;
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${page < numberOfPages}">&nbsp;&nbsp;<a
                            href="javascript:setPage(${page+1})"><img src="../../images/table/nextPage.gif" height="12" width="15"></a></c:if>
                    </td>
                    <td style="white-space:nowrap;vertical-align:top;font-size:10px;">Display
                        <select name="rowsPerPageDisplay" onchange="setRowsPerPage(this.value)">
                            <option value="5" <c:if test="${rowsPerPage eq 5}">selected</c:if>>5</option>
                            <option value="15" <c:if test="${rowsPerPage eq 15}">selected</c:if>>15</option>
                            <option value="25" <c:if test="${rowsPerPage eq 25}">selected</c:if>>25</option>
                            <option value="50" <c:if test="${rowsPerPage eq 50}">selected</c:if>>50</option>
                            <option value="75" <c:if test="${rowsPerPage eq 75}">selected</c:if>>75</option>
                        </select>
                        rows per page
                    </td>

                </c:otherwise>
            </c:choose>
        </tr>
        <tr>
            <td colspan="5"/>
            &nbsp;</tr>
        <tr>
            <tags:sortablecolumn sortDir="${sortDir}" sort="${sort}" title="Last name" name="lastName"/>
            <tags:sortablecolumn sortDir="${sortDir}" sort="${sort}" title="First name" name="firstName"/>
            <tags:sortablecolumn sortDir="${sortDir}" sort="${sort}" title="Site" name="site"/>
            <tags:sortablecolumn sortDir="${sortDir}" sort="${sort}" title="Study" name="study"/>
            <td class="tableHeader">Status</td>
            <td class="tableHeader" width="80px">Actions</td>
        </tr>
        <c:forEach items="${searchResults}" var="row" varStatus="status">
            <c:set var="class" value="odd"/>
            <c:if test="${status.index%2==0}">
                <c:set var="class" value="even"/>
            </c:if>
            <tr class="${class}">
                <td>${row[0]}</td>
                <td>${row[1]}</td>
                <td>${row[2]}</td>
                <td>${row[3]}</td>
                <td>${row[4]}</td>
                <td>${row[5]}</td>
            </tr>
        </c:forEach>
    </table>
</chrome:box>


</body>
</html>