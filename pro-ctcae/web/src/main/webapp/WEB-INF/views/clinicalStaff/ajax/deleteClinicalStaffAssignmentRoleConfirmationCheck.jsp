<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>

<body>

<chrome:box title="clinicalStaff.label.delete.site.role">
<chrome:division>
<p>
    <strong><tags:message code="clinicalStaff.label.delete.site.role.instruction"/></strong>

    <br/>
</p>

</div>
<br>


<div class="flow-buttons">

    <input type="button" id="flow-update" class="next" value="<tags:message code="label.delete"/>"
           alt="Delete" onclick="deleteSiteRoleConfirm('${clinicalStaffAssignmentIndex}','${clinicalStaffAssignmentRoleIndex}')"/>


    <input type="button" id="flow-cancel" class="previous ibutton" value="<tags:message code="label.cancel"/>"
           alt="Cancel" onclick="closeWindow()"/>


    </chrome:division>
    </chrome:box>
</body>