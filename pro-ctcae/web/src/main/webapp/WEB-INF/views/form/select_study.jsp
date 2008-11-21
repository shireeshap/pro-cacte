<%-- This is the standard decorator for all caAERS pages --%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>

    <tags:includePrototypeWindow/>

    <script type="text/javascript">
        Event.observe(window, "load", function () {
            var studyAutoCompleter = new studyAutoComplter('studyCrf.study');
            acCreate(studyAutoCompleter);
        <c:if test="${command.studyCrf.study ne null}">
            initializeAutoCompleter('studyCrf.study', '${command.studyCrf.study.displayName}', '${command.studyCrf.study.id}')

        </c:if>

            initSearchField();

        })


    </script>

</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" formName="createForm" hideErrorDetails="true">
    <jsp:attribute name="singleFields">

        <p><tags:instructions code="instruction_select_study"/></p>
        <tags:renderAutocompleter propertyName="studyCrf.study" required="true" displayName="form.label.study"
                                  size="70"/>
        <p id="studyCrf.study-selected" style="display: none">
            You have selected the study <span id="studyCrf.study-selected-name"></span>.
        </p>
    </jsp:attribute>


</tags:tabForm>

</body>