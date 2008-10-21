<%@ attribute name="crfItem" type="gov.nih.nci.ctcae.core.domain.CrfItem" required="true" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="box sortable" id="sortable_${crfItem.displayOrder}">

    <span id="${crfItem.displayOrder}" class="sortableSpan">${crfItem.displayOrder}</span>
    <%--${crfItem.displayOrder}--%>
    ${crfItem.proCtcTerm.questionText}</div>

