<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:includePrototypeWindow/>

<c:set var="indices" value="${indices}" />
	
<c:forEach items="${crfCycleDefinitionList}" var="crfCycleDefinition"
                  	 varStatus="status">
    <c:set var ="i" value="${status.index}" />
    
	<c:if test="${not empty crfCycleDefinition}">
		    <div id="hidden_inputs_div_${indices[i]}">
		        <c:forEach items="${crfCycleDefinition.crfCycles}" var="crfCycle" varStatus="cyclestatus">
		            <input type="hidden" name='selecteddays_${indices[i]}_${cyclestatus.index}'
		                   id='selecteddays_${indices[i]}_${cyclestatus.index}'
		                   value="${crfCycle.cycleDays}"/>
								                   
		        </c:forEach>
		    </div>
		    <script type="text/javascript" name="testName">
		        showCyclesForDefinition(${indices[i]}, true);
		    </script>
	</c:if>
	
</c:forEach>


