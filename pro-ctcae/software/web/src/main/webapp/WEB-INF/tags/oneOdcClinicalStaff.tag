<%@ attribute name="index" type="java.lang.String" required="true" %>
<%@ attribute name="odc" type="gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff" required="false" %>
<%@ attribute name="readOnly" type="java.lang.Boolean" required="true" %>
<%@ attribute name="inputName" required="true" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<p id="splitter"/>

<tr id="row-${index}" style="height:30px;">
	<td width="50%">
		<c:choose>
	         <c:when test="${readOnly}">
	             	${odc.displayName} &nbsp;&nbsp;
	         </c:when>
	         <c:otherwise>  
				   <form:input path="${inputName}" id="${inputName}" cssClass="validate-NOTEMPTY"
					            title="Overall Data Coordinator " cssStyle="display:none;"/>
			       <tags:yuiAutocompleter inputName="${inputName}Input"
			                              value="${overallDataCoordinators[index].displayName}" required="false"
			                              hiddenInputName="${inputName}"/>
	         </c:otherwise>
	     </c:choose>	
    </td>
    <c:choose>
    	<c:when test="${readOnly}">
   		    <td width="20%" align="center">
	            <c:choose>
	                <c:when test="${odc.roleStatus.displayName eq 'Active'}">
	                    <tags:button color="red" type="button" value="De-activate"
	                                 onclick="changeStatus('${odc.roleStatus.displayName}','${odc.id}','${tab.targetNumber - 1}')"
	                                 size="small"/>
	                </c:when>
	                <c:otherwise>
	                    <tags:button color="blue" type="button" value="Activate"
	                                 onclick="changeStatus('${odc.roleStatus.displayName}','${odc.id}','${tab.targetNumber - 1}')"
	                                 size="small"/>
	                </c:otherwise>
	            </c:choose>
			</td>
		    <td width="25%" >${odc.roleStatus.displayName} since <tags:formatDate value="${odc.statusDate}"/>
		    </td>
    	</c:when>
    </c:choose>
    <td width="5%">
		     <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}" href="javascript:deleteODC('${index}');">
		        <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete" style="vertical-align:middle;text-align:left">
		    </a>
	</td>
</tr>