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
<%@taglib prefix="organization" tagdir="/WEB-INF/tags/organization" %>

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
	<organization:thirdlevelmenu selected="createOrganization"/>
	<c:choose>
	    <c:when test="${param['organizationId'] eq null}">
	        <chrome:flashMessage flashMessage="New Organization has been created successfully"/>
	    </c:when>
	    <c:otherwise>
	        <chrome:flashMessage flashMessage="Organization has been updated successfully"/>
	    </c:otherwise>
	</c:choose>
	
	<chrome:box title="Confirmation">
	    <chrome:division title="organization.division.details">
	        <table border="0" style="width:100%">
	            <tr>
	                <td>
	                    <div class="row">
	                        <div class="label"><spring:message code="organization.name"/>:</div>
	                        <div class="value">${createOrganizationCommand.organization.name} </div>
	                    </div>
	                </td>
	            </tr>
	            <tr>
	            	<td>
	                    <div class="row">
	                        <div class="label"><spring:message code="organization.assignedIdentifier"/>:</div>
	                        <div class="value">${createOrganizationCommand.organization.nciInstituteCode} </div>
	                    </div>
	            	</td>
	            </tr>
	        </table>
	    </chrome:division>
	</chrome:box>
	
	<div style="float:right">
	    <table>
	        <tr>
	            <td><tags:button value="Return to search results" color="blue" markupWithTag="a"
	                             href="searchOrganization?searchString=${searchString}"/></td>
	            <td>
	            <td><tags:button value="Create Organization" color="blue" markupWithTag="a"
	                             href="createOrganization"/></td>
	            <td>
	            <td><tags:button value="Edit" color="blue" markupWithTag="a"
	                             href="createOrganization?organizationId=${createOrganizationCommand.organization.id}"/></td>
	            <td>
	                <tags:button value="Finish" color="blue" markupWithTag="a"
	                             href="/proctcae"/>
	            </td>
	        </tr>
	    </table>
	</div>
</body>
</html>