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
    <br/>
    <table class="widget" cellspacing="0" align="center">
        <tr>
            <td class="header-top"></td>
            <td class="header-top">
                Symptom
            </td>
            <td class="header-top">
                Number of times added
            </td>
            <c:forEach items="${results}" var="lineitem" varStatus="status">
        <tr id="details_row_${status.index}" onmouseover="highlightrow('${status.index}');"
            onmouseout="removehighlight('${status.index}');">
            <td align="right" style="margin-left:10px;">
                <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                   id="menuActions${status.index}"><span
                        class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
                <script>
                    showPopUpMenu('${status.index}', '${participant.id}', '${status.index}', getLinksHtml('${lineitem[0]}'));
                </script>
            </td>
            <td class="data">
                    ${lineitem[0]}
            </td>
            <td class="data">
                    ${lineitem[1]}
            </td>
        </tr>
        </c:forEach>
    </table>
    <br/>
</chrome:box>
</body>
</html>



