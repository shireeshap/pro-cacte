<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${crfPages}" var="crfPage">
    <tags:oneCrfPage crfPage="${crfPage}" crfPageNumber="${crfPage.pageNumber}"
                     advance="${advance}">
    </tags:oneCrfPage>
</c:forEach>


<c:forEach items="${crfPageItems}" var="selectedCrfPageItem">
    <div id="crfPageNumberToUpdate_${selectedCrfPageItem.crfPage.pageNumber}" style="display:none;"
         class="crfPageItemsToAdd">
        <tags:oneCrfPageItem crfPageItem="${selectedCrfPageItem}" index="${selectedCrfPageItem.displayOrder}"
                             crfPageNumber="${selectedCrfPageItem.crfPage.pageNumber}"
                             advance="${advance}"></tags:oneCrfPageItem>
    </div>

</c:forEach>
