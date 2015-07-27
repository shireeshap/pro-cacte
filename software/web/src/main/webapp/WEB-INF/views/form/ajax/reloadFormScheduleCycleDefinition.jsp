<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:includePrototypeWindow/>

<c:set var="indices" value="${indices}" />

<c:forEach items="${crfCycleDefinitionList}" var="crfCycleDefinition"
    	    		varStatus="status">
     <c:set var ="i" value="${status.index}" />
	 <tags:reloadFormScheduleCycleDefinition cycleDefinitionIndex="${indices[i]}"
                                 	  crfCycleDefinition="${crfCycleDefinition}"
                                  	 repeatOptions="${cycleplannedrepetitions}"/>
</c:forEach>

