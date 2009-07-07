<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <style type="text/css">
        table.widget {
            border: 1px solid #eaeaea;
            border-collapse: collapse;
        }

        table.widget col {
            width: 200px;
        }

        td.data {
            border: 1px solid #eaeaea;
            background-color: #D5D5D5;
            white-space: nowrap;
            text-align: center;

        }

        td.header-top {
            border: 1px solid #eaeaea;
            font-weight: bold;
            text-align: center;
            background-color: #cccccc;
        }

        td.category-name, td.subcategory-name, td.actual-question {
            border: 1px solid #eaeaea;
            text-align: left;
        }

        td.category-name {
            background-color: #fff;
            font-weight: bolder;
        }

        td.subcategory-name {
            background-color: #fff;
            padding-left: 6px;
            vertical-align: top;
        }

        td.help-values {
            border: 1px solid #eaeaea;
            background-color: #fff;
            padding-left: 6px;
            vertical-align: top;
            text-align: center;
        }

        td.actual-question {
            background-color: #009999;
            text-align: center;
            font-weight: bold;
            color: white;
        }

        #careResultsTable {
            overflow-x: scroll;
        }

        /* The hint to Hide and Show */
        .hint {
            z-index: 3; /* To handle the overlapping issue*/
            display: none;
            position: absolute;
            width: 700px;
            white-space: normal;
            margin-top: -4px;
            border: 1px solid #c93;
            padding: 10px 12px;
            opacity: .95;
            background: #ffc url(../images/pointer.gif) no-repeat -10px 5px;
        }
    </style>
</head>
<body>
<span id="attribute-help-content" class="hint" style="display: none;">
    <table class="widget" cellspacing="0" width="100%" align="center">
        <tr>
            <td colspan="6" align="right">
                <a href="javascript:hideHelp();">X</a>
            </td>
        </tr>
        <tr>
            <td class="header-top"></td>
            <td class="header-top">0</td>
            <td class="header-top">1</td>
            <td class="header-top">2</td>
            <td class="header-top">3</td>
            <td class="header-top">4</td>
        </tr>
        <c:forEach items="${questionTypes}" var="questionType">
            <tr>
                <td class="header-top">
                        ${questionType}
                </td>
                <c:forEach items="${questionType.validValues}" var="validValue">
                    <td class="help-values">
                            ${validValue}
                    </td>
                </c:forEach>
            </tr>

        </c:forEach>
    </table>
</span>
<chrome:box title="Report">

    <%--<a href="<c:url value='/pages/reports/studyLevelReportPdf'/>" target="_blank"><img--%>
    <%--src="/proctcae/images/table/pdf.gif"--%>
    <%--alt="pdf"/></a> | --%>
    <div align="right">
        <a href="<c:url value='/pages/reports/studyLevelReportExcel'/>" target="_blank">
            <img src="/proctcae/images/table/xls.gif" alt="xls"/>
        </a>
    </div>
    <div id="careResultsTable">
       <c:forEach items="${table}" var="participantTable">
            <chrome:division
                    title="Participant: ${participantTable.key.displayName} [${participantTable.key.assignedIdentifier}] "/>
            ${participantTable.value}
            <br/>
        </c:forEach>
    </div>
</chrome:box>
</body>
</html>
