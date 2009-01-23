<%@ attribute name="showDelete" type="java.lang.Boolean" %>
<%@ attribute name="crfPageItemDisplayRules" type="java.util.Collection" required="true" %>
<%@ attribute name="selectedQuestionId" type="java.lang.Integer" required="true" %>

<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<c:forEach items="${crfPageItemDisplayRules}" var="crfPageItemDisplayRule">
    <c:set var="inputName"
           value="conditionalQuestion_${selectedQuestionId}_${crfPageItemDisplayRule.proCtcValidValue.id}"/>

    <tr id="${inputName}-row"
        class="conditionalQuestion_${selectedQuestionId}_condition conditionalTriggering_${crfPageItemDisplayRule.proCtcValidValue.proCtcQuestion.id}">

        <td style="border-right:none;">

                ${crfPageItemDisplayRule.proCtcValidValue.proCtcQuestion.questionText}-${crfPageItemDisplayRule.proCtcValidValue.value}
        </td>

        <td style="border-left:none;">
            <c:if test="${showDelete}">
                <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
                   href="javascript:deleteConditions('${selectedQuestionId}','${crfPageItemDisplayRule.proCtcValidValue.id}');">
                    <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                         style="vertical-align:middle">
                </a>
            </c:if>
        </td>


    </tr>

</c:forEach>