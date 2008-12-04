<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/ctcae/tags" %>

<div id="header">

    <div class="background-R">

        <a href="/ctcae/pages/form/createForm" id="logo">ProCtcAE</a>

        <ctcae:authorize>
            <!--<div id="welcome-user">Welcome<b/>-->
            <authz:authentication operation="username"></authz:authentication>
            <!--</div>-->
        </ctcae:authorize>

        <%--<div id="login-action">--%>
        <%--<ctcae:publicAuthorize>--%>
        <%--<a href="<c:url value="/public/login"/>">Log in</a>--%>


        <%--|&nbsp;<a href='<c:url value="/pages/signUp"/>'>Sign up</a>--%>
        <%--</ctcae:publicAuthorize>--%>

        <%--<ctcae:authorize>--%>
        <%--&nbsp;<div id="logout"><a href="<c:url value="/j_acegi_logout"/>">Log out</a> </div>--%>
        <%--|&nbsp;<a href='<c:url value="/pages/myProfile"/>'>My Profile</a>--%>
        <%--</ctcae:authorize>--%>
        <%--</div>--%>


        <ul id="sections" class="tabs">
            <c:forEach items="${sections}" var="section">
                <li class="${section == currentSection ? 'selected' : ''}">

                    <a id="firstlevelnav_${section.mainController}"
                       href="<c:url value="${section.mainUrl}"/>"><spring:message code='${section.displayName}'
                                                                                  text=''/></a>

                </li>
            </c:forEach>
        </ul>

        <div id="taskbar">
            <c:if test="${not empty currentSection.tasks}">
                <c:set var="noOfTasks" value="${fn:length(currentSection.tasks)}"/>
                <!-- test : ${noOfTasks} , ${fn:length(currentSection.tasks)}-->
                <c:forEach items="${currentSection.tasks}" var="task">
                    <c:set var="taskDisplayName">
                        <spring:message code='${task.displayName}' text=''/>

                    </c:set><c:set var="lengthOfTask" value="${fn:length(taskDisplayName)}">
                </c:set>


                    <a class="${(task == currentTask) || (task.displayName == currentTask.displayName) ?  ( noOfTasks gt 4 ? 'selected gt4' : 'selected lte4') : ( noOfTasks gt 4 ? 'gt4' : 'lte4')} ${(lengthOfTask gt 22 ? 'gt18' : '')}"
                       id="secondlevelnav_${task.linkName}" href="<c:url value="${task.url}"/>"><img
                            class="${(lengthOfTask gt 22 ? 'imagegt18' : '')}"
                            src="/ctcae/images/blue/icons/${task.linkName}_icon.png"/><spring:message
                            code='${task.displayName}' text=''/></a>
                </c:forEach>
            </c:if>
        </div>


    </div>
</div>
<!-- end header -->
