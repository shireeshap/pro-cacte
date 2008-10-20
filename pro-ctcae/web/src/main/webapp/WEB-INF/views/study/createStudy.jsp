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


    <tags:dwrJavascriptLink objects="organization"/>
    <script type="text/javascript">

        var studyCoordinatingCenterAutocompleter = {
            basename: "studyCoordinatingCenter.organization",
            populator:   function(autocompleter, text) {
                organization.matchOrganization(text, function(values) {
                    autocompleter.setChoices(values)
                })
            },
            valueSelector: function (obj) {
                return obj.displayName;
            }

        }
        var studyFundingSponsorAutocompleter = {
            basename: "studyFundingSponsor.organization",
            populator:   function(autocompleter, text) {
                organization.matchOrganization(text, function(values) {
                    autocompleter.setChoices(values)
                })
            },
            valueSelector: function (obj) {
                return obj.displayName;
            }

        }

        function acPostSelect(mode, selectedChoice) {
            $(mode.basename).value = selectedChoice.id;
        }
        function acCreate(mode) {
            new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices",
                    mode.populator, {
                valueSelector: mode.valueSelector,
                afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                    acPostSelect(mode, selectedChoice)
                },
                indicator: mode.basename + "-indicator"
            })

        }


        Event.observe(window, "load", function() {
            acCreate(studyCoordinatingCenterAutocompleter)
            acCreate(studyFundingSponsorAutocompleter)

        <c:if test="${studyFundingSponsor ne null}">
            $('studyFundingSponsor.organization-input').value = '${studyFundingSponsor.organization.displayName}';
            $('studyFundingSponsor.organization').value = '${studyFundingSponsor.organization.id}';
            $('studyFundingSponsor.organization-input').class = 'autocomplete';

        </c:if><c:if test="${studyCoordinatingCenter ne null}">
            $('studyCoordinatingCenter.organization-input').value = '${apartment.organization.displayName}';
            $('studyCoordinatingCenter.organization').value = '${apartment.organization.id}';
            $('studyCoordinatingCenter.organization-input').class = 'autocomplete';

        </c:if>
            initSearchField()
        })


    </script>


</head>
<body>

<form:form method="post" commandName="studyCommand">
    <chrome:box title="Study details" autopad="true">

        <p><tags:instructions code="study.study_details.top"/></p>

        <tags:renderText propertyName="assignedIdentifier" displayName="Assigned Identifier"
                         required="true" help="true" size="50"/>
        <tags:renderText propertyName="shortTitle" displayName="Short title"
                         required="true" help="true" size="50"/>

        <tags:renderTextArea propertyName="longTitle" displayName="Long title"
                         required="true" help="true" cols="70"/>


        <tags:renderTextArea propertyName="description" displayName="Description"
                             required="false" help="true" cols="70"/>

        <chrome:division title="Coordinating center details">
            <tags:renderAutocompleter propertyName="studyCoordinatingCenter.organization" displayName="Coordinating center"
                                      required="true" help="true"/>

        </chrome:division>
        <chrome:division title="Funding sponsor details">
            <tags:renderAutocompleter propertyName="studyFundingSponsor.organization" displayName="Funding sponsor"
                                      required="true" help="true"/>

        </chrome:division>


        <div class="row">
            <div class="submit">
                <input type="submit" id="submitButton" value="Submit"/>
            </div>
        </div>


    </chrome:box>
</form:form>

</body>
</html>