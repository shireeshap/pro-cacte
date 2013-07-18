<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@attribute name="command" type="gov.nih.nci.ctcae.web.form.CreateFormCommand"required="true" %>


 <c:forEach items="${command.crf.crfPagesSortedByPageNumber}"
            var="selectedCrfPage"
            varStatus="status">
     <tags:oneCrfPage crfPage="${selectedCrfPage}"
                      crfPageNumber="${status.index}"
                      advance="${command.crf.advance}"
                      isEq5d="${command.crf.eq5d}">
     </tags:oneCrfPage>
 </c:forEach>
 
 <script type="text/javascript">
 	updateQuestionsId();
 </script>
 
 <div id="hiddenCrfPageDiv"></div>

 