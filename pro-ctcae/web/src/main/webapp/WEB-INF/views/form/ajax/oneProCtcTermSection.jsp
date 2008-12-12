<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${proCtcTerm.proCtcQuestions}"
		   var="proCtcQuestion">
	<tags:oneQuestion proCtcQuestion="${proCtcQuestion}" displayOrder="${displayOrder}"></tags:oneQuestion>

</c:forEach>
