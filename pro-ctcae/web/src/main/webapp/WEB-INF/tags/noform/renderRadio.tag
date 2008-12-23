<%@ attribute name="questionId" %>
<%@ attribute name="items" type="java.util.List" %>
<%@ attribute name="propertyValue" %>
<%@ attribute name="size" %>

<%@ attribute name="propertyName" required="true" %>
<%@ attribute name="displayName" required="true" %>
<%@ attribute name="required" %>
<%@ attribute name="help" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="title"><spring:message code='${displayName}' text='${displayName}'/></c:set>

<div id="${propertyName}-row" class="row">
	<div class="label">
		<label for="${propertyName}">${title}
		</label>
	</div>
	<div class="value">
		<c:forEach items="${items}" var="item" varStatus="status">
			<c:choose>
				<c:when test="${item.code eq propertyValue}">
					<input type="radio" class="longselect-radio" name="${propertyName}"
						   id="${propertyName}-radio-${status.index}"
						   value="${item.code}"
						   checked="checked" style="margin:3px"
						/>${item.desc}
				</c:when><c:otherwise>


				<input type="radio" class="longselect-radio" name="${propertyName}"
					   id="${propertyName}-radio-${status.index}"
					   value="${item.code}"
					   style="margin:3px"/>${item.desc}
			</c:otherwise>
			</c:choose>
		</c:forEach>


	</div>
</div>



