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
            acCreate(new siteAutoComplter('study.studyCoordinatingCenter.organization'))
            acCreate(new siteAutoComplter('study.studyFundingSponsor.organization'))


        <c:if test="${command.study.studyFundingSponsor ne null}">
            initializeAutoCompleter('study.studyFundingSponsor.organization',
                    '${command.study.studyFundingSponsor.organization.displayName}', '${command.study.studyFundingSponsor.organization.id}')


        </c:if>

        <c:if test="${command.study.studyCoordinatingCenter ne null}">
            initializeAutoCompleter('study.studyCoordinatingCenter.organization',
                    '${command.study.studyCoordinatingCenter.organization.displayName}', '${command.study.studyCoordinatingCenter.organization.id}')


        </c:if>


            initSearchField()


        })


    </script>


</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="false">
   <jsp:attribute name="singleFields">

        <p><tags:instructions code="study.study_details.top"/></p>

        <tags:renderText propertyName="study.assignedIdentifier" displayName="study.label.assigned_identifier"
                         required="true" help="true" size="50"/>

        <tags:renderText propertyName="study.shortTitle" displayName="study.label.short_title"
                         required="true" help="true" size="50"/>

        <tags:renderTextArea propertyName="study.longTitle" displayName="study.label.long_title"
                             required="true" help="true" cols="47"/>


        <tags:renderTextArea propertyName="study.description" displayName="study.label.description"
                             required="false" help="true" cols="47"/>

        <tags:renderAutocompleter propertyName="study.studyCoordinatingCenter.organization"
                                  displayName="study.label.study_coordinating_center"
                                  required="true" help="true" size="50"/>

        <tags:renderAutocompleter propertyName="study.studyFundingSponsor.organization"
                                  displayName="study.label.study_funding_sponsor"
                                  required="true" help="true" size="50"/>




   </jsp:attribute>

</tags:tabForm>

</body>
</html>