<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

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
            var request = new Ajax.Request("<c:url value="/pages/clinicalStaff/addSite"/>", {
                onComplete:addSiteDiv,
                parameters:"subview=subview&",
                method:'get'
            })
        }

        function fireDelete(index, divToRemove) {
            if ($('objectsIdsToRemove').value != '') {
                $('objectsIdsToRemove').value = $('objectsIdsToRemove').value + "," + index;
            }
            else {
                $('objectsIdsToRemove').value = index;
            }

            $(divToRemove).remove();

        }


    </script>

</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <li id="thirdlevelnav" class="tab selected">
                <div>
                    <a href="createClinicalStaff">Create/Edit Clinical Staff</a>
                </div>
            </li>
            <li id="thirdlevelnav" class="tab">
                <div>
                    <a href="searchClinicalStaff">Search Clinical Staff</a>
                </div>
            </li>
        </ul>
    </div>
</div>

<form:form method="post" commandName="clinicalStaffCommand">

    <chrome:box title="Clinical Staff details">
        <tags:hasErrorsMessage hideErrorDetails="false"/>

        <p><tags:instructions code="clinicalStaff.clinicalStaff_details.top"/></p>
        <chrome:division title="Clinical Staff details"></chrome:division>
        <table>
            <tr>
                <td>

                    <tags:renderText propertyName="clinicalStaff.firstName" displayName="First name"
                                     required="true"/>
                    <tags:renderText propertyName="clinicalStaff.middleName" displayName="Middle name"/>
                    <tags:renderText propertyName="clinicalStaff.lastName" displayName="Last name"
                                     required="true"/>
                    <tags:renderText propertyName="clinicalStaff.nciIdentifier" displayName="Clinical Staff identifier"
                                     required="true"/>

                </td>
                <td style="vertical-align:top">

                    <tags:renderEmail propertyName="clinicalStaff.emailAddress" displayName="Email address"
                                      required="true"/>
                    <tags:renderPhoneOrFax propertyName="clinicalStaff.phoneNumber" displayName="Phone"
                                           required="true"/>
                    <tags:renderPhoneOrFax propertyName="clinicalStaff.faxNumber" displayName="Fax"/>


                </td>
            </tr>
        </table>

        <chrome:division title="Clinical Staff sites">

            <input type="hidden" value="" id="objectsIdsToRemove" name="objectsIdsToRemove"/>

            <div align="left" style="margin-left: 50px">
                <table width="55%" class="tablecontent">
                    <tr id="ss-table-head" class="amendment-table-head">
                        <th width="95%" class="tableHeader"><tags:requiredIndicator/>Site</th>
                        <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

                    </tr>
                    <c:forEach items="${clinicalStaffCommand.clinicalStaff.siteClinicalStaffs}" var="siteClinicalStaff"
                               varStatus="status">

                        <tags:oneOrganization index="${status.index}"
                                              inputName="clinicalStaff.siteClinicalStaffs[${status.index}].organization"
                                              title="Clinical Staff Site" displayError="true"></tags:oneOrganization>
                    </c:forEach>

                    <tr id="hiddenDiv"></tr>

                </table>

            </div>
            <tags:tabControls>
                   <jsp:attribute name="localButtons">
                       <input type="button" value="Add Site" onClick="addSite()" class="button"/>

                   </jsp:attribute>
            </tags:tabControls>


        </chrome:division>


    </chrome:box>
    <%--<tags:tabControls willSave="true"/>--%>
</form:form>

</body>
</html>