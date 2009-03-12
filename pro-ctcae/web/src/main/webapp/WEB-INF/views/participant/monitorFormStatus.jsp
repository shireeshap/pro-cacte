<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Simple jsp page</title>
    <style type="text/css">
        table.widget {
            border-left: 1px solid #C3D9FF;
            border-bottom: 1px solid #C3D9FF;
            width: 90%;
            background-color: #FFFFFF;
            font-size: x-small;
            font-family: verdana, arial;
        }

        td.header {
            border-bottom: 1px solid #77a9ff;
            border-left: 1px solid #77a9ff;
            font-weight: normal;
            font-size: x-small;
        }

        td.header-left {
            border-bottom: 1px solid #77a9ff;
            border-left: 1px solid #77a9ff;
            font-weight: normal;
            font-size: x-small;
            font-weight: bold;;
        }

        td.header-top {
            border-top: 1px solid #77a9ff;
            border-bottom: 1px solid #77a9ff;
            border-left: 1px solid #77a9ff;
            font-weight: normal;
            font-size: x-small;
            font-weight: bold;
        }

    </style>
</head>
<body>
<table class="widget" cellpadding="0" cellspacing="0">
    <tr>
        <td class="header-top">&nbsp;
        </td>
        <c:forEach items="${calendar}" var="date">
            <td class="header-top">
                    ${date}
            </td>
        </c:forEach>
    </tr>
    <c:forEach items="${crfStatusMap}" var="crfStatus">
        <tr>
            <td class="header-left">
                    ${crfStatus.key.displayName}
            </td>
            <c:forEach items="${crfStatus.value}" var="crfStatusName">
                <td class="header">
                        ${crfStatusName}&nbsp;
                </td>
            </c:forEach>
        </tr>
    </c:forEach>

</table>


</body>
</html>