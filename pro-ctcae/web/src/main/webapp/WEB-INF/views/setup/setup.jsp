<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<html>
<body>
<form:form method="post">

    <chrome:box title="Create the first administrative account" id="setup-input" cssClass="paired">
        <chrome:division>
            <p class="instructions">
                Every ProCtcAe instance needs at least one System Administrator. The sysadmin creates users and
                grants them privileges in ProCtcAe. He or she also performs basic configuration of the application.
                Since you're filling out this form, the sysadmin's probably you. Please select a username and
                enter a secure password. You'll be able to change the password later, but not the username.
            </p>
        </chrome:division>
        <h3>Enter sysadmin info</h3>
        <tags:hasErrorsMessage hideErrorDetails="false"/>

        <chrome:division title="">
            <tags:renderEmail propertyName="user.username"
                              displayName="clinicalStaff.label.email_address"
                              required="true" help="true"/>

            <tags:renderPassword propertyName="user.password"
                                 displayName="clinicalStaff.label.password"
                                 required="true"/>

            <tags:renderPassword propertyName="user.confirmPassword"
                                 displayName="Confirm Password"
                                 required="true"/>

        </chrome:division>

    </chrome:box>
    <chrome:box title="Tips" autopad="true" id="setup-tips" cssClass="paired">
        <ul class="tips">
            <li>You'll be able to create more System Administrator accounts later, if you like.</li>
            <li>You'll also be able to grant other privileges to the account you create here if
                you need to.
            </li>
        </ul>
    </chrome:box>
    <tags:tabControls willSave="true"/>

</form:form>


</body>
</html>