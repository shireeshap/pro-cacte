<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>

<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>

    <script type="text/javascript">

        Event.observe(window, "load", function() {
        <c:forEach  items="${clinicalStaffCommand.clinicalStaff.siteClinicalStaffs}" var="siteClinicalStaff" varStatus="status">
            var siteBaseName = 'clinicalStaff.siteClinicalStaffs[${status.index}].organization'
            acCreate(new siteAutoComplter(siteBaseName));
            initializeAutoCompleter(siteBaseName, '${siteClinicalStaff.organization.displayName}', '${siteClinicalStaff.organization.id}');
        </c:forEach>
            initSearchField()
        })


        function addSiteDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }

        function addSite() {
            var request = new Ajax.Request("<c:url value="/pages/clinicalStaff/addClinicalStaffCompoent"/>", {
                onComplete:addSiteDiv,
                parameters:"subview=subview&componentTyep=site",
                method:'get'
            })
        }


        function deleteSite(siteClinicalStaffIndex) {

            var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
                parameters:"confirmationType=deleteSiteClinicalStaff&subview=subview&siteClinicalStaffIndex=" + siteClinicalStaffIndex,
                onComplete:function(transport) {
                    showConfirmationWindow(transport);

                } ,
                method:'get'
            });


        }
        function deleteSiteConfirm(siteClinicalStaffIndex) {
            closeWindow();
            $('showForm').value = true;
            $('siteClinicalStaffIndexToRemove').value = siteClinicalStaffIndex;
            $('clinicalStaffCommand').submit();

        }

    </script>

</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <li id="thirdlevelnav" class="tab selected">
                <div>
                    <a href="createClinicalStaff"><tags:message code="clinicalStaff.tab.createStaff"/></a>
                </div>
            </li>
            <li id="thirdlevelnav" class="tab">
                <div>
                    <a href="searchClinicalStaff"><tags:message code="clinicalStaff.tab.searchStaff"/></a>
                </div>
            </li>
        </ul>
    </div>
</div>

<form:form method="post" commandName="clinicalStaffCommand">

    <chrome:box title="">
        <tags:hasErrorsMessage hideErrorDetails="false"/>

        <input type="hidden" id="showForm" name="showForm" value=""/>
        <form:hidden path="siteClinicalStaffIndexToRemove" id="siteClinicalStaffIndexToRemove"/>

        <p><tags:instructions code="clinicalStaff.clinicalStaff_details.top"/></p>
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
                        <tags:renderText propertyName="clinicalStaff.nciIdentifier"
                                         displayName="clinicalStaff.label.identifier"
                                         required="true"/>

                    </td>
                    <td style="vertical-align:top">

                        <tags:renderEmail propertyName="clinicalStaff.emailAddress"
                                          displayName="clinicalStaff.label.email_address"
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


                                <c:forEach items="${clinicalStaffCommand.clinicalStaff.siteClinicalStaffs}"
                                           var="siteClinicalStaff"
                                           varStatus="status">

                                    <administration:siteClinicalStaff
                                            siteClinicalStaff="${siteClinicalStaff}"
                                            siteClinicalStaffIndex="${status.index}"/>
                                </c:forEach>

                                <tr id="hiddenDiv" align="center"></tr>
                            </table>
                        </div>
                    </td>
                    <td valign="top">
                        <tags:button type="anchor" icon="add" value="clinicalStaff.button.add.site"
                                     onClick="javascript:addSite()"></tags:button>
                    </td>
                </tr>
            </table>


        </chrome:division>
    </chrome:box>
    <tags:tabControls willSave="true"/>
</form:form>

</body>
</html>