<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<tags:dwrJavascriptLink objects="clinicalStaff"/>
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>

<script type="text/javascript">
    function getStaff(sQuery) {
               var callbackProxy = function(results) {
                   aResults = results;
               };
               var callMetaData = { callback:callbackProxy, async:false};
               clinicalStaff.matchOrganizationClinicalStaffByStudyOrganizationId(unescape(sQuery), ${studyOrganizationClinicalStaff.studyOrganization.id}, callMetaData);
               return aResults;
           }

           var managerAutoComp;
           function initializeAutoCompleter(){
               new YUIAutoCompleter('studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaffInput', getStaff, handleSelect);
               $('studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaffInput').value = "${studyOrganizationClinicalStaff.displayName}";
               $('studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaffInput').removeClassName('pending-search');

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

    <%--acCreate(new organizationClinicalStaffAutoComplter('studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaff',--%>
            <%--'${studyOrganizationClinicalStaff.studyOrganization.id}'))--%>
//   initSearchField()
</script>
<tags:noForm>  
    <study:studySiteClinicalStaff studyOrganizationClinicalStaff="${studyOrganizationClinicalStaff}"
                                  studyOrganizationClinicalStaffIndex="${studyOrganizationClinicalStaffIndex}"
                                  roleStatusOptions="${roleStatusOptions}" notifyOptions="${notifyOptions}"/>
</tags:noForm>