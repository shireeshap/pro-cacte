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
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>
    <tags:dwrJavascriptLink objects="study"/>
    <tags:includePrototypeWindow/>

    <script type="text/javascript">

        function getStudies(sQuery) {
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            study.matchStudy(unescape(sQuery), callMetaData);
            return aResults;
        }

        var managerAutoComp;
        Event.observe(window, 'load', function() {
            new YUIAutoCompleter('crf.studyInput', getStudies, handleSelect);

            if ("${command.crf.study.displayName}" != '') {
                $('crf.studyInput').value = "${command.crf.study.displayName}";
                $('crf.studyInput').removeClassName('pending-search');
            }

        })
       ;

        function handleSelect(stype, args) {
            var ele = args[0];
            var oData = args[2];
            ele.getInputEl().value = oData.displayName;
            var id = ele.getInputEl().id;
            var hiddenInputId = id.substring(0, id.indexOf('Input'));
            Element.update(hiddenInputId + "-selected-name", oData.displayName)
            $(hiddenInputId + '-selected').show()
            new Effect.Highlight(hiddenInputId + "-selected")
            $(hiddenInputId).value = oData.id;

        }

       function clearInput(inputId) {
            $(inputId).clear();
            $(inputId + 'Input').clear();
            $(inputId + 'Input').focus();
            $(inputId + 'Input').blur();
        }

    </script>
    <style type="text/css">
        * {
            zoom: 0;
        }
    </style>
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

<tags:tabForm tab="${tab}" flow="${flow}" willSave="${willSave}" formName="createForm" hideErrorDetails="true">
    <jsp:attribute name="singleFields">
        <c:choose>
            <c:when test="${command.crf.crfVersion eq 1.0}">
                <p><tags:instructions code="instruction_select_study"/></p>
                <%--<tags:renderAutocompleter propertyName="crf.study" required="true" displayName="form.label.study"--%>
                                          <%--size="100"/>--%>

                <form:input path="crf.study" id="crf.study" cssClass="validate-NOTEMPTY"
                    title="Study"
                    cssStyle="display:none;"/>
                <div class="row">
            <div class="label"><tags:requiredIndicator/><tags:message code='form.label.study'/></div>
            <div class="value">
                <tags:yuiAutocompleter inputName="crf.studyInput" value="${command.crf.study.shortTitle}" required="true"
                               hiddenInputName="crf.study"/>
                </div></div>
                <p id="crf.study-selected" style="display: none">
                    You have selected the study <span id="crf.study-selected-name"></span>.
                </p>
            </c:when>
            <c:otherwise>&nbsp;&nbsp;&nbsp;<b>Study</b>&nbsp;&nbsp; <input type="text"
                                                                           value="${command.crf.study.displayName}"
                                                                           disabled="true" size="60"></c:otherwise>
        </c:choose>
    </jsp:attribute>


</tags:tabForm>

</body>