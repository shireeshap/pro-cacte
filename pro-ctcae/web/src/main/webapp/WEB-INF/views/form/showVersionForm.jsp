<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<html>
<head><title></title>
    <style type="text/css">
        .shaded {
            background-color: #99ff99;
        }
    </style>
</head>
<body>


<c:forEach items="${crfs}" var="crf">
    <tr class="even childTableRow_${parentCrfId}" >
        <td class="data"></td>
        <td class="data"></td>
        <td class="data"><img src="../../images/arrow.jpg" height="13"></td>
        <td class="data shaded">${crf.crfVersion}</td>
        <td class="data shaded"><tags:formatDate value="${crf.effectiveStartDate}"/></td>
            <%--<td class="data shaded"><tags:formatDate value="${crf.effectiveEndDate}"/></td>--%>
        <td class="data shaded">${crf.status}</td>
    </tr>
    <%--<tr class="even childTableRow_${parentCrfId}" id="details_row_${crf.id}" onmouseover="highlightrow('${crf.id}');"--%>
        <%--onmouseout="removehighlight('${crf.id}');">--%>
        <%--<td class="data">--%>
            <%--<div id="img_${crf.id}" class="indIcon"--%>
                 <%--onclick="showPopUpMenu('${crf.id}',-105,-130,'${crf.status}')">--%>
                <%--<img src="../../images/menu.png" alt=""/>--%>
            <%--</div>--%>
        <%--</td>--%>
        <%--<td class="data"></td>--%>
        <%--<td class="data"><img src="../../images/arrow.jpg" height="13"></td>--%>
        <%--<td class="data shaded">${crf.crfVersion}</td>--%>
        <%--<td class="data shaded"><tags:formatDate value="${crf.effectiveStartDate}"/></td>--%>
            <%--<td class="data shaded"><tags:formatDate value="${crf.effectiveEndDate}"/></td>--%>
        <%--<td class="data shaded">${crf.status}</td>--%>
    <%--</tr>--%>
</c:forEach>
</body>
</html>