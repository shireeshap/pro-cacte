<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<html>
<head>
    <jsp:useBean id="today" class="java.util.Date"/>
    <c:set var="fDate" value='<tags:formatDate value="${today}"/>'/>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:stylesheetLink name="cycledefinitions"/>
    <tags:javascriptLink name="cycledefinitions"/>
    <tags:javascriptLink name="yui"/>
    <script type="text/javascript">
        function getCalendar(dir) {
            var request = new Ajax.Request("<c:url value="/pages/user/displayUserCalendar"/>", {
                onComplete:function(transport) {
                    showCalendar(transport);
                },
                parameters:<tags:ajaxstandardparams/>+"&dir=" + dir,
                method:'get'
            })
        }

        function showCalendar(transport) {
            var items = $('calendar_outer').childElements();
            var len = items.length;
            for (var i = 0; i < len; i++) {
                if (items[i].id != 'calendar_inner') {
                    items[i].remove();
                }
            }
            new Insertion.After('calendar_inner', transport.responseText);
        }

        function showDetailsWindow(day) {
        var request = new Ajax.Request("<c:url value="/pages/user/dayScheduleDetails"/>", {
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 450, 300);
                },
        parameters:<tags:ajaxstandardparams/> +"&day=" + day,
                method:'get'
            })
        }

        function showPopUpMenuAlert(day) {
            var html = '<div id="search-engines"><ul>';
            html += '<li><a href="#" onclick="javascript:showDetailsWindow(' + day + ');">Show details</a></li>';
            html += '</ul></div>';
            jQuery('#scheduleActions' + day).menu({
                content: html,
                maxHeight: 350,
                positionOpts: {
                    directionV: 'down',
                    posX: 'left',
                    posY: 'bottom',
                    offsetX: 0,
                    offsetY: 0
                },
                showSpeed: 300
            });
        }
    </script>
    <style type="text/css">


    </style>
</head>
<body>
<div id="calendar_outer">
    <div id="calendar_inner"></div>
    <tags:userCalendar userCalendarCommand="${userCalendarCommand}"/>
</div>
</body>
</html>