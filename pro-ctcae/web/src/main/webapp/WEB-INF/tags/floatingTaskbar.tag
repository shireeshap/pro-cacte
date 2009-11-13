<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--<%@taglib prefix="csmauthz" uri="http://csm.ncicb.nci.nih.gov/authz" %>--%>

<script language="JavaScript">

    var TASKS = [
        <c:forEach items="${sections}" var="section">
        <c:set var="noOfTasks" value="${fn:length(section.tasks)}" />
        ['<c:out value="${section.displayName}" />', ${noOfTasks}, ${section == currentSection ? "true" : "false"},
            <c:forEach items="${section.tasks}" var="task" varStatus="index">
            ['<c:out value="${task.displayName}" />', '<c:url value="${task.url}"/>', '/proctcae/images/blue/icons/${task.linkName}_icon.png'] ${!index.last ? ", " : ""}
            </c:forEach>
        ],
        </c:forEach>
    ];

    var FloatingTaskbar = Class.create({
        ms      : 500,
        hidden  : true,
        initialize: function() {
        },

        show: function(index) {
            var sz = TASKS[index].size();
            if (sz <= 3) ft.hide();
            if (TASKS[index][2]) {
                ft.hidden = true;
                ft.hide();
                return;
            }
            ;

            var _string = "<id='secondLevelTaskbar'>";
            for (i = 3; i < sz; i++) {
                var _length = TASKS[index][i][0].length;
                _string += "<class='" + ((sz - 3 > 4) ? "gt4" : "lte4") + "'><a href='" + TASKS[index][i][1] + "' class='" + (_length > 21 ? "gt18" : "") + "'><img class='" + (_length > 21 ? "imagegt18" : "") + "' alt='' src='" + TASKS[index][i][2] + "'/><span class='spangt18'>" + TASKS[index][i][0] + "</span></a>";
            }
            //            _string += "</ul>";
            //                            alert(_string)
            $('floatingTaskbar').innerHTML = _string;
            $('floatingTaskbar').show();
        },

        hide: function() {
            $('floatingTaskbar').hide();
        },

        checkSubmenu: function() {
            if (ft.hidden) {
                $('floatingTaskbar').hide();
                selectedFirstLevel.removeClassName("fthover");
            }
        }
    });

    var ft = new FloatingTaskbar();
    var selectedFirstLevel;

    $$('#sections.tabs li a').each(function(element) {
        Event.observe(element, "mouseover", function() {
            timer = setTimeout(function() {
                $$('#sections.tabs li a').each(function(element) {
                    element.removeClassName('fthover')
                });
                selectedFirstLevel = element;
                ft.show(Element.readAttribute(element, 'index'));
                ft.hidden = false;
                element.addClassName("fthover");
            }, 200);
        });
        Event.observe(element, "mouseout", function() {
            ft.hidden = true;
            setTimeout("ft.checkSubmenu()", ft.ms);
        });
    });

    Event.observe($('floatingTaskbar'), "mouseover", function() {
        ft.hidden = false;
        clearTimeout(timer);
    });
    Event.observe($('floatingTaskbar'), "mouseout", function() {
        ft.hidden = true;
        setTimeout("ft.checkSubmenu()", ft.ms);
    });

</script>