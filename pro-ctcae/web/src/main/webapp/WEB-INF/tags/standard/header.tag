<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/ctcae/tags" %>
<%@ taglib prefix="security" uri='http://www.springframework.org/security/tags' %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<div id="header">

    <div class="background-R">


        <div id="login-action">

            <ctcae:urlAuthorize url="/pages/home">
                <div id="welcome-user">Welcome
                    <authz:authentication property="name"></authz:authentication>
                </div>
            </ctcae:urlAuthorize>

            <ctcae:publicAuthorize>
                <a href="<c:url value="/public/login"/>">Log in</a>

            </ctcae:publicAuthorize>

            <ctcae:urlAuthorize url="/pages/j_spring_security_logout">
                &nbsp;<div id="logout"><a href="<c:url value="/pages/j_spring_security_logout"/>">Log out</a> </div>
            </ctcae:urlAuthorize>
        </div>


        <ul id="sections" class="tabs">
            <c:forEach items="${sections}" var="section">

                <ctcae:urlAuthorize url="${section.mainUrl}">

                    <li class="${section == currentSection ? 'selected' : ''}">

                        <a id="firstlevelnav_${section.mainController}"
                           href="<c:url value="${section.mainUrl}"/>"><spring:message code='${section.displayName}'
                                                                                      text=''/></a>

                    </li>
                </ctcae:urlAuthorize>


            </c:forEach>
        </ul>

        <div id="taskbar">
            <c:if test="${not empty currentSection.tasks}">
                <c:set var="noOfTasks" value="${fn:length(currentSection.tasks)}"/>
                <!-- test : ${noOfTasks} , ${fn:length(currentSection.tasks)}-->

                <c:forEach items="${currentSection.tasks}" var="task">

                    <ctcae:urlAuthorize url="${task.url}">

                        <c:set var="taskDisplayName"><tags:message code='${task.displayName}'/></c:set>
                        <c:set var="lengthOfTask" value="${fn:length(taskDisplayName)}"/>
                        <a class="${(task == currentTask) || (task.displayName == currentTask.displayName) ?  ( noOfTasks gt 4 ? 'selected gt4' : 'selected lte4') : ( noOfTasks gt 4 ? 'gt4' : 'lte4')} ${(lengthOfTask gt 22 ? 'gt18' : '')}"
                           id="secondlevelnav_${task.linkName}" href="<c:url value="${task.url}"/>"><img
                                class="${(lengthOfTask gt 22 ? 'imagegt18' : '')}"
                                src="/ctcae/images/blue/icons/${task.linkName}_icon.png"/><spring:message
                                code='${task.displayName}' text=''/></a>

                    </ctcae:urlAuthorize>
                </c:forEach>
            </c:if>
        </div>


    </div>
</div>
<!-- end header -->
