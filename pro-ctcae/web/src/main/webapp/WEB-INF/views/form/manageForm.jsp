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
<tags:dwrJavascriptLink objects="crf"/>

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
    crf.searchCrf(parameterMap, id, showTable)
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

//     function copyForm(studyCrfId) {
//         var request = new Ajax.Request("<c:url value="/pages/form/copyForm"/>", {
//             parameters:"studyCrfId=" + studyCrfId + "&subview=subview",
//            onComplete:function(transport) {
//               buildTable('assembler');

//            },
//            method:'get'
//        })
//     }

function deleteForm(studyCrfId) {
    var request = new Ajax.Request("<c:url value="/pages/form/deleteForm"/>", {
        parameters:"studyCrfId=" + studyCrfId + "&subview=subview",
        onComplete:showDeleteFormWindow,
        method:'get'
    })
}

function versionForm(studyCrfId) {
    var request = new Ajax.Request("<c:url value="/pages/form/versionForm"/>", {
        parameters:"studyCrfId=" + studyCrfId + "&subview=subview",
        onComplete:showDeleteFormWindow,
        method:'get'
    })
}
function showVersionForm(crfId) {
    var request = new Ajax.Request("<c:url value="/pages/form/showVersionForm"/>", {
        parameters:"crfId=" + crfId + "&subview=subview",
        onComplete:function(transport) {
            var response = transport.responseText;
            var selectedCrfId;
            $$('tr.crf_' + crfId).each(function(item) {
                item.id = 'selectedCrf_' + crfId;
                selectedCrfId = item.id;

            })
            new Insertion.After('selectedCrf_' + crfId, response);
            $('crfVersionShowImage_' + crfId).hide();
            $('crfVersionHideImage_' + crfId).show();
        },
        method:'get'
    })
}
function hideVersionForm(crfId) {
    $('crfVersionShowImage_' + crfId).show();
    $('crfVersionHideImage_' + crfId).hide();
    $$('tr.childTableRow_' + crfId).each(function(item){
        item.remove();
    });


}

function releaseForm(studyCrfId) {

    var request = new Ajax.Request("<c:url value="/pages/form/releaseForm"/>", {
        parameters:"studyCrfId=" + studyCrfId + "&subview=subview",
        onComplete:showReleaseFormWindow,
        method:'get'
    })

}

function showReleaseFormWindow(transport) {
    var win = Windows.getFocusedWindow();
    if (win == null) {
        win = new Window({ id: '100' , className: "alphacube", closable : true, minimizable : false, maximizable :
                true, title: "", height:230, width: 550,top:250,left:200});
        win.setDestroyOnClose();
        win.setHTMLContent(transport.responseText);
        win.show(true)

    } else {
        win.setHTMLContent(transport.responseText);
        win.refresh();
    }
}

function showDeleteFormWindow(transport) {
    var win = Windows.getFocusedWindow();
    if (win == null) {
        win = new Window({ id: '100' , className: "alphacube", closable : true, minimizable : false, maximizable :
                true, title: "", height:300, width: 550,top:250,left:200});
        win.setDestroyOnClose();
        win.setHTMLContent(transport.responseText);
        win.show(true)

    } else {
        win.setHTMLContent(transport.responseText);
        win.refresh();
    }


}


</script>

</head>
<body>
<chrome:box title="form.box.select_study" id="study-entry" cssClass="small">
    <p><tags:instructions code="instruction_select_study"/></p>
    <tags:displayAutocompleter inputName="study" required="true" displayName="Study" size="60"/>
    <p id="studyCrf.study-selected" style="display: none">
        You have selected the study <span id="studyCrf.study-selected-name"></span>.
    </p>
    <br>
    <tags:indicator id="indicator"/>

</chrome:box>
</div>
<div id="crfItem_50"></div>
<div id="noForm" style="display:none;">
    <a href="" id="newFormUrl">New Form</a>
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

</body>