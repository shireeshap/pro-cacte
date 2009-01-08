<%@ attribute name="displayError" type="java.lang.Boolean" %>
<%@ attribute name="inputName" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@ attribute name="startDate" type="java.util.Date" required="false" %>
<%@ attribute name="dueDate" type="java.util.Date" required="false" %>
<%@ attribute name="status" type="gov.nih.nci.ctcae.core.domain.CrfStatus" required="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tr id="${inputName}-row">
    <td style="border-right:none;">
        <c:choose>
            <c:when test="${status eq 'In-progress' or status eq 'Completed'}">
                <input id="${inputName}.startDate" type="text"
                       value='<tags:formatDate value="${startDate}"/>'
                       title="start date" name="${inputName}.startDate" readonly="true" disabled="true"/>
            </c:when>
            <c:otherwise>
                <input id="${inputName}.startDate" class="date validate-NOTEMPTY&&DATE" type="text"
                       value='<tags:formatDate value="${startDate}"/>'
                       title="start date" name="${inputName}.startDate"/>
                <a href="#" id="${title}-calbutton">
                    <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17" height="16" border="0"
                         align="absmiddle"/>
                </a>
                <i>(mm/dd/yyyy)</i>
            </c:otherwise>
        </c:choose>
    </td>
    <td style="border-right:none;">
        <c:choose>
            <c:when test="${status eq 'Completed'}">
                <input id="${inputName}.dueDate" type="text"
                       value='<tags:formatDate value="${dueDate}"/>'
                       title="due date" name="${inputName}.dueDate" readonly="true" disabled="true"/>
            </c:when>
            <c:otherwise>
                <input id="${inputName}.dueDate" class="date validate-NOTEMPTY&&DATE" type="text"
                       value='<tags:formatDate value="${dueDate}"/>'
                       title="due date" name="${inputName}.dueDate"/>
                <a href="#" id="${title}-calbutton">
                    <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17" height="16" border="0"
                         align="absmiddle"/>
                </a>
                <i>(mm/dd/yyyy)</i>
            </c:otherwise>
        </c:choose>
    </td>
    <td style="border-right:none;">
        ${status}
    </td>
    <td style="border-left:none;">

        <c:if test="${status ne 'In-progress' and status ne 'Completed'}">
            <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
               href="javascript:fireDelete('${index}','${inputName}-row');">
                <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                     style="vertical-align:middle">
            </a>
        </c:if>
    </td>


</tr>


