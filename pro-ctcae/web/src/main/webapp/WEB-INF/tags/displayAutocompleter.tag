<%@ attribute name="inputName" required="true" %>
<%@ attribute name="required" %>
<%@ attribute name="displayName" %>
<%@ attribute name="size" %>

<div class="row">
    <div class="label">${displayName}</div>
    <div class="value"><input id="${inputName}" type="hidden" value="" name="study"/>
        <span class="required-indicator">*</span>
        <input id="${inputName}-input" class="autocomplete" type="text" value="" autocomplete="off" size="${size}"/>
        <input id="${inputName}-clear" type="button" value="Clear"/>
        <img id="${inputName}-indicator" class="indicator" alt="activity indicator"
             src="/caaers/images/indicator.white.gif"/>

        <div id="${inputName}-choices" class="autocomplete" style="display: none;"/>
        <p id="${inputName}-selected" style="display: none;">

    </div>

</div>
