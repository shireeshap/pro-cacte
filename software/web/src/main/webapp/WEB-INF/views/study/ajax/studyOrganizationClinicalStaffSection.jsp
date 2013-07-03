<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>


<script type="text/javascript">
    function getStaff(sQuery) {
        showIndicator("studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaffInput-indicator");
        var callbackProxy = function(results) {
            aResults = results;
        };
        var callMetaData = { callback:callbackProxy, async:false};
        clinicalStaff.matchOrganizationClinicalStaffByStudyOrganizationId(unescape(sQuery), ${studyOrganizationClinicalStaff.studyOrganization.id}, callMetaData);
        hideIndicator("studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaffInput-indicator");
        return aResults;
    }

    var managerAutoComp;
    function initializeAutoCompleter() {
        new YUIAutoCompleter('studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaffInput', getStaff, handleSelect);
        var staffDisplayname = "${studyOrganizationClinicalStaff.displayName}";
        if (staffDisplayname != "") {
            $('studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaffInput').value = "${studyOrganizationClinicalStaff.displayName}";
            $('studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaffInput').removeClassName('pending-search');

        }
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
	        ele.getInputEl().removeClassName('pending-search');
	        var hiddenInputId = id.substring(0, id.indexOf('Input'));
	        $(hiddenInputId).value = oData.id;
        }
    }

    initializeAutoCompleter();

    <%--acCreate(new organizationClinicalStaffAutoComplter('studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaff',--%>
    <%--'${studyOrganizationClinicalStaff.studyOrganization.id}'))--%>
    //   initSearchField();
</script>
<tags:noForm>
    <study:studySiteClinicalStaff studyOrganizationClinicalStaff="${studyOrganizationClinicalStaff}"
                                  studyOrganizationClinicalStaffIndex="${studyOrganizationClinicalStaffIndex}"
                                  roleStatusOptions="${roleStatusOptions}" notifyOptions="${notifyOptions}"/>
</tags:noForm>