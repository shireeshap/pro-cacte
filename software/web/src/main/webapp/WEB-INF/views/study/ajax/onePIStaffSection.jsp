<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:dwrJavascriptLink objects="organization"/>
<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>

<script type="text/javascript">
	function getLeadStaff1(sQuery) {
	    showIndicator("principalInvestigators[${index}].organizationClinicalStaffInput-indicator");
	    var callbackProxy = function(results) {
	        aResults = results;
	    };
	    var callMetaData = { callback:callbackProxy, async:false};
	    clinicalStaff.matchOrganizationClinicalStaffByStudyOrganizationId(unescape(sQuery), ${command.study.leadStudySite.id}, 'PI', callMetaData);
	    hideIndicator("principalInvestigators[${index}].organizationClinicalStaffInput-indicator");
	    return aResults;
	}

	var managerAutoComp;
	function initializeAutoCompleter() {
		new YUIAutoCompleter('principalInvestigators[${index}].organizationClinicalStaffInput', getLeadStaff1, handleSelect);
	}
	
	function handleSelect(stype, args) {
	    var ele = args[0];
	    var oData = args[2];
	    if (oData == null) {
	        ele.getInputEl().value = "(Begin typing here)";
	        ele.getInputEl().addClassName('pending-search');
	    } else {
	        ele.getInputEl().value = oData.displayName;
	        var id = ele.getInputEl().id;
	        ele.getInputEl().removeClassName('pending-search');
	        var hiddenInputId = id.substring(0, id.indexOf('Input'));
	        $(hiddenInputId).value = oData.id;
	    }
	}

    initializeAutoCompleter();

</script>
<tags:noForm>
    <tags:onePIClinicalStaff index="${index}" inputName="principalInvestigators[${index}].organizationClinicalStaff" readOnly="false"/>
</tags:noForm>


