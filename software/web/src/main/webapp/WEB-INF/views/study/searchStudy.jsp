<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <tags:formActionMenu/>
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
    Event.observe(window, "load", function() {

        acCreate(new siteAutoComplterWithSecurity('site'))


        initializeAutoCompleter('site',
                '${site.displayName}', '${site.id}')

        initSearchField()
    })

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
        <input name="useReqParam" value="true" type="hidden"/>

        <p><tags:instructions code="study.search.top"/></p>

        <div class="content">
            <div class="row" name="inputs">
                <div class="label"> Search By</div>
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
                <tags:renderAutocompleter propertyName="site"
                                          displayName="study.label.study_site"
                                          required="false" size="70" noForm="true"/>
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
<a name="searchResults"/>
<chrome:box title="Results">

    <table width="100%">
        <tr>
            <c:choose>
                <c:when test="${totalRecords eq 0}">
                    <td colspan="5">No results found</td>
                </c:when>
                <c:otherwise>
                    <td style="white-space:nowrap;font-size:10px;">${totalRecords} results found, displaying ${begin}
                        to ${end}</td>
                    <td colspan="3" style="text-align:center;font-size:12px;"> Page:
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
            <tags:sortablecolumn sortDir="${sortDir}" sort="${sort}" title="Study identifier" name="studyIdentifier"/>
            <tags:sortablecolumn sortDir="${sortDir}" sort="${sort}" title="Short title" name="shortTitle"/>
            <tags:sortablecolumn sortDir="${sortDir}" sort="${sort}" title="Funding sponsor" name="fundingSponsor"/>
            <tags:sortablecolumn sortDir="${sortDir}" sort="${sort}" title="Coordinating center"
                                 name="coordinatingCenter"/>
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
            </tr>

        </c:forEach>
    </table>
</chrome:box>


</body>
</html>