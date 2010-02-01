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

    <tags:includePrototypeWindow/>
    <tags:stylesheetLink name="cycledefinitions"/>
    <tags:javascriptLink name="cycledefinitions"/>
    <script type="text/javascript">
        function registerme(a) {
        }
        Event.observe(window, "load", function () {
            addRemoveConditionalTriggeringDisplayToQuestion();
            reOrderQuestionNumber();
            showTab('questions')
        })
        var tabnames = new Array();
        tabnames[0] = 'questions';
        tabnames[1] = 'schedules';
        tabnames[2] = 'notifications';

        function showTab(tabname) {
            var ul = document.getElementById('menu');
            var li = $(ul).childElements();
            for (var i = 0; i < li.length; i++) {
                $(li[i]).removeClassName('selected');
            }
            var selectLink = tabname + 'Link';
            for (var i = 0; i < li.length; i++) {
                if (li[i].title == selectLink) {
                    $(li[i]).addClassName('selected');
                }
            }
            for (var i = 0; i < tabnames.length; i++) {
                var thisTab = tabnames[i];
                var thisTabLink = thisTab + 'Link';
                if (thisTab == tabname) {
                    $(thisTab).show();
                } else {
                    $(thisTab).hide();
                }
            }
        }
    </script>
</head>
<body>
<tags:view_form_thirdlevelmenu/>
<div class="instructions">
    <div class="summarylabel"><tags:message code='form.label.study'/></div>
    <div class="summaryvalue">${crf.study.displayName}</div>
</div>
<div class="instructions">
    <div class="summarylabel"><tags:message code='form.label.title'/></div>
    <div class="summaryvalue">${crf.title}</div>
</div>
<div id="questions" style="display:block;">
    <table id="formbuilderTable">
        <tr>
            <td>
                <c:forEach items="${crf.crfPagesSortedByPageNumber}" var="crfPage">
                    <div class="formpages">
                        <div class="formpageheader">${crfPage.description}</div>
                        <br/>
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
    <c:forEach items="${crf.formArmSchedules}" var="formArmSchedule" varStatus="status">
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
                                    crfIndex="${status.index}"/>
                            <script type="text/javascript">
                                showCyclesForDefinition('${status.index}_${statuscycledefinition.index}', ${crfCycleDefinition.cycleLength}, '${crfCycleDefinition.cycleLengthUnit}', '${crfCycleDefinition.repeatTimes}');
                            </script>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:forEach>
</div>
<div id="notifications" style="display:none;" class="formpages">
    <c:forEach items="${crf.crfNotificationRules}" var="notificationRule" varStatus="status">
        <tags:formRule rule="${notificationRule}" ruleIndex="${status.index}" readOnly="true"/>
    </c:forEach>
</div>

<br>
<br>


</body>