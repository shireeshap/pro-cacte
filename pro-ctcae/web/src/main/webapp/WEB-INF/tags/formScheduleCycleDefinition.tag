<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@attribute name="cycleDefinitionIndex" type="java.lang.Short" required="true" %>
<%@attribute name="crfCycleDefinition" type="gov.nih.nci.ctcae.core.domain.CRFCycleDefinition" required="true" %>
<%@attribute name="readonly" type="java.lang.Boolean" required="false" %>
<%@attribute name="crfIndex" required="false" %>
<%@attribute name="repeatOptions" type="java.util.List" required="false" %>


<c:if test="${cycleDefinitionIndex > 0}">
    <c:set var="extraText" value="(applied consecutively after completion of above cycle definition)"/>
</c:if>
<c:set var="cycleTitle">
    Cycle definition
    <%=
    (char) (cycleDefinitionIndex + 65)
    %>
    ${extraText}
</c:set>
<c:choose>
    <c:when test="${readonly}">
        <chrome:division title="${cycleTitle}">
            <c:set var="cycleDefinitionIndex" value="${crfIndex}_${cycleDefinitionIndex}"/>
            <b>1. <tags:message
                    code="form.schedule.cycle_length"/>: ${crfCycleDefinition.cycleLength} ${crfCycleDefinition.cycleLengthUnit}</b>

            <div id="div_cycle_table_${cycleDefinitionIndex}" style="display:none;">
                <table id="cycle_table_${cycleDefinitionIndex}" cellpadding="0" cellspacing="0" align="center">
                    <tbody></tbody>
                </table>
                <c:forEach items="${crfCycleDefinition.crfCycles}" var="crfCycle" varStatus="status">
                    <input name='selecteddays_${cycleDefinitionIndex}_${status.index}'
                           id='selecteddays_${cycleDefinitionIndex}_${status.index}'
                           value='${crfCycle.cycleDays}' type="hidden"/>
                </c:forEach>
            </div>
        </chrome:division>
    </c:when>
    <c:otherwise>
        <chrome:division title="${cycleTitle}" enableDelete="true"
                         deleteParams="deleteCycleDefinition('${cycleDefinitionIndex}')">

            <b><tags:message code="form.schedule.cycle_length"/></b>
            <input id="cycle_length_${cycleDefinitionIndex}" type="text" size="2"
                   value="${crfCycleDefinition.cycleLength}"
                   name="selectedFormArmSchedule.crfCycleDefinitions[${cycleDefinitionIndex}].cycleLength"
                   class="validate-NUMERIC"
                   onchange="javascript:showCyclesForDefinition(${cycleDefinitionIndex}, false);"/>
            <tags:renderSelect
                    propertyName="selectedFormArmSchedule.crfCycleDefinitions[${cycleDefinitionIndex}].cycleLengthUnit"
                    noForm="true"
                    options="${cyclelengthunits}" doNotshowLabel="true"
                    propertyValue="${crfCycleDefinition.cycleLengthUnit}"
                    onchange="javascript:showCyclesForDefinition(${cycleDefinitionIndex}, false);"/>
            &nbsp;&nbsp;
            <b><tags:message code="form.schedule.repeat"/></b>
            <c:choose>
                <c:when test="${crfCycleDefinition.repeatTimes eq '-1'}">
                    <tags:renderSelect noForm="true" options="${repeatOptions}" doNotshowLabel="true"
                                       onchange="javascript:changePlannedRep('${cycleDefinitionIndex}',this.value);"
                                       propertyValue="-1" propertyName="select_repeat_${cycleDefinitionIndex}"/>
                </c:when>
                <c:otherwise>
                    <tags:renderSelect noForm="true" options="${repeatOptions}" doNotshowLabel="true"
                                       onchange="javascript:changePlannedRep('${cycleDefinitionIndex}',this.value);"
                                       propertyValue="-2" propertyName="select_repeat_${cycleDefinitionIndex}"/>
                </c:otherwise>
            </c:choose>
            <input id="cycle_repeat_${cycleDefinitionIndex}" type="text" size="2"
                   value="${crfCycleDefinition.repeatTimes}"
                   name="selectedFormArmSchedule.crfCycleDefinitions[${cycleDefinitionIndex}].repeatTimes"
                   onchange="javascript:showCyclesForDefinition(${cycleDefinitionIndex}, false);"/>
           <font size="1"> <tags:message code="form.generate.schedule"/>  </font>

            <div id="div_cycle_selectdays_${cycleDefinitionIndex}" style="display:none;">
                <b><tags:message code="form.schedule.select_cycle_days"/></b>
            </div>

            <div id="div_cycle_table_${cycleDefinitionIndex}" style="display:none;">
                <table id="cycle_table_${cycleDefinitionIndex}" cellpadding="0" cellspacing="0" align="center">
                    <tbody></tbody>
                </table>
            </div>
        </chrome:division>
    </c:otherwise>
</c:choose>
<br/>

