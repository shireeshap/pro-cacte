<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="hiddenInputName" %>
<%@ attribute name="value" %>
<%@attribute name="inputName" required="true" %>
<%@attribute name="required" required="false" %>
<%@attribute name="contentBoxMaxHeight" required="false" %>
<%@attribute name="staff" type="gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff" required="false" %>

<c:if test="${contentBoxMaxHeight eq null}">
     <c:set var="contentBoxMaxHeight" value="20"/>
</c:if>

    <!--[if !IE 7]><!-->
        <style>
            .yui-skin-sam .yui-ac-content {
                max-height: 20em;
                overflow-x: hidden;
                overflow-y: auto;
                width:400px;
            }
        </style>
    <!--<![endif]-->

    <!--[if IE 7]>
        <style>
            .yui-skin-sam .yui-ac-content {
                max-height: ${contentBoxMaxHeight}em;
                overflow-x: hidden;
                overflow-y: auto;
                width:335px;
        }
        </style>
    <![endif]-->

<table cellpadding="0" cellspacing="0">
    <tr height="0">
        <td height="0" style="vertical-align:top;border:0px;">
            <div class="yui-skin-sam" >
                <div id="midSize-AutoComplete" style="position:relative;padding-bottom:0px; width:400px;">
                    <c:if test="${required}"><tags:requiredIndicator/></c:if>
                    <input id="${inputName}" type="text" value="${value}" class="pending-search" />
                </div>
                <div id="${inputName}Autocomplete" style="z-index:300000;width:335px;" class="yui-ac-container"></div>
            </div>
        </td>
        <td style="vertical-align:top;padding-left:10px;border:0px;">
        
            <a href="javascript:clearInput('${hiddenInputName}')">
                <img id="${inputName}-clear" style="vertical-align: top;"
                     src="/proctcae/images/blue/clear-left-button.png"
                     value="Clear" name="C"/>
            </a>
            <tags:indicator id="${inputName}-indicator"/>
        </td>
        <td>
	        <!-- TBD:
	        	- Currently the activate/de-activate button is not fully functional.
	        	- It is permanantely associated with the staff it had while the pageload.
	        	- Need to address activate/de-activate for ODC & PI 
	         -->
        	<c:choose>
	            <c:when test="${staff.roleStatus.displayName eq 'Active'}">
	                <tags:button color="red" type="button" value="De-activate"
	                             onclick="changeStatus('${staff.roleStatus.displayName}','${staff.id}','${tab.targetNumber}')"
	                             size="small"/>
	            </c:when>
	            <c:otherwise>
	                <tags:button color="blue" type="button" value="Activate"
	                             onclick="changeStatus('${staff.roleStatus.displayName}','${staff.id}','${tab.targetNumber}')"
	                             size="small"/>
	            </c:otherwise>
	       	</c:choose>
        </td>
    </tr>
</table>


