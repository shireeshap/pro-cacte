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

        Event.observe(window, "load", function () {
            sortQustions();
        <c:if test="${not empty command.studyCrf.crf.crfItems}">
            reOrderQuestionNumber();
            hideQuestionsFromForm();
        </c:if>

        })

        Event.observe(window, "load", function () {
            var formNameInPlaceEdit = new Ajax.InPlaceEditor('crfTitle', '/ctcae/pages/form/setName', {
                rows:1,
                cancelControl:false,
                okControl:false,
                submitOnBlur:true,
                onEnterEditMode:function() {
                    if ($('crfTitle').innerHTML == 'Click here to name') {
                        $('crfTitle').innerHTML = ''

                    }

                }  ,
                onComplete:function(transport) {
                    $('crfTitle').innerHTML = transport.responseText;
                    $('formTitle').value = transport.responseText;
                    if ($('formTitle').value == '') {
                        //    $('crfTitle').innerHTML = 'Click here to name';

                    }


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

<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
<jsp:attribute name="singleFields">


        <div class="instructions">

            <div class="summarylabel">Study</div>
            <div class="summaryvalue">${command.studyCrf.study.displayName}</div>
        </div>


            <table id="formbuilderTable">
                <tr>
                    <td id="left">
                        <div class="instructions">

                            <div class="summarylabel">Question bank</div>
                        </div>
                        <ul class="tree">
                            <c:forEach items="${ctcCategoryMap}" var="ctcCategory">

                                <li><a href="#">${ctcCategory.key.name}</a>
                                    <ul>
                                        <c:forEach items="${ctcCategory.value}" var="proCtcTerm">
                                            <li class="closed"><a href="#">${proCtcTerm.term}</a>
                                                <ul><c:forEach items="${proCtcTerm.proCtcQuestions}"
                                                               var="proCtcQuestion">

                                                    <li id="question_${proCtcQuestion.id}">
                                                        <tags:formbuilderBox>
                                                            ${proCtcQuestion.questionText}
                                                            <a href="javascript:addQuestion(${proCtcQuestion.id})">Add</a>
                                                        </tags:formbuilderBox>
                                                    </li>

                                                </c:forEach>
                                                </ul>
                                            </li>
                                        </c:forEach>
                                        <br>
                                    </ul>
                                </li>

                            </c:forEach>
                        </ul>


                        <c:forEach items="${proCtcQuestions}" var="proCtcQuestion">
                            <tags:formbuilderBox id="question_${proCtcQuestion.id}">
                                ${proCtcQuestion.questionText}
                                <a href="javascript:addQuestion(${proCtcQuestion.id})">Add</a>
                                <ul>
                                    <c:forEach items="${proCtcQuestion.validValues}" var="proCtcValidValue">
                                        <li>${proCtcValidValue.value}</li>
                                    </c:forEach>
                                </ul>
                            </tags:formbuilderBox>
                        </c:forEach>

                    </td>
                    <td id="right">
                        <table style="border-collapse:collapse; height:800px;">
                            <tr style="height:100%;">
                                <td id="formbuilderTable-middle">
                                    <div id="formbuilderTable-borderTop">
                                        <span class="formbuilderHeader" id="crfTitle">${command.title}</span>
                                        <br/>
                                        <form:hidden path="studyCrf.crf.title" id="formTitle"/>

                            <span class="formbuildersubHeader">There <span id="plural1">are</span> <span
                                    id="totalQuestionDivision">${totalQuestions}</span> question<span
                                    id="plural2">s</span> in this form.</span>
                                    </div>
                                    <div id="sortable">
                                        <form:hidden path="questionsIds" id="questionsIds"/>
                                        <input type="hidden" id="totalQuestions" value="${totalQuestions}">
                                        <c:forEach items="${command.studyCrf.crf.crfItems}" var="crfItem"
                                                   varStatus="status">

                                            <tags:oneQuestion proCtcQuestion="${crfItem.proCtcQuestion}"
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
</jsp:attribute>
</tags:tabForm>
</body>