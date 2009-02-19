<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@attribute name="cycleIndex" type="java.lang.Integer" required="true" %>
<%@attribute name="crfCycle" type="gov.nih.nci.ctcae.core.domain.CRFCycle" required="true" %>
<%@attribute name="readonly" type="java.lang.Boolean" required="false" %>
<%@attribute name="crfIndex" type="java.lang.Integer" required="false" %>

<br/>
<c:choose>
    <c:when test="${readonly}">
        <chrome:division title="Cycle">
            <c:set var="cycleIndex" value="${crfIndex}-${cycleIndex}"/>
            <b>1. <tags:message code="form.schedule.cycle_length"/> ${crfCycle.cycleLength} ${crfCycle.cycleLengthUnit}</b>

            <div id="div_cycle_selectdays_${cycleIndex}" style="display:none;">
                <br/><b>2. <tags:message code="form.schedule.select_cycle_days_participant"/></b>
                <div id="div_selecteddays_${cycleIndex}"/>
            </div>
            <input type="hidden" name="crf.crfCycles[${cycleIndex}].cycleDays" id="selecteddays[${cycleIndex}]"
                   value="${crfCycle.cycleDays}"/>
            <div id="div_cycle_table_${cycleIndex}" style="display:none;">
                <table id="cycle_table_${cycleIndex}" cellpadding="0" cellspacing="0" align="center">
                    <tbody></tbody>
                </table>
            </div>
            <br/>
            <div id="div_cycle_repeat_${cycleIndex}" style="display:none;">
                <b>3. <tags:message code="form.schedule.repeat"/> ${crfCycle.repeatTimes}</b>
            </div>
        </chrome:division>
    </c:when>
    <c:otherwise>
        <chrome:division title="Cycle ${cycleIndex+1}" enableDelete="true" deleteParams="deleteCycle('${cycleIndex}')">
            <b>1. <tags:message code="form.schedule.cycle_length"/></b>
            <input id="cycle_length_${cycleIndex}" type="text" size="2" value="${crfCycle.cycleLength}"
                   name="crf.crfCycles[${cycleIndex}].cycleLength" class="validate-NUMERIC"
                   onblur="javascript:showCycle(${cycleIndex});" onkeyup="javascript:showCycle(${cycleIndex});"/>
            <tags:renderSelect propertyName="crf.crfCycles[${cycleIndex}].cycleLengthUnit" noForm="true"
                               options="${cyclelengthunits}" doNotshowLabel="true"
                               onchange="javascript:showCycle(${cycleIndex});"
                               propertyValue="${crfCycle.cycleLengthUnit}"/>
            <%--<input type="button" value="Apply" onclick="javascript:showCycle(${cycleIndex});"/>--%>

            <div id="div_cycle_selectdays_${cycleIndex}" style="display:none;"><br/><b>2. <tags:message
                    code="form.schedule.select_cycle_days"/></b>
                <div id="div_selecteddays_${cycleIndex}"/>
            </div>
            <input type="hidden" name="crf.crfCycles[${cycleIndex}].cycleDays" id="selecteddays[${cycleIndex}]"
                   value="${crfCycle.cycleDays}"/>
            <br/>

            <div id="div_cycle_table_${cycleIndex}" style="display:none;">
                <table id="cycle_table_${cycleIndex}" cellpadding="0" cellspacing="0" align="center">
                    <tbody></tbody>
                </table>
            </div>
            <div id="div_cycle_repeat_${cycleIndex}" style="display:none;">
                <br/><b>3. <tags:message code="form.schedule.repeat"/></b> <input id="cycle_repeat_${cycleIndex}"
                                                                                  type="text"
                                                                                  size="2"
                                                                                  value="${crfCycle.repeatTimes}"
                                                                                  name="crf.crfCycles[${cycleIndex}].repeatTimes"
                                                                                  class="validate-NUMERIC"/>
            </div>

        </chrome:division>
    </c:otherwise>
</c:choose>
