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
    <standard:head/>

    <tags:includeScriptaculous/>
    <script type="text/javascript">
        var elements = ['sort1','sort2']
        Event.observe(window, "load", function () {
            sortQustions()
        })

        function sortQustions() {
            Sortable.destroy("sortable")
            Sortable.create("sortable", {
                tag:'div',
                only:['box','sortable'],

                onUpdate:function () {
                    var questionOrder = Sortable.serialize("sortable", {
                        name:'questionText'
                    });
                    reOrderQuestion(questionOrder);

                },
                onChange:function () {
                    var i = 0;
                    $$("span.sortableSpan").each(function (item) {
                        // alert(item.id)
                        item.innerHTML = i + ":";
                        i = i + 1;


                    })
                }

            })
        }

        function addQuestionDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
            sortQustions()

        }
        function addQuestion(questionId) {
            var request = new Ajax.Request("<c:url value="/pages/form/addOneQuestion"/>", {
                parameters:"questionId=" + questionId + "&subview=subview",
                onComplete:addQuestionDiv,
                method:'get'
            })

        }
        function reOrderQuestion(questionOrder) {
            var request = new Ajax.Request("<c:url value="/pages/form/reorderQuestions"/>", {
                parameters:"subview=subview&" + questionOrder,
                // onComplete:addQuestionDiv,
                method:'get'
            })

        }

    </script>
    <style type="text/css">

        .makeDraggable {
            cursor: move;
        }
    </style>

</head>
<body>

<form:form method="post" commandName="createFormCommand">

    <table class="tablecontent">
        <tr>
            <td class="heading">Items</td>
            <td class="heading"> My Form</td>

        </tr>
        <tr>
            <td>
                <c:forEach items="${proCtcTerms}" var="proCtcTerm">
                    <chrome:box>
                        ${proCtcTerm.questionText}

                        <a href="javascript:addQuestion(${proCtcTerm.id})">Add</a>
                        <ul>
                            <c:forEach items="${proCtcTerm.validValues}" var="proCtcValidValue">
                                <li>${proCtcValidValue.value}</li>
                            </c:forEach>
                        </ul>
                    </chrome:box>
                </c:forEach>

            </td>
            <td>
                <div id="sortable">

                    <c:forEach items="${createFormCommand.crf.crfItems}" var="crfItem" varStatus="index">

                        <tags:oneQuestion crfItem="${crfItem}"></tags:oneQuestion>
                    </c:forEach>


                    <div id="hiddenDiv"></div>
                </div>
            </td>

        </tr>

    </table>


</form:form>

</body>