<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="selected" required="true" %>
<script type="text/javascript">
    <c:choose>
    <c:when test="${selected eq 'overallStudy'}">
    var displaySymptom = false;
    var studySiteMandatory = true;
    </c:when>
    <c:when test="${selected eq 'symptomOverTime'}">
    var allSymptoms = false;
    </c:when>
    <c:when test="${selected eq 'participantAddedQuestions'}">
    var displaySymptom = false;
    </c:when>
    </c:choose>
</script>

