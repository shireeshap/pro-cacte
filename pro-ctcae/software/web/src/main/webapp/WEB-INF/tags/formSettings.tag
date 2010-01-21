<%@ attribute name="crf" required="true" type="gov.nih.nci.ctcae.core.domain.CRF" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
    function selectRecallPeriod(value) {
        if (value == 'other') {
            $('recallPeriodOtherSpecifyInput').show();
            $('recallPeriodOtherSpecifyInput').value = '';
        } else {
            $('recallPeriodOtherSpecifyInput').hide();
            $('recallPeriodOtherSpecifyInput').value = value;
        }

    }
    function selectOtherSpecifyRecallPeriod() {
        $('crf.recallPeriodOtherSpecifyInput').enable();
        $('crf.recallperiodOther-radio').value = $('crf.recallPeriodOtherSpecifyInput').value;

    }
    function baselinCheck(obj) {
        if (obj.checked) {
            $('crf.createBaseline').value = true;
        } else {
            $('crf.createBaseline').value = false;
        }
    }
</script>
<c:set var="isOther" value="true"/>
<c:set var="style" value="margin:3px;"/>
<c:forEach items="${recallPeriods}" var="recallPeriod">
    <c:if test="${recallPeriod.desc eq crf.recallPeriod}">
        <c:set var="isOther" value="false"/>
        <c:set var="style" value="margin:3px;display:none"/>
    </c:if>
</c:forEach>
<td style="text-align:right;font-weight:bold;"><tags:requiredIndicator/><b><tags:message code="recall.period"/></b></td>
<td style="padding-left:10px;">
    <select onchange="javascript:selectRecallPeriod(this.value)">
        <c:forEach items="${recallPeriods}" var="recallPeriod">
            <c:choose>
                <c:when test="${(recallPeriod.desc eq crf.recallPeriod) or (recallPeriod.code eq 'other' and isOther eq 'true')}">
                    <option value="${recallPeriod.code}" selected>${recallPeriod.desc}</option>
                </c:when>
                <c:otherwise>
                    <option value="${recallPeriod.code}">${recallPeriod.desc}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
    <input type="text" name="crf.recallPeriod" id="recallPeriodOtherSpecifyInput" value="${crf.recallPeriod}" size="36"
           style="${style}" class="validate-NOTEMPTY" title="Recall period"/>
</td>
<td>
    <c:if test="${crf.createBaseline}">
        <c:set var="checked" value="checked='true'"/>
    </c:if>
    <input type="checkbox" name="baselineCheck" ${checked} onclick="javascript:baselinCheck(this);"/><b>Create
    baseline </b>
    <input type="hidden" name="crf.createBaseline" value="${crf.createBaseline}" id="crf.createBaseline"/>
</td>