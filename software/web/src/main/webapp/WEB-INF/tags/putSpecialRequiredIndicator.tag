<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<c:set var="propertyName" value="${propertyName}"></c:set>
   <span id="${propertyName}-indicator">
       <c:if test="${propertyName=='aeReport.adverseEvents[0].startDate'}">
           <tags:requiredIndicator/>
       </c:if>
   </span>