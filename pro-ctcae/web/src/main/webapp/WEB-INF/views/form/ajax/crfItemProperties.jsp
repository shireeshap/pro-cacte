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

<head>


</head>
<body>
<c:set var="properties"><tags:message code='form.label.properties'/></c:set>

<chrome:box title="${properties}">
    <noform:renderText propertyName="crfItem.instructions" displayName="form.label.instructions"
                       propertyValue="${crfItem.instructions}"></noform:renderText>

    <noform:renderSelect propertyName="crfItem.responseRequired" displayName="form.label.response_required"
                         propertyValue="${crfItem.responseRequired}" items="${responseRequired}">

    </noform:renderSelect>

    <noform:renderSelect
            propertyName="crfItem.crfItemAllignment" displayName="form.label.allignment"
            propertyValue="${crfItem.crfItemAllignment}" items="${crfItemAllignments}">

    </noform:renderSelect>

</chrome:box>
<input type="image" src="<c:url value="/images/blue/save_btn.png"/>" id="flow-update"
       class="tab" value="Save" alt="Save" onclick="submitCrfItemPropertiesWindow(${crfItem.proCtcQuestion.id})"/>

</body>