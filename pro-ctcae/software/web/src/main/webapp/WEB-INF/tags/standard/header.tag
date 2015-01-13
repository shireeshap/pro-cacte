<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="security" uri='http://www.springframework.org/security/tags' %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<tags:javascriptLink name="google_analytics_trackers"/>
<tags:includeVirtualKeyboard/>
<script type="text/javascript">
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
	 } catch(err) {
		 console.log('Error in Google analytics initialization');
		}

      function openNewWindow(url) {
        popupWin = window.open(url,
                'open_window',
                'menubar, toolbar, location, directories, status, scrollbars, resizable, dependent, width=700, height=700, left=0, top=0')
    }

    function openQuickGuideWindow(url) {
        popupWin = window.open(url,
                'open_window',
                'menubar, toolbar, location, directories, status, scrollbars, resizable, dependent, width=700, height=700, left=0, top=0')
    }
</script>

<div id="header">

    <div class="background-R">
        <div id="login-action">
            <br/>
            <table width="100%" height="78px">
                <tr>
                    <td><a href="/proctcae" title="Home"><img
                            src='<tags:imageUrl name="blue/PRO-CTCAE-logo.png"/>'/></a></td>
                    <td width="50%" style="color:white;vertical-align:top;"><proctcae:urlAuthorize url="/pages/home">&nbsp;&nbsp;&nbsp;<tags:message code="label.welcome" />
                        <b><authz:authentication property="name"></authz:authentication></b></proctcae:urlAuthorize>&nbsp;&nbsp;&nbsp;
                    </td>
                    <td align="right" width="252px">
                        <c:if test="${pageContext.request.requestURI eq '/proctcae/public/login'}">
                            <c:if test="${empty param.lang}">
                                <c:set var="currentEn" value="current"/>
                                <c:if test="${pageContext.response.locale eq 'es'}">
                                    <c:set var="currentEs" value="current"/>
                                    <c:set var="currentEn" value=""/>
                                </c:if>
                            </c:if>

                            <c:if test="${param.lang eq 'en'}">
                                <c:set var="currentEn" value="current"/>
                                <c:set var="currentEs" value=""/>
                            </c:if>
                            <c:if test="${param.lang eq 'es'}">
                                <c:set var="currentEn" value=""/>
                                <c:set var="currentEs" value="current"/>
                            </c:if>
                            <div class="language-toggle1" style="float:right;margin-right:26px">
                                <a class="left ${currentEn}" href="?lang=en">English</a>
                                <a class="right ${currentEs}" href="?lang=es">Espa�ol</a>
                            </div>
                            <div style="margin-top:0px;margin-right:-5px">
                                <div style="margin-top:30px;color:white;margin-left:0px;font-size:12px;font-family:'Lucida Grande',sans-serif;text-shadow:none">
                                    <table>
                                        <tr>
                                            <td>
                                                <c:set var="url" value="/proctcae/images/quickstart_guide_v5.pdf"/>
                                                <c:if test="${param.lang eq 'es'}">
                                                    <c:set var="url" value="/proctcae/images/quickstart_guide_spanish_v2.pdf"/>
                                                </c:if>
                                                <a onclick="openQuickGuideWindow('${url}');"
                                                   style="cursor:pointer;color:white;border:0px;background:none;">
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                    <img src="/proctcae/images/table/pdf.jpg"/>
                                                            </td>
                                                            <td>
                                                                <b><tags:message
                                                                        code="login.quickStart"/></b>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </a>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${param.lang eq 'es'}">
                                                            <a onclick="openNewWindow('showVideo?lang=es');"
                                                                style="cursor:pointer;color:white;border:0px;background:none;">
                                                    </c:when>
                                                    <c:otherwise>
                                                            <a onclick="openNewWindow('showVideo?lang=en');"
                                                                style="cursor:pointer;color:white;border:0px;background:none;">
                                                    </c:otherwise>
                                                </c:choose>
                                                    <table width="100%">
                                                        <tr>
                                                            <td width="35px;" align="left">
                                                                <img style="margin:0px;"
                                                                     src="<chrome:imageUrl name="../video_camera_icon.png"/>"
                                                                     alt=""/>
                                                            </td>
                                                            <td valign="middle" align="left">
                                                                <b><spring:message code="help.video"/></b>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </a>
                                            </td>

                                        </tr>
                                    </table>

                                </div>
                            </div>
                        </c:if>
                    </td>
                </tr>
            </table>
           <div class="top-btns">
                <proctcae:urlAuthorize url="/pages/j_spring_security_logout">
                    <c:set var="_tabNum" value="${(not empty tab and tab.number gt 0) ? tab.number : ''}"/>
                    <c:choose>
                    	 <c:when test="${not empty currentSection.tasks and not empty currentTask}">
                     		<c:set var="helpLinkCode" value="${currentTask.linkName}" />
	                    </c:when>
	                    <c:when test="${not empty currentSection.mainController and currentSection.mainController ne 'null' }">
	                        <c:set var="helpLinkCode" value="${currentSection.mainController}" />
	                    </c:when>
	                    <c:otherwise>
	                    	<c:set var="helpLinkCode" value="_home" /> 
	                    </c:otherwise>
                    </c:choose>
                    <spring:message var="helpLink" code="${helpLinkCode}" text=""/>
                    <a id="help" href="https://wiki.nci.nih.gov/display/PROCTCAEHELP${helpLink}"
                       target="_blank"><tags:message code="label.help" /></a>
                </proctcae:urlAuthorize>

                <proctcae:urlAuthorize url="/pages/j_spring_security_logout">
                    <a id="logout" href="<c:url value="/pages/j_spring_security_logout"/>"><tags:message code="label.logout" /></a>
                </proctcae:urlAuthorize>
            </div>

        </div>
        <%--${backUpKey}--%>
        <ul id="sections" class="tabs">
            <c:forEach items="${sections}" var="section" varStatus="index">
                <proctcae:urlAuthorize url="${section.mainUrl}">
                    <%--${section.mainUrl}--%>
                    <li class="${section == currentSection ? 'selected' : ''}">
                        <a id="firstlevelnav_${section.mainController}" index="${index.index}"
                           href="<c:url value="${section.mainUrl}"/>">
                            <spring:message code='${section.displayName}' text=''/></a>
                    </li>
                </proctcae:urlAuthorize>
            </c:forEach>
        </ul>

        <div id="taskbar">
            <c:if test="${not empty currentSection.tasks}">
                <c:set var="noOfTasks" value="${fn:length(currentSection.tasks)}"/>
                <!-- test : ${noOfTasks} , ${fn:length(currentSection.tasks)}-->

                <c:forEach items="${currentSection.tasks}" var="task">

                    <proctcae:urlAuthorize url="${task.url}">

                        <c:set var="taskDisplayName"><tags:message code='${task.displayName}'/></c:set>
                        <c:set var="lengthOfTask" value="${fn:length(taskDisplayName)}"/>
                        <a class="${(task == currentTask) || (task.displayName == currentTask.displayName) ?  ( noOfTasks gt 4 ? 'selected gt4' : 'selected lte4') : ( noOfTasks gt 4 ? 'gt4' : 'lte4')} ${(lengthOfTask gt 22 ? 'gt18' : '')}"
                           id="secondlevelnav_${task.linkName}" href="<c:url value="${task.url}"/>"><img
                                class="${(lengthOfTask gt 22 ? 'imagegt18' : '')}"
                                src="/proctcae/images/blue/icons/${task.linkName}_icon.png"/><spring:message
                                code='${task.displayName}' text=''/></a>

                    </proctcae:urlAuthorize>
                </c:forEach>
            </c:if>
            <c:if test="${pageContext.request.requestURI eq '/proctcae/public/login' }">
                <p align="center">
                    <input id='usevirtualkeyboard' type="checkbox" onclick="showVirtualKeyBoard(this,'username');">&nbsp;
                    <img src="/proctcae/images/keyboard-icon.png"/>
                    <tags:message code="login.userVirtualKeyboard"/></p>
            </c:if>

        </div>
        <div id="floatingTaskbar" style="display:none;">
            <tags:floatingTaskbar/>
        </div>

    </div>

    <spring:message code="user.keepworking" var="keepWorking"/>
    <div id="logout_warning" style="display:none;text-align:left;padding-left:10px; width:410px;">
        <p>
            <font size="3"> <tags:message code="instruction_logout_warning"/></font>
        </p>

        <div class="content buttons autoclear" style="margin-top:20px; width:400px;">
            <div class="flow-buttons">
	            <span class="next">
	                <tags:button color="blue" value="${keepWorking}" type="submit"
                                 onclick="logOutOKClicked();"></tags:button>
	            </span>
            </div>
        </div>
    </div>

</div>


<!-- end header -->
