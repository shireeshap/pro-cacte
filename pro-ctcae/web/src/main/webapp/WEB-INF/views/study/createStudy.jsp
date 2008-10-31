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


        function addStudySiteDiv(transport) {

            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);


        }
        function addStudySite() {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudySite"/>", {
                onComplete:addStudySiteDiv,
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


        Event.observe(window, "load", function() {
            acCreate(new siteAutoComplter('study.studyCoordinatingCenter.organization'))
            acCreate(new siteAutoComplter('study.studyFundingSponsor.organization'))


        <c:if test="${studyCommand.study.studyFundingSponsor ne null}">
            initializeAutoCompleter('study.studyFundingSponsor.organization',
                    '${studyCommand.study.studyFundingSponsor.organization.displayName}', '${studyCommand.study.studyFundingSponsor.organization.id}')


        </c:if>

        <c:if test="${studyCommand.study.studyCoordinatingCenter ne null}">
            initializeAutoCompleter('study.studyCoordinatingCenter.organization',
                    '${studyCommand.study.studyCoordinatingCenter.organization.displayName}', '${studyCommand.study.studyCoordinatingCenter.organization.id}')


        </c:if>
        <c:forEach  items="${studyCommand.study.studySites}" var="studySite" varStatus="status">
            var siteBaseName = 'study.studySites[${status.index}].organization'
            acCreate(new siteAutoComplter(siteBaseName));
            initializeAutoCompleter(siteBaseName, '${studySite.organization.displayName}', '${studySite.organization.id}');
        </c:forEach>

            initSearchField()


        })


    </script>


</head>
<body>

<form:form method="post" commandName="studyCommand">
    <chrome:box title="Study details" autopad="true">
        <tags:hasErrorsMessage hideErrorDetails="false"/>


        <p><tags:instructions code="study.study_details.top"/></p>

        <tags:renderText propertyName="study.assignedIdentifier" displayName="Assigned identifier"
                         required="true" help="true" size="50"/>

        <tags:renderText propertyName="study.shortTitle" displayName="Short title"
                         required="true" help="true" size="50"/>

        <tags:renderTextArea propertyName="study.longTitle" displayName="Long title"
                             required="true" help="true" cols="47"/>


        <tags:renderTextArea propertyName="study.description" displayName="Description"
                             required="false" help="true" cols="47"/>

        <tags:renderAutocompleter propertyName="study.studyCoordinatingCenter.organization"
                                  displayName="Coordinating center"
                                  required="true" help="true" size="50"/>

        <tags:renderAutocompleter propertyName="study.studyFundingSponsor.organization"
                                  displayName="Funding sponsor"
                                  required="true" help="true" size="50"/>


        <chrome:division title="Study sites">
            <p><tags:instructions code="study.study_sites.top"/></p>

            <input type="hidden" value="" id="objectsIdsToRemove" name="objectsIdsToRemove"/>

            <div align="left" style="margin-left: 50px">
                <table width="55%" class="tablecontent">
                    <tr id="ss-table-head" class="amendment-table-head">
                        <th width="95%" class="tableHeader"><tags:requiredIndicator/>Site</th>
                        <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

                    </tr>
                    <c:forEach items="${studyCommand.study.studySites}" var="studySite" varStatus="status">

                        <tags:oneOrganization index="${status.index}"
                                              inputName="study.studySites[${status.index}].organization"
                                              title="Study Site" displayError="true"></tags:oneOrganization>
                    </c:forEach>


                    <tr id="hiddenDiv"></tr>

                </table>

            </div>
            <tags:tabControls willSave="${willSave}" saveButtonLabel="${saveButtonLabel}">
                <jsp:attribute name="localButtons">
                    <input type="button" value="Add Study Site" onClick="addStudySite()" class="button"/>
                    
                </jsp:attribute>
            </tags:tabControls>


        </chrome:division>

    </chrome:box>
    <tags:tabControls willSave="true"/>

</form:form>

</body>
</html>