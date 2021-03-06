<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<style>
body {
	background-position:center -98px;
}
#taskbar {
	padding-top:11px;
}
#main{
	top:40px;
}
#taskbar{
	top:0;
}
#logout, #home {
	z-index:2;
}
</style>

<tags:javascriptLink name="google_analytics_trackers"/>
<script type="text/javascript">
	/* Noheader.tag is included in participant interface.
	   Tracker declared here enables participant interface for google analytics. */
	   try {
		    (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		    	(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		    	m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		    	})(window,document,'script','//www.google-analytics.com/analytics.js','pageTracker');
		    
		    	if(isPROD()) {
			    	// Current PRO-CTCAE PROD tracker.
					pageTracker('create', 'UA-26475546-1', 'auto', {'siteSpeedSampleRate': 100});
			    	pageTracker('send', 'pageview');
		    	}
		    	
		    	if(isTier_SB_DEV()) {
			    	//Tracker for SemanticBits DEV tier
			    	pageTracker('create', 'UA-26475546-2', 'auto', {'name':'gaTrackerSBDev', 'siteSpeedSampleRate': 100});
			    	pageTracker('gaTrackerSBDev.send', 'pageview');
		    	}
		    	
		    	if(isTier_SB_QA()) {
			    	//Tracker for SemanticBits QA tier	
			    	pageTracker('create', 'UA-26475546-3', 'auto', {'name':'gaTrackerSBQA', 'siteSpeedSampleRate': 100});
			    	pageTracker('gaTrackerSBQA.send', 'pageview');
		    	}
		    	
		    	if(isTier_NCI_DEV()) {
		    		//Tracker for NCI-DEV tier	
			    	pageTracker('create', 'UA-26475546-4', 'auto', {'name':'gaTrackerNCIDEV', 'siteSpeedSampleRate': 100});
			    	pageTracker('gaTrackerNCIDEV.send', 'pageview');
		    	}
		    	
		    	if(isTier_NCI_STAGE()) {
		    		//Tracker for NCI-STAGE tier	
			    	pageTracker('create', 'UA-26475546-5', 'auto', {'name':'gaTrackerNCISTAGE', 'siteSpeedSampleRate': 100});
			    	pageTracker('gaTrackerNCISTAGE.send', 'pageview');
		    	}
		    	
		    	if(isTier_SB_Demo()) {
		    		//Tracker for SemanticBits SB-DEMO tier	
			    	pageTracker('create', 'UA-26475546-6', 'auto', {'name':'gaTrackerSBDEMO', 'siteSpeedSampleRate': 100});
			    	pageTracker('gaTrackerSBDEMO.send', 'pageview');
		    	}
		 } catch(err) {
			 console.log('Error in Google analytics initialization');
	    	}
</script>

<div id="header">

    <div class="background-R">
        <%--<a href="/proctcae" id="logo">ProCtcAE</a>--%>
		
        <div id="taskbar">
        <table width="100%">
        	<tr>
	        	<td width="60%"><spring:message code='label.welcome'/>&nbsp;<b><authz:authentication property="name"></authz:authentication></b>
	        	</td>
	        	<td></td>
        	</tr>
        </table>
        </div>
        <div class="top-btns">
        	<a href="/proctcae" id="home"><spring:message code='label.home'/></a>
	        <proctcae:urlAuthorize url="/pages/j_spring_security_logout">
	            <a id="logout" href="<c:url value="/pages/j_spring_security_logout"/>"><spring:message code='label.logout'/></a>
	        </proctcae:urlAuthorize>
	    </div>
    </div>
    
    <tags:includePrototypeWindow/>
   	<spring:message code="user.keepworking" var="keepWorking"/>
    <div id="logout_warning" style="display:none;text-align:left;padding-left:10px; width:410px;">
	    <p>
	        <font size="3"> <tags:message code="instruction_logout_warning" /></font>
	    </p>
	    <div class="content buttons autoclear" style="margin-top:20px; width:400px;">
	        <div class="flow-buttons">
	            <span class="next">
	                <tags:button color="blue" value="${keepWorking}" type="submit" onclick="logOutOKClicked();"></tags:button>
	            </span>
	        </div>
	    </div>
	</div>

</div>
<!-- end header -->
