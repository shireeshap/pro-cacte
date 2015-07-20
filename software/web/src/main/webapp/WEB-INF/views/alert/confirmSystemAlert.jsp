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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <style type="text/css">
        .label {
            width: 12em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
        }

        #studyDetails td.label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            width: 12em;
            padding: 1px;
        }
    </style>
</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <li id="thirdlevelnav-x" class="tab selected">
                <div>
                    <a href="createSystemAlert"><tags:message code="alert.tab.createAlert"/></a>
                </div>
            </li>
            <li id="thirdlevelnav-x" class="tab">
                <div>
                    <a href="searchAlert"><tags:message code="alert.tab.searchAlert"/></a>
                </div>
            </li>
        </ul>
    </div>
</div>
<c:choose>
    <c:when test="${param['systemAlertId'] eq null}">
        <chrome:flashMessage flashMessage="New system alert has been created successfully"/>
    </c:when>
    <c:otherwise>
        <chrome:flashMessage flashMessage="System alert has been updated successfully"/>
    </c:otherwise>
</c:choose>

<chrome:box title="Confirmation">
    <chrome:division title="clinicalStaff.division.details">
        <table border="0" style="width:100%">
	        <tr>
		         <div class="row">
		             <div class="label"><spring:message code="alert.label.startDate"/>:</div>
		             <div class="value">${createSystemAlertCommand.alert.startDate} </div>
		         </div>
	        </tr>
	        <tr>
	            <div class="row">
	                <div class="label"><spring:message code="alert.label.endDate"/>:</div>
	                <div class="value">${createSystemAlertCommand.alert.endDate} </div>
	            </div>
	        </tr>
	        <tr>
	            <div class="row">
	                <div class="label"><spring:message code="alert.label.alertMessage"/>:</div>
	                <div class="value">${createSystemAlertCommand.alert.alertMessage} </div>
	            </div>
	        </tr>
        </table>
    </chrome:division>
</chrome:box>

<div style="float:right">
    <table>
        <tr>
            <td><tags:button value="Return to search results" color="blue" markupWithTag="a"
                             href="searchAlert?searchString=${systemAlertId}"/></td>
            <td>
            <td><tags:button value="Create New Alert" color="blue" markupWithTag="a"
                             href="createSystemAlert"/></td>
            <td>
            <td><tags:button value="Edit" color="blue" markupWithTag="a"
                             href="createSystemAlert?systemAlertId=${createSystemAlertCommand.alert.id}"/></td>
            <td>
                <tags:button value="Finish" color="blue" markupWithTag="a"
                             href="/proctcae"/>
            </td>
        </tr>
    </table>
</div>

</body>
</html>