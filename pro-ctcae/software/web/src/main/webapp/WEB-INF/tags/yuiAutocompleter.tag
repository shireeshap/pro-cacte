<%@ attribute name="hiddenInputName" %>
<%@ attribute name="value" %>
<%@attribute name="inputName" required="true" %>
<%@attribute name="required" required="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<table>
    <tr>
        <td style="border:0px;">
            <div class="yui-skin-sam">
                <div id="midSize-AutoComplete">
                    <c:if test="${required}"><tags:requiredIndicator/></c:if>
                    <input id="${inputName}" type="text" value="${value}">

                    <div id="${inputName}Autocomplete"></div>
                </div>
            </div>
        </td>
        <td style="vertical-align:top;padding-left:10px;border:0px;">
            <a href="javascript:clearInput('${hiddenInputName}')">
                <img id="${inputName}-clear" style="vertical-align: top;"
                     src="/proctcae/images/blue/clear-left-button.png"
                     value="Clear" name="C"/>
            </a>
        </td>
    </tr>
</table>


