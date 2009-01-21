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
    <strong>This will delete all changes that you made to this page.</strong>.
    <br/>
</p>


<p>
    <strong id="conditionsWarningForCrfPage_${selectedCrfPageNumber}" style="display:none"><tags:message
            code="form.label.delete_conditional_triggering_question_instruction"/></strong>
</p>

Click on cancel button to cancel it or click on delete button to delete this page.


</div>
<br>


<div class="flow-buttons">

    <input type="button" id="flow-update" class="next" value="<tags:message code="label.delete"/>"
           alt="Delete" onclick="deleteCrfPageConfirm('${selectedCrfPageNumber}','${proCtcTermId}')"/>


    <input type="button" id="flow-cancel" class="previous ibutton" value="<tags:message code="label.cancel"/>"
           alt="Cancel" onclick="closeWindow()"/>


    </chrome:division>
    </chrome:box>
</body>