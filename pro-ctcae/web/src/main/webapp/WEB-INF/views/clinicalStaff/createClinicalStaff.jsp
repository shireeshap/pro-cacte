<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>
<%@ taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/ctcae/tags" %>

<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>

    <script type="text/javascript">

        Event.observe(window, "load", function() {
        <c:forEach  items="${clinicalStaffCommand.clinicalStaff.organizationClinicalStaffs}" var="organizationClinicalStaff" varStatus="status">
            var siteBaseName = 'clinicalStaff.organizationClinicalStaffs[${status.index}].organization'
            acCreate(new siteAutoComplter(siteBaseName));
            initializeAutoCompleter(siteBaseName, '${organizationClinicalStaff.organization.displayName}', '${organizationClinicalStaff.organization.id}');
        </c:forEach>
            initSearchField()
        })


        function addSiteDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }

        function addSite() {
            var request = new Ajax.Request("<c:url value="/pages/admin/clinicalStaff/addClinicalStaffComponent"/>", {
                onComplete:addSiteDiv,
                parameters:"subview=subview&componentTyep=site",
                method:'get'
            })
        }


        function deleteSite(organizationClinicalStaffIndex) {

            var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
                parameters:"confirmationType=deleteOrganizationClinicalStaff&subview=subview&organizationClinicalStaffIndex=" + organizationClinicalStaffIndex,
                onComplete:function(transport) {
                    showConfirmationWindow(transport);

                } ,
                method:'get'
            });


        }
        function deleteSiteConfirm(organizationClinicalStaffIndex) {
            closeWindow();
            $('showForm').value = true;
            $('organizationClinicalStaffIndexToRemove').value = organizationClinicalStaffIndex;
            $('clinicalStaffCommand').submit();

        }

    </script>

</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <ctcae:urlAuthorize url="/pages/admin/clinicalStaff/createClinicalStaff">

                <li id="thirdlevelnav 1" class="tab selected">
                    <div>
                        <a href="createClinicalStaff"><tags:message code="clinicalStaff.tab.createStaff"/></a>
                    </div>
                </li>
            </ctcae:urlAuthorize>
            <ctcae:urlAuthorize url="/pages/admin/clinicalStaff/searchClinicalStaff">

                <li id="thirdlevelnav 2" class="tab">
                    <div>
                        <a href="searchClinicalStaff"><tags:message code="clinicalStaff.tab.searchStaff"/></a>
                    </div>
                </li>
            </ctcae:urlAuthorize>
        </ul>
    </div>
</div>

<form:form method="post" commandName="clinicalStaffCommand">

    <chrome:box title="">
        <tags:hasErrorsMessage hideErrorDetails="false"/>
        <ctcae:urlAuthorize url="/pages/admin/clinicalStaff/createCCA">
            <input type="hidden" name="cca" value="true" id="cca"/>
        </ctcae:urlAuthorize>

        <input type="hidden" id="showForm" name="showForm" value=""/>
        <form:hidden path="organizationClinicalStaffIndexToRemove" id="organizationClinicalStaffIndexToRemove"/>

        <p><tags:instructions code="clinicalStaff.clinicalStaff_details.top"/></p>
        <chrome:division title="clinicalStaff.division.user_account">
            <tags:renderEmail propertyName="clinicalStaff.emailAddress"
                              displayName="clinicalStaff.label.email_address"
                              required="true" help="true"/>

            <tags:renderPassword propertyName="clinicalStaff.user.password"
                                 displayName="clinicalStaff.label.password"
                                 required="true"/>

            <tags:renderPassword propertyName="clinicalStaff.user.confirmPassword"
                                 displayName="Confirm Password"
                                 required="true"/>

        </chrome:division>
        <chrome:division title="clinicalStaff.division.details">
            <table>
                <tr>
                    <td>

                        <tags:renderText propertyName="clinicalStaff.firstName"
                                         displayName="clinicalStaff.label.first_name"
                                         required="true"/>
                        <tags:renderText propertyName="clinicalStaff.middleName"
                                         displayName="clinicalStaff.label.middle_name"/>
                        <tags:renderText propertyName="clinicalStaff.lastName"
                                         displayName="clinicalStaff.label.last_name"
                                         required="true"/>

                    </td>
                    <td style="vertical-align:top">

                        <tags:renderText propertyName="clinicalStaff.nciIdentifier"
                                         displayName="clinicalStaff.label.identifier"
                                         required="true"/>

                        <tags:renderPhoneOrFax propertyName="clinicalStaff.phoneNumber"
                                               displayName="clinicalStaff.label.phone"
                                               required="true"/>
                        <tags:renderPhoneOrFax propertyName="clinicalStaff.faxNumber"
                                               displayName="clinicalStaff.label.fax"/>


                    </td>
                </tr>
            </table>
        </chrome:division>
        <chrome:division title="clinicalStaff.division.sites">

            <table cellspacing="0" width="90%">
                <tr>
                    <td>
                        <div align="left" style="margin-left: 145px">
                            <table width="90%" class="tablecontent">
                                <tr id="ss-table-head" class="amendment-table-head">
                                    <th width="95%" class="tableHeader">
                                        <tags:requiredIndicator/><tags:message
                                            code='clinicalStaff.division.sites'/></th>
                                    <th width="5%" class="tableHeader" style=" background-color: none">
                                        &nbsp;</th>

                                </tr>


                                <c:forEach items="${clinicalStaffCommand.clinicalStaff.organizationClinicalStaffs}"
                                           var="organizationClinicalStaff"
                                           varStatus="status">

                                    <administration:organizationClinicalStaff
                                            organizationClinicalStaff="${organizationClinicalStaff}"
                                            organizationClinicalStaffIndex="${status.index}"/>
                                </c:forEach>

                                <tr id="hiddenDiv" align="center"></tr>
                            </table>
                        </div>
                    </td>
                    <td valign="top">
                        <ctcae:urlAuthorize url="/pages/admin/clinicalStaff/addClinicalStaffComponent">

                            <tags:button color="blue" markupWithTag="a" icon="add" value="clinicalStaff.button.add.site"
                                         onclick="javascript:addSite()"></tags:button>
                        </ctcae:urlAuthorize>
                    </td>
                </tr>
            </table>


        </chrome:division>
    </chrome:box>
    <tags:tabControls willSave="true"/>
</form:form>

</body>
</html>