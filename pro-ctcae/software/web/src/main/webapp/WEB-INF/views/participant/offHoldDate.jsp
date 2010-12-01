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

<form:form method="post"  >
    <c:set var="url" value=""<c:url value="/pages/participant/participantOffHold"/>" + "?id=" + id + "&date=" + date + "&index=" + index + "&subview=x""
   vvvvvvvvvv${pageContext.request.requestURL} ${request.requestURI} ..... ${param.id} ,,,,, ${requestScope.date} ,,${requestScope.index}
    <chrome:box title="participant.label.remove_hold_date">
        <chrome:division>
                  <input type="hidden" name="subview" value="subview" > 
            <div id="offTreatment">
                <tags:errors path="*"/>

                </div>
                <p>
                    Participant <strong>${command.participant.displayName}</strong>
                    will be re-assigned to treatment and all the schedules will be reinstated. <br><br>
                    <input type="radio" name="recreate" value="continue" onclick="javascript:hideCycleDay();"> Continue from the date below, the missed schedules will be cancelled  <br>
                    <input type="radio" name="recreate" value="move" onclick="javascript:hideCycleDay();"> Continue cycle from where the treatment was put on hold  <br>
                    <input type="radio" name="recreate" value="cycle" onclick="javascript:showCycleDay();"> Continue from a specific cycle and day
                    <div id="cycle_day" style="display:none;">
                      Cycle <input name="cycle" type="text" value="${cycle}" class="validate-NOTEMPTY&&NUMERIC" size="2">
                      Day   <input name="day" type="text" value="${day}" class="validate-NOTEMPTY&&NUMERIC" size="2">
                    </div>
                    <tags:renderDate propertyName="offHoldTreatmentDate"
                                     displayName="participant.label.remove_hold_date" required="true"/>

                </p>

            </div>

            <div class="flow-buttons">

                <tags:button type="submit" id="flow-update"
                             cssClass="next" value="Assign" icon="check" color="orange"/>


                <tags:button type="button" id="flow-cancel"
                             cssClass="previous ibutton" value="Cancel" icon="x" color="red"
                             onclick="closeWindow()"/>
            </div>


        </chrome:division>
    </chrome:box>
</form:form>
</body>