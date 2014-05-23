<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="page" required="true" %>
<%@ attribute name="resultsMap" type="java.util.TreeMap" required="true" %>
<%@attribute name="dates" type="java.util.List" required="true"%> 

<div id="participantReport_${page}" style="display : none;">
	<table class="widget" cellspacing="0">
	    <col/>
		<tr>
	         <td class="header-top">Symptom</td>
	         <td class="header-top">Attribute<img alt="Help" src="/proctcae/images/q.gif"
	                                              onclick="$('attribute-help-content').style.display='inline'"/>
	         </td>
	         <c:forEach items="${dates}" var="date">
	         	<td class="header-top">${date}</td>
	         </c:forEach>
	         
	         <c:forEach items="${resultsMap}" var="symptomMap">
		      	<c:set var="flag" value="false"></c:set>
		        <c:forEach items="${symptomMap.value}" var="careResults">
			        <tr>
			            <td class="subcategory-name">
			                <c:if test="${flag eq false}">
			                    <a href="javascript:getChart('${symptomMap.key[0]}')" class="link">${symptomMap.key[1]}</a>
			                    <c:set var="flag" value="true"></c:set>
			                </c:if>
			            </td>
			            
			            <td class="actual-question">
			                    ${fn:toUpperCase(careResults.key.proCtcQuestionType.displayName)}
			            </td>
			            
			            <!-- begin="4" end="16" step="1" -->
			            <c:forEach items="${careResults.value}" var="value">
			                <td class="data displayOrder${value.displayOrder}">
			                        ${value.value}
			                </td>
			            </c:forEach>
			        </tr>
	      		</c:forEach>
		</c:forEach>
	</table>
</div>

