<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@ attribute name="arm" type="gov.nih.nci.ctcae.core.domain.Arm" required="true" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tr id="${inputName}-row">
    <td style="border-right:none;">
        <tags:renderText propertyName="study.arms[${index}].title" displayName="Title" required="true"
                         size="30" noForm="true" propertyValue="${arm.title}"/>
    </td>
    <td>
        <tags:renderText propertyName="study.arms[${index}].description" displayName="Description"
                        size="70" noForm="true" propertyValue="${arm.description}"/>
    </td>
    <%--<td style="border-left:none;"></td>--%>
</tr>


