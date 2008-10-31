<%@ attribute name="displayError" type="java.lang.Boolean" %>
<%@ attribute name="inputName" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<script type="text/javascript">
    acCreate(new siteAutoComplter('${inputName}'))
    initSearchField()
</script>
<tr id="${inputName}-row">
    <td style="border-right:none;">
        <input id="${inputName}-input" class="autocomplete validate-NOTEMPTY"
               type="text"
               title="${title}" size="50" autocomplete="off"/>

        <tags:indicator id="${inputName}-indicator"/>
        <input type="button" id="${inputName}-clear" name="C" value="Clear"
               onClick="javascript:$('${inputName}-input').clear();$('${inputName}').clear();"/>

        <div id="${inputName}-choices" class="autocomplete"
             style="display: none"></div>


        <input id="${inputName}" class="validate-NOTEMPTY" type="text" value=""
               title="${title}" style="display: none;" name="${inputName}"/>

    </td>

    <td style="border-left:none;">

        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:fireDelete('${index}','${inputName}-row');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>


</tr>


