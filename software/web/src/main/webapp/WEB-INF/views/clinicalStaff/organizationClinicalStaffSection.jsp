<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>
<tags:dwrJavascriptLink objects="organization"/>

<script type="text/javascript">

    function getOrgs(sQuery) {
		showIndicator('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organizationInput-indicator');
        var callbackProxy = function(results) {
            aResults = results;
        };
        var ALL_STUDY_SITES='GetAllStudySites';
        var callMetaData = { callback:callbackProxy, async:false};
        if (${cca}) {
            organization.matchOrganizationForStudySites(unescape(sQuery), ALL_STUDY_SITES, callMetaData);
        } else {
            organization.matchOrganizationForStudySitesWithSecurity(unescape(sQuery), callMetaData);
        }
        hideIndicator('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organizationInput-indicator');
        return aResults;
    }

    function handleSelect(stype, args) {
        var ele = args[0];
        var oData = args[2];
        if(oData == null){
        	ele.getInputEl().value="(Begin typing here)";
        	ele.getInputEl().addClassName('pending-search');
        } else {
	        ele.getInputEl().value = oData.displayName;
	        var id = ele.getInputEl().id;
	        var hiddenInputId = id.substring(0, id.indexOf('Input'));
	        ele.getInputEl().removeClassName('pending-search');
	        $(hiddenInputId).value = oData.id;
        }

    }

    function clearInput(inputId) {
        $(inputId).clear();
        $(inputId + 'Input').clear();
        $(inputId + 'Input').focus();
        $(inputId + 'Input').blur();
    }

    function initializeAutoCompleter() {
        new YUIAutoCompleter('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organizationInput', getOrgs, handleSelect);
        var orgDisplayName = "${organizationClinicalStaff.organization.displayName}";
        if (orgDisplayName != '') {
            $('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organizationInput').value = "${organizationClinicalStaff.organization.displayName}";
            $('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organizationInput').removeClassName('pending-search');
        }
    }

    initializeAutoCompleter();
</script>

<administration:organizationClinicalStaff organizationClinicalStaff="${organizationClinicalStaff}"
                                          organizationClinicalStaffIndex="${organizationClinicalStaffIndex}"/>



