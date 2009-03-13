<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head><title>Simple jsp page</title>
    <style type="text/css">
        table.widget {
            border-left: 1px solid #C3D9FF;
            border-bottom: 1px solid #C3D9FF;
            width: 90%;
            font-size:small;
        }

        td.data {
            border-bottom: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-size: small;
        }

        td.data-left {
            border-bottom: 1px solid #77a9ff;
            border-left: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-weight: bold;
            white-space: nowrap;
        }

        td.header-top {
            border-top: 1px solid #77a9ff;
            border-bottom: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-weight: bold;
            text-align: center;
        }

        #formStatusTable {
            overflow: scroll;
            margin: 0 20px;
        }

        td.Scheduled {
            background-color: #0051fc;
        }

        td.In-progress {
            background-color: #ff9900;
        }

        td.Completed {
            background-color: #00cc00;
        }
        td.Past due{
            background-color: red;
        }

    </style>
</head>
<body>
<div id="formStatusTable">
    <table class="widget" cellspacing="0">
        <tr>
            <td class="header-top"><spring:message code="schedulecrf.label.participant"/>
            </td>
            <c:forEach items="${calendar}" var="date">
                <td class="header-top">
                        ${date}
                </td>
            </c:forEach>
        </tr>
        <c:forEach items="${crfStatusMap}" var="crfStatus">
            <tr>
                <td class="data-left">
                        ${crfStatus.key.displayName}
                </td>
                <c:forEach items="${crfStatus.value}" var="crfStatusName">
                    <td class="data ${crfStatusName}">
                            ${crfStatusName}&nbsp;
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
</div>
<br>

</body>
</html>