<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<option value=""></option>


<c:forEach items="${selectedCrfPageItems}" var="selectedCrfPageItem" varStatus="status">

	<optgroup label="${status.index+1} ${selectedCrfPageItem.proCtcQuestion.shortText}"
			  id="condition_${selectedCrfPageItem.proCtcQuestion.id}" class="conditions">
		<c:forEach items="${selectedCrfPageItem.proCtcQuestion.validValues}" var="validValue">
			<option value="${validValue.id}">${validValue.displayName}</option>
		</c:forEach>
	</optgroup>

</c:forEach>