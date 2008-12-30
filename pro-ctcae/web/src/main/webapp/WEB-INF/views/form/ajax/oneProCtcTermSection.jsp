<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${crfPageItems}" var="selectedCrfPageItem" varStatus="status">
	<tags:oneCrfPageItem crfPageItem="${selectedCrfPageItem}" index="${status.index}" crfPageIndex="${crfPageIndex}"></tags:oneCrfPageItem>
	<script type="text/javascript">
		updateSelectedCrfItems('${selectedCrfPageItem.proCtcQuestion.id}')
	</script>
</c:forEach>
