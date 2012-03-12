<%@ attribute name="displayError" type="java.lang.Boolean" %>
<%@ attribute name="inputName" required="true" %>
<%@ attribute name="required" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="isLeadSite" required="false" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@ attribute name="readOnly" type="java.lang.Boolean" required="false" %>
<%@ attribute name="studySite" type="gov.nih.nci.ctcae.core.domain.StudySite" required="false" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<div id="row-${index}" <c:if test="${isLeadSite}"> style="display:none;width:inherit;"</c:if>>
    <%--<div style="width:400px;">--%>
        <c:choose>
            <c:when test="${readOnly}">
                ${studySite.organization.displayName}
            </c:when>
            <c:otherwise>  <br>
                <form:input path="${inputName}"
                            id="${inputName}" cssClass="validate-NOTEMPTY"
                            title="Study site"
                            cssStyle="display:none;"/>

                <tags:yuiAutocompleter inputName="${inputName}Input"
                                       value=" ${studySite.organization.displayName}" required="false"
                                       hiddenInputName="${inputName}"/>

                <script type="text/javascript">
                </script>

            </c:otherwise>
        </c:choose>

    <%--</div>--%>
    <div style="width:400px;border:2em;float:right">
         <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:deleteStudySite('${index}');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle;text-align:left">
        </a>
    </div>

</div>
