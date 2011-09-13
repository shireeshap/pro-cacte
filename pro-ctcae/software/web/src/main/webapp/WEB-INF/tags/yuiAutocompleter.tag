<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="hiddenInputName" %>
<%@ attribute name="value" %>
<%@attribute name="inputName" required="true" %>
<%@attribute name="required" required="false" %>

<style>
.yui-skin-sam .yui-ac-content {
    max-height: 20em;
    overflow-x: hidden;
    overflow-y: auto;
    width:335px;
}
</style>
<table cellpadding="0" cellspacing="0">
    <tr height="0">
        <td height="0" style="vertical-align:top;border:0px;">
            <div class="yui-skin-sam" >
                <div id="midSize-AutoComplete" style="position:relative;padding-bottom:0px">
                    <c:if test="${required}"><tags:requiredIndicator/></c:if>
                    <input id="${inputName}" type="text" value="${value}" class="pending-search" />
                </div>
                <div id="${inputName}Autocomplete" style="z-index:300000;" class="yui-ac-container"></div>
            </div>
        </td>
        <td style="vertical-align:top;padding-left:10px;border:0px;">
        
            <a href="javascript:clearInput('${hiddenInputName}')">
                <img id="${inputName}-clear" style="vertical-align: top;"
                     src="/proctcae/images/blue/clear-left-button.png"
                     value="Clear" name="C"/>
            </a>
            <tags:indicator id="${inputName}-indicator"/>
        </td>
    </tr>
</table>


