<%@ attribute name="proCtcQuestion" type="gov.nih.nci.ctcae.core.domain.ProCtcQuestion" required="true" %>
<%@ attribute name="displayOrder" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<div class="sortable makeDraggable" id="sortable_${proCtcQuestion.id}">
    <table class="formbuilderboxTable">
        <tr>
            <td class="TL"></td>
            <td class="T"></td>
            <td class="TR"></td>
        </tr>
        <tr>
            <td class="L"></td>
            <td class="formbuilderboxContent">
                <span id="${displayOrder}" class="sortableSpan">${displayOrder}</span>
                ${proCtcQuestion.questionText}
                <a id="del-${proCtcQuestion.id}" class="del"
                   href="javascript:deleteQuestion('${proCtcQuestion.id}');">

                    <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                         style="vertical-align:middle">
                </a>

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

