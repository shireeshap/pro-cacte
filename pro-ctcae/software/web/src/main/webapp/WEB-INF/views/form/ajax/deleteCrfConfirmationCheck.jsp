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
    <strong>Do you really want to delete all ${crfPageDescription} questions?  </strong>
        <br/>
    </p>


<p>
    <strong id="conditionsWarningForCrfPage_${selectedCrfPageNumber}" style="display:none"><tags:message
            code="form.label.delete_conditional_triggering_question_instruction"/></strong>
    

 </p>

</div>
<br>


<div class="flow-buttons">
     <span class="previous ibutton">
      <tags:button onclick="closeWindow()" color="blue" value="Cancel" markupWithTag="a" icon="x"/>     
  	</span>
    <span class="next" style="position:relative; top:-30px;">
        <tags:button id="flow-update" color="red" icon="check" value="Delete"
                     onclick="deleteCrfPageConfirm('${selectedCrfPageNumber}','${proCtcTermId}')"/>
    </span>
    </chrome:division>
    </chrome:box>
</body>