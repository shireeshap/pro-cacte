<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>

<body>

<chrome:box title="form.label.delete_question">
<chrome:division>
<p>
    <strong><tags:message code="form.label.delete_question_instruction"/></strong>

    <br/>
</p>

</div>
<br>


<div class="flow-buttons">
    <span class="next">
        <tags:button id="flow-update" color="red" icon="check" value="Delete"
                     onclick="deleteQuestionConfirm('${questionId}','${proCtcTermId}')"/>
    </span>
  <span class="previous ibutton">
      <tags:button onclick="closeWindow()" color="blue" value="Cancel" markupWithTag="a" icon="x"/>
  </span>

    </chrome:division>
    </chrome:box>
</body>