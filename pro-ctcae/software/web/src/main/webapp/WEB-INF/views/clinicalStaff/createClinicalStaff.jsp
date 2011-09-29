<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<tags:dwrJavascriptLink objects="nameValidator,userNameValidation,uniqueStaffEmailAddress"/>
<tags:javascriptLink name="ui_fields_validation"/>

<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>
<tags:dwrJavascriptLink objects="organization"/>

<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>

    <script type="text/javascript">

	    function getOrgs(sQuery) {
	        showIndicator('clinicalStaff.organizationClinicalStaffs[0].organizationInput-indicator');
	        var callbackProxy = function(results) {
	            aResults = results;
	        };
	        var callMetaData = { callback:callbackProxy, async:false};
	        if(${cca}){
	            organization.matchOrganizationForStudySites(unescape(sQuery), callMetaData);
	        }else{
	            organization.matchOrganizationForStudySitesWithSecurity(unescape(sQuery), callMetaData);
	        }
	        hideIndicator('clinicalStaff.organizationClinicalStaffs[0].organizationInput-indicator');
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

        var managerAutoComp;
        var orgName;
        Event.observe(window, "load", function() {
            <c:forEach  items="${clinicalStaffCommand.clinicalStaff.organizationClinicalStaffs}" var="organizationClinicalStaff" varStatus="status">
                <c:if test="${organizationClinicalStaff.id eq null}">
                    new YUIAutoCompleter('clinicalStaff.organizationClinicalStaffs[${status.index}].organizationInput', getOrgs, handleSelect);
                    orgName = "${organizationClinicalStaff.organization.displayName}";
                    if(orgName != ''){
                        $('clinicalStaff.organizationClinicalStaffs[${status.index}].organizationInput').value = "${organizationClinicalStaff.organization.displayName}";
                        $('clinicalStaff.organizationClinicalStaffs[${status.index}].organizationInput').removeClassName('pending-search');
                    }

                </c:if>
            </c:forEach>
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

        function showOrHideUserAccountDetails(value) {
            try {
                if (value) {
                    $('div_useraccount_details').show();
                    $('username').addClassName("validate-NOTEMPTY&&MAXLENGTH2000");
                } else {
                    $('div_useraccount_details').hide();
                    $('username').removeClassName("validate-NOTEMPTY&&MAXLENGTH2000");
                    jQuery('#userNameError').hide();
                    jQuery('#userNameLengthError').hide();
                    hideError();
                }
            } catch(err) {
            }
        }
        function disableCCA(obj) {
            if (obj.checked) {
                $('cca').disabled = true;
            } else {
                $('cca').disabled = false;
            }
        }
        function disableAdmin(obj) {
            if (obj.checked) {
                $('admin').disabled = true;
            } else {
                $('admin').disabled = false;
            }
        }

        function checkFirstName() {
            var firstName = $('clinicalStaff.firstName').value;
            if (firstName != "") {
                nameValidator.validateName(firstName, nameReturnValue);
                return;
            }

            else {
                jQuery('#nameError1').hide();
            }
            hideError();
        }
        function checkLastName() {
            var lastName = $('clinicalStaff.lastName').value;
            if (lastName != "") {
                nameValidator.validateName(lastName, nameReturnValue1);
                return;
            }
            else {
                jQuery('#nameError2').hide();
            }
            hideError();
        }
        function nameReturnValue(returnValue) {
            if (!returnValue) {
                jQuery('#nameError1').show();
            }
            else {
                jQuery('#nameError1').hide();
            }
            hideError();
        }
        function nameReturnValue1(returnValue) {
            if (!returnValue) {
                jQuery('#nameError2').show();
            }
            else {
                jQuery('#nameError2').hide();
            }
            hideError();
        }
        // checking if there are any error based on class name and style.
        function hideError() {
            var errorlist;
            var count = 0;
            var hideError = false;
            for (i = 0; i < document.getElementsByClassName('errors').length; i++) {
                errorlist = document.getElementsByClassName('errors')[i];
                if (errorlist.style.display != 'none') {
                    count++;
                    if (errorlist.id == "") {
                        hideError = true;
                        errorlist.hide();

                    }
                }
            }
        }
        function checkUniqueUserName() {

            var userName = $('username').value;
            var staffId = "${param['clinicalStaffId']}";
            if (staffId == "") {
                var userId = "${clinicalStaffCommand.clinicalStaff.user.id}";
            }
            if (userName != "") {
                if (userName.length < 6) {
                    jQuery('#userNameError').hide();
                    jQuery('#userNameLengthError').show();
                    hideError();
                }
                else {
                    userNameValidation.validateDwrUniqueName(userName, userId, userReturnValue);
                    jQuery('#userNameLengthError').hide();
                    return;
                }
            }
            else {
                jQuery('#userNameError').hide();
                jQuery('#userNameLengthError').hide();
            }

        }
        function userReturnValue(returnValue) {
            showOrHideErrorField(returnValue, '#userNameError');
            hideError();
        }

        //validation check for staff email address
        function checkUniqueEmailAddress() {
            var staffId = "${param['clinicalStaffId']}";
            var email = $('clinicalStaff.emailAddress').value;
            if (email != "") {
                uniqueStaffEmailAddress.validateStaffEmail(email, staffId, emailReturnValue);
                return;
            }
            else {
                jQuery('#emailError').hide();
            }
            hideError();
        }
        function emailReturnValue(returnValue) {
            showOrHideErrorField(returnValue, '#emailError');
            hideError();
        }

    </script>
    <!--[if IE]>
    <style>
        div.row div.value {
            margin-left: 0;
        }
    </style>
    <![endif]-->
</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
                <li id="thirdlevelnav-x" class="tab selected">
                    <div>
                        <a href="createClinicalStaff"><tags:message code="clinicalStaff.tab.createStaff"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/admin/searchClinicalStaff">
                <li id="thirdlevelnav-x" class="tab">
                    <div>
                        <a href="searchClinicalStaff"><tags:message code="clinicalStaff.tab.searchStaff"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
        </ul>
    </div>
</div>

<ctcae:form method="post" commandName="clinicalStaffCommand">
<c:set var="hasUserAccount"
       value="${clinicalStaffCommand.clinicalStaff.user ne null}"/>
<c:set var="isEdit"
       value="${param['clinicalStaffId'] ne null}"/>
<input name="isEdit" value="${isEdit}" type="hidden"/>
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
                                 required="true" onblur="checkFirstName();"/>
                <ul id="nameError1" style="display:none; padding-left:12em " class="errors">
                    <li><spring:message code='firstName_validation'
                                        text='firstName_validation'/>
                    </li>
                </ul>
                <tags:renderText propertyName="clinicalStaff.middleName"
                                 displayName="clinicalStaff.label.middle_name"/>
                <tags:renderText propertyName="clinicalStaff.lastName"
                                 displayName="clinicalStaff.label.last_name"
                                 required="true" onblur="checkLastName()"/>
                <ul id="nameError2" style="display:none; padding-left:12em " class="errors">
                    <li><spring:message code='lastName_validation'
                                        text='lastName_validation'/>
                    </li>
                </ul>
            </td>
            <td style="vertical-align:top">
                <tags:renderPhoneOrFax propertyName="clinicalStaff.phoneNumber"
                                       displayName="clinicalStaff.label.phone"
                                       required="true"/>
                <tags:renderEmail propertyName="clinicalStaff.emailAddress"
                                  displayName="clinicalStaff.label.email_address"
                                  required="true" size="40" onblur="checkUniqueEmailAddress();"/>
                <ul id="emailError" style="display:none; padding-left:12em " class="errors">
                    <li><spring:message code='clinicalStaff.unique_emailAddress'
                                        text='clinicalStaff.unique_emailAddress'/></li>
                </ul>
                <tags:renderText propertyName="clinicalStaff.nciIdentifier"
                                 displayName="clinicalStaff.label.identifier"/>
            </td>
        </tr>
    </table>
    <c:choose>
        <c:when test="${isEdit && hasUserAccount}">
            <c:if test="${error eq null}">
                <input type="hidden" name="userAccount" value="true" id="hasUserAccount"/>
                <c:set var="div_useraccount_details_style" value=""/>
            </c:if>
            <c:if test="${error}">
                <input type="checkbox" name="userAccount" value="true"
                       id="hasUserAccount" checked="true"
                       onclick="showOrHideUserAccountDetails(this.checked)"/> Create a user account for this clinical staff
                <c:set var="div_useraccount_details_style" value=""/>
            </c:if>
        </c:when>
        <c:when test="${isEdit && not hasUserAccount }">
            <input type="checkbox" name="userAccount" value="true"
                   id="hasUserAccount"
                   onclick="showOrHideUserAccountDetails(this.checked)"/> Create a user account for this clinical staff
            <c:set var="div_useraccount_details_style" value="display:none"/>
        </c:when>

        <c:otherwise>
            <input type="checkbox" name="userAccount" value="true"
                   id="hasUserAccount"
                   onclick="showOrHideUserAccountDetails(this.checked)"
                   checked/> Create a user account for this clinical staff
            <c:set var="div_useraccount_details_style" value=""/>
        </c:otherwise>
    </c:choose>
</chrome:division>
<div id="div_useraccount_details" style="${div_useraccount_details_style}">
    <chrome:division title="clinicalStaff.division.user_account">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${empty param['clinicalStaffId'] or (not hasUserAccount)}">
                            <tags:renderText propertyName="username"
                                             displayName="participant.label.username"
                                             required="true" onblur="checkUniqueUserName();"/>
                            <ul id="userNameError" style="display:none; padding-left:12em " class="errors">
                                <li><spring:message code='clinicalStaff.unique_userName'
                                                    text='clinicalStaff.unique_userName'/></li>
                            </ul>
                            <ul id="userNameLengthError" style="display:none; padding-left:12em "
                                class="errors">
                                <li><spring:message code='clinicalStaff.username_length'
                                                    text='clinicalStaff.username_length'/></li>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <c:if test="${error eq null}">
                                    <div class="label"><spring:message code="participant.label.username"/>:
                                    </div>
                                    <div class="value">

                                        &nbsp;${clinicalStaffCommand.username}</div>

                                    <input type="hidden" id="clinicalStaff.user.username"
                                           name="clinicalStaff.user.username"
                                           value="${clinicalStaffCommand.username}">
                                </c:if>

                                <form:errors path="*">
                                    <tags:renderText propertyName="username"
                                                     displayName="participant.label.username"
                                                     required="true" onblur="checkUniqueUserName();"/>
                                    <ul id="userNameError" style="display:none; padding-left:12em "
                                        class="errors">
                                        <li><spring:message code='clinicalStaff.unique_userName'
                                                            text='clinicalStaff.unique_userName'/></li>
                                    </ul>
                                    <ul id="userNameLengthError" style="display:none; padding-left:12em "
                                        class="errors">
                                        <li><spring:message code='clinicalStaff.username_length'
                                                            text='clinicalStaff.username_length'/></li>
                                    </ul>
                                </form:errors>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </td>

            </tr>
        </table>

    </chrome:division>

    <c:set var="cca" value="false"/>
    <c:set var="admin" value="false"/>
    <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/createCCA">
        <c:set var="cca" value="true"/>
    </proctcae:urlAuthorize>
    <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/createAdmin">
        <c:set var="admin" value="true"/>
    </proctcae:urlAuthorize>

    <c:if test="${cca eq 'true' or admin eq 'true'}">
        <chrome:division title="Additional Options">
            <c:if test="${cca eq 'true'}">
                <input type="checkbox" name="cca" value="true"
                       id="cca"
                       <c:if test="${clinicalStaffCommand.cca}">checked disabled</c:if>
                       <c:if test="${clinicalStaffCommand.admin}">disabled</c:if> onclick="disableAdmin(this);"/>
                This user is a <u>Coordinating Center Administrator</u>
            </c:if>
            <br/>
            <c:if test="${admin eq 'true'}">
                <input type="checkbox" name="admin" value="true"
                       id="admin"
                       <c:if test="${clinicalStaffCommand.admin}">checked disabled</c:if>
                       <c:if test="${clinicalStaffCommand.cca}">disabled</c:if> onclick="disableCCA(this);"/>
                This user is a <u>System Administrator</u>
            </c:if>
        </chrome:division>
    </c:if>


    <br/>
</div>
<chrome:division title="clinicalStaff.division.sites">
    <div align="left" style="margin-left: 145px">
        <table cellspacing="0" width="90%">
            <tr>
                <td>

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
                                    readOnly="${organizationClinicalStaff.organization ne null}"/>
                        </c:forEach>

                        <tr id="hiddenDiv" align="center"></tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/addClinicalStaffComponent">
                        <tags:button color="blue" markupWithTag="a" icon="add"
                                     value="clinicalStaff.button.add.site"
                                     onclick="javascript:addSite()" size="small"></tags:button>
                    </proctcae:urlAuthorize>
                </td>
            </tr>
        </table>
    </div>
</chrome:division>
</chrome:box>
<div style="text-align:right"><tags:button type="submit" color="green" value="Save" icon="save"/></div>
</ctcae:form>
<c:if test="${hasUserAccount && isEdit}">
    <script type="text/javascript">
        showOrHideUserAccountDetails(true);
    </script>
</c:if>
<c:if test="${!hasUserAccount && isEdit}">
    <c:if test="${error}">
        <script type="text/javascript">
            showOrHideUserAccountDetails(true);
        </script>
    </c:if>
    <c:if test="${error eq null}">
        <script type="text/javascript">
            showOrHideUserAccountDetails(false);
        </script>
    </c:if>
</c:if>

</body>
</html>