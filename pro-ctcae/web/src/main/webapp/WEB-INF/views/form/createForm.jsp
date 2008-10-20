<%-- This is the standard decorator for all caAERS pages --%>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <standard:head/>

    <tags:includeScriptaculous/>
    <script type="text/javascript">
        var elements = ['drag1','drag2']
        Event.observe(window, "load", function () {
            elements.each(function(item) {
                new Draggable(item, {
                    ghosting:false,
                    constraint:'vertical'


                })
            })
        })

    </script>
    <style type="text/css">

        .makeDraggable {
            cursor: move;
        }
    </style>

</head>
<body>

<form:form method="post">
    <chrome:box title="My Form" autopad="true">

        <chrome:box title="My Form" autopad="true" id="drag1" cssClass="makeDraggable">

            First question


        </chrome:box><chrome:box title="My Form" autopad="true" id="drag2" cssClass="makeDraggable">

            second question


        </chrome:box>


    </chrome:box>
</form:form>

</body>