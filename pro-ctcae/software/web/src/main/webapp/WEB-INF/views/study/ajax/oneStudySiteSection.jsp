<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tags:dwrJavascriptLink objects="organization"/>
<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>

<script type="text/javascript">
    function getOrgs(sQuery) {
        showIndicator("study.studySites[${index}].organizationInput-indicator");
        var callbackProxy = function(results) {
            aResults = results;
        };
        var callMetaData = { callback:callbackProxy, async:false};
        organization.matchOrganizationForStudySites(unescape(sQuery), callMetaData);
        hideIndicator("study.studySites[${index}].organizationInput-indicator");
        return aResults;
    }

    var managerAutoComp;
    function initializeAutoCompleter() {
        new YUIAutoCompleter('study.studySites[${index}].organizationInput', getOrgs, handleSelect);
        var orgDisplayName = "${command.study.studySites[$index].organization.displayName}";
        if (orgDisplayName != "") {
            $('study.studySites[${index}].organizationInput').value = "${command.study.studySites[$index].organization.displayName}";
            $('study.studySites[${index}].organizationInput').removeClassName('pending-search');
        }
    }

    function handleSelect(stype, args) {
        var ele = args[0];
        var oData = args[2];
        ele.getInputEl().value = oData.displayName;
        var id = ele.getInputEl().id;
        var hiddenInputId = id.substring(0, id.indexOf('Input'));
        $(hiddenInputId).value = oData.id;
    }


    initializeAutoCompleter();

</script>
<tags:noForm>
    <tags:oneOrganization index="${index}" inputName="study.studySites[${index}].organization"
                          title="Study Site" displayError="false" required="true"></tags:oneOrganization>
</tags:noForm>


