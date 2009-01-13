<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>

<body>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" formName="createForm" hideErrorDetails="true">
    <jsp:attribute name="singleFields">
		<%--<p><tags:instructions code="instruction_select_study"/></p>--%>
        <%----%>
        <tags:reviewForm crf="${command.crf}"></tags:reviewForm>
		
    </jsp:attribute>


</tags:tabForm>
</body>
</html>