<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    acCreate(new siteAutoComplter('clinicalStaff.siteClinicalStaffs[${index}].organization'))
    initSearchField()
</script>

<chrome:division title="clinicalStaff.division.sites">

    <%--isSiteAutoCompleter="true" --%>
    <tags:renderAutocompleter propertyName="clinicalStaff.siteClinicalStaffs[${index}].organization"
                              displayName="Site" noForm="true" required="true" />


    <input type="hidden" value="" id="objectsIdsToRemove" name="objectsIdsToRemove"/>

    <div align="left" style="margin-left: 145px">
        <table width="50%" class="tablecontent">
            <tr id="ss-table-head" class="amendment-table-head">
                <th width="35%" class="tableHeader"><tags:requiredIndicator/><tags:message
                        code="clinicalStaff.label.role"/></th>

                <th width="30%" class="tableHeader"><tags:requiredIndicator/><tags:message
                        code="clinicalStaff.label.role.status"/></th>
                <th width="30%" class="tableHeader"><tags:requiredIndicator/><tags:message
                        code="clinicalStaff.label.role.status.date"/></th>
                <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

            </tr>

            <tr id="hiddenDivForRole_${index}"></tr>

        </table>

    </div>
    <br>
    <tags:button type="anchor" icon="add" value="clinicalStaff.button.add.role"
                 onClick="javascript:addRole(${index})"></tags:button>


</chrome:division>


