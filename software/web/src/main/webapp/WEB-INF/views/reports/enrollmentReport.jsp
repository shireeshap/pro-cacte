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
            <td class="header-top">
                Institute
            </td>
            <td class="header-top">
                Number of participants enrolled
            </td>
            <td class="header-top">
                Date of last enrollment
            </td>
            <c:forEach items="${results}" var="lineitem" varStatus="status">
        <tr>
            <td class="data">
                    ${lineitem.studySite.displayName}
            </td>
            <td class="data">
                    ${lineitem.numberOfParticipants}
            </td>
            <td class="data">
                <tags:formatDate value="${lineitem.lastEnrollment}"/>
            </td>
        </tr>
        </c:forEach>
    </table>
    <br/>
</chrome:box>
</body>
</html>



