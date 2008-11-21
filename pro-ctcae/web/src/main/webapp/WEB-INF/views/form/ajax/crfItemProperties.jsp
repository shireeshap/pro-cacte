<%-- This is the standard decorator for all caAERS pages --%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="noform" tagdir="/WEB-INF/tags/noform" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head></head>
<body>

<chrome:box title="crfItem.label.properties">
    <noform:renderTextArea propertyName="crfItem.instructions" displayName="crfItem.label.instructions"
                           propertyValue="${crfItem.instructions}"></noform:renderTextArea>

    <noform:renderRadio propertyName="crfItem.responseRequired" displayName="crfItem.label.response_required"
                        propertyValue="${crfItem.responseRequired}" items="${responseRequired}">

    </noform:renderRadio>


    <noform:renderRadio
            propertyName="crfItem.crfItemAllignment" displayName="crfItem.label.allignment"
            propertyValue="${crfItem.crfItemAllignment}" items="${crfItemAllignments}">

    </noform:renderRadio>

</chrome:box>
<div id="previewQuestion">
    <tags:questionReview crfItem="${crfItem}" showInstructions="false"/>


    <br>
    <br>
</div>
<div class="flow-buttons">

    <input type="image" src="<c:url value="/images/blue/save_btn.png"/>" id="flow-update"
           class="next" value="Save" alt="Save" onclick="submitCrfItemPropertiesWindow(${crfItem.proCtcQuestion.id})"/>
</div>
</body>