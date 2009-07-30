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

<head>

    <tags:formBuilder/>
    <tags:includePrototypeWindow/>
    <tags:stylesheetLink name="cycledefinitions"/>
    <tags:javascriptLink name="cycledefinitions"/>
    <style type="text/css">
        .link {
            font-family: Verdana, Arial, Helvetica, sans-serif;
            font-size: 13px;
            font-weight: normal;
            padding-left: 10px;
            padding-right: 10px;
        }

        .link:hover {
            color: #ff3333;
        }

        .linkselected {
            font-family: Verdana, Arial, Helvetica, sans-serif;
            font-size: 13px;
            font-weight: normal;
            padding-left: 10px;
            padding-right: 10px;
            text-decoration: none;
            font-weight: bold;
            cursor:default;
        }

    </style>
    <script type="text/javascript">
        function registerme(a) {
        }
        Event.observe(window, "load", function () {
            addRemoveConditionalTriggeringDisplayToQuestion();
            reOrderQuestionNumber();
        })
        var tabnames = new Array();
        tabnames[0] = 'questions';
        tabnames[1] = 'schedules';
        tabnames[2] = 'notifications';

        function showTab(tabname) {
            for (var i = 0; i < tabnames.length; i++) {
                var thisTab = tabnames[i];
                var thisTabLink = thisTab + 'Link';
                $(thisTabLink).removeClassName('linkselected');
                $(thisTabLink).removeClassName('link');
                if (thisTab == tabname) {
                    $(thisTab).show();
                    $(thisTabLink).addClassName('linkselected')
                } else {
                    $(thisTab).hide();
                    $(thisTabLink).addClassName('link')
                }
            }
        }
    </script>

</head>
<body>


<div class="instructions">
    <div class="summarylabel"><tags:message code='form.label.study'/></div>
    <div class="summaryvalue">${crf.study.displayName}</div>
</div>
<div class="instructions">
    <div class="summarylabel"><tags:message code='form.label.title'/></div>
    <div class="summaryvalue">${crf.title}</div>
</div>
<table>
    <tr>
        <td>
            <a href="javascript:showTab('questions')" class="linkselected" id="questionsLink">Items</a> |
        </td>
        <td>
            <a href="javascript:showTab('schedules')" class="link" id="schedulesLink">Schedules</a> |
        </td>
        <td>
            <a href="javascript:showTab('notifications')" class="link" id="notificationsLink">Notifications</a>
        </td>
    </tr>
</table>
<div id="questions" style="display:block;">
    <table id="formbuilderTable">
        <tr>
            <td id="left">
                <c:forEach items="${crf.crfPagesSortedByPageNumber}" var="crfPage">
                    <div class="formpages">
                        <tags:recallPeriodFormatter desc="${crfPage.instructions}"/>
                        <br>
                        <c:forEach items="${crfPage.crfPageItems}" var="crfPageItem">
                            <tags:reviewCrfPageItem crfPageItem="${crfPageItem}" showInstructions="true"
                                                    displayOrder="${crfPageItem.displayOrder}"></tags:reviewCrfPageItem>
                        </c:forEach>
                        <br>

                    </div>
                </c:forEach>
            </td>
        </tr>
    </table>
</div>
<div id="schedules" style="display:none;">
    <c:forEach items="${crf.formArmSchedules}" var="formArmSchedule">
        <div class="formpages">
            <chrome:division title="Arm: ${formArmSchedule.arm.title}"/>
            <table class="top-widget" cellspacing="0" align="center">
                <c:forEach items="${formArmSchedule.crfCycleDefinitions}" var="crfCycleDefinition"
                           varStatus="statuscycledefinition">
                    <tr>
                        <td>
                            <tags:formScheduleCycleDefinition
                                    cycleDefinitionIndex="${statuscycledefinition.index}"
                                    crfCycleDefinition="${crfCycleDefinition}"
                                    readonly="true"
                                    crfIndex="0"/>
                            <script type="text/javascript">
                                showCyclesForDefinition('0_${statuscycledefinition.index}', ${crfCycleDefinition.cycleLength}, '${crfCycleDefinition.cycleLengthUnit}', '${crfCycleDefinition.repeatTimes}');
                            </script>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:forEach>
</div>
<div id="notifications" style="display:none;" class="formpages">
    <c:forEach items="${rules}" var="proCtcAeRule" varStatus="status">
        <tags:formRule proCtcAeRule="${proCtcAeRule}" ruleIndex="${status.index}" isSite="true"
                       siteReadOnlyView="true"/>
    </c:forEach>

</div>

<br>
<br>


</body>