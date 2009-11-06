<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<head>

    <tags:formBuilder/>
    <tags:includePrototypeWindow/>

    <script type="text/javascript">
        Event.observe(window, "load", function () {
            addRemoveConditionalTriggeringDisplayToQuestion();
            reOrderQuestionNumber();
        })

    </script>
    <style type="text/css">
        * {
            zoom: 1
        }

        .formpages {
            width: 920px;
        }
    </style>

</head>
<body>
<chrome:flashMessage flashMessage="Form has been created successfully"/>
<div class="instructions">
    <div class="summarylabel"><tags:message code='form.label.study'/></div>
    <div class="summaryvalue">${crf.study.displayName}</div>
</div>
<div class="instructions">

    <div class="summarylabel"><tags:message code='form.label.title'/></div>
    <div class="summaryvalue">${crf.title}</div>
</div>
<table id="formbuilderTable">
    <tr>
        <td id="left">
            <c:forEach items="${crf.crfPagesSortedByPageNumber}" var="crfPage">
                <div class="formpages">
                    <div class="formpageheader">${crfPage.description}</div>
                    <br/>
                    <tags:recallPeriodFormatter desc="${crfPage.instructions}"/>
                    <br>
                    <c:forEach items="${crfPage.crfPageItems}" var="crfPageItem">
                        <tags:reviewCrfPageItem crfPageItem="${crfPageItem}" showInstructions="true"
                                                displayOrder="${crfPageItem.displayOrder}"></tags:reviewCrfPageItem>
                    </c:forEach>
                    <br>
                </div>
            </c:forEach>
        </td>
    </tr>
</table>

<div class="flow-buttons">
    <span class="prev">
        <c:if test="${crf.status ne 'Released'}">
            <tags:button color="blue" markupWithTag="a" icon="window" value="Release Form" onclick="javascript:releaseForm('${crf.id}')"/>
        </c:if>
    </span>
	<span class="next">
		<tags:button color="green" markupWithTag="a" icon="check" value="Save as draft" href="/proctcae"/>
	</span>
</div>
</body>