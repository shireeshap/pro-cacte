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
        var elements = ['sort1','sort2']
        Event.observe(window, "load", function () {
            sortQustions();
            <c:if test="${not empty command.crf.crfItems}">
            reOrderQuestionNumber();
            </c:if>
        })

        function sortQustions() {
            Sortable.destroy("sortable")
            Sortable.create("sortable", {
                tag:'div',
                only:['box','sortable'],

                onUpdate:function () {
                    updateQuestionsId();

                },
                onChange:function(){
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
            $('question_' + questionId).hide();
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

        }

        function deleteQuestion(questionId) {
            $('sortable_' + questionId).remove();
            sortQustions();
            updateQuestionsId();
            reOrderQuestionNumber()
            $('question_' + questionId).show();

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

    <table id="formbuilderTable">
        <tr>
            <td id="left">
                Questions
                <c:forEach items="${proCtcTerms}" var="proCtcTerm">
                    <tags:formbuilderBox>
                        ${proCtcTerm.questionText}
                        <a href="javascript:addQuestion(${proCtcTerm.id})" id="question_${proCtcTerm.id}">Add</a>
                        <ul>
                            <c:forEach items="${proCtcTerm.validValues}" var="proCtcValidValue">
                                <li>${proCtcValidValue.value}</li>
                            </c:forEach>
                        </ul>
                    </tags:formbuilderBox>
                </c:forEach>

            </td>
            <td id="right">
                <chrome:division>
                    Total questions are: <span id="totalQuestionDivision">${totalQuestions}</span>
                </chrome:division>
                <div id="sortable">
                    <form:hidden path="questionsIds" id="questionsIds"/>
                    <input type="hidden" id="totalQuestions" value="${totalQuestions}">
                    <c:forEach items="${command.crf.crfItems}" var="crfItem" varStatus="status">

                        <tags:oneQuestion proCtcTerm="${crfItem.proCtcTerm}"
                                          displayOrder="${status.index}"></tags:oneQuestion>
                    </c:forEach>


                    <div id="hiddenDiv"></div>
                </div>
            </td>

        </tr>

    </table>


    <tags:flowControls willSave="true" saveAction="review" saveButtonLabel="Review"/>

</form:form>

</body>