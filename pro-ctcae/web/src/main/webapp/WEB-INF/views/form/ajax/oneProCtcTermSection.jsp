<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${crfPageItems}" var="selectedCrfPageItem" varStatus="status">
	<tags:oneCrfPageItem crfPageItem="${selectedCrfPageItem}" index="${status.index}"
						 crfPageNumber="${crfPageNumber}"></tags:oneCrfPageItem>

</c:forEach>
