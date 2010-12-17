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
                Surveys for the participant <i><b>"${command.participant.displayName}"</b></i> have been put on hold beginning
                <%--The participant <strong><i>"${command.participant.displayName}"</i></strong> was put on hold beginning--%>
                    <b><tags:formatDate value="${command.studyParticipantAssignment.onHoldTreatmentDate}"/></b>,
                    <c:if test="${cycle ne null}">which was <b>Cycle ${cycle}</b>, <b>Day ${day}</b>.</c:if><br> <br>
               Specify the date on which surveys will resume. <tags:renderDate propertyName="offHoldTreatmentDate"
                                     displayName="participant.label.remove_hold_date1" required="true"/>

                <%--<c:if test="${cycleNumber ne 0}">--%>
                <%--<i>For the selected date, the cycle number is ${cycleNumber} and day is ${dayNumber}</i> <br>--%>
                <%--</c:if>--%>


                <%--<input type="checkbox" name="recreate" value="cycle"--%>
                       <%--onclick="javascript:showHideCycleDay(this.checked);"> --%>
                Specify the cycle and day corresponding to the above selected off hold date. <br>
            <div id="cycle_day" style="display:block;width:258px" align="right">
                <b> Cycle</b> &nbsp;<input name="cycle" type="text" size="2"> and <b> Day </b> &nbsp;<input name="day"
                                                                                                      type="text"
                                                                                                      size="2">
            </div>


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