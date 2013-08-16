<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<style>
	.message_style{
		padding: 0.1em;
		margin-top: 12px;
	}
	
	.button_style{
		float: right;
		margin-top: 60px;
		margin-right: 12px;
	}
</style>

<body>
	<chrome:box omitBorders="true">
		<div class="message_style">
			Cannot move the schedule prior to participant's start date.
		</div>
		<div class="flow-buttons" style="float: right; margin-top: 70px; margin-right: 12px;">
			 <table width="100%" border="0">
			 	<td>
			 	   <tags:button type="button" id="flow-cancel"
                             cssClass="previous ibutton" value="Ok" icon="x" color="red" size="large"
                             onclick="closeAndRefresh()"/>
			 	</td>
			 </table>
		</div>
	</chrome:box>
</body>