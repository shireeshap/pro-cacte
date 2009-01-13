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
            var studyAutoCompleter = new studyAutoComplter('crf.study');
            acCreate(studyAutoCompleter);
        <c:if test="${command.crf.study ne null}">
            initializeAutoCompleter('crf.study', '${command.crf.study.displayName}', '${command.crf.study.id}')

        </c:if>

            initSearchField();

        })


    </script>

</head>
<body>
<c:choose>
    <c:when test="${command.crf.id ne null}">
        <c:set var="willSave" value="true"/>
    </c:when>
    <c:otherwise>
        <c:set var="willSave" value="false"/>
    </c:otherwise>
</c:choose>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="${willSave}" formName="createForm" hideErrorDetails="true"
              boxClass="small">
    <jsp:attribute name="singleFields">
        <c:choose>
            <c:when test="${command.crf.crfVersion eq 1.0}">
                <p><tags:instructions code="instruction_select_study"/></p>
                <tags:renderAutocompleter propertyName="crf.study" required="true" displayName="form.label.study"
                                          size="70"/>
                <p id="crf.study-selected" style="display: none">
                    You have selected the study <span id="crf.study-selected-name"></span>.
                </p>
            </c:when>
            <c:otherwise>&nbsp;&nbsp;&nbsp;<b>Study</b>&nbsp;&nbsp; <input type="text"
                                                                           value="${command.crf.study.displayName}"
                                                                           disabled="true" size="70"></c:otherwise>
        </c:choose>
    </jsp:attribute>


</tags:tabForm>

</body>