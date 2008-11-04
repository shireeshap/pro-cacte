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
<tags:stylesheetLink name="ae"/>

<tags:includeScriptaculous/>

<tags:includePrototypeWindow/>

<script type="text/javascript">
var elements = ['sort1','sort2']
Event.observe(window, "load", function () {
    sortQustions();
<c:if test="${not empty command.studyCrf.crf.crfItems}">
    reOrderQuestionNumber();
    hideQuestionsFromForm();
</c:if>
    new Ajax.InPlaceEditor('crfTitle', '/ctcae/pages/form/setName', {
        rows:1,
        onComplete:function(transport) {
            $('crfTitle').innerHTML = transport.responseText;
            $('formTitle').value = transport.responseText;


        },
        callback:function(form, value) {
            return 'crfTitle=' + encodeURIComponent(value)
        }
    });

})


function sortQustions() {
    Sortable.destroy("sortable")
    Sortable.create("sortable", {
        tag:'div',
        only:['sortable'],

        onUpdate:function () {
            updateQuestionsId();

        },
        onChange:function() {
            reOrderQuestionNumber();
        }

    })
}
function reOrderQuestionNumber() {
    var i = 1;
    $$("span.sortableSpan").each(function (item) {
        item.innerHTML = i + ":";
        i = i + 1;


    })

}
function hideQuestionsFromForm() {
    $$("a.del").each(function (item) {
        var questionId = item.id.substr(4, item.id.length);
        hideQuestionFromForm(questionId);

    })

}

function hideQuestionFromForm(questionId) {
    $('question_' + questionId).hide();

}
function showQuestionInForm(questionId) {
    $('question_' + questionId).show();
}
function addQuestionDiv(transport) {
    var response = transport.responseText;
    new Insertion.Before("hiddenDiv", response);
    sortQustions()
    updateQuestionsId()

}
function addQuestion(questionId) {
    var displayOrder = parseInt($('totalQuestions').value) + parseInt(1);
    var request = new Ajax.Request("<c:url value="/pages/form/addOneQuestion"/>", {
        parameters:"questionId=" + questionId + "&subview=subview&displayOrder=" + displayOrder,
        onComplete:addQuestionDiv,
        method:'get'
    })
    hideQuestionFromForm(questionId);
    $('totalQuestions').value = displayOrder;
    $('totalQuestionDivision').innerHTML = displayOrder;

}

function updateQuestionsId() {
    var questionsId = '';
    var i = 0;
    $$("div.sortable").each(function (item) {
        var questionId = item.id.substr(9, item.id.length)
        if (questionsId == '') {
            questionsId = questionId;
        } else {
            questionsId = questionsId + ',' + questionId;
        }
        i = i + 1
    });

    $('questionsIds').value = questionsId;
    $('totalQuestions').value = i;
    $('totalQuestionDivision').innerHTML = i;
    if (i == 1) {
        $('plural1').innerHTML = 'is';
        $('plural2').innerHTML = '';
    } else {
        $('plural1').innerHTML = 'are';
        $('plural2').innerHTML = 's';
    }

}

function deleteQuestion(questionId) {
    $('sortable_' + questionId).remove();
    sortQustions();
    updateQuestionsId();
    reOrderQuestionNumber()
    showQuestionInForm(questionId);

}

</script>
<style type="text/css">

    .makeDraggable {
        cursor: move;
    }
</style>

</head>
<body>

<form:form modelAttribute="command" method="post">

        <div class="row">

            <div class="summarylabel">Study</div>
            <div class="summaryvalue">${command.studyCrf.study.displayName}</div>
        </div>



    <table id="formbuilderTable">
        <tr>
            <td id="left">
                <span class="formbuilderHeader">Question Bank</span>
                <c:forEach items="${proCtcTerms}" var="proCtcTerm">
                    <tags:formbuilderBox id="question_${proCtcTerm.id}">
                        ${proCtcTerm.questionText}
                        <a href="javascript:addQuestion(${proCtcTerm.id})">Add</a>
                        <ul>
                            <c:forEach items="${proCtcTerm.validValues}" var="proCtcValidValue">
                                <li>${proCtcValidValue.value}</li>
                            </c:forEach>
                        </ul>
                    </tags:formbuilderBox>
                </c:forEach>

            </td>
            <td id="right">
                <table style="border-collapse:collapse; height:800px; width:100%;">
                   
                    <tr style="height:100%;">
                        <td id="formbuilderTable-middle">
                         <div id="formbuilderTable-borderTop">
                            <div class="formbuilderHeader" id="crfTitle">
                                <c:choose>
                                    <c:when test="${command.studyCrf.crf.title eq ''}">Click here to name
                                    </c:when>

                                    <c:when test="${command.studyCrf.crf.title ne null}"> ${command.studyCrf.crf.title}
                                    </c:when>

                                    <c:otherwise> Click here to name
                                    </c:otherwise>
                                </c:choose>

                            </div>
                            <br/>
                            <form:hidden path="studyCrf.crf.title" id="formTitle"/>
                            <div class="formbuildersubHeader">There <span id="plural1">are</span> <span
                                    id="totalQuestionDivision">${totalQuestions}</span> question<span
                                    id="plural2">s</span> in this form.</div>
						   </div>
                            <div id="sortable">
                                <form:hidden path="questionsIds" id="questionsIds"/>
                                <input type="hidden" id="totalQuestions" value="${totalQuestions}">
                                <c:forEach items="${command.studyCrf.crf.crfItems}" var="crfItem" varStatus="status">

                                    <tags:oneQuestion proCtcTerm="${crfItem.proCtcTerm}"
                                                      displayOrder="${status.index}"></tags:oneQuestion>
                                </c:forEach>


                                <div id="hiddenDiv"></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td id="formbuilderTable-borderBottom">
                        </td>
                    </tr>
                </table>
            </td>

        </tr>

    </table>


    <tags:flowControls willSave="true" showBack="false" saveAction="review" saveButtonLabel="Review"/>

</form:form>

</body>