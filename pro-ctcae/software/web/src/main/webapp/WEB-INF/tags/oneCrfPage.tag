<%@ attribute name="advance" type="java.lang.Boolean" required="true" %>
<%@ attribute name="crfPage" type="gov.nih.nci.ctcae.core.domain.CRFPage" required="true" %>


<%@ attribute name="crfPageNumber" required="true" %>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="formpages" id="form-pages_${crfPageNumber}" onclick="javascript:selectPage('${crfPageNumber}')">

    <div class="formpageheader">
        <a href="javascript:unselectPage('${crfPageNumber}')"
           id="form-pages-image_${crfPageNumber}" style="display:none;">
            <img src="<tags:imageUrl name="arrow.png"/>" style="position:absolute; left:-22px;"/>
        </a>

        <c:set var="crfPageTitle" value="${empty crfPage.description ?'':crfPage.description}"/>

        <span id="crf.crfPagesSortedByPageNumber[${crfPageNumber}].description-property"
              class="crfPageTitle">${crfPageTitle}</span>

        <div class="formbuilderBoxControls">
            <div class="formbuilderBoxControls-left">
                    <a style="cursor:pointer;"
                       onClick="SwitchCollapsableState('pages_${crfPageNumber}', '${crfPageNumber}')">
                        <img id="image-${crfPageNumber}" src="<tags:imageUrl name="arrow-down.png" />"
                             border="0"
                             height="16"/></a>
                <a href="javascript:moveCrfPageUp('${crfPageNumber}');" id="crfPageUpLink_${crfPageNumber}">
                    <img src="<tags:imageUrl name="blue/up.png"/>" alt="Up"/>
                </a>

                <a href="javascript:moveCrfPageDown('${crfPageNumber}');"
                   id="crfPagDownLink_${crfPageNumber}">
                    <img src="<tags:imageUrl name="blue/down.png"/>" alt="Down"/>
                </a>
                <a href="javascript:deleteCrfPage('${crfPageNumber}','${crfPage.proCtcTerm.id}');">
                    <img src="<tags:imageUrl name="checkno.gif"/>" alt="Delete"/>
                </a>
            </div>
            <div class="formbuilderBoxControls-right"></div>
        </div>
    </div>
    <div id="pages_${crfPageNumber}">
        <div id="sortablePage_${crfPageNumber}">
            <div class="sortable makeDraggable" id="dummySortable_${crfPageNumber}"></div>
            <c:forEach items="${crfPage.crfPageItems}" var="selectedCrfPageItem">
                <tags:oneCrfPageItem crfPageItem="${selectedCrfPageItem}" crfPageNumber="${crfPageNumber}"
                                     advance="${advance}"/>

            </c:forEach>
        </div>
    </div>
</div>
    <script type="text/javascript">
        SwitchCollapsableState('pages_${crfPageNumber}', '${crfPageNumber}');
    </script>

