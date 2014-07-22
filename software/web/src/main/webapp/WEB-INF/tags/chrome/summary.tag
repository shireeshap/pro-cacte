<%@ attribute name="label" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="id" %>
<%@attribute name="cssClass" %>
<%@attribute name="style" %>
<%@attribute name="deleteParams" %>

<%@attribute name="content" fragment="true" description="The summary content" %>

<div class="instructions">
    <div class="summarylabel">${label}</div>
    <div class="summaryvalue">
        <jsp:invoke fragment="content"/>
    </div>
</div>


