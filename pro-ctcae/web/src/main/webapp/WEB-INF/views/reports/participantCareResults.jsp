<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
            width: 100px;
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
            background-color: #D5D5D5;
            padding-left: 18px;
        }

        td.displayOrder0 {
            background-color: #ccccff;
        }

        td.displayOrder1 {
            background-color: #ccccff;
        }

        td.displayOrder2 {
            background-color: #ccccff;
        }

        td.displayOrder3 {
            background-color: #ccccff;
        }

        td.displayOrder4 {
            background-color: red;
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
            background: #ffc url( ../images/pointer.gif ) no-repeat -10px 5px;
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
<chrome:box title="Report - ${participant.displayName} [${participant.assignedIdentifier}]">

    <div id="careResultsTable">
        <a href="javascript:getChartView()">Switch to graphical view</a> | <a
            href="<c:url value='/pages/reports/participantCarePdf'/>" target="_blank"><img
            src="/proctcae/images/table/pdf.gif"
            alt="pdf"/></a> | <a
            href="<c:url value='/pages/reports/participantCareExcel'/>" target="_blank"><img
            src="/proctcae/images/table/xls.gif"
            alt="xls"/></a>

        <br/>
        <br/>
        <table class="widget" cellspacing="0">
            <col/>
            <tr>
                <td class="header-top">
                    Symptom
                </td>
                <td class="header-top">
                    Attribute
                    <img alt="Help" src="/proctcae/images/q.gif"
                         onclick="$('attribute-help-content').style.display='inline'"/>
                </td>
                <c:forEach items="${dates}" var="dates">

                <td class="header-top">
                    <fmt:formatDate value="${dates}" pattern="MM/dd/yy"/>
                </td>

                </c:forEach>
                <c:forEach items="${resultsMap}" var="symptomMap">
            <tr>
                <td class="subcategory-name">
                    <b>${symptomMap.key.term}</b>
                </td>
            </tr>
            <c:forEach items="${symptomMap.value}" var="careResults">
                <tr>
                    <td>&nbsp;</td>
                    <td class="actual-question">
                            ${careResults.key.proCtcQuestionType.displayName}
                    </td>
                    <c:forEach items="${careResults.value}" var="value">
                        <td class="data displayOrder${value.displayOrder}">
                                ${value.value}
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
            </c:forEach>
        </table>

    </div>
    <div id="careResultsGraph" style="display:none">
        <a href="javascript:getTableView()">Switch to tabular view</a>

        <table class="widget" cellspacing="0" width="100%">
            <tr>
                <td class="subcategory-name">
                    <c:forEach items="${resultsMap}" var="symptomMap">
                        <a href="javascript:getChart('${symptomMap.key.id}')"><b>${symptomMap.key.term}</b></a>
                        <br/>
                    </c:forEach>
                </td>
                <td>
                    <c:forEach items="${resultsMap}" var="symptomMap">
                        <div id="div_questiontype_${symptomMap.key.id}" name="div_questiontype"
                             style="display:none">
                            <c:forEach items="${symptomMap.value}" var="careResults">
                                <input type="checkbox" checked="true" name="questiontype_${symptomMap.key.id}"                 
                                       value="${careResults.key.proCtcQuestionType.displayName}"
                                       onclick="updateChart(this,'${symptomMap.key.id}');">${careResults.key.proCtcQuestionType.displayName}
                                &nbsp;
                            </c:forEach>
                        </div>
                    </c:forEach>
                    <iframe id="graph" height="500" width="600" frameborder="0" scrolling="auto"></iframe>
                    <table class="widget" cellspacing="0">
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
                                    <td class="subcategory-name">
                                            ${validValue}
                                    </td>
                                </c:forEach>
                            </tr>

                        </c:forEach>
                    </table>
                </td>
            </tr>
        </table>
    </div>
</chrome:box>
</body>
</html>
