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
<tags:formbuilderBox cssClass="review">
	<tags:formbuilderBoxControls add="false"
								 proCtcQuestionId="${crfPageItem.proCtcQuestion.id}"/>

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

              <span id="${displayOrder}"
					class="sortableSpan">${displayOrder}</span>
              <c:if test="${crfPageItem.responseRequired == true}">
				  <span class="required-indicator">*</span>
			  </c:if>
			  ${crfPageItem.proCtcQuestion.formattedQuestionText}
			  <c:choose>
				  <c:when test="${crfPageItem.crfItemAllignment eq 'Horizontal'}">
					  <c:set var="horizontalCrfItemsStyle" value=""/>
					  <c:set var="verticalCrfItemsStyle" value="display:none;"/>

				  </c:when>
				  <c:otherwise>
					  <c:set var="horizontalCrfItemsStyle" value="display:none;"/>
					  <c:set var="verticalCrfItemsStyle" value=""/>
				  </c:otherwise>
			  </c:choose>

              <div id="horizontalCrfItems_${crfPageItem.proCtcQuestion.id}" style="${horizontalCrfItemsStyle}">
				  <br>
				  <br>
				  <c:forEach items="${crfPageItem.proCtcQuestion.validValues}" var="validValue"
							 varStatus="status">
					  <input type="radio" disabled="true"> ${validValue.displayName}
				  </c:forEach>
			  </div>
              <div id="verticalCrfItems_${crfPageItem.proCtcQuestion.id}" style="${verticalCrfItemsStyle}">
				  <ul>
					  <c:forEach items="${crfPageItem.proCtcQuestion.validValues}" var="proCtcValidValue">
						  <li>${proCtcValidValue.displayName}</li>
					  </c:forEach>
				  </ul>
			  </div>

          </jsp:attribute>
	</chrome:summary>
</tags:formbuilderBox>


