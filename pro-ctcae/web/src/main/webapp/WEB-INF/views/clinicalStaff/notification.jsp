<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<link rel="stylesheet" type="text/css"
      href="<c:url value="/css/extremecomponents.css"/>">

<html>


<body>

<div style="float:right; padding-right:5px">
<tags:button color="blue" markupWithTag="a" value="Print"
             onclick="window.print();return false;"/>
</div>
${notification.notification.text}

<br>

<a class="link" href="javascript:completedForm('${spCrfId}');">View all responses</a>

</body>
</html>