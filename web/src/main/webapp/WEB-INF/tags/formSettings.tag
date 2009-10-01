<%@ attribute name="crf" required="true" type="gov.nih.nci.ctcae.core.domain.CRF" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
    function selectRecallPeriod() {
        $('crf.recallPeriodOtherSpecifyInput').disable();

    }
    function selectOtherSpecifyRecallPeriod() {
        $('crf.recallPeriodOtherSpecifyInput').enable();
        $('crf.recallperiodOther-radio').value = $('crf.recallPeriodOtherSpecifyInput').value;

    }
</script>
<div id="crf.recallPeriod-row" class="row">
    <div class="label">
        <label for="crf.recallPeriod-row"><tags:message code="recall.period"/>
        </label>
    </div>

    <c:set var="otherSpecify" value="${crf.recallPeriod}"/>

    <div class="value">
        <c:forEach items="${recallPeriods}" var="item" varStatus="status">
            <c:set var="code"><tags:message code='${item.code}'/></c:set>
            <c:set var="desc"><tags:message code='${item.desc}'/></c:set>
            <c:choose>
                <c:when test="${code eq crf.recallPeriod}">
                    <input type="radio" class="longselect-radio recallperiod" name="crf.recallPeriod"
                           id="crf.recallPeriod-radio-${status.index}"
                           value="${code}"
                           checked="checked" style="margin:3px" onclick="javascript:selectRecallPeriod()"/>
                    <tags:recallPeriodFormatter desc="${desc}"/>
                    <c:set var="otherSpecify" value=""/>

                </c:when><c:when test="${item.code eq crf.recallPeriod}">
                <input type="radio" class="longselect-radio recallperiod" name="crf.recallPeriod"
                       id="crf.recallPeriod-radio-${status.index}"
                       value="${code}"
                       checked="checked" style="margin:3px" onclick="javascript:selectRecallPeriod()"/>
                <tags:recallPeriodFormatter desc="${desc}"/>
                <c:set var="otherSpecify" value=""/>

            </c:when>
                <c:otherwise>
                    <input type="radio" class="longselect-radio recallperiod" name="crf.recallPeriod"
                           id="crf.recallPeriod-radio-${status.index}"
                           value="${code}"
                           style="margin:3px" onclick="javascript:selectRecallPeriod()"/> <tags:recallPeriodFormatter
                        desc="${desc}"/>

                </c:otherwise>
            </c:choose>
            <br>
        </c:forEach>

        <c:choose>
            <c:when test="${not empty otherSpecify}">
                <input type="radio" class="longselect-radio recallperiodOther"
                       name="crf.recallPeriod"
                       id="crf.recallperiodOther-radio"
                       style="margin:3px" checked="checked"
                       value="${otherSpecify}"
                       onclick="selectOtherSpecifyRecallPeriod()"/><tags:message
                    code="recall.period.other.specify"/>
                <input type="text" class="longselect-radio" name="crf.recallPeriodOtherSpecifyInput"
                       id="crf.recallPeriodOtherSpecifyInput"
                       value="${otherSpecify}" onmouseout="selectOtherSpecifyRecallPeriod()"
                       style="margin:3px"/>)

            </c:when>
            <c:otherwise>
                <input type="radio" class="longselect-radio recallperiodOther" name="crf.recallPeriod"
                       id="crf.recallperiodOther-radio"
                       style="margin:3px" onclick="selectOtherSpecifyRecallPeriod()"/>
                <tags:message
                        code="recall.period.other.specify"/>
                <input type="text" class="longselect-radio" name="crf.recallPeriodOtherSpecifyInput"
                       id="crf.recallPeriodOtherSpecifyInput"
                       value="${otherSpecify}" onmouseout="selectOtherSpecifyRecallPeriod()" disabled="disabled"
                       style="margin:3px"/>)
            </c:otherwise>
        </c:choose>

    </div>
</div>



