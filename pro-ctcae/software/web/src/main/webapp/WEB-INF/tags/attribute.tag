<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="name" required="true" %>
<%@attribute name="value" required="true" %>
<c:if test="${not empty value}">${name}="${value}"</c:if>