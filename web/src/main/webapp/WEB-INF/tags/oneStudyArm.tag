<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@ attribute name="arm" type="gov.nih.nci.ctcae.core.domain.Arm" required="true" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tr id="${inputName}-row">
    <td style="border-right:none;">
        <tags:renderText propertyName="study.arms[${index}].title" displayName="Name"
                         size="20" noForm="true" propertyValue="${arm.title}" required="true"/>
    </td>
    <td>
        <tags:renderText propertyName="study.arms[${index}].description" displayName="Description"
                         size="70" noForm="true" propertyValue="${arm.description}"/>
    </td>
    <c:if test="${param['studyId'] eq null}">
        <td style="border-left:none;">
            <a id="del"
               href="javascript:deleteArm('${index}');">
                <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                     style="vertical-align:middle">
            </a>
        </td>
    </c:if>
</tr>


