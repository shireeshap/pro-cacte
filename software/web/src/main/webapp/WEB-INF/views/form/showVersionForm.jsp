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

<table width="100%">
    <tr>
        <td>
            <b>Title</b>
        </td>
        <td>
            <b>Version</b>
        </td>
        <td>
            <b>Effective date</b>
        </td>
        <td>
            <b>Status</b>
        </td>
        <td>
            <b>Action</b>
        </td>
    </tr>
    <c:forEach items="${crfs}" var="crf">
        <tr class="even childTableRow_${parentCrfId}">
            <td class="data">${crf.title}</td>
            <td class="data">${crf.crfVersion}</td>
            <td class="data"><tags:formatDate value="${crf.effectiveStartDate}"/></td>
            <td class="data">${crf.status}</td>
            <td class="data"><a href="viewForm?crfId=${crf.id}"><u>View form</u></a></td>
        </tr>
    </c:forEach>
</table>
<br><br>

<div style="float:right;">
    <tags:button color="blue" type="button" id="flow-cancel" size="small"
                 cssClass="previous ibutton" value="Close" icon="x"
                 onclick="closeWindow()"/>
</div>
</body>
</html>