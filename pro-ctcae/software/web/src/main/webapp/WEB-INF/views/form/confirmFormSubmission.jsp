<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>

    <style type="text/css">
        .label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            padding: 1px;
            font-size: 12px;
        }

    </style>

</head>
<body>
<spring:message code="confirmation" var="confirmation" />
<chrome:box title="${confirmation}" autopad="true">
    <table>
        <tr>
            <td class="label">
                <br/>
                <spring:message code="youCan" />&nbsp;<a href="../participant/participantInbox">
                <spring:message code="clickHere" /></a>&nbsp;<spring:message code="goToInbox" />
                <br/><br/>
<!--            <spring:message code="youCan" />&nbsp;<a href="../participant/responseReport?id=${scheduleid}">
                <spring:message code="clickHere" /></a>&nbsp;<spring:message code="viewOldResponses" />			 --> 
            </td>
        </tr>
    </table>
</chrome:box>

</body>
</html>