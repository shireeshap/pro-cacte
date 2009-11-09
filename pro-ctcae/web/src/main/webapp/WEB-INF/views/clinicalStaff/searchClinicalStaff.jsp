<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<link rel="stylesheet" type="text/css"
      href="<c:url value="/css/extremecomponents.css"/>">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <tags:javascriptLink name="extremecomponents"/>
    <tags:dwrJavascriptLink objects="clinicalStaff"/>

    <script>

        function buildTable(form) {

            var firstName = $F('firstName')
            var lastName = $F('lastName')
            var nciIdentifier = $F('nciIdentifier')

            //     alert(lastName);

            if (firstName == '' && lastName == '' && nciIdentifier == '') {
                $('error').innerHTML = "<font color='#FF0000'>Provide at least one value in the search field</font>";

            } else {
                $('error').innerHTML = ""
                $('bigSearch').show()
                //		//showing indicator and hiding pervious results. (#10826)
                $('indicator').className = '';
                //	$('assembler_table').hide();  //do not hide the results..becz filter string get disappear
                var parameterMap = getParameterMap(form);
                //   clinicalStaff.searchClinicalStaff(parameterMap, showTable);
                clinicalStaff.searchClinicalStaff(parameterMap, firstName, lastName, nciIdentifier, showTable);
            }
        }

        function navigate(e) {
            if (event.keyCode == 13)  //enter pressed
                doSend();
        }
        document.onkeypress = navigate;
        function doSend() {
            buildTable('assembler');
        }

    </script>


</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">


            <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/createClinicalStaff">
                <li id="thirdlevelnav-x" class="tab ">
                    <div>
                        <a href="<c:url value="createClinicalStaff"/>"><tags:message
                                code="clinicalStaff.tab.createStaff"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/searchClinicalStaff">

                <li id="thirdlevelnav-x" class="tab selected">
                    <div>
                        <a href="<c:url value="searchClinicalStaff"/>"><tags:message
                                code="clinicalStaff.tab.searchStaff"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>

        </ul>
    </div>
</div>
<chrome:box title="clinicalStaff.box.searchCriteria" autopad="true">
    <p><tags:instructions code="clinicalStaff.search.top"/></p>

    <!-- <table>
    <tr><td>First Name</td><td><input type="text" id="firstName" name="firstName" maxlength="30"/></td></tr>
    <tr><td>Last Name</td><td><input type="text" id="lastName" name="lastName" maxlength="30"/></td></tr>
    <tr><td>ClinicalStaff Number</td><td><input type="text" id="nciIdentifier"name="nciIdentifier" maxlength="30"/></td></tr>
    </table>
    -->
    <div class="row">
        <div class="label"><tags:message code="clinicalStaff.label.first_name"/></div>
        <div class="value"><input type="text" id="firstName" name="firstName" maxlength="30"/></div>
    </div>
    <div class="row">
        <div class="label"><tags:message code="clinicalStaff.label.last_name"/></div>
        <div class="value"><input type="text" id="lastName" name="lastName" maxlength="30"/></div>
    </div>
    <div class="row">
        <div class="label"><tags:message code="clinicalStaff.label.identifier"/></div>
        <div class="value"><input type="text" id="nciIdentifier" name="nciIdentifier" maxlength="30"/></div>
    </div>

    <div id="error"></div>
    <div class="row">
        <div class="label"></div>
        <div class="value">
            <c:set var="search"><tags:message code="clinicalStaff.button.search"/></c:set>
            <tags:button color="blue" icon="search" type="button" onclick="buildTable('assembler');" value='${search}'/>
            <tags:indicator id="indicator"/>
        </div>
    </div>


</chrome:box>

<div id="bigSearch" style="display:none;">
    <div class="endpanes"/>
    <chrome:box title="clinicalStaff.box.results">
        <p><tags:instructions code="study.search.results"/></p>
        <form:form id="assembler">
            <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/createClinicalStaff">
                <tags:button color="blue" markupWithTag="a" id="newFormUrl" icon="add" value="New Staff Profile"
                             href="createClinicalStaff"/>
            </proctcae:urlAuthorize>
            <chrome:division id="single-fields">
                <div id="tableDiv">
                    <c:out value="${assembler}" escapeXml="false"/>
                </div>
            </chrome:division>
        </form:form>
    </chrome:box>
</div>


</body>
</html>