<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${crfItems}"
		   var="crfItem" varStatus="status">
	<tags:oneCrfItem crfItem="${crfItem}" index="${status.index}"></tags:oneCrfItem>

</c:forEach>
