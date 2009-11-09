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
    <tr class="even childTableRow_${parentCrfId}">
        <td class="data"></td>
        <td class="data"></td>
        <td class="data"><a href="viewForm?crfId=${crf.id}">View form</a></td>
        <td class="data shaded">${crf.crfVersion}</td>
        <td class="data shaded"><tags:formatDate value="${crf.effectiveStartDate}"/></td>
        <td class="data shaded">${crf.status}</td>
    </tr>
</c:forEach>
</body>
</html>