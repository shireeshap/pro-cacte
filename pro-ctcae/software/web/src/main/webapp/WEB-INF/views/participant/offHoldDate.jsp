<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<body>

<form:form method="post">
    <%--<c:set var="url" value=""<c:url value="/pages/participant/participantOffHold"/>" + "?id=" + id + "&date=" + date + "&index=" + index + "&subview=x""--%>
    <%--vvvvvvvvvv${pageContext.request.requestURL} ${request.requestURI} ..... ${param.id} ,,,,, ${requestScope.date} ,,${requestScope.index}--%>
    <chrome:box title="participant.label.remove_hold_date">
        <chrome:division>
            <input type="hidden" name="subview" value="subview">

            <div id="offTreatment">
                <tags:errors path="*"/>
            </div>
            <p>
                <i>Participant <strong>${command.participant.displayName}</strong> was put on hold from <b><tags:formatDate value="${command.studyParticipantAssignment.onHoldTreatmentDate}"/></b>
                <c:if test="${cycle ne null}">which was cycle ${cycle} and day ${day}.</c:if></i><br>
                Participant will resume form administration and the held forms will be reinstated based on the option selected below:<br>

                <c:if test="${cycleNumber ne 0}">
                <i>For the selected date, the cycle number is ${cycleNumber} and day is ${dayNumber}</i> <br>
                </c:if>
                <input type="radio" name="recreate" value="continue" onclick="javascript:hideCycleDay();"> resume from
                the date below (the missed schedules will be cancelled) <br>
                <input type="radio" name="recreate" value="cycle" onclick="javascript:showCycleDay();"> resume on a
                specific cycle and day

            <div id="cycle_day" style="display:none; width:251px" align="right">
               <b> Cycle</b> &nbsp;&nbsp;<input name="cycle" type="text" value="${cycle eq null ? "1" : cycle}"
                             class="validate-NOTEMPTY&&NUMERIC" size="2">  &nbsp;&nbsp;
               <b> Day </b> &nbsp;&nbsp;<input name="day" type="text" value="${day eq null ? "1" : day}" class="validate-NOTEMPTY&&NUMERIC"
                           size="2">
            </div>
            <tags:renderDate propertyName="offHoldTreatmentDate"
                             displayName="participant.label.remove_hold_date" required="true"/>


            </p>

            </div>

            <div class="flow-buttons">

                <tags:button type="submit" id="flow-update"
                             cssClass="next" value="Remove Hold" icon="check" color="orange"/>


                <tags:button type="button" id="flow-cancel"
                             cssClass="previous ibutton" value="Cancel" icon="x" color="red"
                             onclick="closeWindow()"/>
            </div>


        </chrome:division>
    </chrome:box>
</form:form>
</body>