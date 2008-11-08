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
    <tags:dwrJavascriptLink objects="studyCrf"/>

    <script type="text/javascript">
        Event.observe(window, "load", function () {
            var studyAutoCompleter = new studyAutoComplter('study');
            acCreateStudy(studyAutoCompleter, displayForms);
        <c:if test="${study ne null}">
            initializeAutoCompleter('study',
                    '${study.displayName}', '${study.id}')

            displayForms();
        </c:if>
            initSearchField();

        })


        function displayForms() {
            $('noForm').show();
            var url = 'createForm?studyId=' + $('study').value
            $('newFormUrl').href = url;

            buildTable('assembler')

        }
        function buildTable(form) {
            var id = $('study').value
            var parameterMap = getParameterMap(form);
            $('bigSearch').show();
            studyCrf.searchStudyCrf(parameterMap, id, showTable)
        }

        function acCreateStudy(mode) {
            new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices",
                    mode.populator, {
                valueSelector: mode.valueSelector,
                afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                    acPostSelect(mode, selectedChoice);
                    displayForms();
                },
                indicator: mode.basename + "-indicator"
            })

        }

    </script>

</head>
<body>

<chrome:box title="Select Study" id="study-entry">
    <p><tags:instructions code="instruction_select_study"/></p>
    <tags:displayAutocompleter inputName="study" required="true" displayName="Study" size="70"/>
    <p id="studyCrf.study-selected" style="display: none">
        You have selected the study <span id="studyCrf.study-selected-name"></span>.
    </p>
    <br>
    <tags:indicator id="indicator"/>

    <div id="noForm" style="display:none;">
        Click <a href="" id="newFormUrl">here</a> to create a new
        form.
    </div>

    <div id="bigSearch" style="display:none;">
        <div class="endpanes"/>

        <form:form id="assembler">
            <chrome:division id="single-fields">
                <div id="tableDiv">
                    <c:out value="${assembler}" escapeXml="false"/>
                </div>
            </chrome:division>
        </form:form>

    </div>


</chrome:box>


</body>