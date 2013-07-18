<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<body>
	<chrome:box title="">
	<chrome:division>
	    <p>
	        <strong>Selecting from a new Item Bank will delete all existing questions. Would you like to continue anyway? </strong>
	        <br />
	    </p><br />
	    <input type="hidden" id="isConfirm" value="true"/>
	    <div class="flow-buttons">
	     <span class="previous ibutton">
	      <tags:button onclick="closeWindow()" color="blue" value="Cancel" markupWithTag="a" icon="x"/>
	    </span>
	    <span class="next" style="position:relative; top:-30px;">
	    <c:if test="${componentType == 'proCtcTerm'}">
                <tags:button id="flow-update" color="red" icon="check" value="Delete"
                     onclick="deleteExistingAndAddItemsForQuestion('${crfPageNumber}','${proCtcTermId}', '${componentType}', '${categoryName}')"/>
	    </c:if>
	    
	    <c:if test="${componentType == 'ctcCategory'}">
               <tags:button id="flow-update" color="red" icon="check" value="Delete"
                     onclick="deleteExistingAndAddItemsForCategory('${componentType}', '${ctcCategoryId }', '${categoryName}', '${crfPageNumbers}')"/>
	    </c:if>

	    </span>
	    </div>
    </chrome:division>
    </chrome:box>
</body>