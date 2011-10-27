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
<tr id="row-${index}" <c:if test="${isLeadSite}"> style="display:none"</c:if>>
    <td style="border-right:none;">
        <c:choose>
            <c:when test="${readOnly}">
                ${studySite.organization.displayName}
            </c:when>
            <c:otherwise>
   <form:input path="${inputName}"
                       id="${inputName}" cssClass="validate-NOTEMPTY"
                       title="Study site"
                       cssStyle="display:none;"/>

                   <tags:yuiAutocompleter inputName="${inputName}Input"
                                          value=" ${studySite.organization.displayName}" required="false"
                                          hiddenInputName="${inputName}"/>


                <%--<tags:renderAutocompleter propertyName="${inputName}" displayName="${title}" required="${required}"--%>
                                          <%--size="100" doNotshowLabel="true" noForm="true"/>--%>


                <script type="text/javascript">
                    <%--acCreate(new siteAutoComplter('${inputName}'))--%>
//                    initSearchField()
                </script>

            </c:otherwise>
        </c:choose>
    </td>
    <td style="border-left:none;">
        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:deleteStudySite('${index}');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle;text-align:left">
        </a>
    </td>
</tr>


