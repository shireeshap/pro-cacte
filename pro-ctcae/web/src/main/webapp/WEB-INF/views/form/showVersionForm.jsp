<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<html>
<head><title></title></head>
<body>


<c:forEach items="${crfs}" var="crf">

    <tr class="even childTableRow_${parentCrfId}">
        <td></td>

        <td></td>
        <td>${crfVersion}</td>

        <td><tags:formatDate value="${crf.effectiveStartDate}"/></td>
        <td><tags:formatDate value="${crf.effectiveEndDate}"/></td>
        <td>${crf.status}</td>
        <td>
            <c:choose>
                <c:when test="${crf.status eq 'Released'}">
                    <c:if test="${crf.nextVersionId eq null}">
                        <a href="../../pages/participant/schedulecrf?crfId=${crf.id}">Schedule |</a>
                    </c:if>
                    <a href="../../pages/form/copyForm?crfId=${crf.id}">Copy </a>

                    <c:if test="${crf.nextVersionId eq null}">
                        <a href="javascript:versionForm('${crf.id}')">Version </a>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <a href="javascript:releaseForm('${crf.id}')">Release |</a>
                    <a href="/pages/form/copyForm?crfId=${crf.id}">Copy |</a>

                    <a href="javascript:deleteForm('${crf.id}')">Delete |</a>
                </c:otherwise>
            </c:choose>

            <c:if test="${crf.status eq 'Draft'}">
                <a href="scheduleCrf?crfId=${crf.id}">Edit</a>
            </c:if>


        </td>

        <!--<td><a href="/proctcae/pages/participant/schedulecrf?crfId=15">Schedule</a> | <a-->
        <!--href="/proctcae/pages/form/copyForm?crfId=15">Copy</a></td>-->
    </tr>
</c:forEach>
</body>
</html>