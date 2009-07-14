<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <tags:stylesheetLink name="table_menu"/>
</head>
<body>
<chrome:box title="Report">
    <a href="javascript:reportResults('','${group}');" style="margin-left:2em;" class="link">Back to chart</a>
    <br/>
    <br/>
    <chrome:division title="${title}"/>
    <table class="widget" cellspacing="0" align="center">
        <c:forEach items="${results}" var="row">
            <c:set var="participant" value="${row.key}"/>
            <tr id="items_row_${participant.id}">
                <td style="padding-left:2em;width:1%">
                    <a href="javascript:showItems('${participant.id}','${row.value}','${att}','${period}')">
                        <img id="pShowImage_${participant.id}" src="../../images/arrow-right.png" style=""/>
                    </a>
                    <a href="javascript:hideItems('${participant.id}')">
                        <img id="pHideImage_${participant.id}" src="../../images/arrow-down.png" style="display:none"/>
                    </a>
                </td>
                <td style="text-align:left;padding-left:2em;white-space:nowrap">
                        ${participant.displayName}
                </td>
                <td colspan="4"/>
            </tr>
        </c:forEach>
    </table>
    <br/>
</chrome:box>
</body>
</html>



