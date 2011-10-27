<%@ attribute name="crfPageItem" type="gov.nih.nci.ctcae.core.domain.CrfPageItem" required="true" %>
<%@ attribute name="crfPageNumber" required="true" %>

<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="index" value="${crfPageItem.displayOrder}"/>
<div id="questionPropertiesDiv_${crfPageItem.proCtcQuestion.id}">
    <div id="questionProperties_${crfPageItem.proCtcQuestion.id}" style="display:none;"
         class="questionProperties leftBox">
        <tags:instructions code="form.label.question_properties.instructions"/>

        <div align="left" style="margin-left: 50px;display:none">
            <table width="95%" class="tablecontent"
                   id="conditionsTable_${crfPageItem.proCtcQuestion.id}" style="display:none;">
                <tags:conditions crfPageItemDisplayRules="${crfPageItem.crfPageItemDisplayRules}"
                                 selectedQuestionId="${crfPageItem.proCtcQuestion.id}"></tags:conditions>
                <tr id="conditions_${crfPageItem.proCtcQuestion.id}"></tr>
            </table>
        </div>
        <tags:renderRadio
                propertyName="crf.crfPagesSortedByPageNumber[${crfPageNumber}].crfPageItems[${index}].responseRequired"
                displayName="crfItem.label.response_required"
                propertyValue="${crfPageItem.responseRequired}"
                values="${responseRequired}" noForm="true"/>

        <%--<tags:message code="instruction_conditional_question"/>--%>
        <%--<br>--%>
        <%--<br>--%>

        <%--<div>--%>
            <%--<table width="100%">--%>
                <%--<c:forEach items="${selectedCrfPageItems}" var="selectedCrfPageItem" varStatus="status">--%>
                    <%--<tr class="conditions condition_${selectedCrfPageItem.proCtcQuestion.id}">--%>
                        <%--<td colspan="5">--%>
                            <%--<b>${status.index+1}. ${selectedCrfPageItem.proCtcQuestion.proCtcTerm.term}-${selectedCrfPageItem.proCtcQuestion.proCtcQuestionType}</b>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                    <%--<tr class="conditions condition_${selectedCrfPageItem.proCtcQuestion.id}">--%>
                        <%--<c:forEach items="${selectedCrfPageItem.proCtcQuestion.validValues}" var="validValue">--%>
                            <%--<c:set var="ruleExists" value="false"/>--%>
                            <%--<c:forEach items="${crfPageItem.crfPageItemDisplayRules}" var="displayRule">--%>
                                <%--<c:if test="${not ruleExists}">--%>
                                    <%--<c:if test="${displayRule.proCtcValidValue.id eq validValue.id}">--%>
                                        <%--<c:set var="ruleExists" value="true"/>--%>
                                    <%--</c:if>--%>
                                <%--</c:if>--%>
                            <%--</c:forEach>--%>
                            <%--<td>--%>
                                <%--<c:choose>--%>
                                    <%--<c:when test="${ruleExists}">--%>
                                        <%--<input type="checkbox" value="${validValue.id}" checked class="condition_validvalue_${crfPageItem.proCtcQuestion.id}">${validValue.value}--%>
                                    <%--</c:when>--%>
                                    <%--<c:otherwise>--%>
                                        <%--<input type="checkbox" value="${validValue.id}" class="condition_validvalue_${crfPageItem.proCtcQuestion.id}">${validValue.value}--%>
                                    <%--</c:otherwise>--%>
                                <%--</c:choose>--%>
                            <%--</td>--%>
                        <%--</c:forEach>--%>
                    <%--</tr>--%>
                <%--</c:forEach>--%>
            <%--</table>--%>
        <%--</div>--%>
        <%--<br>--%>
        <%--<tags:button type="button" icon="add" value="Apply" color="blue" onclick="javascript:addConditionalQuestion('${crfPageItem.proCtcQuestion.id}')"/>--%>
    </div>
</div>