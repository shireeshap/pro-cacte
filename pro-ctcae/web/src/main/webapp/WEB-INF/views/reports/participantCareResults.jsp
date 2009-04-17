<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
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
            background-color: #f1f1f1;
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
        }

        td.actual-question {
            background-color: #f1f1f1;
            padding-left: 18px;
        }

        td.displayOrder4 {
            background-color: red;
        }

        td.displayOrder5 {
            background-color: red;
        }


    </style>
</head>
<body>
<div id="careResultsTable">

    <table class="widget" cellspacing="0">
        <col/>
        <tr>
            <td>
                &nbsp;
            </td>
            <c:forEach items="${dates}" var="dates">

            <td class="header-top">

                <fmt:formatDate value="${dates}" pattern="MM-dd-yy"/>
            </td>

            </c:forEach>
            <c:forEach items="${resultsMap}" var="categoryMap">
        <tr>
            <td class="category-name">
                    ${categoryMap.key.name}
            </td>
        </tr>
        <c:forEach items="${categoryMap.value}" var="symptomMap">
            <tr>
                <td class="subcategory-name">
                        ${symptomMap.key.term}
                </td>
            </tr>
            <c:forEach items="${symptomMap.value}" var="careResults">
                <tr>
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
        </c:forEach>
    </table>

</div>
</body>
</html>
