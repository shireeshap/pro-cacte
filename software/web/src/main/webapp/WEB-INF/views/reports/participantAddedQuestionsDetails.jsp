<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
</head>
<body>
<chrome:box title="Report - ${symptom}">
    <br/>
    <table class="widget" cellspacing="0" align="center">
        <tr>
                <%--<td class="header-top"></td>--%>
            <td class="header-top">
                Participant
            </td>
            <td class="header-top">
                Response date
            </td>
            <td class="header-top">
                Response
            </td>
            <c:forEach items="${results}" var="lineitem" varStatus="status">
            <c:forEach items="${lineitem.value}" var="arr">
        <tr>
            <td class="data">
                    ${lineitem.key.displayName}
            </td>
            <td class="data">
                    ${arr[0]}
            </td>
            <td class="data">
                    ${arr[1]}
            </td>
        </tr>
        </c:forEach>
        </c:forEach>
    </table>
    <br/>
    <div style="float:right; padding-right:10px">
        <tags:button color="blue" type="button" id="flow-cancel"
                     cssClass="previous ibutton" value="Close" icon="x"
                     onclick="closeWindow()" size="small"/>
    </div>
    <br/>  <br/>
</chrome:box>
</body>
</html>



