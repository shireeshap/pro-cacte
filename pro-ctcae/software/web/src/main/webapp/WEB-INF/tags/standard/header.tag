<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="security" uri='http://www.springframework.org/security/tags' %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:javascriptLink name="ga"/>

<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "js/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
    try {
        var pageTracker = _gat._getTracker("UA-15798971-1");
        pageTracker._trackPageview();
    } catch(err) {
    }</script>

<div id="header">

    <div class="background-R">


        <div id="login-action">

            <proctcae:urlAuthorize url="/pages/home">
                <div id="logo">
                    <a href="/proctcae" title="Home">PRO-CTCAE</a>
                </div>
                <div id="welcome-user">Welcome
                    <b><authz:authentication property="name"></authz:authentication></b>
                </div>
            </proctcae:urlAuthorize>
            <%--
                   <ctcae:publicAuthorize>
                       <a href="<c:url value="/public/login"/>">Log in</a>
                   </ctcae:publicAuthorize>
               --%>
            <proctcae:urlAuthorize url="/pages/j_spring_security_logout">
                <a id="home" href="<c:url value="/"/>">Home</a>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/j_spring_security_logout">
                <a id="logout" href="<c:url value="/pages/j_spring_security_logout"/>">Log out</a>
            </proctcae:urlAuthorize>
        </div>


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
        </div>
        <div id="floatingTaskbar" style="display:none;">
            <tags:floatingTaskbar/>
        </div>

    </div>
</div>
<!-- end header -->
