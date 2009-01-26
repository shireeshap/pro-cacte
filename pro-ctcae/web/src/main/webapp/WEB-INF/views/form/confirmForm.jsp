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
<head>
    <script type="text/javascript">
        Event.observe(window, "load", function () {
            addRemoveConditionalTriggeringDisplayToQuestion();
        })

    </script>

</head>
<body>
<chrome:flashMessage flashMessage="form.save.confirmation"></chrome:flashMessage>

<chrome:box title="form.confirmation">

    <tags:reviewForm crf="${crf}"/>

</chrome:box>

</body>
</html>