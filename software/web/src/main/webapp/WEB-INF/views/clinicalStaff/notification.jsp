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

<a class="link" href="javascript:completedForm('${spCrfId}');">Show all responses</a>
   <br><br><br>
 <div style="float:right; padding-right:10px">
<tags:button color="blue" type="button" id="flow-cancel"
                                         cssClass="previous ibutton" value="Close" icon="x"
                                         onclick="closeWindow()"/>
 </div>

</body>
</html>