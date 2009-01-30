<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ attribute name="crf" type="gov.nih.nci.ctcae.core.domain.CRF" %>


<div class="instructions">
    <div class="summarylabel"><tags:message code='form.label.study'/></div>
    <div class="summaryvalue">${crf.study.displayName}</div>
</div>


<div class="instructions">

    <div class="summarylabel"><tags:message code='form.label.title'/></div>
    <div class="summaryvalue">${crf.title}</div>
</div>
<br>

<table id="formbuilderTable">
    <tr>
        <td id="left">
            <div class="instructions">
                <div class="summarylabel"><tags:message code='form.label.questions'/></div>
            </div>

            <c:forEach items="${crf.crfPagesSortedByPageNumber}" var="crfPage">

                <div class="formpages">
                    <div class="formpageheader">
                            ${crfPage.description}
                    </div>
                    <tags:recallPeriodFormatter desc="${crfPage.instructions}"/>
                    <br>

                    <c:forEach items="${crfPage.crfItemsSortedByDislayOrder}" var="crfPageItem">
                        <tags:questionReview crfPageItem="${crfPageItem}" showInstructions="true"
                                             displayOrder="${crfPageItem.displayOrder}"></tags:questionReview>
                    </c:forEach>
                    <br>

                </div>
            </c:forEach>

        </td>
    </tr>

</table>

<br>
<br>
