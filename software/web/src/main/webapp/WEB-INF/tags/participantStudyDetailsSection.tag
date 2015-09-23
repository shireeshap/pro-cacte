<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="command" type="gov.nih.nci.ctcae.web.participant.ParticipantCommand" required="true" %>
<%@ attribute name="isCreateFlow" required="false"%>
    
<c:if test="${not empty command.participant.id}">
     <table cellpadding="0" width="100%" border="0">
         <tr>
             <td class="tableHeader" width="5%">
                 Select
             </td>
             <td class="tableHeader">
                 Study
             </td>
             <c:if test="${isEdit}">
                 <td width="20%" class="tableHeader">
                     Treatment End/On-hold Date
                 </td>
             </c:if>
         </tr>
         
          <c:forEach items="${command.participant.studyParticipantAssignments}"
                     var="studyParticipantAssignment" varStatus="spastatus">
              <c:set var="studysite" value="${studyParticipantAssignment.studySite}"/>

              <tags:studySite studysite="${studysite}" selected="true" isEdit="true"
                              studyParticipantAssignment="${studyParticipantAssignment}"
                              participant="${command.participant}" isCreateFlow="${isCreateFlow}"/>
          </c:forEach>
     </table>
</c:if>