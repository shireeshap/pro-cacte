<%@ attribute name="proCtcTermId" required="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="proCtcQuestionId" %>
<%@attribute name="add" %>
<%@attribute name="delete" %>
<%@attribute name="properties" %>
<c:if test="${(properties) || (add) || (delete)}">
    <%--<div class="formbuilderBoxControls">--%>
        <%--<div class="formbuilderBoxControls-left">--%>
            <%--<c:if test="${delete}">--%>
                <%--<a href="javascript:deleteQuestion('${proCtcQuestionId}','${proCtcTermId}');">--%>
                    <%--<img src="<tags:imageUrl name="checkno.gif"/>" alt="Delete"/>--%>
                <%--</a>--%>


            <%--</c:if>--%>
        <%--</div>--%>
        <%--<div class="formbuilderBoxControls-right"></div>--%>
    <%--</div>--%>
</c:if>
