<%@ attribute name="crfItem" type="gov.nih.nci.ctcae.core.domain.CrfItem" required="true" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<tags:formbuilderBox>
    ${crfItem.displayOrder} : ${crfItem.proCtcQuestion.questionText}
    <ul>
        <c:forEach items="${crfItem.proCtcQuestion.validValues}" var="proCtcValidValue">
            <li>${proCtcValidValue.value}</li>
        </c:forEach>
    </ul>
</tags:formbuilderBox>