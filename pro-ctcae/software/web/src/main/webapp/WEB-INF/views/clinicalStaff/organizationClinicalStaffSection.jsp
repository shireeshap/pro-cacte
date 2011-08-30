<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>
<tags:dwrJavascriptLink objects="organization"/>

<script type="text/javascript">

    <%--<c:choose>--%>
    <%--<c:when test="${cca}">--%>
    <%--acCreate(new siteAutoComplter('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organization'))--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
    <%--acCreate(new siteAutoComplterWithSecurity('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organization'))--%>
    <%--</c:otherwise>--%>
    <%--</c:choose>--%>

    <%--initSearchField()--%>


    function getOrgs(sQuery) {

        var callbackProxy = function(results) {
            aResults = results;
        };
        var callMetaData = { callback:callbackProxy, async:false};
        if(${cca}){
            organization.matchOrganizationForStudySites(unescape(sQuery), callMetaData);
        }else{
            organization.matchOrganizationForStudySitesWithSecurity(unescape(sQuery), callMetaData);
        }
        return aResults;
    }

    function handleSelect(stype, args) {
        var ele = args[0];
        var oData = args[2];
        ele.getInputEl().value = oData.displayName;
        var id = ele.getInputEl().id;
        var hiddenInputId = id.substring(0, id.indexOf('Input'));
//            Element.update(hiddenInputId + "-selected-name", oData.displayName)
//            $(hiddenInputId + '-selected').show()
//            new Effect.Highlight(hiddenInputId + "-selected")
        $(hiddenInputId).value = oData.id;

    }

   function clearInput(inputId) {
        $(inputId).clear();
        $(inputId + 'Input').clear();
        $(inputId + 'Input').focus();
        $(inputId + 'Input').blur();
    }

   function initializeAutoCompleter(){
        new YUIAutoCompleter('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organizationInput', getOrgs, handleSelect);
            $('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organizationInput').value = "${organizationClinicalStaff.organization.displayName}";
            $('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organizationInput').removeClassName('pending-search');
   }

  initializeAutoCompleter();
</script>

<administration:organizationClinicalStaff organizationClinicalStaff="${organizationClinicalStaff}"
                                          organizationClinicalStaffIndex="${organizationClinicalStaffIndex}"/>



