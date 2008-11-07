<%@ attribute name="displayError" type="java.lang.Boolean" %>
<%@ attribute name="inputName" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>

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
        <input id="${inputName}.startDate" class="date validate-NOTEMPTY&&DATE" type="text" value=""
               title="start date"  name="${inputName}.startDate"/>
        <a href="#" id="${title}-calbutton">
            <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17" height="16" border="0"
            align="absmiddle"/>
    </a>
    <i>(mm/dd/yyyy)</i>
    </td>
    <td style="border-right:none;">
        <input id="${inputName}.dueDate" class="date validate-NOTEMPTY&&DATE" type="text" value=""
               title="due date"  name="${inputName}.dueDate"/>
        <a href="#" id="${title}-calbutton">
            <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17" height="16" border="0"
            align="absmiddle"/>
    </a>
    <i>(mm/dd/yyyy)</i>
    </td>
    <td style="border-left:none;">

        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:fireDelete('${index}','${inputName}-row');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>


</tr>


