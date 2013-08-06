<%@ attribute name="displayOrder" %>
<%@ attribute name="showInstructions" type="java.lang.Boolean" %>
<%@ attribute name="crfPageItem" type="gov.nih.nci.ctcae.core.domain.CrfPageItem" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
    .review {
        font-weight: bold;
        font-size: 15px;
    }
</style>

<div class="sortable" id="sortable_${crfPageItem.proCtcQuestion.id}">

    <tags:formbuilderBox
            cssClass="review">
        <tags:formbuilderBoxControls add="false"
                                     proCtcQuestionId="${crfPageItem.proCtcQuestion.id}"
                                     proCtcTermId="${crfPageItem.proCtcQuestion.proCtcTerm.id}"/>

        <c:if test="${showInstructions}">
            <c:if test="${not empty crfPageItem.instructions}">
                <chrome:summary label="Instructions">
          <jsp:attribute name="content">
           ${crfPageItem.instructions} 
          </jsp:attribute>

                </chrome:summary>
            </c:if>
        </c:if>
        <chrome:summary>
          <jsp:attribute name="content">

              <div id="${displayOrder}"
                   class="sortableSpan">${displayOrder}</div>
              <c:if test="${crfPageItem.responseRequired == true}">
                  <span class="required-indicator">*</span>
              </c:if>
              ${crfPageItem.proCtcQuestion.questionText}
              <img alt="This is a conditional question which will only be asked if there is a positive response to the above question" src="<tags:imageUrl name="blue/conditional-icon.png"/>"
                   id="conditionsImage_${crfPageItem.proCtcQuestion.id}" style="display:none;"/>

               <img alt="Conditional Triggering Question"
                    src="<tags:imageUrl name="blue/conditional-triggering-icon.png"/>"
                    id="conditionalTriggeringImage_${crfPageItem.proCtcQuestion.id}" style="display:none;"/>

              <c:choose>
                  <c:when test="${crfPageItem.crfItemAllignment.displayName eq 'Horizontal' and !crfPageItem.crfPage.crf.eq5d}">
                      <c:set var="horizontalCrfItemsStyle" value=""/>
                      <c:set var="verticalCrfItemsStyle" value="display:none;"/>
                  </c:when>
                  <c:otherwise>
                      <c:set var="horizontalCrfItemsStyle" value="display:none;"/>
                      <c:set var="verticalCrfItemsStyle" value=""/>
                  </c:otherwise>
              </c:choose>

              <div id="horizontalCrfItems_${crfPageItem.proCtcQuestion.id}" style="${horizontalCrfItemsStyle}"><br/>
                  <c:forEach items="${crfPageItem.proCtcQuestion.validValues}" var="validValue"
                             varStatus="status">
                      <input type="radio" disabled="true"> ${validValue.value}
                  </c:forEach>
              </div>
              <div id="verticalCrfItems_${crfPageItem.proCtcQuestion.id}" style="${verticalCrfItemsStyle}"><br/>
                  <ul>
                      <c:forEach items="${crfPageItem.proCtcQuestion.validValues}" var="proCtcValidValue">
                          <li>${proCtcValidValue.value}</li>
                      </c:forEach>
                  </ul>
              </div>

			  <c:if test="${not empty crfPageItem.crfPageItemDisplayRules }">
                  <div id="conditions_${crfPageItem.proCtcQuestion.id}">
                      <tags:instructions code="instruction_conditional_question"/>

                      <div align="left" style="margin-left: 50px">
                          <table width="95%" class="tablecontent"
                                 id="conditionsTable_${crfPageItem.proCtcQuestion.id}">
                              <tr id="ss-table-head" class="amendment-table-head">
                                  <th width="95%" class="tableHeader"><tags:message
                                          code='crfItem.label.conditions'/></th>
                                  <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

                              </tr>

                              <tags:conditions crfPageItemDisplayRules="${crfPageItem.crfPageItemDisplayRules}"
                                               selectedQuestionId="${crfPageItem.proCtcQuestion.id}"
                                               showDelete="false"></tags:conditions>
                              <tr id="conditions_${crfPageItem.proCtcQuestion.id}"></tr>
                          </table>
                      </div>
                  </div>
              </c:if>
          </jsp:attribute>
        </chrome:summary>
    </tags:formbuilderBox></div>


