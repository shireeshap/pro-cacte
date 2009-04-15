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
            border-left: 1px solid #C3D9FF;
            border-bottom: 1px solid #C3D9FF;
            width: 100%;
            font-size: small;
        }

        td.data {
            border-bottom: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-size: small;
            white-space: nowrap;
            text-align: center;

        }

        td.data-left {
            border-bottom: 1px solid #77a9ff;
            border-left: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-weight: bold;
            white-space: nowrap;
            background-color: #cccccc;
            text-align: center;
        }

        td.header-top {
            border-top: 1px solid #77a9ff;
            border-bottom: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-weight: bold;
            text-align: center;
            background-color: #cccccc;
        }

    </style>
</head>
<body>
<div id="careResultsTable">

    <table class="widget" cellspacing="0">
        <td>

        </td>
        <c:forEach items="${dates}" var="dates">
            <td class="header-top">
               <fmt:formatDate value="${dates}" pattern="MM-dd-yy"/>
            </td>
        </c:forEach>
        <c:forEach items="${resultsMap}" var="categoryMap">
            <tr>
                <td class="data-left">
                        ${categoryMap.key.name}
                </td>
            </tr>
            <c:forEach items="${categoryMap.value}" var="symptomMap">
                <tr>
                    <td class="data-left">
                            ${symptomMap.key.term}
                    </td>
                </tr>
                <c:forEach items="${symptomMap.value}" var="careResults">
                <tr>
                    <td class="data-left">
                        ${careResults.key.proCtcQuestionType.displayName}
                    </td>
                    <c:forEach items="${careResults.value}" var="value">
                        <td class="data">
                            ${value}
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