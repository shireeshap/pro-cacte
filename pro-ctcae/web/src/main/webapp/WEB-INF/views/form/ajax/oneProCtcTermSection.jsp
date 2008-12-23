<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${crfItems}"
		   var="selectedCrfItem" varStatus="status">
	<tags:oneCrfItem crfItem="${selectedCrfItem}" index="${status.index}"></tags:oneCrfItem>
	<script type="text/javascript">
		updateSelectedCrfItems('${selectedCrfItem.proCtcQuestion.id}')
	</script>
</c:forEach>
