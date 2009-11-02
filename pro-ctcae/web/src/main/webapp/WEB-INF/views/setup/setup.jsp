<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<html>
<body>

<form:form method="post">
    <chrome:box title="Create the first administrative account" id="setup-input">
        <tags:hasErrorsMessage hideErrorDetails="false"/>
        <p><tags:instructions code="clinicalStaff.sysadmin_details.top"/></p>
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
                        <tags:renderPhoneOrFax propertyName="clinicalStaff.phoneNumber"
                                               displayName="clinicalStaff.label.phone"
                                               required="true"/>
                        <tags:renderEmail propertyName="clinicalStaff.emailAddress"
                                          displayName="clinicalStaff.label.email_address"
                                          required="true" size="40"/>
                        <tags:renderText propertyName="clinicalStaff.nciIdentifier"
                                         displayName="Identifier"/>
                    </td>
                </tr>
            </table>
        </chrome:division>
        <chrome:division title="clinicalStaff.division.user_account">
            <tags:renderText propertyName="clinicalStaff.user.username"
                             displayName="participant.label.username" required="true"/>
            <tags:renderPassword propertyName="clinicalStaff.user.password"
                                 displayName="clinicalStaff.label.password"
                                 required="true"/>
            <tags:renderPassword propertyName="clinicalStaff.user.confirmPassword"
                                 displayName="clinicalStaff.label.confirm_password"
                                 required="true"/>
            <input type="checkbox" name="email" value="true"
                   id="email"/> Send email to the user with username and password details
            <br/>
        </chrome:division>
    </chrome:box>
    <div style="text-align:right"><tags:button type="submit" color="green" value="Save" icon="save"/></div>
</form:form>
</body>
</html>