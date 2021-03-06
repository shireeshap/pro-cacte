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
<tags:stylesheetLink name="tabbedflow"/>
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

<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" doNotShowSave="true" doNotShowBack="true" notDisplayInBox="true" noFlashMessage="true">
    <jsp:attribute name="singleFields">
	<c:choose>
	    <c:when test="${param['crfId'] eq null}">
	        <chrome:flashMessage flashMessage="Form has been created successfully"/>
	    </c:when>
	    <c:otherwise>
	        <chrome:flashMessage flashMessage="Form has been saved successfully"/>
	    </c:otherwise>
	</c:choose>
	
	<chrome:box>
		<table>
		    <tr>
		        <td width="40%" style="text-align:right;font-weight:bold;margin-left:3em;"><tags:message code='form.label.study'/>:</td>
		        <td style="padding-left:10px;">${command.crf.study.displayName}</td>
		    </tr>
		    <tr>
		        <td style="text-align:right;font-weight:bold;margin-left:3em;"><tags:message code='form.label.title'/>:</td>
		        <td style="padding-left:10px;">${command.crf.title}</td>
		    </tr>
		    <tr><td colspan="2"></td></tr>
		</table>
	</chrome:box>
		
	<table id="formbuilderTable">
	    <tr>
	        <td id="left">
	            <c:forEach items="${crf.crfPagesSortedByPageNumber}" var="crfPage">
	                <div class="formpages">
	                    <div class="formpageheader" style="font-size:12pt;"><b>${crfPage.description}</b></div>
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
	        <c:if test="${crf.status.displayName ne 'Released'}">
	            <tags:button color="green" markupWithTag="a" icon="window" value="Release Form"
	                         onclick="javascript:releaseForm('${crf.id}')"/>
	        </c:if>
	    </span>
	     <span class="prev">
	        <c:if test="${crf.status.displayName ne 'Released'}">
	            <tags:button type="submit" color="blue" id="flow-prev" cssClass="tab${tab.number - 1}"
	                             value="Back" icon="Back"/>
	        </c:if>
	    </span>
		<span class="next">
			<tags:button color="green" markupWithTag="a" icon="check" value="Finish" href="/proctcae"/>
		</span>
	</div>
	<br>
	<br>
	</jsp:attribute>
</tags:tabForm>
</body>