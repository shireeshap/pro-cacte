<%@ attribute name="desc" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
    <c:when test="${fn:contains(desc,'past' )}">
        ${fn:substring(desc,0,fn:indexOf(desc,'over')+4 )}
        <b><u>${fn:substring(desc,fn:indexOf(desc,'over')+4,fn:length(desc) )}</u></b>
    </c:when>
    <c:when test="${fn:contains(desc,'last cancer treatment' )}">
        ${fn:substring(desc,0,fn:indexOf(desc,'since')+5 )}
        <b><u>${fn:substring(desc,fn:indexOf(desc,'since')+5,fn:length(desc) )}</u></b>
    </c:when>

</c:choose>

<%--<b>${desc}</b>--%>