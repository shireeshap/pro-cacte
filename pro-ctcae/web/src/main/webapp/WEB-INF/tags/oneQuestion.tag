<%@ attribute name="crfItem" type="gov.nih.nci.ctcae.core.domain.CrfItem" required="true" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="sortable" id="sortable_${crfItem.displayOrder}">
<table class="formbuilderboxTable">
 <tr>
  <td class="TL"></td>
  <td class="T"></td>
  <td class="TR"></td>
 </tr>
 <tr>
  <td class="L"></td>
  <td class="formbuilderboxContent">
    <span id="${crfItem.displayOrder}" class="sortableSpan">${crfItem.displayOrder}</span>
    <%--${crfItem.displayOrder}--%>
    ${crfItem.proCtcTerm.questionText}
	  </td>
  <td class="R"></td>
 </tr>
 <tr>
  <td class="BL"></td>
  <td class="B"></td>
  <td class="BR"></td>
 </tr>
</table>
</div>