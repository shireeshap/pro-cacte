<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<c:forEach items="${crfPages}" var="crfPage">
    <tags:oneCrfPage crfPage="${crfPage}" crfPageNumber="${crfPage.pageNumber}"
                     advance="${advance}" isEq5d="${crfPage.crf.eq5d}">
    </tags:oneCrfPage>
</c:forEach>


 <script type="text/javascript">
    updateQuestionsId();
 </script>
 <div id="hiddenCrfPageDiv"></div>