<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="value" type="java.util.Date" required="true" %>
<%-- TODO: get this pattern from the global configuration --%><fmt:formatDate value="${value}" pattern="MM/dd/yyyy"/>