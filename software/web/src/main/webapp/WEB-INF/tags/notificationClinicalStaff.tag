<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="clinicalStaff" required="false" %>
<%@attribute name="index" required="true" %>
<%@attribute name="rnIndex" required="true" %>
<%@attribute name="tpIndex" required="true" %>
<%@attribute name="studySiteId" required="false" %>
<%@attribute name="notify" required="false" %>
<%@attribute name="notifyOptions" required="true" type="java.util.List" %>
<%@attribute name="role" required="false" %>
<%@attribute name="addAction" required="true" %>

<p id="splitter"/>
<c:choose>
	<c:when test="${addAction == 'addRn'}">
		<tr id="rnRow-${index}-${rnIndex}"  align="left">
             <td valign="middle">
                <c:set var="nurse" value="participant.studyParticipantAssignments[${index}].researchNurses[${rnIndex}].studyOrganizationClinicalStaff"/>
                <input type="text"  name="${nurse}" id="${nurse}" style="display:none;" class="validate-NOTEMPTY" title="Research Nurse"/>
                <tags:yuiAutocompleter inputName="${nurse}Input" required="false"
                       value="${command.participant.studyParticipantAssignments[index].researchNurses[rnIndex].studyOrganizationClinicalStaff.displayName}"
                       hiddenInputName="participant.studyParticipantAssignments[${index}].researchNurses[${rnIndex}].studyOrganizationClinicalStaff"/>
            </td>   
           <td>
				<tags:renderSelect propertyName="participant.studyParticipantAssignments[${index}].researchNurses[${rnIndex}].notify" displayName="participant.label.notification"
                                 required="false" options="${notifyOptions}" doNotshowLabel="true" noForm="true" propertyValue="${notify}"/>
		   </td>
		   <td align="center">              
                 <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}" href="javascript:deleteResearchNurse('${index}','${rnIndex}');">
                     <img src="../../images/checkno.gif" border="0" alt="delete" style="vertical-align:middle">
                 </a>
           </td>
        </tr>
	</c:when>
	<c:otherwise>
		<tr id="tpRow-${index}-${tpIndex}"  align="left">
             <td valign="middle">
               <c:set var="treatingPhysican" value="participant.studyParticipantAssignments[${index}].treatingPhysicians[${tpIndex}].studyOrganizationClinicalStaff"/>
               <input type="text" name="${treatingPhysican}" id="${treatingPhysican}" style="display:none;" class="validate-NOTEMPTY" title="Treating Physician"/>
               <tags:yuiAutocompleter inputName="${treatingPhysican}Input"
                                           value="${command.participant.studyParticipantAssignments[index].treatingPhysicians[tpIndex].studyOrganizationClinicalStaff.displayName}" required="false"
                                           hiddenInputName="participant.studyParticipantAssignments[${index}].treatingPhysicians[${tpIndex}].studyOrganizationClinicalStaff"/>
             </td>
             <td>
             <tags:renderSelect propertyName="participant.studyParticipantAssignments[${index}].treatingPhysicians[${tpIndex}].notify" displayName="participant.label.notification"
                     			   required="false" options="${notifyOptions}" doNotshowLabel="true" noForm="true" propertyValue="${notify}"/>
             
            </td>
            <td align="center">
                 <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}" href="javascript:deleteTreatingPhysician('${index}','${tpIndex}');">
                     <img src="../../images/checkno.gif" border="0" alt="delete" style="vertical-align:middle">
                 </a>
             </td>		                
         </tr>
	</c:otherwise>
</c:choose>
