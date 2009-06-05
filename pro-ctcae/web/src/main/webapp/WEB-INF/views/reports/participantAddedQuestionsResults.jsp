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
            <td align="right">
                <div id="img_${status.index}" class="indIcon"
                     onclick="showPopUpMenu('${status.index}','${participant.id}','${schedule.id}',-105,-130,getLinksHtml('${lineitem[0]}'))">
                    <img src="../../images/menu.png" alt=""/>
                </div>
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
<div id="dropnoteDiv" class="ddnotediv shadowB" style="display:none;left:0;top:0">
    <div id="dropnoteinnerDiv" class="shadowr">
    </div>
</div>
</body>
</html>



