<%@ attribute name="index" required="true" %>
<%@ attribute name="advance" type="java.lang.Boolean" required="true" %>

<%@ attribute name="crfPageItem" type="gov.nih.nci.ctcae.core.domain.CrfPageItem" required="true" %>
<%@ attribute name="crfPageNumber" required="true" %>

<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="noform" tagdir="/WEB-INF/tags/noform" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript">
    updateSelectedCrfItems('${crfPageItem.proCtcQuestion.id}')
</script>
<div class="sortable makeDraggable" id="sortable_${crfPageItem.proCtcQuestion.id}"
     onclick="javascript:showCrfItemPropertiesTab(${crfPageItem.proCtcQuestion.id})">

    <tags:formbuilderBoxControls delete="true" properties="${advance}"
                                 proCtcQuestionId="${crfPageItem.proCtcQuestion.id}"
                                 proCtcTermId="${crfPageItem.proCtcQuestion.proCtcTerm.id}"/>
    <table class="formbuilderboxTable">
        <tr>
            <td class="TL"></td>
            <td class="T"></td>
            <td class="TR"></td>
        </tr>
        <tr>
            <td class="L"></td>
            <td class="formbuilderboxContent">
                <%--<img class="arrow" alt="" src="<tags:imageUrl name="arrow.png"/>"
                             id="arrow_${crfPageItem.proCtcQuestion.id}" style="display:none;" />--%>

                <div id="selectedProCtcTerm_${crfPageItem.proCtcQuestion.proCtcTerm.id}"
                     class="selectedCrfPageForProCtcTerm_${crfPageNumber} selectedProCtcTerm_${crfPageItem.proCtcQuestion.proCtcTerm.id}"
                     style="display:none;"></div>

                <div id="sortableSpan_${crfPageItem.proCtcQuestion.id}"
                     class="sortableSpan">${crfPageItem.displayOrder}</div>
                ${crfPageItem.proCtcQuestion.shortText}

                <img alt="Conditional Question" src="<tags:imageUrl name="blue/conditional-icon.png"/>"
                     id="conditionsImage_${crfPageItem.proCtcQuestion.id}" style="display:none;"/>

                <img alt="Conditional Triggering Question"
                     src="<tags:imageUrl name="blue/conditional-triggering-icon.png"/>"
                     id="conditionalTriggeringImage_${crfPageItem.proCtcQuestion.id}" style="display:none;"/>

            </td>
            <td class="R"></td>
        </tr>
        <tr>
            <td class="BL"></td>
            <td class="B"></td>
            <td class="BR"></td>
        </tr>
    </table>

    <tags:oneCrfPageItemProperties crfPageItem="${crfPageItem}" crfPageNumber="${crfPageNumber}"
                                   index="${index}"></tags:oneCrfPageItemProperties>
</div>
