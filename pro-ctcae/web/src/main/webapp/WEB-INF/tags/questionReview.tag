<%@ attribute name="displayOrder" %>
<%@ attribute name="showInstructions" type="java.lang.Boolean" %>
<%@ attribute name="crfItem" type="gov.nih.nci.ctcae.core.domain.CrfItem" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
	.review {
		font-weight: bold;
		font-size: 15px;
	}
</style>
<tags:formbuilderBox cssClass="review">
	<tags:formbuilderBoxControls add="false"
								 proCtcQuestionId="${crfItem.proCtcQuestion.id}"/>

	<c:if test="${showInstructions}">
		<c:if test="${not empty crfItem.instructions}">
			<chrome:summary label="Instructions">
          <jsp:attribute name="content">
           ${crfItem.instructions}
          </jsp:attribute>

			</chrome:summary>
		</c:if>
	</c:if>
	<chrome:summary>
          <jsp:attribute name="content">

              <span id="${displayOrder}"
					class="sortableSpan">${displayOrder}</span>
              <c:if test="${crfItem.responseRequired == true}">
				  <span class="required-indicator">*</span>
			  </c:if>
			  ${crfItem.proCtcQuestion.formattedQuestionText}
			  <c:choose>
				  <c:when test="${crfItem.crfItemAllignment eq 'Horizontal'}">
					  <c:set var="horizontalCrfItemsStyle" value=""/>
					  <c:set var="verticalCrfItemsStyle" value="display:none;"/>

				  </c:when>
				  <c:otherwise>
					  <c:set var="horizontalCrfItemsStyle" value="display:none;"/>
					  <c:set var="verticalCrfItemsStyle" value=""/>
				  </c:otherwise>
			  </c:choose>

              <div id="horizontalCrfItems_${crfItem.proCtcQuestion.id}" style="${horizontalCrfItemsStyle}">
				  <br>
				  <br>
				  <c:forEach items="${crfItem.proCtcQuestion.validValues}" var="validValue"
							 varStatus="status">
					  <input type="radio" disabled="true"> ${validValue.displayName}
				  </c:forEach>
			  </div>
              <div id="verticalCrfItems_${crfItem.proCtcQuestion.id}" style="${verticalCrfItemsStyle}">
				  <ul>
					  <c:forEach items="${crfItem.proCtcQuestion.validValues}" var="proCtcValidValue">
						  <li>${proCtcValidValue.displayName}</li>
					  </c:forEach>
				  </ul>
			  </div>

          </jsp:attribute>
	</chrome:summary>
</tags:formbuilderBox>


