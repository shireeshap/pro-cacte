<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

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
                parameters:<tags:ajaxstandardparams/>+"&componentType=site",
                method:'get'
            })
        }


        function deleteSite(organizationClinicalStaffIndex) {
            var request = new Ajax.Request("<c:url value="/pages/admin/clinicalStaff/addClinicalStaffComponent"/>", {
                parameters:<tags:ajaxstandardparams/>+"&action=delete&organizationClinicalStaffIndex=" + organizationClinicalStaffIndex,
                onComplete:function(transport) {
                    $('row-' + organizationClinicalStaffIndex).remove();
                } ,
                method:'get'
            });
        }
        function showpassword(show) {
            if (show) {
                $('passwordfields').show();
                $('resetpass').innerHTML = '<a href="javascript:showpassword(false);">Hide password</a>';
            } else {
                $('passwordfields').hide();
                $('resetpass').innerHTML = '<a href="javascript:showpassword(true);">Reset password</a>';
            }
        }

        function showOrHideUserAccountDetails(value) {
            if (value) {
                $('div_useraccount_details').show();
                $('clinicalStaff.user.username').addClassName("validate-NOTEMPTY&&MAXLENGTH2000")
                $('clinicalStaff.user.password').addClassName("validate-NOTEMPTY&&MAXLENGTH2000")
                $('clinicalStaff.user.confirmPassword').addClassName("validate-NOTEMPTY&&MAXLENGTH2000")
            } else {
                $('div_useraccount_details').hide();
                $('clinicalStaff.user.username').removeClassName("validate-NOTEMPTY&&MAXLENGTH2000")
                $('clinicalStaff.user.password').removeClassName("validate-NOTEMPTY&&MAXLENGTH2000")
                $('clinicalStaff.user.confirmPassword').removeClassName("validate-NOTEMPTY&&MAXLENGTH2000")
            }
        }

    </script>

</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/createClinicalStaff">

                <li id="thirdlevelnav-x" class="tab selected">
                    <div>
                        <a href="createClinicalStaff"><tags:message code="clinicalStaff.tab.createStaff"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/searchClinicalStaff">

                <li id="thirdlevelnav-x" class="tab">
                    <div>
                        <a href="searchClinicalStaff"><tags:message code="clinicalStaff.tab.searchStaff"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
        </ul>
    </div>
</div>

<form:form method="post" commandName="clinicalStaffCommand">
    <c:set var="hasUserAccount"
           value="${clinicalStaffCommand.clinicalStaff.user.username ne null}"/>
    <chrome:box title="">
        <tags:hasErrorsMessage hideErrorDetails="false"/>
        <input type="hidden" id="showForm" name="showForm" value=""/>

        <p><tags:instructions code="clinicalStaff.clinicalStaff_details.top"/></p>
        <chrome:division title="clinicalStaff.division.details">
            <table width="100%">
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
                        <tags:renderEmail propertyName="clinicalStaff.emailAddress"
                                          displayName="clinicalStaff.label.email_address"
                                          required="true" size="40"/>
                    </td>
                </tr>
            </table>
            <c:choose>
                <c:when test="${hasUserAccount}">
                    <input type="hidden" name="userAccount" value="true" id="hasUserAccount"/>
                    <c:set var="useraccountdetailsstyle" value=""/>
                </c:when>
                <c:otherwise>
                    <input type="checkbox" name="userAccount" value="true"
                           id="hasUserAccount"
                           onclick="showOrHideUserAccountDetails(this.checked)"/> Create a user account for this clinical staff
                    <c:set var="useraccountdetailsstyle" value="display:none"/>
                </c:otherwise>
            </c:choose>
        </chrome:division>
        <div id="div_useraccount_details" style="${useraccountdetailsstyle}">
            <chrome:division title="clinicalStaff.division.user_account">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td>
                            <tags:renderText propertyName="clinicalStaff.user.username"
                                             displayName="participant.label.username"
                                             required="true"/>
                        </td>
                        <td>
                            <c:if test="${not empty clinicalStaffCommand.clinicalStaff.user.password}">
                                <c:set var="style" value="display:none"/>
                                <div id="resetpass" class="label">
                                    &nbsp;<a href="javascript:showpassword(true);">Reset password</a></div>
                            </c:if>
                        </td>
                    </tr>
                </table>
                <div id="passwordfields" style="${style}">
                    <tags:renderPassword propertyName="clinicalStaff.user.password"
                                         displayName="clinicalStaff.label.password"
                                         required="true"/>

                    <tags:renderPassword propertyName="clinicalStaff.user.confirmPassword"
                                         displayName="clinicalStaff.label.confirm_password"
                                         required="true"/>
                </div>
                <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/createCCA">
                    <input type="checkbox" name="cca" value="true"
                           id="cca"/> This clinical staff is a Coordinating Center Administrator
                    <br>
                </proctcae:urlAuthorize>
                <input type="checkbox" name="email" value="true"
                       id="email"/> Send email to the user with username and password details
            </chrome:division>
        </div>
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
                                            organizationClinicalStaffIndex="${status.index}"
                                            readOnly="true"/>
                                </c:forEach>

                                <tr id="hiddenDiv" align="center"></tr>
                            </table>
                        </div>
                    </td>
                    <td valign="top">
                        <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/addClinicalStaffComponent">
                            <tags:button color="blue" markupWithTag="a" icon="add" value="clinicalStaff.button.add.site"
                                         onclick="javascript:addSite()"></tags:button>
                        </proctcae:urlAuthorize>
                    </td>
                </tr>
            </table>


        </chrome:division>
    </chrome:box>
    <div style="text-align:right"><tags:button type="submit" color="green" value="Save" icon="save"/></div>
</form:form>
<c:if test="${not hasUserAccount}">
    <script type="text/javascript">
        showOrHideUserAccountDetails(false);
    </script>
</c:if>
</body>
</html>