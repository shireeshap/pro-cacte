<%@ attribute name="crfPageItem" type="gov.nih.nci.ctcae.core.domain.CrfPageItem" required="true" %>
<%@ attribute name="crfPageNumber" required="true" %>

<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="index" value="${crfPageItem.displayOrder}"/>
<div id="questionPropertiesDiv_${crfPageItem.proCtcQuestion.id}">
    <div id="questionProperties_${crfPageItem.proCtcQuestion.id}" style="display:none;"
         class="questionProperties leftBox">
        <span class="propertiesHeader"><tags:message code="crfItem.label.properties"/> </span>

        <tags:renderTextArea
                propertyName="crf.crfPagesSortedByPageNumber[${crfPageNumber}].crfPageItems[${index}].instructions"
                displayName="crfItem.label.instructions"
                noForm="true"
                propertyValue="${crfPageItem.instructions}"/>

        <tags:renderRadio
                propertyName="crf.crfPagesSortedByPageNumber[${crfPageNumber}].crfPageItems[${index}].responseRequired"
                displayName="crfItem.label.response_required"
                propertyValue="${crfPageItem.responseRequired}"
                values="${responseRequired}" noForm="true"/>

        <tags:renderRadio
                propertyName="crf.crfPagesSortedByPageNumber[${crfPageNumber}].crfPageItems[${index}].crfItemAllignment"
                displayName="crfItem.label.allignment" propertyValue="${crfPageItem.crfItemAllignment}"
                values="${crfItemAllignments}"
                noForm="true">

        </tags:renderRadio>


        <span class="propertiesHeader"><tags:message code="form.conditional_question"/> </span>
        <tags:instructions code="instruction_conditional_question"/>

        <div align="left" style="margin-left: 50px">
            <table width="95%" class="tablecontent"
                   id="conditionsTable_${crfPageItem.proCtcQuestion.id}" style="display:none;">
                <tr id="ss-table-head" class="amendment-table-head">
                    <th width="95%" class="tableHeader"><tags:message
                            code='crfItem.label.conditions'/></th>
                    <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

                </tr>

                <tags:conditions crfPageItemDisplayRules="${crfPageItem.crfPageItemDisplayRules}"
                                 selectedQuestionId="${crfPageItem.proCtcQuestion.id}"
                                 showDelete="true"></tags:conditions>


                <tr id="conditions_${crfPageItem.proCtcQuestion.id}"></tr>

            </table>

        </div>
        <br>
        <br>

        <div>
            <select name="switchTriggerSelect" id="selectedCrfPageItems_${crfPageItem.proCtcQuestion.id}"
                    multiple=""
                    size="20" class="selectedCrfPageItems">
                <option value=""></option>
                <c:forEach items="${selectedCrfPageItems}" var="selectedCrfPageItem" varStatus="status">

                    <optgroup label="${status.index+1} ${selectedCrfPageItem.proCtcQuestion.questionText}"
                              id="condition_${selectedCrfPageItem.proCtcQuestion.id}" class="conditions">
                        <c:forEach items="${selectedCrfPageItem.proCtcQuestion.validValues}" var="validValue">
                            <option value="${validValue.id}">${validValue.value}</option>
                        </c:forEach>
                    </optgroup>

                </c:forEach>
            </select>
        </div>
        <br>
        <input type="button" value="Add Conditions" onClick="javascript:addConditionalQuestion('${crfPageItem.proCtcQuestion.id}',
			$F('selectedCrfPageItems_${crfPageItem.proCtcQuestion.id}'))" class="button"/>


        <%--<div id="previewQuestion" class="review">--%>
        <%--<tags:questionReview crfItem="${crfItem}" showInstructions="false" displayOrder="${crfItem.displayOrder}"/>--%>
        <%--<br>--%>
        <%--<br>--%>
        <%--</div>--%>
    </div>
</div>