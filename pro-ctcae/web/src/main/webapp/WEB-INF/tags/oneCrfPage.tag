<%@ attribute name="crfPage" type="gov.nih.nci.ctcae.core.domain.CRFPage" required="true" %>


<%@ attribute name="index" required="true" %>

<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="noform" tagdir="/WEB-INF/tags/noform" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script type="text/javascript">
	var description = 'studyCrf.crf.crfPages[${index}].description';
	var descriptionProperty = description + '-property';


	Event.observe(window, "load", function () {
		var formNameInPlaceEdit = new Ajax.InPlaceEditor(descriptionProperty, '/ctcae/pages/form/setName', {
			rows:1,
			cancelControl:false,
			okControl:false,
			size:18,
			submitOnBlur:true,
			onEnterEditMode:function() {
				if ($(descriptionProperty).innerHTML == 'Click here to name') {
					$(descriptionProperty).innerHTML = ''

				}

			}  ,
			onComplete:function(transport) {
				$(descriptionProperty).innerHTML = transport.responseText;
				$(description).value = transport.responseText;
				if ($(descriptionProperty).value == '') {
					$(descriptionProperty).innerHTML = 'Click here to name';

				}


			},
			callback:function(form, value) {
				return 'crfTitle=' + encodeURIComponent(value)
			}
		});

	})

</script>
<div class="formpages" id="form-pages_${index}">

	<chrome:box>

		<span class="formbuilderHeader" id="studyCrf.crf.crfPages[${index}].description-property">${crfPage.description}Page${index}</span>

		<input id="studyCrf.crf.crfPages[${index}].description" type="hidden" size="30" value="${crfPage.description}"
			   name="studyCrf.crf.crfPages[${index}].description"/>

		<div id="sortable_${index}">
			<div class="sortable makeDraggable"></div>
			<c:forEach items="${crfPage.crfPageItems}" var="selectedCrfPageItem"
					   varStatus="status">

				<tags:oneCrfPageItem crfPageItem="${selectedCrfPageItem}"
									 index="${status.index}" crfPageIndex="${index}">
				</tags:oneCrfPageItem>
			</c:forEach>
			<c:if test="${index ==0}">
				<div id="hiddenDiv"/>
			</c:if>
		</div>
	</chrome:box>

</div>
