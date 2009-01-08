<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<option value="">Please select..</option>
<c:forEach items="${selectedCrfPageItems}" var="selectedCrfPageItem">

	<optgroup label="${selectedCrfPageItem.proCtcQuestion.shortText}"
			  id="condition_${selectedCrfPageItem.proCtcQuestion.id}" class="conditions">
		<c:forEach items="${selectedCrfPageItem.proCtcQuestion.validValues}" var="validValue">
			<option value="${validValue.id}">${validValue.displayName}</option>
		</c:forEach>
	</optgroup>

</c:forEach>