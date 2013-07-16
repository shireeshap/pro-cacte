<%@ attribute name="index" type="java.lang.String" required="true" %>
<%@ attribute name="leadCRA" type="gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff" required="false" %>
<%@ attribute name="readOnly" type="java.lang.Boolean" required="true" %>
<%@ attribute name="inputName" required="true" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<p id="splitter"/>

<tr id="row-${index}">
	<td>
		<c:choose>
	         <c:when test="${readOnly}">
	             	${leadCRA.displayName} &nbsp;&nbsp;
	         </c:when>
	         <c:otherwise>  
				   <form:input path="${inputName}" id="${inputName}" cssClass="validate-NOTEMPTY"
					            title="Lead Site CRA" cssStyle="display:none;"/>
			       <tags:yuiAutocompleter inputName="${inputName}Input"
			                              value="${leadCRAs[index].displayName}" required="false"
			                              hiddenInputName="${inputName}"/>
	         </c:otherwise>
	     </c:choose>	
    </td>
    <c:choose>
    	<c:when test="${readOnly}">
   		    <td width="20%" align="center">
	            <c:choose>
	                <c:when test="${leadCRA.roleStatus.displayName eq 'Active'}">
	                    <tags:button color="red" type="button" value="De-activate"
	                                 onclick="changeStatus('${leadCRA.roleStatus.displayName}','${leadCRA.id}','${tab.targetNumber}')"
	                                 size="small"/>
	                </c:when>
	                <c:otherwise>
	                    <tags:button color="blue" type="button" value="Activate"
	                                 onclick="changeStatus('${leadCRA.roleStatus.displayName}','${leadCRA.id}','${tab.targetNumber}')"
	                                 size="small"/>
	                </c:otherwise>
	            </c:choose>
			</td>
		    <td width="25%" >${leadCRA.roleStatus.displayName} since <tags:formatDate
		            value="${leadCRA.statusDate}"/>
		    </td>
    	</c:when>
    </c:choose>
    <td>
		     <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}" href="javascript:deleteLeadCra('${index}');">
		        <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete" style="vertical-align:middle;text-align:left">
		    </a>
	</td>
</tr>