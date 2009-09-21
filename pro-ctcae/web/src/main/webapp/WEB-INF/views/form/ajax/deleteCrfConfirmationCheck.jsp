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

<chrome:box title="form.label.delete_page">
<chrome:division>
<p>
    <strong>Do you really want to delete all questions for ${crfPageDescription} symptom?  </strong>
        <br/>
    </p>


<p>
    <strong id="conditionsWarningForCrfPage_${selectedCrfPageNumber}" style="display:none"><tags:message
            code="form.label.delete_conditional_triggering_question_instruction"/></strong>
    <tags:message code="form.label.delete_crf_page_instruction_more1"/>


 </p>

</div>
<br>


<div class="flow-buttons">
    <span class="next">
        <tags:button id="flow-update" color="red" icon="check" value="Delete"
                     onclick="deleteCrfPageConfirm('${selectedCrfPageNumber}','${proCtcTermId}')"/>
    </span>
     <span class="previous ibutton">
      <tags:button onclick="closeWindow()" color="blue" value="Cancel" markupWithTag="a" icon="x"/>     
  </span>

    </chrome:division>
    </chrome:box>
</body>