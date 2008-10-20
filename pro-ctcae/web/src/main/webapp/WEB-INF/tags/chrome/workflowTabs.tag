<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<ul id="workflow-tabs" class="tabs autoclear">
    <c:set var="unfilledTabs">${UNFILLED_TABS}</c:set>
    <c:set var="mandatoryTabs">${MANDATORY_TABS}</c:set>
    <div>
        <a href="#" class="tab0">test</a>
    </div>
</ul>
