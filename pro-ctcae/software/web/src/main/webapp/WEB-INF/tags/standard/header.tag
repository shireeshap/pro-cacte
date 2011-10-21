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

<tags:javascriptLink name="ga"/>
<tags:includeVirtualKeyboard/>
<script type="text/javascript">
    try {
        var pageTracker = _gat._getTracker("UA-15798971-1");
        pageTracker._trackPageview();
    } catch(err) {
    }

    try {
        var pageTracker = _gat._getTracker("UA-15798971-4");
        pageTracker._trackPageview();
    } catch(err) {
    }
</script>

<div id="header">

    <div class="background-R">
        <div id="login-action">
            <br/>
            <table>
                <tr>
                    <td><a href="/proctcae" title="Home"><img
                            src='<tags:imageUrl name="blue/PRO-CTCAE-logo.png"/>'/></a></td>
                    <td width="65%" style="color:white;vertical-align:top;"><proctcae:urlAuthorize url="/pages/home">&nbsp;&nbsp;&nbsp;Welcome
                        <b><authz:authentication property="name"></authz:authentication></b></proctcae:urlAuthorize>&nbsp;&nbsp;&nbsp;
                    </td>
                    <td align="right">
                        <c:if test="${pageContext.request.requestURI eq '/proctcae/public/login'}">
                           <c:if test="${empty param.lang}">
                               <c:set var="currentEn" value="current"/>
                               <c:if test="${pageContext.response.locale == 'es'}">
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
                            <div class="language-toggle1" style="float:right">
                                <a class="left ${currentEn}" href="?lang=en">English</a>
                                <a class="right ${currentEs}" href="?lang=es">Español</a>
                            </div>
	                    	<%--<span style="float: right;position:relative;bottom:35px">--%>
							    <%--<a style="color:white" href="?lang=en">English</a>--%>
							   <%--<span style="color:white">|</span>--%>
							    <%--<a style="color:white" href="?lang=es">Spanish</a>--%>
							<%--</span>--%>
                        </c:if>
                    </td>
                </tr>
            </table>
            <%--<proctcae:urlAuthorize url="/pages/j_spring_security_logout">--%>
            <%--<a id="home" href="<c:url value="/"/>">Home</a>--%>
            <%--</proctcae:urlAuthorize>--%>
            <proctcae:urlAuthorize url="/pages/j_spring_security_logout">
                <c:set var="_tabNum" value="${(not empty tab and tab.number gt 0) ? tab.number : ''}"/>
                <c:set var="helpKey" value="${currentTask.linkName}${_tabNum}"/>
                <c:if test="${empty currentTask.linkName}">
                    <c:set var="backUpKey"><%=request.getPathInfo().replaceAll("/", "_")%>
                    </c:set>
                </c:if>

                <spring:message var="helpLink" code="${empty currentTask.linkName? backUpKey:helpKey}"
                                text="NO_${helpKey}"/>
                <a id="help" href="https://wiki.nci.nih.gov/display/PROCTCAEHELP${helpLink}"
                   target="_blank">Help</a>
            </proctcae:urlAuthorize>

            <proctcae:urlAuthorize url="/pages/j_spring_security_logout">
                <a id="logout" href="<c:url value="/pages/j_spring_security_logout"/>">Log out</a>
            </proctcae:urlAuthorize>
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
