<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@ attribute name="arm" type="gov.nih.nci.ctcae.core.domain.Arm" required="true" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tr id="${index}-row">
    <td style="border-right:none;">
    	&nbsp;<tags:requiredIndicator />&nbsp;<b>Name&nbsp;&nbsp;</b>
    	<input name="nonDefaultArms[${index}].title" type="text" size="20"  value="${arm.title}" class="validate-NOTEMPTY"/>
    </td>
    <td>
     	&nbsp;<b>Description&nbsp;&nbsp;</b>
    	<input name="nonDefaultArms[${index}].description" type="text" size="45"  value="${arm.description}" />
    </td>
    <td style="border-left:none;">
        <a id="del" href="javascript:deleteArm('${index}', '${param['studyId']}');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>
</tr>


