<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="selected" required="true" %>
<script type="text/javascript">
    <c:choose>
    <c:when test="${selected eq 'symptomOverTime'}">
    var displaySymptom = true;
    var displayFilterBy = true;
    </c:when>
    <c:when test="${selected eq 'symptomSummary'}">
    var displayFilterBy = true;
    </c:when>
    </c:choose>
</script>

<c:if test="${selected eq 'symptomSummary'}">
    <input type="hidden" name="symptom" id="symptom"/>
</c:if>
